package yeti.environments.csharp;

import yeti.YetiLogProcessor;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiTestManager;

/**
 * Class that represents the Csharp specific properties 
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 21, 2009
 *
 */
public class YetiCsharpProperties extends YetiProgrammingLanguageProperties {
	

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
		 * The socketConnector.
		 */
		protected YetiServerSocket socketConnector = null;
		
		public YetiCsharpProperties(YetiInitializer initializer, YetiTestManager testManager, YetiLogProcessor logProcessor, YetiServerSocket socketConnector) {
			this.initializer = initializer;
			this.testManager = testManager;
			this.logProcessor = logProcessor;
			this.socketConnector = socketConnector;
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
	
	public YetiServerSocket getServerSocket() {
		
		return socketConnector;
	}


}
