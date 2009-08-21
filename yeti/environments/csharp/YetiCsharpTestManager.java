package yeti.environments.csharp;

import java.io.IOException;
import java.util.ArrayList;


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
 * @date Aug 10, 2009
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

	/**
	 * A thread group to which all workers belong.
	 */
	public static ThreadGroup workersGroup = new ThreadGroup("workers");
	/* (non-Javadoc)
	 * 
	 * We make the call.
	 * 
	 * @see yeti.environments.YetiTestManager#makeNextCall(yeti.YetiModule, yeti.YetiStrategy)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void makeNextCall(YetiModule mod, YetiStrategy strategy) {


		YetiRoutine r = strategy.getNextRoutine(mod);

		// if there is a routine...
		if (r != null) {
		// we make the actual call
				try {
					r.makeCall(strategy.getAllCards(r));
				} catch (ImpossibleToMakeConstructorException e) {
					// TODO Auto-generated catch block
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
		
			YetiServerSocket.sendData("! STOP TESTING !");
			 @SuppressWarnings("unused")
			ArrayList<String> a = YetiServerSocket.getData();


		
		
	}

}
