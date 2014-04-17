
import java.util.*;

public class BitField{

	int size;
	ArrayList<Byte> content;

	public BitField(int size){
		this(size, false);
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
	

		

}
