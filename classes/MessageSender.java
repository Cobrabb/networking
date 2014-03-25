import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class MessageSender implements Runnable 
{

	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private byte[] message;
	
	//MessageSender thread constructor
	public MessageSender(Socket s)
	{
		this.socket = s;
		message = new byte[32];
		System.out.println("Message Sender: Opened");
	}
	
	@Override
	public void run() 
	{
		System.out.println("Message Sender: Run");
		
		try
		{
			//try opening input and output streams for the given socket
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//test write a blank message to socket
			out.write(message);
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void closeSender()
	{
		try
		{
			//attempt to close input stream for socket
			if(in != null)
				in.close();
			
			//attempt to close output stream for socket
			if(out != null)
				out.close();
			
			System.out.println("Sending Thread closed successfully");
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}
