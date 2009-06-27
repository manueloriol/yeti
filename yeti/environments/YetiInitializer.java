package yeti.environments;

import yeti.YetiInitializationException;

/**
 * Class that represents an initializer to call at the beginning of a Yeti session.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public abstract class YetiInitializer {
	
	/**
	 * Method that initialises a Yeti session using the arguments of the program.
	 * 
	 * @param args the arguments used.
	 * @throws YetiInitializationException if the initialization cannot proceed.§
	 */
	public abstract void initialize(String []args) throws YetiInitializationException;

}
