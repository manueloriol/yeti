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
	
	/**
	 * The array of classes on which the debug messages should be shown. 
	 * example of classes to debug:	
	 * <code>public static String []enabledDebugClasses={"yeti.YetiType", "yeti.environments.java.YetiJavaMethod"};</code>
	 */
	//public static String []enabledDebugClasses={"yeti.YetiType", "yeti.environments.java.YetiJavaMethod"};
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
			if (objectInWhichCalled instanceof Class)
				System.err.println("YETI DEBUG:"+className+": "+message);
			else
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
	public static void printYetiLog(String message, Object objectInWhichCalled){
		if (Yeti.pl.isRawLog())
			System.err.println("YETI LOG: "+message);
		else
			proc.appendToCurrentLog(message);
	}

	/**
	 * Method used to store or print log of a Throwable (Exception or Error).
	 * 
	 * @param t the Throwable to print.
	 * @param objectInWhichCalled the object in which this was called.
	 */
	public static void printYetiThrowable(Throwable t, Object objectInWhichCalled){
		if (Yeti.pl.isRawLog()){
			System.err.println("YETI EXCEPTION - START ");
			t.printStackTrace(System.err);
			System.err.println("YETI EXCEPTION - END ");
		} else {
			proc.appendFailureToCurrentLog("/**YETI EXCEPTION - START ");
			OutputStream os=new ByteArrayOutputStream();
			t.printStackTrace(new PrintStream(os));
			proc.appendFailureToCurrentLog(os.toString());
			proc.appendFailureToCurrentLog("YETI EXCEPTION - END**/ ");
			YetiLog.printDebugLog(os.toString(), YetiLog.class);
		}
	}

	

}
