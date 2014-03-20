public class ServerProtocol{
	
	private int peerNum;
	private boolean choked;
	private boolean interested;

	private enum ServerState  {
		NONE,
		SENTHANDSHAKE,
		CONNECTIONOPEN
	};
	private ServerState myServerState;

	public ServerProtocol(int peernum){
		this.peerNum = peernum;
		this.myServerState = ServerState.NONE;
	}

	//methods for receiving messages

	public void handShakeIn(){
		sendHandShake();
	}

	public void bitFieldIn(){
		//update bitfield for client
		//if this has pieces send a bitfield reply
		sendBitField();	
		this.myServerState = ServerState.CONNECTIONOPEN;
	}

	public void interestedIn(){
		this.interested = true;
	}

	public void notInterestedIn(){
		this.interested = false;
	}


	public void requestIn(){
		if(!choked){
			sendPiece();
		}
	}


	//methods for sending messages
	public void sendHandShake(){
		this.myServerState = ServerState.SENTHANDSHAKE;
	}

	public void sendHave(){
		//send a have message
	}

	public void sendChoke(){
		//send a choke message
	}

	public void sendUnchoke(){
		//send a unchoke message
	}

	public void sendPiece(){
		//send a piece message
	}

	public void sendBitField(){
		//send a bitfield message
	}

}
