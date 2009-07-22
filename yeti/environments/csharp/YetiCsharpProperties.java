package yeti.environments.csharp;

import yeti.YetiLogProcessor;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiServerSocket;
import yeti.environments.YetiTestManager;
import yeti.environments.csharp.YetiCsharpInitializer;
import yeti.environments.csharp.YetiCsharpLogProcessor;
import yeti.environments.csharp.YetiCsharpTestManager;

/**
 * Class that represents the Csharp specific properties 
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 21, 2009
 *
 */
public class YetiCsharpProperties extends YetiProgrammingLanguageProperties {
	


	/**
	 * The Csharp intialiser.
	 */
	private YetiCsharpInitializer csinit=new YetiCsharpInitializer();
	
	/**
	 * The Csharp test manager.
	 */
	private YetiCsharpTestManager cstm=new YetiCsharpTestManager();
	
	/**
	 * The Csharp logProcessor.
	 */
	private YetiCsharpLogProcessor cslp=new YetiCsharpLogProcessor();
	
	private YetiServerSocket cscon = new YetiServerSocket();
	
	/* (non-Javadoc)
	 * 
	 * Returns an initializer.
	 * 
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getInitializer()
	 */
	@Override
	public YetiInitializer getInitializer() {
		
		return csinit;
	}

	/* (non-Javadoc)
	 * Returns the test manager
	 * 
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getTestManager()
	 */
	@Override
	public YetiTestManager getTestManager() {
		return cstm;
	}

	/* (non-Javadoc)
	 * Returns the log processor
	 * 
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getTestManager()
	 */
	@Override
	public YetiLogProcessor getLogProcessor() {
		return cslp;
	}
	
	public YetiServerSocket getServerSocket() {
		
		return cscon;
	}


}
