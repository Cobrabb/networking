
import java.io.*;

public class PeerThread extends Thread{

	private int peerNum;
	private int numberOfPreferredNeighbors;
	private int unchokingInterval;
	private int optimisticUnchokingInterval;
	private String fileName;
	private int fileSize;
	private int pieceSize;

	//Client and Server
	private Client client;	

	public PeerThread(int num, File common){
		this.peerNum = num;
		
		//read in the properties from common.cfg.
		BufferedReader br = null;		
		String[] params = new String[6];
		String line;

		try{

			br = new BufferedReader(new FileReader(common));

			for(int i=0; (line=br.readLine()) != null ; i++){
				params[i] = line.split("\\s")[1];	
	
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

		
		//set the properties
		numberOfPreferredNeighbors = Integer.parseInt(params[0]);	
		unchokingInterval = Integer.parseInt(params[1]);	
		optimisticUnchokingInterval = Integer.parseInt(params[2]);
		fileName = params[3];	
		fileSize = Integer.parseInt(params[4]);	
		pieceSize = Integer.parseInt(params[5]);	


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
		try{

			Thread.sleep(1000);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	
	}
}
