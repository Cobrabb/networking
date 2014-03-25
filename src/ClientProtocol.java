public class ClientProtocol{

	private int peerNum;
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
		this.peerNum = peerNum;
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
	public String processInput(String s){
		if(s.equals("Handshake")){
			return handShakeIn();
		}
		if(s.equals("Bitfield")){
			return bitFieldIn();
		}
		if(s.equals("Choke")){
			chokeIn();
			return "";
		}
		if(s.equals("Unchoke")){
			unchokeIn();
			return "";
		}
		if(s.equals("Have")){
			return haveIn();
			
		}
		if(s.equals("Piece")){
			return pieceIn();	
		}
		return "error";
	}

	//this happens every so often and does not block on server messages
	public String tick(long interval){
		if(interval>=nextInt){
			nextInt += 500;
			return "";
		}
		if(!choked&&!requestOut&&interested){
			return sendRequest();
		}
		return "";
	}

	//methods for handling incoming messages
	public String handShakeIn(){
		if(myClientState==ClientState.SENTHANDSHAKE){
			//can accept a handshake in
			return sendBitField();
		}
		return "error";
	}	

	public String bitFieldIn(){
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
		return "error";
	}

	public String haveIn(){
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

		return "";

		
	}

	public void chokeIn(){
		choked = true;
		requestOut = false; //if a choke message comes in, the previously sent request is invalid
	}

	public void unchokeIn(){
		choked = false;
	}

	public String pieceIn(){
		havecount--; //TODO: remove once real interested logic is in place
		System.out.println("We got a piece!."); //TODO: replace with actual piece handling
		requestOut = false;

		calculateInterest();
		if (!interested){
			return "NotInterested";
		}
		return "";
	}

	//methods for sending messages
	public void initiateContact(){
		sendHandShake();
	}

	public void sendHandShake(){
		myClientState = ClientState.SENTHANDSHAKE;
	}

	public String sendBitField(){
		//TODO: Actually construct a message which has a payload of a bitfield
		myClientState = ClientState.SENTBITFIELD;
		return "Bitfield";
	}

	public String sendRequest(){
		requestOut = true;	
		return "Request";
	}

	public String sendInterested(){
		return "Interested";
	}

	public String sendNotInterested(){
		return "NotInterested";
	}

	//methods for calculation
	public void calculateInterest(){
		//TODO: fix this message to have actual interested/not interested calculatons
		if(havecount > 0){ 
			interested = true;
		}
		else{
			interested = false;
		}
	}
}
