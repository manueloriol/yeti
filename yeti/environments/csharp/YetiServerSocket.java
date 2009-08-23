package yeti.environments.csharp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
//import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.SocketAddress;
//import java.net.UnknownHostException;
import java.util.ArrayList;

import yeti.YetiLog;

/**
 * Class that holds the methods with which the Csharp environment
 * in Java can obtain the information YETI needs
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 15, 2009
 */
public class YetiServerSocket {
	
	public static ServerSocket s = null;
	public static Socket clientSocket = null;
	public static InputStream input = null;
	public static OutputStream output = null;
	public static BufferedReader reader = null;
	public static PrintStream ps =null;

	private static boolean startServer=true;
	public YetiServerSocket() 
	{
		if(startServer)
		{
			try {
				s=new ServerSocket(2300);
				startServer=false;
				//Hold until data are sent by the other part
				clientSocket=s.accept();
				YetiLog.printDebugLog("after s.accept", this);
				input = clientSocket.getInputStream();
				output = clientSocket.getOutputStream();
				reader = new BufferedReader(new InputStreamReader(input));
				ps = new PrintStream(output);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	    
	/**
	 * It is a method that gets the data from the specified socket
	 * it will hold until the other par sends data
	 * 
	 * @param soc the integer that specifies the socket to listen
	 * @return it returns an ArrayList<String> so we can use the info	 
	 */
	public static ArrayList<String> getData()
	{	
		ArrayList<String> temp = new ArrayList<String>();
		String received="INITIAL";
		
		while(true)
		{
			try {
				received = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//when the other part sends "stop" the getData method will terminate
			if (received != null){
				if("stop".equals(received.trim())){
					break;
				}
				else{
					temp.add(received);
				}
			}
		}	
		for (String s0: temp) 
			YetiLog.printDebugLog("<-"+s0, YetiServerSocket.class);

		return temp;
		
		
	}
	
	/**
	 * It is a method that sends the data to the CsharpReflexiveLayer
	 * 
	 * @param msg is the call message that CsharpReflexiveLayer has to execute
	 */
	public static void sendData(String msg)
	{			   
		ps.println(msg);
		YetiLog.printDebugLog("->"+msg, YetiServerSocket.class);		
	}
	



}

