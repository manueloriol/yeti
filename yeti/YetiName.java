package yeti;

/**
 * Class that represents a name in Yeti.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiName {
	/**
	 * The value of the name.
	 */
	protected String value;
	
	/**
	 * Creates a name from a String (appending a number to it)
	 * 
	 * @param s the String to use as a basis.
	 */
	public YetiName(String s){
		value = s;
	}
	
	/**
	 * Used to get unique names. Number appended to the name.
	 */
	public static long index=0;
	
	/**
	 * Get a fresh name.
	 * 
	 * @param name the basis.
	 * @return the fresh name (the basis + the unique number)
	 */
	public static YetiName getFreshNameFrom(String name){
		return new YetiName(name+"_"+index++);
	}

	/**
	 * Getter for the value of this name.
	 * 
	 * @return the value of this name.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter for the value of this name.
	 * 
	 * @param value the value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
