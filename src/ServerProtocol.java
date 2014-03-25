import java.util.*;
public class ServerProtocol{
	

	private ServerClient peer1;
	private long unchokingInterval;
	private long optimisticUnchokingInterval;

	private enum ServerState  {
		NONE,
		SENTHANDSHAKE,
		CONNECTIONOPEN
	};
	private ServerState myServerState;

	public ServerProtocol(int peernum){
		peer1 = new ServerClient(peernum);
		this.myServerState = ServerState.NONE;

		//TODO: Unhardcode these values
		unchokingInterval = 4000;
		optimisticUnchokingInterval = 5000;
	}

	public boolean isOpen(){
		return myServerState == ServerState.CONNECTIONOPEN;
	}

	//things that happen often but do not block on the client happen here.
	public String tick(long interval){ 
		Random rand = new Random();
		if(myServerState == ServerState.SENTHANDSHAKE|| isOpen()){

			if(rand.nextInt(2000000)==75){ //TODO: replace with real logic to check to see if a piece has come in.
				return sendHave();
			}

		}

		if(interval>=unchokingInterval){

			unchokingInterval += 4000; //TODO: Unhardcode this value

			if(isOpen()){ // should not send choke/unchoke if the connection is not open yet.
		
				//currently, the server just toggles every interval 
				//TODO: replace with real logic for checking for choked/unchoked
				if(peer1.getChoked()&&peer1.getInterested()){
					peer1.setChoked(false);
					return sendUnchoke();
				}
				else if(!peer1.getChoked()){
					peer1.setChoked(true);
					return sendChoke();
				}
			}
	
			
		}
		return "";
	}

	//message handling happens here
	public String processInput(String s){
		if(s.equals("Handshake")){
			return handShakeIn();
		}	
		else if(s.equals("Bitfield")){
			return bitFieldIn();
		}
		else if(s.equals("Interested")){
			interestedIn();
			return "";
		}
		else if(s.equals("NotInterested")){
			notInterestedIn();
			return "";
		}
		else if(s.equals("Request")){
			return requestIn();
		}
		return "error";
	}

	//methods for receiving messages

	public String handShakeIn(){
		if(myServerState==ServerState.NONE){ //handshake can only happen if nothing else has
			return sendHandShake();
		}
		return "error";
	}

	public String  bitFieldIn(){ 
		//TODO: update bitfield for client, parse the payload of this message.
		if(myServerState==ServerState.SENTHANDSHAKE){ //bitfield can only happen after handshake but before connection open
			return sendBitField();	
		}
		return "error";
	}

	public void interestedIn(){
		if(myServerState==ServerState.CONNECTIONOPEN){
			peer1.setInterested(true);
		}
	}

	public void notInterestedIn(){
		if(myServerState==ServerState.CONNECTIONOPEN){
			peer1.setInterested(false);
		}
	}


	public String requestIn(){
		if(myServerState==ServerState.CONNECTIONOPEN){
	
			if(!peer1.getChoked()){ //can't send the piece if choked
				return sendPiece();
			}
			else return "";
		}
		return "error";
	}


	//methods for sending messages
	public String sendHandShake(){
		this.myServerState = ServerState.SENTHANDSHAKE;
		return "Handshake";
	}

	public String sendHave(){
		//send a have message
		this.myServerState = ServerState.CONNECTIONOPEN;
		return "Have";
	}

	public String sendChoke(){
		//send a choke message
		return "Choke";
	}

	public String sendUnchoke(){
		//send a unchoke message
		return "Unchoke";
	}

	public String sendPiece(){
		//send a piece message
		return "Piece";
	}

	public String sendBitField(){
		//send a bitfield message

		if(false){ //TODO: replace this with logic to see if the server actually has the files
			this.myServerState = ServerState.CONNECTIONOPEN;
			return "Bitfield";	
		}
		return "";
	}

}
