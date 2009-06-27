package yeti;

/**
 * Class that represents an exception at the initialization step. 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
@SuppressWarnings("serial")
public class YetiInitializationException extends Exception {
	/**
	 * The reason for the error.
	 */
	String Reason;
	
	/**
	 * Constructor for the exception.
	 * 
	 * @param s
	 */
	public YetiInitializationException(String s){
		Reason=s;
	}

}
