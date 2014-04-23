
import java.io.*;


public class Test{


	public static void main(String[] args) throws IOException{
		/*
		ExecutorService ex = Executors.newCachedThreadPool();

		File f = new File("Common.cfg");
		PeerThread p = new PeerThread(1001, f, 6008, "localhost");
		p.run();
	
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
		byte[] b = new byte[26];
		for (int i = 0; i < b.length; i++){
			//putting alphabet into array
			b[i]= (byte) (i+65);
			//System.out.print(b[i]+" ");
		}
		
		
		//Create instance of RAF
		//RandomAccess r = new RandomAccess(256,"RAF.dat");
		//r.writeRAF(b, 2);
		//r.writeRAF(b, 4);
		
		//Write values of byte array to file with offset

		/*
		RAF.seek(10);
		RAF.write(b, 0, 1);
		RAF.write(b, 1, 1);
		RAF.write(b, 0, 1);
		RAF.write(b, 25, 1);
		//RAF.seek(2);
		RAF.write(b, 24, 1);
		 */
		/*
		raf.RAF("RAF.dat", b, 20);
		raf.RAF("RAF.dat", b, 56);
		
		RAF.close();
		*/
	}
}
