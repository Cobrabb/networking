public class ClientProtocol{

	private int peerNum;
	private boolean interested;
	private boolean choked;

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
	}

	//methods for handling incoming messages
	public void handShakeIn(){
		if(myClientState==ClientState.SENTHANDSHAKE){
			//can accept a handshake in
			System.out.println("Recieved a handshake message.");
			sendBitField();
		}
		else{
			//raise an error
		}
	}	

	public void bitFieldIn(){
		if(myClientState==ClientState.SENTBITFIELD){
			System.out.println("Recieved a bitfield message.");
			//update bitfield of relevant peer	
			//calc interested or not interested
			calculateInterest();
			System.out.println("Recieved a bitfield message.");
			
			if(interested){
				sendInterested();
			}
			else{
				sendNotInterested();
			}

			if(interested&&!choked){
				sendRequest();
			}
		}
		else{
			//error
		}
	}

	public void haveIn(){
		System.out.println("Recieved a have message.");
		//update bitfield of relevant peer
		//calc interested or not interested
		calculateInterest();

		if(interested&&!choked){
			sendRequest();
		}
		
	}

	public void chokedIn(){
		System.out.println("Recieved a choked message.");
		choked = true;
	}

	public void unchokedIn(){
		System.out.println("Recieved a unchoked message.");
		choked = false;
	}

	public void pieceIn(){
		System.out.println("Recieved a piece message.");
	}

	//methods for sending messages
	public void initiateContact(){
		//use the peer num to get a handle on another peer
		//send the peer a handshake message
		sendHandShake();
	}

	public void sendHandShake(){
		//create a handshake message
		//send
		System.out.println("Sending a handshake message from the client to peer "+peerNum);
		myClientState = ClientState.SENTHANDSHAKE;
	}

	public void sendBitField(){
		//send the bitField message...
		//need a handle on a bitField, etc	
		System.out.println("Sending a bitfield message from the client to peer "+peerNum);
		myClientState = ClientState.SENTBITFIELD;
	}

	public void sendRequest(){
		//create a request message
		//send
		System.out.println("Sending a request message from the client to peer "+peerNum);
	}

	public void sendInterested(){
		System.out.println("Sending an interested message from the client to peer "+peerNum);
	}

	public void sendNotInterested(){
		System.out.println("Sending a not interested message from the client to peer "+peerNum);
	}

	//methods for calculation
	public void calculateInterest(){
		interested = true;
	}
}
