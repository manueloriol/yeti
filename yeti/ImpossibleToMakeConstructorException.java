package yeti;


/**
 * Class that represents exceptions raised when it is impossible to call a constructor in a reasonable amount of time.
 * 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
@SuppressWarnings("serial")
public class ImpossibleToMakeConstructorException extends Exception {

	/**
	 * The name of the type of constructor to use.
	 */
	String typeName;
	
	/**
	 * Creation procedure for this exception.
	 * 
	 * @param typeName the name of the type.
	 */
	public ImpossibleToMakeConstructorException(String typeName){
		this.typeName=typeName;
	}
}
