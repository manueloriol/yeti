package yeti.environments.jml;

import yeti.YetiLogProcessor;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiTestManager;
import yeti.environments.java.YetiJavaProperties;

/**
 * 
 * Class that represents the JML specific properties.
 * 
 * @author Vasileios Dimitriadis (vd508@cs.york.ac.uk, vdimitr@hotmail.com)
 * @date 20 Jul 2009
 *
 */
public class YetiJMLProperties extends YetiJavaProperties {

	public YetiJMLProperties(YetiInitializer initializer, YetiTestManager testManager, YetiLogProcessor logProcessor) {
		super(initializer, testManager, logProcessor);
	}

}
