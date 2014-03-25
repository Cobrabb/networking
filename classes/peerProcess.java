import java.net.ServerSocket;
import java.net.Socket;


public class peerProcess 
{
	public static int listeningPort;
	public static ServerSocket listeningSocket;
	public static Socket sendingSocket;
	public static MessageListener messageListener;
	public static MessageSender messageSender;
	
	public static void main(String args[])
	{
		//peerProcess pProcess = new peerProcess();
		
		
		//Assign listening port number to 6008.
		//port number should be read from PeerInfo.cfg when ready.
		listeningPort = 6008;
		
		try
		{
			//create new ServerSocket to communicate with remote peer
			//create message listening thread for that peer and start it
			System.out.println("Attempting to start Listening Thread");
			listeningSocket = new ServerSocket(listeningPort);
			messageListener = new MessageListener(listeningSocket);
			messageListener.run();
			
			//create new Socket for sending messages to peer
			//create message sending thread for that peer and start it
			System.out.println("Attempting to start Sending Thread");
			sendingSocket = listeningSocket.accept();
			messageSender = new MessageSender(sendingSocket);
			messageSender.run();
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		
		//just waiting some amount of time
		int i = 0;
		
		while(i<1000000)
		{
			i++;
		}
		
		//closing listening and sending threads
		System.out.println("Attempting to close Listening Thread");
		messageListener.closeListener();
		
		System.out.println("Attempting to close Sending Thread");
		messageSender.closeSender();
		
	}

}
