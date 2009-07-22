package yeti.environments;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

//import yeti.environments.csharp.YetiCsharpProperties;
/**
 * Class that holds the methods with which the Csharp environment
 * in Java can obtain the information YETI needs
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 15, 2009
 */
public class YetiServerSocket {
	
    //public static ArrayList<String> allTypes = new ArrayList();
	/**
	 * It is a method that gets the data from the specified socket
	 * it will hold until the other par sends data
	 * 
	 * @param soc the integer that specifies the socket to listen
	 * @return it returns an ArrayList<String> so we can use the info
	 */
	public ArrayList<String> getData(int soc) throws IOException
	{
		ServerSocket s;
		ArrayList<String> temp = new ArrayList<String>();
		
		s=new ServerSocket(soc);
		//Socket s1 = s.accept();
		boolean read=true;

		//OutputStream output = s1.getOutputStream();
		//InputStream input = s1.getInputStream();
		//PrintStream ps = new PrintStream(output);
		//System.out.println(input.toString());
		String received="INITIAL";
		
		int i = 0;
		//while (true){
		while(read)
		try {
		//Thread.sleep(1000);
			System.out.println("%%%%%%%%%%%%%%%%%%");
		//Hold until data are sent by the othe part
		Socket s1 = s.accept();
		InputStream input = s1.getInputStream();
		//Convert stream to string so we can manipulate it
		received = YetiServerSocket.convertStreamToString(input);
		//when the other part sends "stop" the getData method will terminate
		if("stop".equals(received.trim()))
			read =false;
		else
		//System.out.println(received);
		temp.add(received);
		//allTypes.add(received);
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		i++;
		//ps.println("My message "+ i);
		
		//}
		return temp;
		
		
	}
	
	
	public static void main(String []args) throws UnknownHostException, IOException{
	
		
		
	/*YetiCsharpProperties pr = new YetiCsharpProperties();
	YetiServerSocket con = pr.getServerSocket();
	
	/*allTypes = con.getData();
	for(String s: con.allTypes)
		System.out.println(s);
	/*ServerSocket s;
		
	s=new ServerSocket(2000);
	//Socket s1 = s.accept();
	boolean read=true;

	//OutputStream output = s1.getOutputStream();
	//InputStream input = s1.getInputStream();
	//PrintStream ps = new PrintStream(output);
	//System.out.println(input.toString());
	String received="INITIAL";
	
	int i = 0;
	//while (true){
	while(read)
	try {
	//Thread.sleep(1000);
	Socket s1 = s.accept();
	InputStream input = s1.getInputStream();
	received = YetiServerSocket.convertStreamToString(input);
	if("stop".equals(received.trim()))
	{
		System.out.println("I AM IN");
		read =false;
	}
	else
	System.out.println(received);
	} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	i++;
	//ps.println("My message "+ i);
	
	//}*/
	}
	/**
	 * A helper method that converts a stream to string
	 * @param is denotes the input stream
	 * @return it returns the string that has all the data sent
	 */
	private static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line + "\n");
	    }
	    is.close();
	    return sb.toString();
	  }

}