public class ServerClient{
	
	private int peerNum;
	//whether or not this client is choked
	private boolean choked;
	//whether or not this client is interested
	private boolean interested;	

	public ServerClient(int pn){
		peerNum = pn;
		choked = true;
		interested = false;
	}

	public int getPeerNum(){
		return peerNum;
	}

	public boolean getChoked(){
		return choked;
	}

	public boolean getInterested(){
		return interested;
	}

	public void setChoked(boolean b){
		choked = b;
	}

	public void setInterested(boolean b){
		interested = b;
	}

}
