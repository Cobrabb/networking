import java.io.*;
import java.net.*;

public class ClientThread {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
	ClientProtocol pro = new ClientProtocol(1);
	pro.initiateContact();

	Socket clientSocket = null;
	OutputStream o = null;
	InputStream i = null;
	DataOutputStream out = null;
	DataInputStream in = null;
	ByteArrayOutputStream baos = null;
        try 
         {
	    clientSocket = new Socket(hostName, portNumber);
	    o = clientSocket.getOutputStream();
	    i = clientSocket.getInputStream();
	    out = new DataOutputStream(o);
	    in = new DataInputStream(i);
	    baos = new ByteArrayOutputStream();
	    Message pass;
            long openTime = System.currentTimeMillis();

	    //send original handshake

            while (true) {
		if(in.available()>0){
 			byte buffer[] = new byte[1024];
                        for(int s; (s=in.read(buffer)) != -1; ){
                                baos.write(buffer, 0, s);
                        }
                        byte result[] = baos.toByteArray();
			if(result==null){
				break;
			}
			Message m = new Message(result);
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
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
