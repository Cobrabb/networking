import java.util.*;
import java.nio.ByteBuffer;
public class ServerProtocol{
	

	private ServerClient peer1;
	private long unchokingInterval;
	private long optimisticUnchokingInterval;
	private int myPeerNum;
	private BitField myBitField;
	private int numberOfPreferredNeighbors;
	private String fileName;
	private int fileSize;
	private int pieceSize;

	private enum ServerState  {
		NONE,
		SENTHANDSHAKE,
		CONNECTIONOPEN
	};
	private ServerState myServerState;

	public ServerProtocol(int peernum, int numprefneighbor, int unchoking, int opunchoking, String filename, int filesize, int piecesize){
		myPeerNum = peernum;
		numberOfPreferredNeighbors= numprefneighbor;
		unchokingInterval = unchoking*1000;
		optimisticUnchokingInterval = opunchoking*1000;
		fileName = filename;
		fileSize = filesize;
		pieceSize = piecesize;
	
		int bitsize = (int)Math.ceil((double)fileSize/(double)(pieceSize));
		myBitField = new BitField(bitsize);
		peer1 = new ServerClient(0);
		this.myServerState = ServerState.NONE;

	}

	public boolean isOpen(){
		return myServerState == ServerState.CONNECTIONOPEN;
	}

	//things that happen often but do not block on the client happen here.
	public Message tick(long interval){ 
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
		return null;
	}

	//message handling happens here
	public Message processInput(Message in){
		System.out.println("Got "+in.getType());
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
			peer1.setInterested(true);
		}
	}

	public void notInterestedIn(){
		if(myServerState==ServerState.CONNECTIONOPEN){
			peer1.setInterested(false);
		}
	}


	public Message requestIn(Message in){
		if(myServerState==ServerState.CONNECTIONOPEN){
	
			if(!peer1.getChoked()){ //can't send the piece if choked
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
		return new Message(1, 7, null);
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

}
