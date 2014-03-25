/**
 * 
 */

/**
 * @author Ahmad Abukhalil
 *
 */

import java.io.*;

public class ByteStream {
	
	//public static void main (String[] args) throws IOException{
	public byte[] toByteStream(String inputFile) throws IOException{
		
		//FileInputStream in = null;
		//FileOutputStream out = null;
		ByteArrayInputStream in = null;
		ByteArrayOutputStream out = null;
		
		try{
			/*
			//in = new FileInputStream("Input.txt");
            //out = new FileOutputStream("Output.txt");
			in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);
            */
			
			in = new ByteArrayInputStream(inputFile.getBytes());
			out = new ByteArrayOutputStream();
            int c;
            
            while ((c = in.read()) != -1) {
            	//System.out.println("Byte " + in + " is :" + c);
                out.write(c);
            }
            
		}catch (Exception e){
			e.printStackTrace();
			
		}finally {
			
            if (in != null) {
                in.close();
            }
            
            if (out != null) {
                out.close();
            }
        }
		return out.toByteArray();
	}

}
