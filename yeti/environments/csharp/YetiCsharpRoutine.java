package yeti.environments.csharp;

import yeti.YetiCard;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;


/**
 * Class that represents... 
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 20, 2009
 *
 */
public class YetiCsharpRoutine extends YetiRoutine {

	
	/**
	 * 
	 * Creates a Csharp routine.
	 * 
	 * @param name the name of the routine.
	 * @param openSlots the open slots for the routine.
	 * @param returnType the type of the returned value.
	 * @param originatingModule the module in which it was defined
	 */
	public YetiCsharpRoutine(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule) {
		super();
		this.name = name;
		this.openSlots = openSlots;
		this.returnType = returnType;
		this.originatingModule = originatingModule;
	}

	/* (non-Javadoc)
	 * 
	 * Checks the arguments. In the case of Java, arguments match (TODO: change that for generics).
	 * 
	 * @see yeti.YetiRoutine#checkArguments(yeti.YetiCard[])
	 */
	@Override
	public boolean checkArguments(YetiCard[] arg) {
			
		return true;
	}

	/* (non-Javadoc)
	 * We make the call. In this super type, we simply print a message but do not make the call.
	 * 
	 * @see yeti.YetiRoutine#makeCall(yeti.YetiCard[])
	 */
	@Override
	public Object makeCall(YetiCard[] args) {
		YetiLog.printDebugLog("Calling "+this.name.getValue(), this);
		return null;
	}
	
	@Override
	public String makeEffectiveCall(YetiCard[] arg) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
