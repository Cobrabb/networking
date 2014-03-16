
import java.util.*;

public class BitField{

	int size;
	ArrayList<Byte> content;

	public BitField(int size){
		this.size = size;
		this.content = new ArrayList<Byte>();
		
		byte b = 0;	
		for(int i=0; i<size; i++){
			content.add(new Byte(b));
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
	public boolean toggleBit(int pos){
		//figure out which byte contains the relevant bit
		int contentPos = pos/8;

		if(contentPos >= content.size()){
			//invalid input
			return false;
		}
		
		//toggles it on, if it is off. The rest of the method needs to be written still
		byte b = content.get(contentPos).byteValue();
		b += Math.pow(2, pos%8);
		content.set(contentPos, new Byte(b));
		return true;
	}

		

}
