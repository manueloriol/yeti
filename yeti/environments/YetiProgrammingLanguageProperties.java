package yeti.environments;

import yeti.YetiLogProcessor;


/**
 * Class that represents the properties of a language for this session. 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public abstract class YetiProgrammingLanguageProperties {
	/**
	 * Should we get the logs rather than the test case
	 */
	private boolean isRawLog = false;
	
	/**
	 * Returns if it is a raw log (unprocessed)
	 * 
	 * @return true if this is raw logs, false otherwise.
	 */
	public boolean isRawLog() {
		return isRawLog;
	}

	/**
	 * Sets it as rawLogs
	 * 
	 * @param isRawLog true if it is going to be a raw log
	 */
	public void setRawLog(boolean isRawLog) {
		this.isRawLog = isRawLog;
	}
	/**
	 * Returns the test manager for this language.
	 * 
	 * @return the test manager for this language.
	 */
	public abstract YetiTestManager getTestManager();
	
	/**
	 * Returns the initialiser for this session and this language
	 * 
	 * @return the initialiser for this language and this session.
	 */
	public abstract YetiInitializer getInitializer();

	/**
	 * Returns the log processor for this session and this language
	 * 
	 * @return the log processor for this language and this session.
	 */
	public abstract YetiLogProcessor getLogProcessor();
}
