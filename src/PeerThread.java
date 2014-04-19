
import java.util.concurrent.*;
import java.io.*;
import java.util.ArrayList;

class PeerInfo{
	public int peerNum;
	public String hostName;
	public int portNum;
	public boolean hasFile;
}

public class PeerThread extends Thread{

	private int peerNum;
	private int numberOfPreferredNeighbors;
	private int unchokingInterval;
	private int optimisticUnchokingInterval;
	private String fileName;
	private int fileSize;
	private int pieceSize;
	private ArrayList<PeerInfo> peers;
	private boolean hasFile;
	private int portNum;
	private BitField myBitField;
	private ArrayList<ClientRateInfo> rates;

	//Client and Server
	private BigServerThread server;
	private ArrayList<ClientThread> clients;

	public PeerThread(int num, File common, File peerInfo){
		this.peerNum = num;
		
		//read in the properties from common.cfg.
		BufferedReader br = null;		
		String[] params = new String[6];
		
		String line;
		peers = new ArrayList<PeerInfo>();

		try{

			br = new BufferedReader(new FileReader(common));

			for(int i=0; (line=br.readLine()) != null ; i++){
				params[i] = line.split("\\s")[1];	
	
			}
			

			br.close();
			br = new BufferedReader(new FileReader(peerInfo));
		
			for(int i=0; (line=br.readLine()) != null; i++){
				String[] params2 = line.split("\\s");
				PeerInfo p = new PeerInfo();
				p.peerNum = Integer.parseInt(params2[0]);
				p.hostName = params2[1];
				p.portNum = Integer.parseInt(params2[2]);

				if(params2[3] == "1") p.hasFile = true;
				else p.hasFile = false;
				
				if(p.peerNum == this.peerNum){
					this.hasFile = p.hasFile;
					this.portNum = p.portNum;
				}
				else peers.add(p);	
			}

			br.close();
		}
		catch(FileNotFoundException e){
			System.out.println("The file specified was unable to be opened.");
			e.printStackTrace();
		}
		catch(IOException e){
			System.out.println("Something went wrong while reading the file.");
			e.printStackTrace();
		}

		
		rates = new ArrayList<ClientRateInfo>();
	
		//set the properties
		numberOfPreferredNeighbors = Integer.parseInt(params[0]);	
		unchokingInterval = Integer.parseInt(params[1]);	
		optimisticUnchokingInterval = Integer.parseInt(params[2]);
		fileName = params[3];	
		fileSize = Integer.parseInt(params[4]);	
		pieceSize = Integer.parseInt(params[5]);	

		myBitField = new BitField((int)Math.ceil((float)fileSize/(float)pieceSize), hasFile);

		server = new BigServerThread(peerNum, numberOfPreferredNeighbors, unchokingInterval, optimisticUnchokingInterval, fileName, fileSize, pieceSize, portNum, myBitField, rates);	

		clients = new ArrayList<ClientThread>();
		for(int i=0; i<peers.size(); i++){
			ClientRateInfo info = new ClientRateInfo();
			info.rate = 0;
			info.peerNum = peers.get(i).peerNum;
			ClientThread c = new ClientThread(peers.get(i).peerNum, fileSize, pieceSize, peers.get(i).portNum, peers.get(i).hostName,fileName, myBitField, info);
			clients.add(c);
			rates.add(info);
		}
	}

	//getters; setters should not be allowed for the default properties
	public int getPeerNum(){
		return this.peerNum;
	}

	public int getNumberOfPreferredNeighbors(){
		return this.numberOfPreferredNeighbors;
	}

	public int getUnchokingInterval(){
		return this.unchokingInterval;
	}

	public int getOptimisticUnchokingInterval(){
		return this.optimisticUnchokingInterval;
	}

	public String getFileName(){
		return this.fileName;
	}

	public int getFileSize(){
		return this.fileSize;
	}
	
	public int getPieceSize(){
		return this.pieceSize;
	}

	//main method for the thread
	//required by Thread
	public void run(){

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(server);
		for(int i=0; i<clients.size(); i++){
			executor.execute(clients.get(i));
		}
		
	}
}
