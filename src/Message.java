

/**
 * @author Ahmad Abukhalil
 *
 */

public class Message {

	protected static int length;
	protected static int type;
	protected static byte[] payload;
	//length = message length + 4
	
	protected static int peerID;
	
	public Message (byte[] m)
	{
		if(m[4].intValue() == 79)
		{
			peerID = m[31];
			peerID += m[30]*8;
			peerID += m[29]*16;
			peerID += m[28]*24;
			
			length = 32;
			type = 8;
			payload = null;
		}
		else 
		{
			length = m[3].intValue();
			length += m[2].intValue()*8;
			length += m[1].intValue()*16;
			length += m[0].intValue()*24;
			
			type = m[4].intValue();
			
			if(m.length-5 > 0)
			{
				payload = new byte[m.length-5];
				
				for(int i = 0; i < payload.length; i++)
				{
					payload[i] = m[i+5];
				}
			}
			else
				payload = null;
		}
		//if(m[4].intValue() < 8 && m[4].intValue() >= 0)
	}
	
	public Message (int mLength, 
					int mType, 
					byte[] mPayload){
		length = mLength;
		type = mType;
		payload = mPayload;
	}
	
	public Message(){
		length = 0;
		type = 0;
		payload = null;
	}
	
	public int getLength(){
		return length;
	}
	
	public void setLength(int mLength){
		length = mLength;
	}
	
	public int getType(){
		return type;
	}
	
	public void setType(int mType){
		type = mType;
	}
	
	public byte[] getPayload(){
		return payload;
	}
	
	public void setPayload(byte[] mPayload){
		payload = mPayload;
	}
	
	public byte[] createMessage(){
		byte[] b = new byte[length+4];
	
		b[0] = (byte) (length >> 24);
		b[1] = (byte) (length >> 16);
		b[2] = (byte) (length >> 8);
		b[3] = (byte) (length /*>> 0*/);
		b[4] = (byte)type;
		
		for(int i = 0; i < payload.length; i++){
			b[i+5] = payload[i];
		}

		/*
		String l = String.valueOf(length);
		String t = String.valueOf(type);
		String p = String.valueOf(payload);
		String message = new StringBuilder(l).append(t).append(p).toString();
		*/

		return b;
	}
}
