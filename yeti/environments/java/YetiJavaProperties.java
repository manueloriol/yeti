package yeti.environments.java;

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
	 * The Java intialiser.
	 */
	private YetiJavaInitializer jinit=new YetiJavaInitializer();
	
	/**
	 * The Java test manager.
	 */
	private YetiJavaTestManager jtm=new YetiJavaTestManager();
	
	
	/* (non-Javadoc)
	 * 
	 * Returns an initializer.
	 * 
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getInitializer()
	 */
	@Override
	public YetiInitializer getInitializer() {

		return jinit;
	}

	/* (non-Javadoc)
	 * Returns the test manager
	 * 
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getTestManager()
	 */
	@Override
	public YetiTestManager getTestManager() {
		return jtm;
	}

}
