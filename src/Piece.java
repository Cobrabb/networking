

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

public class Piece extends Message{
	
	private int value;
	
	public Piece (int value,
				  int mLength, 
				  int mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		mType = value;
		
	}
	
	public Piece(){
		
		super(length, type, payload);
		value = 7;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public void findPayload(String name, int offset){
		RandomAccessFile f;
		try {
			f = new RandomAccessFile(name, null);
			f.seek(offset);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
