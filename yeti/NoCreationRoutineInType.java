package yeti;

/**
 * Class that represents the fact that there is no routine for a building a type.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
@SuppressWarnings("serial")
public class NoCreationRoutineInType extends Exception {

	
	/**
	 * Constructor that takes the type name.
	 * 
	 * @param typeName the name of the type for which there is no constructor.  
	 */
	public NoCreationRoutineInType(String message) {
		super(message);
	}
	
	

}
