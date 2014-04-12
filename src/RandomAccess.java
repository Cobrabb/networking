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
	/*
	public static void main(String[] args) throws IOException {
		//Writing Random Access File
		char[] name = {'A','B','C','D','E','F','G','H','I','J','K','L',
				'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		
		RandomAccessFile raf = new RandomAccessFile("RAF.dat","rw");
		
		System.out.println("Beginning to write to RAF\n~~~");
		
		for(int i = 0; i < name.length; i++){
			raf.write(name[i]);
		}
		
		System.out.println("Finished writing to RAF\n~~~");
		raf.close();
		
		//Reading Random Access File
		System.out.println("Beginning to read to RAF\n~~~");
		
		RandomAccessFile RAF = new RandomAccessFile("RAF.dat","rw");
		System.out.println("Done Reading RAF\n~~~");

		RAF.seek(0);
		System.out.print(RAF.read()+" ");	//Read value A == 65 in ASCII
		RAF.seek(1);
		System.out.print(RAF.read()+" ");	//Read value B == 66 in ASCII
		RAF.seek(2);
		System.out.print(RAF.read()+" ");	//Read value C == 67 in ASCII
		RAF.seek(3);
		System.out.print(RAF.read()+" ");	//Read value D == 68 in ASCII
		
		RAF.close();
	}
	*/
	
	public void RAF(String fileName, byte[] message, int startSeek) throws IOException{
		/* Method takes String that corresponds to file name, and using  a start and stop
		   bit of byte array to copy particular bytes to file */
		
		//constructor filename, piecesize
		//startseek = index*piecesize
		//
		
		//Create instance of RAF
		RandomAccessFile raf = new RandomAccessFile(fileName,"rw");
		
		//Sets the file-pointer offset to start value
		raf.seek(startSeek);
		
		//Write values of byte array to file with offset
		raf.write(message, 0, message.length - 1);
		
		raf.close();
		
		/*	Method Testing
		 //Create instance of RAF
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
}
