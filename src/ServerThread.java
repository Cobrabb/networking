import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class ServerThread extends Thread {

        private int peerNum;
        private BitField myBitField;
        private String fileName;
        private int fileSize;
        private int pieceSize;
	private Socket clientSocket;
	private int numberOfPreferredNeighbors;
	private int unchokingInterval;
	private int optimisticUnchokingInterval;
	private Message init;
	private ServerProtocol pro;
	private RandomAccess file;
	private PrintWriter log;
	private int myPeerNum;

	public int getPeerNum(){
		return this.peerNum;
	}

	public ServerThread(int myPeerNum, Socket s, int neighbors, int unchoking, int opunchoking,  String filename, int filesize, int piecesize, BitField b, Message m, RandomAccess f, PrintWriter log){
                fileName = filename;
                fileSize = filesize;
                pieceSize = piecesize;
		clientSocket = s;
		numberOfPreferredNeighbors = neighbors;
		unchokingInterval = unchoking;
		optimisticUnchokingInterval = opunchoking;
		init = m;
		file = f;
		this.log = log;
		this.myPeerNum = myPeerNum;

		//shared with all servers
		myBitField = b;
		
		//get the peerNum of the calling client from the message.	
		peerNum = m.getPeerID();
	}

	public boolean getChoked(){
		if(pro!=null){
			return pro.getChoked();
		}
		return false;
	}

	public boolean getInterested(){
		if(pro!=null){
			return pro.getChoked();
		}
		return false;	
	}

	public void setChoked(boolean b){
		if(pro!=null){
			pro.setChoked(b);
		}
	}

    public void run() {
        
	pro = new ServerProtocol(myPeerNum, peerNum, numberOfPreferredNeighbors, unchokingInterval, optimisticUnchokingInterval, fileName, fileSize, pieceSize, myBitField, file, log);
      
        OutputStream o = null;
	InputStream i = null; 
        DataOutputStream out = null;
	DataInputStream in = null;
	ByteArrayOutputStream baos = null;
	
        try 
         {
	    o = clientSocket.getOutputStream();
	    i = clientSocket.getInputStream();
	    out = new DataOutputStream(o);
            in = new DataInputStream(i); 
	    baos = new ByteArrayOutputStream();
	    long openTime = System.currentTimeMillis();
	    Message pass  = pro.processInput(init);
	    byte[] x = pass.createMessage();
	    out.write(x, 0, x.length);	
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
			//System.out.println("ServerThread: About to send a ticking message "+pass.getType());
			byte[] b = pass.createMessage();
			out.write(b, 0, b.length);
			//System.out.println("ServerThread: Sent a ticking message");
		}
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
