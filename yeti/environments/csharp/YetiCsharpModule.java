package yeti.environments.csharp;

import yeti.YetiModule;


/**
 * Class that represents a Csharp module (typically in Csharp either a class or an assembly).
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 20, 2009
 *
 */

public class YetiCsharpModule extends YetiModule {
	
	/**
	 * In this implementation we consider it to be a class name.
	 * 
	 * @param className the name of the class of this module.
	 */
	public YetiCsharpModule(String className){
		super(className);
	}

	
}
