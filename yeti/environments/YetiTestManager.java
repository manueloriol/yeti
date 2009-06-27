package yeti.environments;

import yeti.YetiModule;
import yeti.YetiStrategy;

/**
 * Class that represents... 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public abstract class YetiTestManager {
	
	/**
	 * Makes the next call according to the module targeted and the strategy of the program.
	 * 
	 * @param mod the module being tested.
	 * @param strategy the strategy being used.
	 */
	public abstract void makeNextCall(YetiModule mod, YetiStrategy strategy);

}
