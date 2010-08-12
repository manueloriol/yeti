/**
 * 
 */
package yeti.environments.commandline;

import yeti.YetiLogProcessor;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiTestManager;

/**
 * Class that represents the code for command-line calls.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 23, 2010
 *
 */
public class YetiCLProperties extends YetiProgrammingLanguageProperties {

	YetiInitializer ycli = new YetiCLInitializer();
	YetiLogProcessor ylp = new YetiCLLogProcessor();
	YetiTestManager ytm = new YetiCLTestManager();

	public YetiCLProperties(YetiInitializer initializer,
			YetiTestManager testManager, YetiLogProcessor logProcessor) {
		ycli = initializer;
		ylp = logProcessor;
		ytm = testManager;
	}

	/* (non-Javadoc)
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getInitializer()
	 */
	@Override
	public YetiInitializer getInitializer() {
		return ycli;
	}

	/* (non-Javadoc)
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getLogProcessor()
	 */
	@Override
	public YetiLogProcessor getLogProcessor() {
		return ylp;
	}

	/* (non-Javadoc)
	 * @see yeti.environments.YetiProgrammingLanguageProperties#getTestManager()
	 */
	@Override
	public YetiTestManager getTestManager() {
		return ytm;
	}

}
