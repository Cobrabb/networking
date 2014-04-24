import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;


public class ClientProtocol{

	private int myPeerNum; //refers to the servers peernum, not the peer which is launching this client
	private int thisPeerNum; //refers to the peer which is launching this client
	private boolean interested;
	private boolean choked;
	private boolean requestOut;
	private int fileSize;
	private int pieceSize;
	private String fileName;
	private BitField serverBitField;
	private BitField myBitField;
	private ClientRateInfo myRate;
	private RandomAccess file;
	private static PrintWriter log;
	private static boolean done;
	

	//enum for the Client State
	private enum ClientState{
		NONE, //this peer is not currently doing anything
		SENTHANDSHAKE, //this peer has sent the handshake message and is waiting for a reply
		SENTBITFIELD,  //this peer has received the handshake reply
		CONNECTIONOPEN
	}
	private ClientState myClientState;
	
	
	public ClientProtocol(boolean done, int tpeernum, int peerNum, int filesize, int piecesize, String fName, BitField mybField, ClientRateInfo c, RandomAccess r, PrintWriter log){
		this.myPeerNum = peerNum;
		myClientState = ClientState.NONE;
		interested = false;
		choked = true;
		pieceSize = piecesize;
		fileSize = filesize;
		fileName = fName;
		myBitField = mybField;
		myRate = c;
		thisPeerNum = tpeernum;
		file = r;
		this.log = log;
		this.done = done;
		
		requestOut = false; 
	}

	public boolean isOpen(){
		return myClientState==ClientState.CONNECTIONOPEN;
	}

	// method for message handling
	public Message processInput(Message in){
		System.out.println("ClientProtocol["+myPeerNum+"]: Got: "+in.getType());
		if(in.getType()==8){
			return handShakeIn();
		}
		if(in.getType()==5){
			return bitFieldIn(in);
		}
		if(in.getType()==0){
			chokeIn();
			return null;
		}
		if(in.getType()==1){
			unchokeIn();
			return null;
		}
		if(in.getType()==4){
			return haveIn(in);
			
		}
		if(in.getType()==7){
			return pieceIn(in);	
		}
		return null;
	}

	//this happens every so often and does not block on server messages
	public Message tick(long interval){

		if(myBitField.done()&&!done){
			log.println(new Date().toString()+": Peer "+thisPeerNum+" has download the complete file.");
			log.flush();
			System.out.println("DONE!");
			done = true;	
		}
		if(myClientState!=ClientState.CONNECTIONOPEN) return null;
		if(!choked&&!requestOut&&interested){
			return sendRequest();
		}

		boolean oldint = interested; //this will still be necesary once real interested logic is in place
		calculateInterest();


		if(interested&&!oldint){
			return sendInterested();
		}
		else if(!interested&&oldint){
			return sendNotInterested();
		}

		return null;
	}

	//methods for handling incoming messages
	public Message handShakeIn(){
		if(myClientState==ClientState.SENTHANDSHAKE){
			//can accept a handshake in
			return sendBitField();
		}
		return null;
	}	

	public Message bitFieldIn(Message in){
		if(myClientState==ClientState.SENTBITFIELD){
			if(myClientState!=ClientState.CONNECTIONOPEN) myClientState = ClientState.CONNECTIONOPEN;
			serverBitField = new BitField(in.getPayload());
			
			calculateInterest();
			
			if(interested){
				return sendInterested();
			}
			else{
				return sendNotInterested();
			}

		}
		return null; 
	}

	public Message  haveIn(Message in){
		//update bitfield of relevant peer
		if(serverBitField == null){
			serverBitField = new BitField(myBitField, false);
		}
		int index = 0;
		byte[] b = in.getPayload();
		for(int i = 0; i < b.length; i++){
			index = (index << 8) + (b[i] & 0xff);
		}
		//System.out.println("ClientProtocol["+myPeerNum+"]: The server claims that it has piece "+index);
		log.println(new Date().toString()+": Peer "+thisPeerNum+" recieved a 'have' message from  "+myPeerNum+" for the piece "+index);
		log.flush();

		serverBitField.toggleBitOn(index);
		//calc interested or not interested
		boolean oldint = interested; //this will still be necesary once real interested logic is in place
		calculateInterest();

		
		if(myClientState!=ClientState.CONNECTIONOPEN) myClientState = ClientState.CONNECTIONOPEN;
		if(interested&&!oldint){
			return sendInterested();
		}
		else if(!interested&&oldint){
			return sendNotInterested();
		}

		return null;

		
	}

	public void chokeIn(){
		choked = true;
		requestOut = false; //if a choke message comes in, the previously sent request is invalid
		log.println(new Date().toString()+": Peer "+thisPeerNum+" is choked by "+myPeerNum);
		log.flush();
	}

	public void unchokeIn(){
		choked = false;
		log.println(new Date().toString()+": Peer "+thisPeerNum+" is unchoked by "+myPeerNum);
		log.flush();
	}

	public Message pieceIn(Message in){
		//calculate the rate
		myRate.numPieces++;
				
		
		int index = 0;
		byte[] b = in.getPayload();
		byte[] newPiece = new byte[b.length-4];
		for(int i = 0; i < 4; i++){
			index = (index << 8) + (b[i] & 0xff);
		}
		
		myBitField.toggleBitOn(index);
		
		for(int i = 0; i < newPiece.length; i++){
			newPiece[i] = b[i+4];
		}
		
		file.writeRAF(newPiece, index);
		
		requestOut = false;

		log.println(new Date().toString()+": Peer "+thisPeerNum+" has downloaded the piece  "+index+" from "+myPeerNum+". Now the number of pieces it has is "+myBitField.getNum());
		log.flush();
		calculateInterest();
		if (!interested){
			return new Message(1, 3, null);
		}
		return null;
	}

	//methods for sending messages
	public Message initiateContact(){
		return sendHandShake();
	}

	public Message sendHandShake(){
		myClientState = ClientState.SENTHANDSHAKE;
		
		byte[] b = new byte[27];
		byte[] pnum = ByteBuffer.allocate(4).putInt(thisPeerNum).array();
		for(int i=0; i<4; i++){
			b[23+i] = pnum[i];
		}
		
                return new Message(0,8, b);
	}

	public Message sendBitField(){
		myClientState = ClientState.SENTBITFIELD;
		byte[] b = myBitField.toByteArray();
			
		return new Message(b.length + 1, 5, b);	
	}

	public Message sendRequest(){
		requestOut = true;
		int i = myBitField.getFirstHas(serverBitField);
		//System.out.println("ClientProtocol["+myPeerNum+"]: About to send a request... here is what I am requesting: "+i);
		byte[] pnum = ByteBuffer.allocate(4).putInt(i).array();
		return new Message(5, 6, pnum);
	}

	public Message sendInterested(){
		return new Message(1, 2, null);
	}

	public Message sendNotInterested(){
		return new Message(1, 3, null);
	}

	//methods for calculation
	public void calculateInterest(){
		int get = -1;
		if(serverBitField!=null){
			get = myBitField.getFirstHas(serverBitField);
		}
		if(get>=0){
			interested = true;
		}else{
			interested = false;
		}
	}
}
