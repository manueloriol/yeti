/**
 * 
 */
package yeti.environments.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import yeti.YetiCard;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.environments.commandline.YetiCLInitializer;
import yeti.environments.commandline.YetiCLRoutine.TestingSession;

/**
 * Class that represents... 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 23, 2010
 *
 */
public class YetiCLRoutine extends YetiRoutine {

	String cmdLine = "";
	// timeout for each commmand line process
	int timeout = YetiCLInitializer.timeout;
	
	/**
	 * 
	 * Creates a Java routine.
	 * 
	 * @param name the name of the routine.
	 * @param openSlots the open slots for the routine.
	 * @param originatingModule the module in which it was defined
	 */
	public YetiCLRoutine(YetiName name, String cmdLine, YetiType[] openSlots, YetiModule originatingModule) {
		super();
		this.name = name;
		this.cmdLine = cmdLine;
		this.openSlots = openSlots;
		this.originatingModule = originatingModule;
	}
	
	
	/* (non-Javadoc)
	 * @see yeti.YetiRoutine#checkArguments(yeti.YetiCard[])
	 */
	@Override
	public boolean checkArguments(YetiCard[] arg) {
		return true;
	}

	/* (non-Javadoc)
	 * @see yeti.YetiRoutine#makeEffectiveCall(yeti.YetiCard[])
	 */
	@Override
	public String makeEffectiveCall(YetiCard[] arg) throws Throwable {
		String cmd = this.cmdLine;
		
		for (YetiCard arg0: arg) {
			//cmd = cmd + " " + arg0.toString();
			cmd = cmd + " " + arg0.getValue();
		}
		
		/*
		Process p = Runtime.getRuntime().exec(cmd);
		int result = p.waitFor();
		if (result!=0) {
			YetiLog.printYetiThrowable(new Exception("returned value: "+result), this);
		} else {
			YetiLog.printYetiLog(cmd, this);
		}
		*/
		
		//executes the command line process in another thread
		TestingSession test = new TestingSession(cmd);
		test.start();
		test.join(timeout);  // join the thread in timeout million seconds
		
		if(test.isAlive()){  // thread still alive when time out
			test.stop();
			YetiLog.printYetiThrowable(new Exception("testing time out"), this);
		}else{				
			int result = test.getTestResult();  // get the exit value of the command line process
			if (result!=0) {                    // process exit abnormally
				YetiLog.printYetiThrowable(new Exception("returned value: "+result), this);
			} else {
				YetiLog.printYetiLog(cmd, this);
			}
		}
	
		return cmd;
	}
	
	/**
	 * inner class presents a thread in which the command line process is executed
	 * */
	class TestingSession extends Thread{
		
		String cmd;
		int result = -1;
		
		public TestingSession(String cmd){
			this.cmd = cmd;
		}
		
		/**
		 * get the process exit value of the command line
		 * */
		public int getTestResult(){
			return result;
		}

		public void run(){
			Process p;
			try {
				// execute the command
				p = Runtime.getRuntime().exec(cmd);
				
				// get the output and err stream of the process in case it is blocked by them
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));	
				BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String received = in.readLine();
				while(received != null){
					System.out.println(received);
					received = in.readLine();
				}
				received = err.readLine();
				while(received != null){
					System.err.println(received);
					received = err.readLine();
				}
				
				// wait the execution of the process and get the exit value 
				result = p.waitFor();
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
