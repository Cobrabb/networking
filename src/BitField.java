
import java.util.*;

public class BitField{

	int size;
	ArrayList<Byte> content;
	int count = 0;
	int bigSize;

	public BitField(int filesize, int piecesize){
		this(filesize, piecesize, false);
	}

	public ArrayList<Byte> getContent(){
		return this.content;
	}

	public int getNum(){
		return count;
	}

	public boolean done(){
		return count == bigSize+2;
	}

	public BitField(int filesize, int piecesize, boolean has){
		this.bigSize = ((int)Math.floor((float)filesize/(float)piecesize));
		System.out.println(bigSize);
		this.size = (int)(Math.ceil(bigSize/8.0));
		this.content = new ArrayList<Byte>();
		
		byte on = 0;
		if(has) on = 1;

		for(int i=0; i<size; i++){
			byte b = 0;
			for(int j=0; j<8; j++){
				b |= on << j;
				if(i==size-1 && j==bigSize-1){
					break;
				}
			}
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

	public BitField(BitField b, boolean f){
		this.size = b.size;
		this.content = new ArrayList<Byte>();
		byte x = 0;
		for(int i=0; i<b.getContent().size(); i++){
			this.content.add(new Byte(x));
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
		if((b & (1 << bitpos)) == 0) count++;
		b |= 1 << bitpos;

		content.set(contentPos, new Byte(b));
			
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

	public int getFirstHas(BitField b){
		if(b.getContent().size()!=content.size()) return -2;

		for(int i=0; i<content.size(); i++){
			if(b.getContent().get(i)!=content.get(i)){
				byte b1 = b.getContent().get(i);
				byte b2 = content.get(i);
				for(int j=7; j>-1; j--){
					int i1 = b1 >> j & 1;
					int i2 = b2 >> j & 1;
					if(i1==1&&i2==0){
						return j+i*8;
					}
				}
			}
		}

		return -1;
	}

		

}
