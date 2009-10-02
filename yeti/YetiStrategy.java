/**
 * 
 */
package yeti;

import javax.swing.JPanel;

import yeti.environments.YetiTestManager;


/**
 * Class that represents a testing strategy. 
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Jun 22, 2009
 */
public abstract class YetiStrategy {

	/**
	 * Probability to generate a new instance rather than use an old one.
	 */
	public static double NEW_INSTANCES_INJECTION_PROBABILITY=.10;

	/**
	 * The test manager used.
	 */
	@SuppressWarnings("unused")
	private YetiTestManager ytm;
	
	/**
	 * Constructor setting the manager up.
	 * 
	 * @param ytm
	 */
	public YetiStrategy (YetiTestManager ytm){
		this.ytm=ytm;
	}

	
	/**
	 * Method to get all parameters for a routine.
	 * 
	 * @param routine the routine to treat.
	 * @return the array of cards.
	 * @throws ImpossibleToMakeConstructorException exception returned when it is impossible to get all arguments.
	 */
	public abstract YetiCard[] getAllCards(YetiRoutine routine) throws ImpossibleToMakeConstructorException; 
	
	/**
	 * Gets the next routine to test in a given module.
	 * 
	 * @param module the module in which look for the routine.
	 * @return the next routine to test.
	 */
	public abstract YetiRoutine getNextRoutine(YetiModule module);
	
	/**
	 * Gets the card at argumentNumber in the routine.
	 * 
	 * @param routine the routine to test.
	 * @param argumentNumber the argument number.
	 * @return the card found.
	 * @throws ImpossibleToMakeConstructorException in case it is not possible to find it.
	 */
	public abstract YetiCard getNextCard(YetiRoutine routine, int argumentNumber) throws ImpossibleToMakeConstructorException;

	/**
	 * This is to provide the GUI with a preference panel.
	 * 
	 * @return the associated panel
	 */
	public JPanel getPreferencePane() {
		return null;
	}
	
	/**
	 * Returns a nice representation of the strategy name
	 * 
	 * @return the nice name
	 */
	public String getName() {
		return "Generic Strategy";
	}

}
