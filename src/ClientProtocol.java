import java.nio.ByteBuffer;


public class ClientProtocol{

	private int myPeerNum; //refers to the servers peernum, not the peer which is launching this client
	private boolean interested;
	private boolean choked;
	private boolean requestOut;
	private int fileSize;
	private int pieceSize;
	private String fileName;
	private BitField serverBitField;
	private BitField myBitField;
	private ClientRateInfo myRate;

	//enum for the Client State
	private enum ClientState{
		NONE, //this peer is not currently doing anything
		SENTHANDSHAKE, //this peer has sent the handshake message and is waiting for a reply
		SENTBITFIELD  //this peer has received the handshake reply
	}
	private ClientState myClientState;
	
	
	public ClientProtocol(int peerNum, int filesize, int piecesize, String fName, BitField mybField, ClientRateInfo c){
		this.myPeerNum = peerNum;
		myClientState = ClientState.NONE;
		interested = false;
		choked = true;
		pieceSize = piecesize;
		fileSize = filesize;
		fileName = fName;
		myBitField = mybField;
		myRate = c;
		
		requestOut = false; 
	}

	public boolean isOpen(){
		return myClientState==ClientState.SENTBITFIELD;
	}

	// method for message handling
	public Message processInput(Message in){
		System.out.println("Got: "+in.getType());
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
		if(!choked&&!requestOut&&interested){
			return sendRequest();
		}
		else{
			boolean oldint = interested; //this will still be necesary once real interested logic is in place
			calculateInterest();


			if(interested&&!oldint){
				return sendInterested();
			}
			else if(!interested&&oldint){
				return sendNotInterested();
			}
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
		int index = 0;
		byte[] b = in.getPayload();
		for(int i = 0; i < b.length; i++){
			index = (index << 8) + (b[i] & 0xff);
		}
		myBitField.toggleBitOn(index);
		//calc interested or not interested
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

	public void chokeIn(){
		choked = true;
		requestOut = false; //if a choke message comes in, the previously sent request is invalid
	}

	public void unchokeIn(){
		choked = false;
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
		
		for(int i = 0; i < b.length; i++){
			newPiece[i] = b[i+4];
		}
		
		RandomAccess r = new RandomAccess(pieceSize, fileName);
		r.writeRAF(newPiece, index);
		
		requestOut = false;

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
		byte[] pnum = ByteBuffer.allocate(4).putInt(myPeerNum).array();
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
		return new Message(1, 6, null);
	}

	public Message sendInterested(){
		return new Message(1, 2, null);
	}

	public Message sendNotInterested(){
		return new Message(1, 3, null);
	}

	//methods for calculation
	public void calculateInterest(){
		if(!myBitField.equals(serverBitField)){
			interested = true;
		}else{
			interested = false;
		}
	}
}
