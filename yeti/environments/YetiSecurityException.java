package yeti.environments;

/**
 * Class that represents security exceptions triggered by Yeti 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 24, 2009
 *
 */
@SuppressWarnings("serial")
public class YetiSecurityException extends SecurityException {

	/**
	 * A creation procedure from super type.
	 * 
	 * @param s The message to show.
	 */
	public YetiSecurityException(String s) {
		super(s);
	}

}
