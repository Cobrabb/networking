import java.util.ArrayList;
public class PreferredNeighborList{

	private ClientRateInfo[] rates;
	private int currentLast;
	private ArrayList<ClientRateInfo> choked;
	

	public PreferredNeighborList(int num){
		rates = new ClientRateInfo[num];
		currentLast = 0;
		choked = new ArrayList<ClientRateInfo>();
	}


	public void refresh(){
		for(int i=0; i<rates.length; i++){
			rates[i] = null;
		}
		currentLast = 0;
		choked = new ArrayList<ClientRateInfo>();
	}

	public void insert(ClientRateInfo c){
		if(currentLast < rates.length){
			rates[currentLast] = c;
			currentLast++;
			return;
		}
		
		if(rates[currentLast-1].rate >= c.rate){
			choked.add(c);
			return;
		}
		
		choked.add(rates[currentLast-1]);
		rates[currentLast-1] = c;
		sort();
	}

	public void sort(){
		
		for(int i=0; i<rates.length; i++){
			for(int j=0; j<rates.length-1; j++){
				if(rates[j].rate < rates[j+1].rate){
					ClientRateInfo temp = rates[j];
					rates[j] = rates[j+1];
					rates[j+1] = temp;
				}
			}
		}
	}

	public ClientRateInfo[] getInfo(){
		return this.rates;
	}

	public boolean chokedEmpty(){
		return this.choked.size() == 0;	
	}

	public ArrayList<ClientRateInfo> getChoked(){
		return this.choked;
	}

}
