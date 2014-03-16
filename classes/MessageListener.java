import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class MessageListener implements Runnable 
{

	private ServerSocket listenerSocket;
	private InputStream in;
	private OutputStream out;
	private Socket socket;
	private byte[] mBytes;
	private Thread messageSender;
	
	//MessageListener thread constructor
	public MessageListener(ServerSocket s)
	{
		this.listenerSocket = s;
		mBytes = new byte[32];
		System.out.println("Message Listener: Opened");
	}
	
	@Override
	public void run() 
	{
		System.out.println("Message Listener: Run");
		
		try
		{			
			//attempt to open input and output streams for the given socket
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			while(true)
			{
				//test read bytes from socket
				in.read(mBytes);
			
				//print the received bytes
				System.out.println(mBytes);
				
				
			}
			
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void closeListener()
	{
		try
		{
			//attempt to close input stream for socket
			if(in != null)
				in.close();
			
			//attempt to close output stream for socket
			if(out != null)
				out.close();
			
			System.out.println("Listening Thread closed successfully");
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}
