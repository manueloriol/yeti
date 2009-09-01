package yeti;


/**
 * Class that represents a routine in Yeti 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public abstract class YetiRoutine {

	/**
	 * The name of the routine.
	 */
	protected YetiName name; 

	/**
	 * The openslots of the routine.
	 */
	protected YetiType []openSlots;

	/**
	 * The return type of the routine.
	 */
	protected YetiType returnType;

	/**
	 * The module in which the routine is defined.
	 */
	protected YetiModule originatingModule;	

	/**
	 * Adds the routine as a creator for its return type.
	 */
	public void addRoutineAsCreatorForType(){
		returnType.addCreationRoutine(this);
	}

	/**
	 * Just to verify that arguments fit the actual routine.
	 * 
	 * @param arg the arguments to check
	 * @return true if the arguments fit, false otherwise.
	 */
	public abstract boolean checkArguments(YetiCard []arg);


	/**
	 * Tracks how many calls were made to this routine. 
	 */
	public int nTimesCalled = 0;

	/**
	 * 
	 * Make a call of this routine if arguments fit the routine.
	 * Can be used to code a specific oracle.
	 * 
	 * @param arg the arguments.
	 * @return the result of the call.
	 */
	public Object makeCall(YetiCard []arg) {
		return nTimesCalled++;
	}

	/**
	 * Makes the effective call (lets return the exceptions and Errors).
	 * 
	 * @param arg the arguments of the call.
	 * @return the logs.
	 * @throws Throwable 
	 */
	public abstract String makeEffectiveCall(YetiCard[] arg) throws Throwable;

	/**
	 * Getter for the name of the routine.
	 * 
	 * @return the name of the routine.
	 */
	public YetiName getName() {
		return name;
	}

	/**
	 * Returns the signature of the routine.
	 * 
	 * @return the signature of the routine.
	 */
	public String getSignature() {
		String arguments = null;

		// we aggregate the arguments types.
		for (YetiType type: this.openSlots) {
			if (arguments==null) {
				arguments=type.getName().toString();
			} else {
				arguments = arguments+" x "+type.getName().toString();
			}
		}
		// if no argument the chain is empty
		if (arguments==null) {
			arguments="";
		}

		// we return the signature
		return (this.toString()+": ("+arguments+")->"+this.returnType.getName());
	}

	/**
	 * Setter of the name of the routine.
	 * 
	 * @param name the name to set.
	 */
	public void setName(YetiName name) {
		this.name = name;
	}

	/**
	 * Getter of the open slots of the routine.
	 * 
	 * @return the open slots of the routine.
	 */
	public YetiType[] getOpenSlots() {
		return openSlots;
	}

	/**
	 * Setter of the open slots.
	 * 
	 * @param openSlots the slots that are open on the routine.
	 */
	public void setOpenSlots(YetiType[] openSlots) {
		this.openSlots = openSlots;
	}

	/**
	 * Getter of the module where the routine is defined.
	 * 
	 * @return the module where the routine is defined.
	 */
	public YetiModule getOriginatingModule() {
		return originatingModule;
	}

	/**
	 * Setter of the defining module of the routine.
	 * 
	 * @param originatingModule the module where the routine is defined.
	 */
	public void setOriginatingModule(YetiModule originatingModule) {
		this.originatingModule = originatingModule;
	}

	/**
	 * Get the return type of the routine.
	 * 
	 * @return the return type of the routine.
	 */
	public YetiType getReturnType() {
		return returnType;
	}

	/**
	 * Setter for the return type of the routine.
	 * 
	 * @param returnType the return type to set.
	 */
	public void setReturnType(YetiType returnType) {
		this.returnType = returnType;
	}

}
