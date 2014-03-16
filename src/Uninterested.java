/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */

public class Uninterested extends Message{
	
	private int value;
	
	public Uninterested (int value,
				  int mLength, 
				  String mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		
	}
	
	public Uninterested(){
		
		super(length, type, payload);
		value = 3;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}