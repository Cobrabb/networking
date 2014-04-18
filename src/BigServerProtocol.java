
import java.util.ArrayList;

public class BigServerProtocol{

	
	private int unchokingInterval;
	private int unchokeTime;

        private int optimisticUnchokingInterval;
	private int optimisticUnchokeTime;

        private BitField myBitField;
        private int numberOfPreferredNeighbors;
        private String fileName;
        private int fileSize;
        private int pieceSize;

	public BigServerProtocol(int numprefneighbor, int unchoking, int opunchoking, String filename, int filesize, int piecesize, BitField b){
                numberOfPreferredNeighbors= numprefneighbor;
                unchokingInterval = unchoking;
                optimisticUnchokingInterval = opunchoking;
                fileName = filename;
                fileSize = filesize;
                pieceSize = piecesize;
		myBitField = b;

		unchokeTime = 0;
		optimisticUnchokeTime = 0;

	}

	public void tick(long interval, ArrayList<ServerThread> servers){
		if(interval >= unchokeTime){
			unchokeTime+=unchokingInterval;
			//TODO: Set choke/unchoke
		}
			
		if(interval >= optimisticUnchokeTime){
			optimisticUnchokeTime+=optimisticUnchokingInterval;
			//TODO: set choke/unchoke
		}
	}
}
