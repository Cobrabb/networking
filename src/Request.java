

/**
 * @author Ahmad Abukhalil
 *
 */

public class Request extends Message{
	
	private int value;
	
	public Request (int value,
				  int mLength, 
				  String mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		
	}
	
	public Request(){
		
		super(length, type, payload);
		value = 6;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
