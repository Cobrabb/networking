/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */

public class Unchoke extends Message{
	
	private int value;
	
	public Unchoke (int value,
				  int mLength, 
				  String mType, 
				  int mPayload){
		
		super(length, type, payload);
		this.setValue(value);
		
	}
	
	public Unchoke(){
		
		super(length, type, payload);
		value = 1;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
