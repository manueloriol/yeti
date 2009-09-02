/**
 * 
 */
package yeti;

/**
 * Class that represents an exception or an error thrown during a call.
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Jul 18, 2009
 */
@SuppressWarnings("serial")
public class YetiCallException extends Exception {
	
	/**
	 * The log of the current call.
	 */
	public String log;
	
	/**
	 * The original Throwable for the exception. 
	 */
	public Throwable originalThrowable;
	
	
	/**
	 * Simple getter for the logs.
	 * @return  the logs for the throwable.
	 */
	public String getLog() {
		return log;
	}

	/**
	 * Simple getter for the Throwable
	 * @return  the Throwable originally thrown.
	 */
	public Throwable getOriginalThrowable() {
		return originalThrowable;
	}


	/**
	 * Simple constructor for the Exception.
	 * 
	 * @param log the log at the moment of the error.
	 * @param originalThrowable the original Throwable.
	 */
	public YetiCallException(String log, Throwable originalThrowable) {
		super();
		this.log = log;
		this.originalThrowable = originalThrowable;
	}
	
	
	

}
