
import java.io.*;
import java.util.concurrent.*;

public class Test{


	public static void main(String[] args){
		ExecutorService ex = Executors.newCachedThreadPool();
		File f = new File("Common.cfg");

		for(int i=0; i<10; i++){
			ex.execute(new PeerThread(i+1, f));
		}


	}
}
