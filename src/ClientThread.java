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

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            String line;
	    String pass;
            long openTime = System.currentTimeMillis();
	    out.println("Handshake");
            while (true) {
		if(in.ready()){
			line = in.readLine();
			System.out.println("The server says: "+line+".");
			if(line==null){
				break;
			}
			pass = pro.processInput(line);	
			if(pass.equals("")){
				continue;
			}	
			else if(pass.equals("error")){
				System.out.println("Client has an error handling input");
				break;
			}
			out.println(pass);
		}
		if(pro.isOpen()){
			long interval = System.currentTimeMillis()-openTime;	
			pass = pro.tick(interval);	
			if(!pass.equals("")){
				out.println(pass);
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
