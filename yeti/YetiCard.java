package yeti;


/**
 * Class that represents cards. A card is a placeholder either for variables or wildcards. A wildcard for example, can be used to generate primitive types values on-the-fly.
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Jun 22, 2009
 */
public class YetiCard {
	
	/**
	 * The identity of the card.
	 */
	protected YetiIdentifier identity;
	
	/**
	 * The type of the card.
	 */
	protected YetiType type;
	
	/**
	 * The value of the card.
	 */
	protected Object value;
	
	/**
	 * Getter for the identity.
	 * @return  the identity of the card.
	 */
	public YetiIdentifier getIdentity(){
		return identity;
	}
	
	/**
	 * Getter for the type.
	 * @return  the type of this card.
	 */
	public YetiType getType(){
		return type;
	}

	
	/**
	 * Getter for the value of this card.
	 * @return  the value of the card.
	 */
	public Object getValue(){
		return value;
	}
	
	/**
	 * Creation procedure for the class.
	 * 
	 * @param id the identity to use for the class.
	 * @param t the type of this card.
	 * @param value the value of this card.
	 */
	public YetiCard(YetiIdentifier id, YetiType t, Object value){
		identity=id;
		type=t;
		this.value=value;
		
	}
	
	/* 
	 * (non-Javadoc)
	 * Override of the <code>toString()</code> method.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return type.name+" "+identity.value;
	}

	/**
	 * Setter for the identity of the card
	 * @param identity  the identity of the card
	 */
	public void setIdentity(YetiIdentifier identity) {
		this.identity = identity;
	}

	/**
	 * Setter for the type of the card.
	 * @param type  the type of the card.
	 */
	public void setType(YetiType type) {
		this.type = type;
	}

	/**
	 * Setter for the value of the card.
	 * @param value  the value of the card.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
}
