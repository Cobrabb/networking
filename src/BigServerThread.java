import java.net.*;
import java.util.concurrent.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class BigServerThread extends Thread {

	private int unchokingInterval;
        private int  optimisticUnchokingInterval;
        private int myPeerNum;
        private BitField myBitField;
        private int numberOfPreferredNeighbors;
        private String fileName;
        private int fileSize;
        private int pieceSize;
	private int portNumber;
	private ArrayList<ServerThread> servers;
	private ArrayList<ClientRateInfo> rates;
	private RandomAccess file;
	private static PrintWriter log;

	public BigServerThread(int peernum, int numprefneighbor, int unchoking, int opunchoking, String filename, int filesize, int piecesize, int portnum, BitField b, ArrayList<ClientRateInfo> r, RandomAccess f, PrintWriter log){
                myPeerNum = peernum;
                numberOfPreferredNeighbors= numprefneighbor;
                unchokingInterval = unchoking;
                optimisticUnchokingInterval = opunchoking;
                fileName = filename;
                fileSize = filesize;
                pieceSize = piecesize;
		portNumber = portnum;
		myBitField = b;
		rates = r;
		file = f;
		this.log = log;

		//initialize an arrayList to store the server threads
		servers = new ArrayList<ServerThread>();
	}

    public void run() {
        
	ExecutorService executor = Executors.newCachedThreadPool();
	BigServerProtocol pro = new BigServerProtocol(numberOfPreferredNeighbors, unchokingInterval, optimisticUnchokingInterval, fileName, fileSize, pieceSize, myBitField);

	ChokingThread c = new ChokingThread(myPeerNum, servers, unchokingInterval, optimisticUnchokingInterval, numberOfPreferredNeighbors, rates, log);
	executor.execute(c);
	//System.out.println("BigServerThread: Launched the ChokingThread");
        
      while(true){ 
	ServerSocket serverSocket = null; 
	Socket clientSocket = null;
        OutputStream o = null;
	InputStream i = null; 
        DataOutputStream out = null;
	DataInputStream in = null;
        try 
         {
            serverSocket = new ServerSocket(portNumber);
		//System.out.println("BigServerThread: about to stall on accept");
            clientSocket = serverSocket.accept();
		//System.out.println("BigServerThread: accepted");
	    i = clientSocket.getInputStream();
            in = new DataInputStream(i); 
	    while(true){
		if(in.available()>0){
			//System.out.println("BigServerThread; got something....");
			byte buffer[] = new byte[in.available()];
			in.readFully(buffer);
			Message m = new Message(buffer);
			log.println(new Date().toString()+": Peer "+myPeerNum+" is connected from Peer "+m.getPeerID());
			log.flush();
			//System.out.println("BigServerThread: recieved a message from "+m.getPeerID());
			if(m.getPeerID()!=0){
				ServerThread s = new ServerThread(myPeerNum, clientSocket, numberOfPreferredNeighbors, unchokingInterval, optimisticUnchokingInterval, fileName, fileSize, pieceSize, myBitField, m, file, log);
			//System.out.println("BigServerThread: passing the client off to its server");
				servers.add(s);
				executor.execute(s);
				break;
			}
			else{
				System.out.println("Error.");
			}
		}
	}		
	    serverSocket.close();
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
  }
}
