import java.net.*;
import java.util.concurrent.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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

	public BigServerThread(int peernum, int numprefneighbor, int unchoking, int opunchoking, String filename, int filesize, int piecesize, int portnum, BitField b, ArrayList<ClientRateInfo> r){
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

		//initialize an arrayList to store the server threads
		servers = new ArrayList<ServerThread>();
	}

    public void run() {
        
	ExecutorService executor = Executors.newCachedThreadPool();
	BigServerProtocol pro = new BigServerProtocol(numberOfPreferredNeighbors, unchokingInterval, optimisticUnchokingInterval, fileName, fileSize, pieceSize, myBitField);

	ChokingThread c = new ChokingThread(servers, unchokingInterval, optimisticUnchokingInterval, numberOfPreferredNeighbors, rates);
	executor.execute(c);
        
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
            clientSocket = serverSocket.accept();
	    i = clientSocket.getInputStream();
            in = new DataInputStream(i); 
		if(in.available()>0){
			byte buffer[] = new byte[in.available()];
			in.readFully(buffer);
			Message m = new Message(buffer);
			if(m.getPeerID()!=0){
				ServerThread s = new ServerThread(clientSocket, numberOfPreferredNeighbors, unchokingInterval, optimisticUnchokingInterval, fileName, fileSize, pieceSize, myBitField, m);
				servers.add(s);
				executor.execute(s);
				break;
			}
			else{
				System.out.println("Error.");
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
