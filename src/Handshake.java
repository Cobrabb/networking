

/**
 * @author Ahmad Abukhalil
 *
 */

public class Handshake extends Message{

	private String header;
	private int zeroBits;
	private int peerID;

	public Handshake(String header, 
					 int zeroBits, 
					 int peerID){
		this.header = header;
		this.zeroBits = zeroBits;
		this.peerID = peerID;
	}
	
	public Handshake(){
		header = "HELLO";
		zeroBits = 0;
		peerID = 0;
	}
	
	public String getHeader(){
		return header;
	}
	
	public void setHeader(String newHeader){
		header = newHeader;
	}
	
	public int getZero(){
		return zeroBits;
	}
	
	public void setZero(int newZero){
		zeroBits = newZero;
	}
	
	public int getPeerID(){
		return peerID;
	}
	
	public void setPeerID(int newID){
		peerID = newID;
	}
}
