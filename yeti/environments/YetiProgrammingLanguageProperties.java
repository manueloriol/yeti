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
