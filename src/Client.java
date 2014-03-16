public class Client{

	private int peerNum;

	//enum for the Client State
	private enum ClientState{
		NONE, //this peer is not currently doing anything
		SENTHANDSHAKE, //this peer has sent the handshake message and is waiting for a reply
		RECIEVEDHANDSHAKE //this peer has recieved the handshake reply
	}
	private ClientState myClientState;
	
	
	public Client(int peerNum){
		this.peerNum = peerNum;
		myClientState = ClientState.NONE;
	}

	//methods for handling incoming messages
	public void handShakeIn(){
		if(myClientState==ClientState.SENTHANDSHAKE){
			//can accept a handshake in
			myClientState = ClientState.RECIEVEDHANDSHAKE;
			sendBitField();
		}
		else{
			//raise an error
		}
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
		myClientState = ClientState.SENTHANDSHAKE;
	}

	public void sendBitField(){
		//send the bitField message...
		//need a handle on a bitField, etc	
	}

}
