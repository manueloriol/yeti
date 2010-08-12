/**
 * 
 */
package yeti.environments.commandline;

import yeti.YetiCard;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;

/**
 * Class that represents... 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 23, 2010
 *
 */
public class YetiCLRoutine extends YetiRoutine {

	String cmdLine = "";
	
	/**
	 * 
	 * Creates a Java routine.
	 * 
	 * @param name the name of the routine.
	 * @param openSlots the open slots for the routine.
	 * @param originatingModule the module in which it was defined
	 */
	public YetiCLRoutine(YetiName name, String cmdLine, YetiType[] openSlots, YetiModule originatingModule) {
		super();
		this.name = name;
		this.cmdLine = cmdLine;
		this.openSlots = openSlots;
		this.originatingModule = originatingModule;
	}
	
	
	/* (non-Javadoc)
	 * @see yeti.YetiRoutine#checkArguments(yeti.YetiCard[])
	 */
	@Override
	public boolean checkArguments(YetiCard[] arg) {
		return true;
	}

	/* (non-Javadoc)
	 * @see yeti.YetiRoutine#makeEffectiveCall(yeti.YetiCard[])
	 */
	@Override
	public String makeEffectiveCall(YetiCard[] arg) throws Throwable {
		String cmd = this.cmdLine;
		
		for (YetiCard arg0: arg) {
			cmd = cmd + " " + arg0.toString();
		}
		
		Process p = Runtime.getRuntime().exec(cmd);
		int result = p.waitFor();
		if (result!=0) {
			YetiLog.printYetiThrowable(new Exception("returned value: "+result), this);
		} else {
			YetiLog.printYetiLog(cmd, this);
		}
		
		return cmd;
	}

}
