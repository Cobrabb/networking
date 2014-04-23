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
	private BitField localBitField; //a local copy of myBitField
	private BitField clientBitField; 
	private boolean choked; //whether or not the peer is choked
	private boolean interested; //whether or not the peer is interested in me
	private boolean chokechange;
	private RandomAccess file;

	private enum ServerState  {
		NONE,
		SENTHANDSHAKE,
		CONNECTIONOPEN
	};
	private ServerState myServerState;

	public ServerProtocol(int peernum, int numprefneighbor, int unchoking, int opunchoking, String filename, int filesize, int piecesize, BitField b, RandomAccess f){
		peerNum = peernum;
		numberOfPreferredNeighbors= numprefneighbor;
		unchokingInterval = unchoking*1000;
		optimisticUnchokingInterval = opunchoking*1000;
		fileName = filename;
		fileSize = filesize;
		pieceSize = piecesize;
		myBitField = b;
		localBitField = new BitField(b);
		file = f;

	
		this.myServerState = ServerState.NONE;

		choked = true;
		interested = false;
		chokechange = false;

	}

	public boolean getInterested(){
		return this.interested;
	}

	public boolean isOpen(){
		return myServerState == ServerState.CONNECTIONOPEN;
	}

	//things that happen often but do not block on the client happen here.
	public Message tick(long interval){ 

		if(myServerState == ServerState.SENTHANDSHAKE|| isOpen()){
			int firstDiff = myBitField.getFirstDiff(localBitField);
			if(firstDiff>=0){
				localBitField.toggleBitOn(firstDiff);
				return sendHave(firstDiff);
			}
		}

		if(chokechange&&choked){
			chokechange = false;
			return sendChoke();
		}	
		if(chokechange&&!choked){
			chokechange = false;
			return sendUnchoke();
		}	
		return null;
	}

	//message handling happens here
	public Message processInput(Message in){
		System.out.println("ServerProtocol["+peerNum+"]: Got: "+in.getType());
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
		//update bitfield for client, parse the payload of this message.
		//System.out.println("ServerProtocol["+peerNum+"]: Got Bitfield");
		clientBitField = new BitField(in.getPayload());
			
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
				return sendPiece(in);
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

	public Message sendHave(int index){
		//send a have message
		this.myServerState = ServerState.CONNECTIONOPEN;
		byte[] b = ByteBuffer.allocate(4).putInt(index).array();	
		
		return new Message(5, 4, b);
	}

	public Message sendChoke(){
		//send a choke message
		return new Message(1, 0, null);
	}

	public Message sendUnchoke(){
		//send a unchoke message
		return new Message(1, 1, null);
	}

	public Message sendPiece(Message in){
		//send a piece message
		int pLoad = 0;
		byte[] b = in.getPayload();
		if(b==null){
			//System.out.println("ServerProtocol["+peerNum+"]: b was null.");
		}
		for (int i = 0; i < 4; i++)
		{
		   pLoad = (pLoad << 8) + (b[i] & 0xff);
		}
		//System.out.println("ServerProtocol["+peerNum+"]: About to send a piece... piecenum Requested was "+pLoad);
		byte[] message = file.readRAF(pLoad);
		if(message==null){
			//System.out.println("ServerProtocol["+peerNum+"]: message was null.");
		}
		byte[] m = new byte[message.length + b.length];
		for (int i = 0; i < 4; i++){
			m[i] = b[i];
		}
		for (int i = 0; i < message.length; i++){
			m[i+4] = message[i];
		}
		return new Message(m.length + 1, 7, m);
	}

	public Message sendBitField(){
		//send a bitfield message
		//System.out.println("ServerProtocol["+peerNum+"]: Trying to send a Bitfield");
		if(!myBitField.empty()){
			this.myServerState = ServerState.CONNECTIONOPEN;
			byte[] b = myBitField.toByteArray();
			
			//System.out.println("ServerProtocol["+peerNum+"]: Bitfield sent");
			return new Message(b.length + 1, 5, b);	
		}
		//System.out.println("ServerProtocol["+peerNum+"]: Bitfield empty");
		return null;
	}

	//method for the bigserver to call after choking logic
	public void setChoked(boolean choked){
		//System.out.println("ServerProtocol: Someone is calling setChoked with: "+choked);
		if(this.choked != choked){
			this.choked = choked;
			chokechange = true;

		}
	}

	public boolean getChoked(){
		return this.choked;
	}	

}
