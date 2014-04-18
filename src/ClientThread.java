import java.io.*;
import java.net.*;

public class ClientThread extends Thread{

	private int fileSize;
	private int pieceSize;
	private int portNum;
	private String host;
	private int peerNum;
	
	public ClientThread(int peernum, int filesize, int piecesize, int portnum, String thehost){
		peerNum = peernum;
		fileSize = filesize;
		pieceSize = piecesize;
		portNum = portnum;
		host = thehost;
	}

    public  void run() {
	ClientProtocol pro = new ClientProtocol(peerNum, fileSize, pieceSize);
	Message handshake = pro.initiateContact();
	byte[] c = handshake.createMessage();

	Socket clientSocket = null;
	OutputStream o = null;
	InputStream i = null;
	DataOutputStream out = null;
	DataInputStream in = null;
	ByteArrayOutputStream baos = null;
	while(true){
      	  try 
       	  {
		    clientSocket = new Socket(host, portNum);
		    o = clientSocket.getOutputStream();
		    i = clientSocket.getInputStream();
		    out = new DataOutputStream(o);
		    in = new DataInputStream(i);
		    baos = new ByteArrayOutputStream();
		    Message pass;
        	    long openTime = System.currentTimeMillis();
	
		    //send original handshake
		    out.write(c, 0, c.length);

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
