/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */

public class Message {

	protected static int length;
	protected static String type;
	protected static int payload;
	
	public Message (int mLength, 
					String mType, 
					int mPayload){
		length = mLength;
		type = mType;
		payload = mPayload;
	}
	
	public Message(){
		length = 0;
		type = "";
		payload = 0;
	}
	
	public int getLength(){
		return length;
	}
	
	public void setLength(int mLength){
		length = mLength;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String mType){
		type = mType;
	}
	
	public int getPayload(){
		return payload;
	}
	
	public void setPayload(int mPayload){
		payload = mPayload;
	}
}
