package yeti.environments;

import yeti.YetiModule;
import yeti.YetiStrategy;

/**
 * Class that represents a test manager.
 * A test manager manages oracles and effectively making the calls.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public abstract class YetiTestManager {
	/**
	 * The standard timeout for a method execution.
	 */
	protected int timeoutInMilliseconds=50;

	
	
	/**
	 * Simple getter for the timeout in this TestManager
	 * 
	 * 
	 * @return the timeout in milliseconds
	 */
	public int getTimeoutInMilliseconds() {
		return timeoutInMilliseconds;
	}



	/**
	 * Simple setter for the timeout in this test manager.
	 * 
	 * @param timeoutInMilliseconds the new value of the timeout in milliseconds.
	 */
	public void setTimeoutInMilliseconds(int timeoutInMilliseconds) {
		this.timeoutInMilliseconds = timeoutInMilliseconds;
	}



	/**
	 * Makes the next call according to the module targeted and the strategy of the program.
	 * 
	 * @param mod the module being tested.
	 * @param strategy the strategy being used.
	 */
	public abstract void makeNextCall(YetiModule mod, YetiStrategy strategy);
	
	/**
	 * Stop the testing. Especially useful in multithreaded environment.
	 * 
	 */
	public void stopTesting() {};
	

}
