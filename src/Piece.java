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
				  String mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		
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
}
