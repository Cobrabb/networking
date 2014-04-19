
import java.io.*;

public class peerProcess{

	public static void main(String[] args){

		if(args.length!=1){

			System.out.println("Please enter the peer number.");
			return;
		}

		int peerNum = Integer.parseInt(args[0]);
		PeerThread peer = new PeerThread(peerNum, new File("Common.cfg"), new File("PeerInfo.cfg"));
		
		peer.run();		
	}
}
