package yeti;

import java.util.HashMap;

/**
 * Class that represents a variable in Yeti. 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiVariable extends YetiCard{

	/**
	 * The probability to use a null value instead of a normal value.
	 */
	public static double PROBABILITY_TO_USE_NULL_VALUE = .01;
	
	/**
	 * Contains all variables available.
	 */
	public static HashMap<String, YetiVariable> allId = new HashMap<String, YetiVariable>();

	/**
	 * The number of variables created in the system (instances used for testing)
	 */
	public static long nVariables = 0;
	
	
	/**
	 * Creates a variable in Yeti. Note that the creation procedure automatically adds 
	 * the instance to the types it has.
	 * 
	 * @param id the identity for the variable.
	 * @param type the type of the variable.
	 * @param value the value of the variable.
	 */
	public YetiVariable( YetiIdentifier id, YetiType type, Object value) {
		super (id, type, value);
		nVariables++;
		allId.put(id.value, this);
		YetiLog.printDebugLog("type: "+type, this);
		// if the type was not created before we create it on the fly
// TODO this is innocuously removed
//		if (type==null){
//			YetiLog.printDebugLog("value's type: "+value.getClass().getName(), this);
//			YetiLoader.yetiLoader.addDefinition(value.getClass());
//			this.type=YetiType.allTypes.get(value.getClass().getName());
//			
//		}
		YetiLog.printDebugLog("type: "+this.type.name, this);
		// we add the instance to the type
		this.type.addInstance(this);
	}

	/* (non-Javadoc)
	 * Returns the identity of the variable, used for pretty print.
	 * 
	 * @see yeti.YetiCard#toString()
	 */
	public String toString() {
		return identity.value;
	}

	/**
	 * Getter for the identifier.
	 * 
	 * @return the identifier.
	 */
	public synchronized YetiIdentifier getId() {
		return identity;
	}

	/**
	 * Setter for the identifier.
	 * 
	 * @param id the identifier to set.
	 */
	public synchronized void setId(YetiIdentifier id) {
		this.identity = id;
	}

	
	/* (non-Javadoc)
	 * Getter for the type.
	 * 
	 * @see yeti.YetiCard#getType()
	 */
	public synchronized YetiType getType() {
		return type;
	}

	/* (non-Javadoc)
	 * Setter for the type.
	 * 
	 * @see yeti.YetiCard#setType(yeti.YetiType)
	 */
	public synchronized void setType(YetiType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * Getter for the value.
	 * 
	 * @see yeti.YetiCard#getValue()
	 */
	public synchronized Object getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * Setter for the value
	 * 
	 * @see yeti.YetiCard#setValue(java.lang.Object)
	 */
	public synchronized void setValue(Object value) {
		this.value = value;
	}

}
