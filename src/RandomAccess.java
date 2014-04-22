import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

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
	
	public RandomAccess(int size, String name){
		pieceSize = size;
		fileName = name;
	}
	
	public void writeRAF(byte[] message, int index){
		/* Method takes String that corresponds to file name, and using  a start and stop
		   bit of byte array to copy particular bytes to file */
		
		//constructor filename, piecesize
		//startseek = index*piecesize
		int startSeek = index*pieceSize;
		
		//Create instance of RAF
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(fileName,"rw");
			//Sets the file-pointer offset to start value
			raf.seek(startSeek);
			
			//Write values of byte array to file with offset
			raf.write(message, 0, message.length - 1);
			
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
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
	public byte[] readRAF(int index){
		byte[] message = new byte[pieceSize];
		RandomAccessFile raf;
		try {
			int startSeek = index*pieceSize;
			raf = new RandomAccessFile(fileName,"rw");
			
			//Sets the file-pointer offset to start value
			raf.seek(startSeek);
			
			//Write values of byte array to file with offset
			raf.read(message, 0, message.length - 1);
			
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return message;
	}
}
