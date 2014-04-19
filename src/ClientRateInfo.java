public class ClientRateInfo{

	public int peerNum;
	public double rate;
	public int numPieces;

	public void calcRate(long unchoking){
		rate = ((double)numPieces)/unchoking;	
	}

}
