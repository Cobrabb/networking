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
	public void RAF(String fileName, byte[] message, int start, int stop) throws IOException{
		/* Method takes String that corresponds to file name, and using  a start and stop
		   bit of byte array to copy particular bytes to file */
		
		//number of bytes to write
		int numOfBytes = (stop - start) + 1;
		
		//Create instance of RAF
		RandomAccessFile raf = new RandomAccessFile(fileName,"rw");
		
		//Sets the file-pointer offset to start value
		//raf.seek(start);
		
		//Write values of byte array to file with offset
		raf.write(message,start,numOfBytes);
		
		raf.close();
	}
}
