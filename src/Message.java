import java.nio.ByteBuffer;

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
		if(m[4] == 79)
		{			
			for (int i = 28; i < m.length; i++)
			{
			   peerID = (peerID << 8) + (m[i] & 0xff);
			}
			
			length = 32;
			type = 8;
			payload = null;
		}
		else 
		{
			
			for (int i = 0; i < 4; i++)
			{
			   length = (length << 8) + (m[i] & 0xff);
			}
			
			type = m[4];
			
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
	
	public int getPeerID()
	{
		return peerID;
	}
	
	public byte[] createMessage(){
		byte[] b;
		if(type == 8){
			b = new byte[32];
			byte[] hello = "HELLO".getBytes();
			for(int i=0; i<5; i++){
				b[i] = hello[i];
			}
		
			byte[] peer = ByteBuffer.allocate(4).putInt(peerID).array();	
			if(payload!=null){
				for(int i=0; i<27; i++){
					b[i+5] = payload[i];
				}
			}
			
		}
		else{
			b = new byte[length+4];
	
			b[0] = (byte) (length >> 24);
			b[1] = (byte) (length >> 16);
			b[2] = (byte) (length >> 8);
			b[3] = (byte) (length /*>> 0*/);
			b[4] = (byte)type;
	
			if(payload!=null){
				for(int i = 0; i < payload.length; i++){
					b[i+5] = payload[i];
				}
			}
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
