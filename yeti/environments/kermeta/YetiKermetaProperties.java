package yeti.environments.kermeta;

import yeti.YetiLogProcessor;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiTestManager;

/**
 * Class that represents the Kermeta specific properties 
 */
public class YetiKermetaProperties extends YetiProgrammingLanguageProperties {

	/**
	 * The initialiser.
	 */
	protected YetiInitializer initializer = null;
	
	/**
	 * The test manager.
	 */
	protected YetiTestManager testManager = null;
	
	/**
	 * The logProcessor.
	 */
	protected YetiLogProcessor logProcessor = null;
	
	/**
	 * A simple constructor for the Java properties.
	 * 
	 * @param initializer the initializer for the Java session.
	 * @param testManager the test manager for the session.
	 * @param logProcessor the log processor for the session.
	 */
	public YetiKermetaProperties(YetiInitializer initializer, YetiTestManager testManager, YetiLogProcessor logProcessor) {
		this.initializer = initializer;
		this.testManager = testManager;
		this.logProcessor = logProcessor;
	}
	
	/* (non-Javadoc)
	 * 
	 * Returns an initializer.
	 * 
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getInitializer()
	 */
	@Override
	public YetiInitializer getInitializer() {
		
		return initializer;
	}

	/* (non-Javadoc)
	 * Returns the test manager
	 * 
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getTestManager()
	 */
	@Override
	public YetiTestManager getTestManager() {
		return testManager;
	}

	/* (non-Javadoc)
	 * Returns the log processor
	 * 
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getTestManager()
	 */
	@Override
	public YetiLogProcessor getLogProcessor() {
		return logProcessor;
	}

}
