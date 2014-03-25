
import java.io.*;
import java.util.concurrent.*;


public class Test{


	public static void main(String[] args){
		/*
		ExecutorService ex = Executors.newCachedThreadPool();
		File f = new File("Common.cfg");

		for(int i=0; i<10; i++){
			ex.execute(new PeerThread(i+1, f));
		}
		*/
		/*
		ByteStream b = new ByteStream();
		int l = 4;
		String type = "Interested";
		int pload = 11;
		String length = String.valueOf(l);
		String payload = String.valueOf(pload);
		
		String message = new StringBuilder(length).append(type).append(payload).toString();
		
		System.out.println("Length int is: " + l);
		System.out.println("Length String is: " + length);
		System.out.println("Type String is: " + type);
		System.out.println("Payload int is: " + pload);
		System.out.println("Payload String is: " + payload);
		System.out.println("The entire message is: " + message);
	
		
		try {
			byte[]array = b.toByteStream(message);
			System.out.println("The Bytes in the message are:");
			for(int i = 0; i < array.length; i++){
				System.out.print(array[i] + "    ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		Message m = new Message();
		ByteStream b = new ByteStream();
		
		String message = m.createMessage(4, 11, 20);
		System.out.println("The message is:");
		System.out.println(message);
		
		try {
			byte[]array = b.toByteStream(message);
			System.out.println("The Bytes in the message are:");
			for(int i = 0; i < array.length; i++){
				System.out.print(array[i] + "    ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
