import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.*;

/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */
public class RandomAccess {

	/**
	 * @param args
	 * @throws IOException 
	 */
	private int pieceSize;
	private String fileName;
	private static Semaphore block;
	private static RandomAccessFile raf;
	
	public RandomAccess(int size, String name){
		pieceSize = size;
		fileName = name;
		block = new Semaphore(1);
		try{
			raf = new RandomAccessFile(fileName, "rw");
		}catch(Exception e){
			System.out.println(fileName);
		}
	}
	
	public synchronized void writeRAF(byte[] message, int index){
		try{
			block.acquire();
		}
		catch(Exception e){
			System.out.println("A semaphore problem");
		}
		System.out.println("Writing 1");
		
		/* Method takes String that corresponds to file name, and using  a start and stop
		   bit of byte array to copy particular bytes to file */
		
		//constructor filename, piecesize
		//startseek = index*piecesize
		int startSeek = index*pieceSize;
		
		//Create instance of RAF
		try {
			//Sets the file-pointer offset to start value
			raf.seek(startSeek);

			String pieceString = "";
			for (int i = 0; i < message.length; i++)
			{
				if(message[i]!=10&&message[i]!=13){
					pieceString += (char)message[i];
				}
			}
			System.out.println("RAF: about to write \""+pieceString+"\" to "+index);
			
			//Write values of byte array to file with offset
			raf.write(message);
			raf.seek(0);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Writing 2");
		block.release();	
		/*	
		~~~~~ Method Testing ~~~~~~ 
		
		Create instance of RAF
		RandomAccessFile RAF = new RandomAccessFile("RAF.dat","rw");
		RandomAccess raf = new RandomAccess();
		
		//Write values of byte array to file with offset

		/*	
		RAF.seek(10);
		RAF.write(b, 0, 1);
		RAF.write(b, 1, 1);
		RAF.write(b, 0, 1);
		RAF.write(b, 25, 1);
		//RAF.seek(2);
		RAF.write(b, 24, 1);
		
		raf.RAF("RAF.dat", b, 20);
		raf.RAF("RAF.dat", b, 56);
		
		RAF.close();
		*/
	}
	public synchronized byte[] readRAF(int index){
		try{
			block.acquire();
		}
		catch(Exception e){
			System.out.println("A semaphore problem");
		}
		System.out.println("Reading 1");

		byte[] message = new byte[pieceSize];
		try {
			int startSeek = index*pieceSize;
			
			//Sets the file-pointer offset to start value
			raf.seek(startSeek);
			
			//Write values of byte array to file with offset
			raf.read(message);

			String pieceString = "";
			for (int i = 0; i < message.length; i++)
			{
				if(message[i]!=10&&message[i]!=13){
					pieceString += (char)message[i];
				}
			}
			System.out.println("RAF: just read \""+pieceString+"\" from "+index);

			raf.seek(0);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		System.out.println("Reading 2");
		block.release();	
		
		return message;
	}
}
