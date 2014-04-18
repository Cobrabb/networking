import java.util.*;
import java.nio.ByteBuffer;
public class ServerProtocol{
	

	private long unchokingInterval;
	private long optimisticUnchokingInterval;
	private int peerNum;
	private int numberOfPreferredNeighbors;
	private String fileName;
	private int fileSize;
	private int pieceSize;
	private BitField myBitField;
	private boolean choked; //whether or not the peer is choked
	private boolean interested; //whether or not the peer is interested in me
	private boolean chokechange;

	private enum ServerState  {
		NONE,
		SENTHANDSHAKE,
		CONNECTIONOPEN
	};
	private ServerState myServerState;

	public ServerProtocol(int peernum, int numprefneighbor, int unchoking, int opunchoking, String filename, int filesize, int piecesize, BitField b){
		peerNum = peernum;
		numberOfPreferredNeighbors= numprefneighbor;
		unchokingInterval = unchoking*1000;
		optimisticUnchokingInterval = opunchoking*1000;
		fileName = filename;
		fileSize = filesize;
		pieceSize = piecesize;
		myBitField = b;
	
		this.myServerState = ServerState.NONE;

		choked = true;
		interested = false;
		chokechange = false;

	}

	public boolean isOpen(){
		return myServerState == ServerState.CONNECTIONOPEN;
	}

	//things that happen often but do not block on the client happen here.
	public Message tick(long interval){ 
		Random rand = new Random();
		if(myServerState == ServerState.SENTHANDSHAKE|| isOpen()){

			if(rand.nextInt(20000)==75){ //TODO: replace with real logic to check to see if a piece has come in.
				return sendHave();
			}

		}

		if(interval>=unchokingInterval){

			unchokingInterval += 4000; //TODO: Unhardcode this value

			if(isOpen()){ // should not send choke/unchoke if the connection is not open yet.
		
				//currently, the server just toggles every interval 
				//TODO: replace with real logic for checking for choked/unchoked
				if(choked&&interested){
					choked = false;
					return sendUnchoke();
				}
				else if(!choked){
					choked = true;
					return sendChoke();
				}
			}
	
			
		}
		return null;
	}

	//message handling happens here
	public Message processInput(Message in){
		if(in.getType()==8){
			return handShakeIn();
		}	
		else if(in.getType()==5){
			return bitFieldIn(in);
		}
		else if(in.getType()==2){
			interestedIn();
			return null;
		}
		else if(in.getType()==3){
			notInterestedIn();
			return null;
		}
		else if(in.getType()==6){
			return requestIn(in);
		}
		return null;
	}

	//methods for receiving messages

	public Message handShakeIn(){
		if(myServerState==ServerState.NONE){ //handshake can only happen if nothing else has
			return sendHandShake();
		}
		return null;
	}

	public Message  bitFieldIn(Message in){ 
		//TODO: update bitfield for client, parse the payload of this message.
		if(myServerState==ServerState.SENTHANDSHAKE){ //bitfield can only happen after handshake but before connection open
			return sendBitField();	
		}
		return null;
	}

	public void interestedIn(){
		if(myServerState==ServerState.CONNECTIONOPEN){
			interested = true;
		}
	}

	public void notInterestedIn(){
		if(myServerState==ServerState.CONNECTIONOPEN){
			interested = false;
		}
	}


	public Message requestIn(Message in){
		if(myServerState==ServerState.CONNECTIONOPEN){
	
			if(!choked){ //can't send the piece if choked
				return sendPiece();
			}
			else return null;
		}
		return null;
	}


	//methods for sending messages
	public Message sendHandShake(){
		this.myServerState = ServerState.SENTHANDSHAKE;
		return new Message(0, 8, null);
	}

	public Message sendHave(){
		//send a have message
		this.myServerState = ServerState.CONNECTIONOPEN;
		return new Message(1, 4, null);
	}

	public Message sendChoke(){
		//send a choke message
		return new Message(1, 0, null);
	}

	public Message sendUnchoke(){
		//send a unchoke message
		return new Message(1, 1, null);
	}

	public Message sendPiece(){
		//send a piece message

		//TODO: actually allocate a piece and put it into the payload
		byte[] b = new byte[3];
		for(int i=0; i<3; i++) b[i] = (byte)fileName.charAt(i);
		return new Message(4, 7, b);
	}

	public Message sendBitField(){
		//send a bitfield message

		if(false){ //TODO: replace this with logic to see if the server actually has the files
			this.myServerState = ServerState.CONNECTIONOPEN;
			//TODO: have the server actually send its bitfield
			return new Message(1, 5, null);	
		}
		return null;
	}

	//method for the bigserver to call after choking logic
	public void setChoked(boolean choked){
		if(this.choked != choked){
			this.choked = choked;
			chokechange = true;

		}
	}

}
