/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteStream {
	
	//public static void main (String[] args) throws IOException{
	public void toByteStream(String inputFile, 
							 String outputFile) throws IOException{
		
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try{
			//in = new FileInputStream("Input.txt");
            //out = new FileOutputStream("Output.txt");
			in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);
            int c;
            
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            
		}catch (FileNotFoundException e){
			e.printStackTrace();
			
		}catch (IOException e) {
			e.printStackTrace();
			
		}finally {
			
            if (in != null) {
                in.close();
            }
            
            if (out != null) {
                out.close();
            }
        }
	}

}
