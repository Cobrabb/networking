import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

public class ServerThread {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);
	ServerProtocol pro = new ServerProtocol(1);
      
	ServerSocket serverSocket = null; 
	Socket clientSocket = null;
        OutputStream o = null;
	InputStream i = null; 
        DataOutputStream out = null;
	DataInputStream in = null;
	ByteArrayOutputStream baos = null;
        try 
         {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
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
