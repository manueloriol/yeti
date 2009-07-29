package yeti.environments.java;

import yeti.YetiLogProcessor;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiTestManager;

/**
 * Class that represents the Java specific properties 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaProperties extends YetiProgrammingLanguageProperties {

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
	
	public YetiJavaProperties(YetiInitializer initializer, YetiTestManager testManager, YetiLogProcessor logProcessor) {
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
