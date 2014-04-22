import java.util.ArrayList;
public class ChokingThread extends Thread{

	private ArrayList<ServerThread> servers;
	private long unchokingInterval;
	private long optimisticUnchokingInterval;
	private long lastUnchoked;
	private long lastOptimisticUnchoked;
	private int numberOfPreferredNeighbors;
	private long openTime;
	private ArrayList<ClientRateInfo> rates;
	private PreferredNeighborList preferredNeighbors;

	public ChokingThread(ArrayList<ServerThread> s, int unchoking, int optimisticunchoking, int numprefneighbor, ArrayList<ClientRateInfo> r){
		servers = s;
		unchokingInterval = unchoking*1000;
		optimisticUnchokingInterval = optimisticunchoking*1000;
		numberOfPreferredNeighbors = numprefneighbor;
	    	openTime = System.currentTimeMillis();
		rates = r;
		preferredNeighbors = new PreferredNeighborList(numprefneighbor);
	}

	public void run(){
		//System.out.println("ChokingThread: Launching");
		while(true){
			long currentTime = System.currentTimeMillis();
			
			if(currentTime-openTime >= lastUnchoked){
				//System.out.println("ChokingThread: Time to calculate unchoking");
				preferredNeighbors.refresh();

				for(int i=0; i<rates.size(); i++){
					rates.get(i).calcRate(unchokingInterval);	
					preferredNeighbors.insert(rates.get(i));
				}
				ClientRateInfo[] c; 
				c = preferredNeighbors.getInfo();
				boolean choked = true;
				for(int i=0; i<servers.size(); i++){
					choked = true;
					for(int j=0; j<c.length; j++){
						if(c[j].peerNum == servers.get(i).getPeerNum()){
							servers.get(i).setChoked(false);
							choked = false;
						}
					}	
					if(choked){
						servers.get(i).setChoked(true);
					}
				}

					
				lastUnchoked = currentTime-openTime + unchokingInterval;
				
			}
			if(currentTime-openTime >= lastOptimisticUnchoked){
				//System.out.println("ChokingThread: Time to calculate unchoking optimistically");
				ArrayList<ClientRateInfo> c;
				if(preferredNeighbors.chokedEmpty()){
					c = rates;
				}	
				else{
					c = preferredNeighbors.getChoked();
				}

				int randomPeer = (int)(Math.random() * (c.size()));
				int randomPeerNum = c.get(randomPeer).peerNum;
				for(int i=0; i<servers.size(); i++){
					if(servers.get(i).getPeerNum()==randomPeerNum){
						servers.get(i).setChoked(false);
					}
				}

				lastOptimisticUnchoked = currentTime-openTime + optimisticUnchokingInterval;

			}

		}
	}

}
