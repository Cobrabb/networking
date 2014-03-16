/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */

public class Have extends Message{
	
	private int value;
	
	public Have (int value,
				  int mLength, 
				  String mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		
	}
	
	public Have(){
		
		super(length, type, payload);
		value = 4;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
