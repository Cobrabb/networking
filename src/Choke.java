

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
		payload = null;
		
	}
	
	public Choke(){
		
		super(length, type, payload);
		value = 0;
		payload = null;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
