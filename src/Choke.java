/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */

public class Choke extends Message{
	
	private int value;
	
	public Choke (int value,
				  int mLength, 
				  String mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		
	}
	
	public Choke(){
		
		super(length, type, payload);
		value = 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
