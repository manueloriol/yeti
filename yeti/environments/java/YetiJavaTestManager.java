package yeti.environments.java;

import yeti.ImpossibleToMakeConstructorException;
import yeti.YetiModule;
import yeti.YetiRoutine;
import yeti.YetiStrategy;
import yeti.environments.YetiTestManager;

/**
 * Class that represents a test manager for Java
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaTestManager extends YetiTestManager {
	
	/**
	 * The standard timeout for a method execution.
	 */
	public static int TIMEOUT_IN_SECONDS = 20;

	/* (non-Javadoc)
	 * 
	 * We make the call. Currently the timeout is not executed.
	 * 
	 * @see yeti.environments.YetiTestManager#makeNextCall(yeti.YetiModule, yeti.YetiStrategy)
	 */
	@Override
	public void makeNextCall(YetiModule mod, YetiStrategy strategy) {
		YetiRoutine r=strategy.getNextRoutine(mod);
		if (r!=null)
			try {
				// TODO add timeout handling;
				r.makeCall(strategy.getAllCards(r));
			} catch (ImpossibleToMakeConstructorException e) {
				// Ignore calls that do not allow to make new instances
				//e.printStackTrace();
			}
	}


}
