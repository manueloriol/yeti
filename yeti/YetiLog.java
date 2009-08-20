package yeti;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Class that manages logging in Yeti. 
 * The generated test cases are also part of the logging mechanism. 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiLog {

	public static YetiLogProcessor proc = null;

	public static long numberOfCalls =0;
	public static long numberOfErrors =0;

	/**
	 * The array of classes on which the debug messages should be shown. 
	 * example of classes to debug:	
	 * <code>public static String []enabledDebugClasses={"yeti.YetiType", "yeti.environments.java.YetiJavaMethod"};</code>
	 */
	//public static String []enabledDebugClasses={"yeti.YetiType", "yeti.environments.java.YetiJavaMethod"};
	//public static String []enabledDebugClasses={"yeti.environments.java.YetiJavaPrefetchingLoader"};
	public static String []enabledDebugClasses={};

	/**
	 * Method used to print debugging messages.
	 * 
	 * @param message the debugging message to use.
	 * @param objectInWhichCalled the caller or the class of the caller in case it is in a static method.
	 */
	@SuppressWarnings("unchecked")
	public static void printDebugLog(String message, Object objectInWhichCalled){
		String className;

		// if it is a class we check directly for this class
		if (objectInWhichCalled instanceof Class)
			className = ((Class)objectInWhichCalled).getName();
		// else we get the class of the object passed as a parameter
		else
			className = objectInWhichCalled.getClass().getName();

		// we check that the class is in the classes to print
		boolean isPrintable = false;
		for(String s : enabledDebugClasses){
			if (className.equals(s)) {
				isPrintable = true; 
				break;
			}
		}
		if (isPrintable) 
			// we print the message with maximum information so it is easier 
			// to know where it comes from 
			System.err.println("YETI DEBUG:"+className+": "+message);
	}

	/**
	 * Method used to print debugging messages. Prints the message if isTemporary is true.
	 * 
	 * @param message the debugging message to use.
	 * @param objectInWhichCalled the caller or the class of the caller in case it is in a static method.
	 * @param isTemporary print the message anyway if true.
	 */
	@SuppressWarnings("unchecked")
	public static void printDebugLog(String message, Object objectInWhichCalled, boolean isTemporary) {
		if (isTemporary)
			if (objectInWhichCalled instanceof Class)
				System.err.println("YETI DEBUG:TMP:"+((Class)objectInWhichCalled).getName()+": "+message);
			else
				System.err.println("YETI DEBUG:TMP:"+objectInWhichCalled.getClass().getName()+": "+message);
	}

	/**
	 * This method is used to store or print Yeti logs.
	 * 
	 * @param message the log to add/print
	 * @param objectInWhichCalled object in which the method was called
	 */
	public static synchronized void printYetiLog(String message, Object objectInWhichCalled){
		numberOfCalls++;
		if (Yeti.pl.isNoLogs())
			proc.printMessageNoLogs(message);
		else {
			if (Yeti.pl.isRawLog())
				proc.printMessageRawLogs(message);
			else
				proc.appendToCurrentLog(message);
		}
	}

	/**
	 * Method used to store or print log of a Throwable (Exception or Error).
	 * 
	 * @param t the Throwable to print.
	 * @param objectInWhichCalled the object in which this was called.
	 */
	public static synchronized void printYetiThrowable(Throwable t, Object objectInWhichCalled){
		printYetiThrowable(t, objectInWhichCalled,true);
	}


	/**
	 * Method used to store or print log of a Throwable (Exception or Error).
	 * 
	 * @param t the Throwable to print.
	 * @param objectInWhichCalled the object in which this was called.
	 * @param isFailure
	 */
	public static synchronized void printYetiThrowable(Throwable t, Object objectInWhichCalled, boolean isFailure){
		if (isFailure) numberOfErrors++;
		if (Yeti.pl.isNoLogs())
			proc.printThrowableNoLogs(t, isFailure);
		else {
			if (Yeti.pl.isRawLog()){
				proc.printThrowableRawLogs(t, isFailure);
			} else {
				proc.appendFailureToCurrentLog("/**YETI EXCEPTION - START ");
				OutputStream os=new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(os);
				if (t!=null) 
					t.printStackTrace(ps);
				else 
					ps.println("Thread killed by Yeti!");
				proc.appendFailureToCurrentLog(os.toString());
				proc.appendFailureToCurrentLog("YETI EXCEPTION - END**/ ");
				YetiLog.printDebugLog(os.toString(), YetiLog.class);
			}
		}
	}


}
