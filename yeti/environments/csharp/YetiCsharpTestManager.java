package yeti.environments.csharp;

import yeti.ImpossibleToMakeConstructorException;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiRoutine;
import yeti.YetiStrategy;
import yeti.environments.YetiTestManager;


/**
 * Class that represents a test manager for Csharp
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 21, 2009
 *
 */
public class YetiCsharpTestManager extends YetiTestManager {


	/**
	 * Number of worker threads started by the infrastructure
	 */
	public static long nThreadsStarted = 0;

	/**
	 * Number of worker threads stopped by the infrastructure
	 */
	public static long nThreadsStopped = 0;

	/* (non-Javadoc)
	 * 
	 * We make the call.
	 * 
	 * @see yeti.environments.YetiTestManager#makeNextCall(yeti.YetiModule, yeti.YetiStrategy)
	 */
	@Override
	public void makeNextCall(YetiModule mod, YetiStrategy strategy) {

		// we pick the routine
		YetiRoutine r = strategy.getNextRoutine(mod);

		if (r != null) {
			try {
				// we make the actual call
				r.makeCall(strategy.getAllCards(r));
			} catch (ImpossibleToMakeConstructorException e) {
				// Ignore calls that do not allow to make new instances
				//e.printStackTrace();
			}
		}

	}

	/* (non-Javadoc)
	 * Will stop the caller thread.
	 * 
	 * @see yeti.environments.YetiTestManager#stopTesting()
	 */
	@Override
	public void stopTesting() {


		//TODO kill the C# part
	}


}
