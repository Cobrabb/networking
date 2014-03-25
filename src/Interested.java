

/**
 * @author Ahmad Abukhalil
 *
 */

public class Interested extends Message{
	
	private int value;
	
	public Interested (int value,
				  int mLength, 
				  String mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		mPayload = 0;
		
	}
	
	public Interested(){
		
		super(length, type, payload);
		value = 2;
		payload = null;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
