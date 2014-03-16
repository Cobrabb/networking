/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */

public class Bitfield_Message extends Message{
	
	private int value;
	
	public Bitfield_Message (int value,
				  int mLength, 
				  String mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		
	}
	
	public Bitfield_Message(){
		
		super(length, type, payload);
		value = 5;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
