import java.nio.ByteBuffer;

public class ClientProtocol{

	private int myPeerNum;
	private boolean interested;
	private boolean choked;
	private boolean requestOut;
	private long nextInt;
	private int havecount; //TODO: remove this once actual interest logic is in place

	//enum for the Client State
	private enum ClientState{
		NONE, //this peer is not currently doing anything
		SENTHANDSHAKE, //this peer has sent the handshake message and is waiting for a reply
		SENTBITFIELD  //this peer has received the handshake reply
	}
	private ClientState myClientState;
	
	
	public ClientProtocol(int peerNum){
		this.myPeerNum = peerNum;
		myClientState = ClientState.NONE;
		interested = false;
		choked = true;
		nextInt = 1000; //TODO: Unhardcode this
		requestOut = false; 
		havecount = 0; //TODO: remove this once actual interest logic is in place
	}

	public boolean isOpen(){
		return myClientState==ClientState.SENTBITFIELD;
	}

	// method for message handling
	public Message processInput(Message in){
		if(in.getType()==8){
			return handShakeIn();
		}
		if(in.getType()==5){
			return bitFieldIn();
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
			return haveIn();
			
		}
		if(in.getType()==7){
			return pieceIn();	
		}
		return null;
	}

	//this happens every so often and does not block on server messages
	public Message tick(long interval){
		if(interval>=nextInt){
			nextInt += 500;
			return null;
		}
		if(!choked&&!requestOut&&interested){
			return sendRequest();
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

	public Message bitFieldIn(){
		if(myClientState==ClientState.SENTBITFIELD){
			//TODO: update bitfield of relevant peer	
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

	public Message  haveIn(){
		//update bitfield of relevant peer
		//calc interested or not interested
		havecount++; //TODO: remove once real interested logic is in place
		boolean oldint = interested; //this will still be necesary once real interested logic is in place
		calculateInterest();


		if(interested&&!oldint){
			return sendInterested();
		}
		else if(!interested){
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

	public Message pieceIn(){
		havecount--; //TODO: remove once real interested logic is in place
		System.out.println("We got a piece!."); //TODO: replace with actual piece handling
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
  		 // "HELL"
                int i = 72<<24+69<<16+76<<8+76;
                byte[] bytes = ByteBuffer.allocate(4).putInt(myPeerNum).array();
                //79 = "O"
                return new Message(i,79, bytes);
	}

	public Message sendBitField(){
		myClientState = ClientState.SENTBITFIELD;
		//TODO: Actually construct a message which has a payload of a bitfield, rather than null
		return new Message(1, 5, null);
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
		//TODO: fix this to have actual interested/not interested calculatons
		if(havecount > 0){ 
			interested = true;
		}
		else{
			interested = false;
		}
	}
}
