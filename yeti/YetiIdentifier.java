package yeti;


/**
 * Class that represents an identity on Yeti
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiIdentifier {
	
	
	/**
	 * The value of the identity (usually a string representing a variable name)
	 */
	protected String value;
	
	/**
	 * The index to generate fresh names.
	 */
	protected static long currentIndex=0;
	
	/**
	 * Constructor of the identifier using a clear name.
	 * 
	 * @param value the name to be used.
	 */
	public YetiIdentifier(String value) {
		super();
		this.value = value;
	}
	
	
	
	/* (non-Javadoc)
	 * 
	 * Overriding of the toString method. Standard implementation returns 
	 * the value of the string representing the identifier.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return value;
	}



	/**
	 * Return a fresh identifier with a fresh name (implementation may vary).
	 * 
	 * @return the new identifyer.
	 */
	public static YetiIdentifier getFreshIdentifier(){
		return new YetiIdentifier("v"+(currentIndex++));
	}



	/**
	 * Getter for the index.
	 * 
	 * @return The current index.
	 */
	public static long getCurrentIndex() {
		return currentIndex;
	}



	/**
	 * Setter for the current index.
	 * 
	 * @param currentIndex the index to set.
	 */
	public static void setCurrentIndex(long currentIndex) {
		YetiIdentifier.currentIndex = currentIndex;
	}



	/**
	 * Getter for the value of the identifier.
	 * 
	 * @return the value of the identifier.
	 */
	public String getValue() {
		return value;
	}



	/**
	 * Setter for the value of the identifier.
	 * 
	 * @param value the value of the identifier.
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
