package yeti;

import java.util.HashMap;
import java.util.Vector;

/**
 * Class that represents a processor for logs coming from Yeti.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public abstract class YetiLogProcessor {
	
	/**
	 * The number of non-unique bugs in last logs.
	 */
	public static int lastNumberOfNonUniqueBugs = 0;

	/**
	 * The number of (non-unique) failures found so far.
	 */
	public int numberOfErrors = 0;
	
	/**
	 * The number of routine calls.
	 */
	public int numberOfCalls = 0;

	/**
	 * A field storing the logs.
	 */
	private Vector <String> logs = new Vector<String>();

	/**
	 * a variable to append to current logs.
	 */
	private String currentLog = "";

	/**
	 * A list of traces for relevant detected errors. 
	 */
	public HashMap<String,Object> listOfErrors = new HashMap<String, Object>();
	
	/**
	 * Constructor of the YetiLogProcessor.
	 */
	public YetiLogProcessor() {
		super();
	}
	
	/**
	 * Add the parameter at the end of the currentLog.
	 * 
	 * @param newLog the log to add.
	 */
	public void appendToCurrentLog(String newLog){
		currentLog=this.currentLog+"\n"+newLog;
		this.numberOfCalls++;
	}
	
	/**
	 * Add the parameter at the end of the currentLog.
	 * 
	 * @param newLog the log to add.
	 */
	public void appendFailureToCurrentLog(String newLog){
		currentLog=this.currentLog+"\n"+"/**Error:Start: "+newLog+"**/\n/**End:Error**/";
		this.numberOfErrors++;
	}	
	/**
	 * Simple setter for the current logs.
	 * 
	 * @param currentLog the logs to set.
	 */
	public void setCurrentLog(String currentLog) {
		this.currentLog = currentLog;
	}

	/**
	 * Adds the currentLog to the logs and reinitializes currentLog.
	 */
	public void newSerieOfLog(){
		logs.add(currentLog);
		currentLog="";
	}
	
	/**
	 * Process the logs currently stored.
	 * 
	 * @return the processed logs, what can be given to end-user.
	 */
	public abstract Vector<String> processLogs();

	/**
	 * Getter for the currentLog variable.
	 * 
	 * @return The value of currentLog
	 */
	public String getCurrentLog() {
		return currentLog;
	}

	/**
	 * Getter for logs.
	 * 
	 * @return the value of the older logs.
	 */
	public Vector<String> getLogs() {
		return logs;
	}
	
	/**
	 * Printer for raw logs
	 * 
	 * @parameter message the message log to print.
	 */
	public void printMessageRawLogs(String message) {
		System.err.println("YETI LOG: "+message);
	}
	
	/**
	 * Printer for throwables in raw logs
	 * 
	 * @parameter t the throwable log to print.
	 */
	public void printThrowableRawLogs(Throwable t) {
		System.err.println("YETI EXCEPTION - START ");
		if (t!=null) 
			t.printStackTrace(System.err);
		else 
			System.err.println("Thread killed by Yeti!");
		System.err.println("YETI EXCEPTION - END ");

	}
	
	/**
	 * Printer for no logs
	 * 
	 * @parameter message the message log not to print.
	 */
	public void printMessageNoLogs(String message) {
	}
	
	/**
	 * Printer for throwables in no logs
	 * 
	 * @parameter t the throwable log not to print.
	 */
	public void printThrowableNoLogs(Throwable t) {

	}
	
}
