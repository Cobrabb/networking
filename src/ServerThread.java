import java.net.*;
import java.io.*;

public class ServerThread {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);
	ServerProtocol pro = new ServerProtocol(1);
        
        try (
            ServerSocket serverSocket =
                new ServerSocket(Integer.parseInt(args[0]));
            Socket clientSocket = serverSocket.accept();     
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String line;
	    String pass;
	    long openTime = System.currentTimeMillis();
            while (true) {
		if(in.ready()){
			line = in.readLine();
			if(line==null){
				break;
			}
			System.out.println("The client says: "+line+".");
			pass = pro.processInput(line);
			if(pass.equals("")){
				continue;
			}
			else if(pass.equals("error")){
				System.out.println("The server could not process the input: "+line);
				break;
			}
			out.println(pass);
		}
		//all logic which does not block for input
		long interval = System.currentTimeMillis()-openTime;
		pass = pro.tick(interval);	
		if(!pass.equals("")){	
			out.println(pass);
		}
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
