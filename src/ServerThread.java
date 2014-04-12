import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

public class ServerThread extends Thread {

	private int unchokingInterval;
        private int  optimisticUnchokingInterval;
        private int myPeerNum;
        private BitField myBitField;
        private int numberOfPreferredNeighbors;
        private String fileName;
        private int fileSize;
        private int pieceSize;
	private int portNumber;

	public ServerThread(int peernum, int numprefneighbor, int unchoking, int opunchoking, String filename, int filesize, int piecesize, int portnum){
                myPeerNum = peernum;
                numberOfPreferredNeighbors= numprefneighbor;
                unchokingInterval = unchoking;
                optimisticUnchokingInterval = opunchoking;
                fileName = filename;
                fileSize = filesize;
                pieceSize = piecesize;
		portNumber = portnum;
	}

    public void run() {
        
        
	ServerProtocol pro = new ServerProtocol(myPeerNum, numberOfPreferredNeighbors, unchokingInterval, optimisticUnchokingInterval, fileName, fileSize, pieceSize);
      
	ServerSocket serverSocket = null; 
	Socket clientSocket = null;
        OutputStream o = null;
	InputStream i = null; 
        DataOutputStream out = null;
	DataInputStream in = null;
	ByteArrayOutputStream baos = null;

        try 
         {
            serverSocket = new ServerSocket(portNumber);
            clientSocket = serverSocket.accept();     
	    o = clientSocket.getOutputStream();
	    i = clientSocket.getInputStream();
	    out = new DataOutputStream(o);
            in = new DataInputStream(i); 
	    baos = new ByteArrayOutputStream();
	    Message pass;
	    long openTime = System.currentTimeMillis();
            while (true) {
		if(in.available()>0){
			byte buffer[] = new byte[in.available()];
			in.readFully(buffer);
			Message m = new Message(buffer);
			pass = pro.processInput(m);
			if(pass == null){
				continue;
			}
			byte[] b = pass.createMessage();
			out.write(b, 0, b.length);
		}
		//all logic which does not block for input
		long interval = System.currentTimeMillis()-openTime;
		pass = pro.tick(interval);	
		if(pass != null){	
			byte[] b = pass.createMessage();
			out.write(b, 0, b.length);
		}
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
