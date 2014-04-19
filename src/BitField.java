
import java.util.*;

public class BitField{

	int size;
	ArrayList<Byte> content;

	public BitField(int size){
		this(size, false);
	}

	public ArrayList<Byte> getContent(){
		return this.content;
	}

	public BitField(int size, boolean has){
		this.size = size;
		this.content = new ArrayList<Byte>();
		byte b;
		if(!has)	
			b = 0;	
		else 
			b = -128;

		for(int i=0; i<size; i++){
			content.add(new Byte(b));
		}
		
	}

	public BitField(BitField b){
		this.size = b.size;
		this.content = new ArrayList<Byte>();
		for(int i=0; i<b.getContent().size(); i++){
			this.content.add(b.getContent().get(i));
		}
		
	}

	public BitField(byte[] b){

		this.size = b.length;
		this.content = new ArrayList<Byte>();
		
		for(int i=0; i<size; i++){
			content.add(new Byte(b[i]));
		}


	}

	public String toString(){
		String s = "";
		for(int i=0; i<content.size(); i++){
			s+=content.get(i);
		}

		return s;
	}


	//returns true if successful, false if not
	public boolean toggleBitOn(int pos){
		//figure out which byte contains the relevant bit
		int contentPos = pos/8;
		int bitpos = pos%8;

		if(contentPos >= content.size()){
			//invalid input
			return false;
		}
		
		//toggles it on, if it is off. The rest of the method needs to be written still
		byte b = content.get(contentPos);
		b |= 1 << bitpos;
			
		return true;
	}

	public boolean checkBit(int pos){
		int contentPos = pos/8;
		int bitpos = pos%8;
		if(contentPos >= content.size()){
			//invalid input
			return false;
		}
		
		byte b = content.get(contentPos);
		int bit = b & (1<<bitpos);
		return bit == 1;
		
	}


	public boolean empty(){

		for(int i=0; i<content.size(); i++){
			if(content.get(i)!=0) return false;
		}

		return true;
	}

	public boolean equals(BitField b){
		if(b.getContent().size()!=content.size()) return false;

		for(int i=0; i<content.size(); i++){
			if(b.getContent().get(i)!=content.get(i)){
				return false;
			}
		}

		return true;
	}

	public byte[] toByteArray(){
		byte[] b = new byte[content.size()];

		for(int i=0; i<content.size(); i++){
			b[i] = content.get(i).byteValue();
		}

		return b;
	}
/*	
	public ArrayList<int> compare(BitField b){
		ArrayList<int> diff = new ArrayList<int>();
		if(b.getContent().size()!=content.size()) return null;

		for(int i=0; i<content.size(); i++){
			if(b.getContent().get(i)!=content.get(i)){
				byte b1 = b.getContent().get(i);
				byte b2 = content.get(i);
				for(int j=0; j<8; j++){
					int i1 = b1 >> j & 1;
					int i2 = b2 >> j & 1;
					if(i1!=i2){
						diff.add(j+i*8);
					}
				}
			}
		}

		return diff;

	}
*/
	public int getFirstDiff(BitField b){
		if(b.getContent().size()!=content.size()) return -2;

		for(int i=0; i<content.size(); i++){
			if(b.getContent().get(i)!=content.get(i)){
				byte b1 = b.getContent().get(i);
				byte b2 = content.get(i);
				for(int j=0; j<8; j++){
					int i1 = b1 >> j & 1;
					int i2 = b2 >> j & 1;
					if(i1!=i2){
						return j+i*8;
					}
				}
			}
		}

		return -1;

	}	

		

}
