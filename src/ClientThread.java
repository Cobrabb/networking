import java.io.*;
import java.net.*;
import java.util.*;

public class ClientThread extends Thread{

	private int fileSize;
	private int pieceSize;
	private int portNum;
	private String host;
	private int peerNum;
	private String fileName;
	private BitField bitField;
	private ClientRateInfo myRate;
	private int thisPeerNum;
	private RandomAccess file;
	private static PrintWriter log;
	private static boolean done;
	
	public ClientThread(boolean done, int tpeernum, int peernum, int filesize, int piecesize, int portnum, String thehost, String fName, BitField bitField, ClientRateInfo c, RandomAccess f, PrintWriter log){
		//System.out.println("ClientThread: Init");
		thisPeerNum = tpeernum;
		peerNum = peernum;
		fileSize = filesize;
		pieceSize = piecesize;
		portNum = portnum;
		host = thehost;
		fileName = fName;
		this.bitField = bitField;
		myRate = c;
		file = f;
		this.log = log;
		this.done = done;
	}

    public  void run() {
	
	//System.out.println("ClientThread: Creating a handshake message");
	ClientProtocol pro = new ClientProtocol(done, thisPeerNum, peerNum, fileSize, pieceSize, fileName, bitField, myRate, file, log);
	Message handshake = pro.initiateContact();
	byte[] c = handshake.createMessage();

	//System.out.println("ClientThread: Created a handshake message");
	Socket clientSocket = null;
	OutputStream o = null;
	InputStream i = null;
	DataOutputStream out = null;
	DataInputStream in = null;
	ByteArrayOutputStream baos = null;
	//System.out.println("ClientThread: about to look for a server");
	while(true){
      	  try 
       	  {
		    clientSocket = new Socket(host, portNum);
		log.println(new Date().toString()+": Peer "+thisPeerNum+" makes a connection to Peer "+peerNum);
		log.flush();
		
		//System.out.println("ClientThread: succeed");
		
		    o = clientSocket.getOutputStream();
		    i = clientSocket.getInputStream();
		    out = new DataOutputStream(o);
		    in = new DataInputStream(i);
		    baos = new ByteArrayOutputStream();
		    Message pass;
        	    long openTime = System.currentTimeMillis();
	
		    //send original handshake
		
		//System.out.println("ClientThread: sent handshake");
		    out.write(c, 0, c.length);

        	    while (true) {
			if(in.available()>0){
 				byte buffer[] = new byte[in.available()];
				in.readFully(buffer);
				Message m = new Message(buffer);
				//System.out.println("ClientThread: Got a message of type: "+m.getType());
				pass = pro.processInput(m);	
				if(pass == null){
					continue;
				}	
				//System.out.println("ClientThread: About to send a message of type: "+pass.getType());
				byte[] b = pass.createMessage();
				out.write(b, 0, b.length);
			}
			if(pro.isOpen()){
				long interval = System.currentTimeMillis()-openTime;	
				pass = pro.tick(interval);	
				if(pass != null){
					byte[] b = pass.createMessage();
					out.write(b, 0, b.length);
				}
			}
           	 }
       	 } catch (UnknownHostException e) {
        	    System.err.println("Don't know about host " + host);
       	 } catch (IOException e) {
     	   } 
  	  }	
	}
}
