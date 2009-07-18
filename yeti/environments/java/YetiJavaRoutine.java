package yeti.environments.java;

import java.lang.reflect.InvocationTargetException;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.YetiVariable;


/**
 * Class that represents... 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaRoutine extends YetiRoutine {

	/**
	 * Result of the last call.
	 */
	protected YetiVariable lastCallResult=null;


	/**
	 * 
	 * Creates a Java routine.
	 * 
	 * @param name the name of the routine.
	 * @param openSlots the open slots for the routine.
	 * @param returnType the type of the returned value.
	 * @param originatingModule the module in which it was defined
	 */
	public YetiJavaRoutine(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule) {
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
	 * Method used to perform the actual call
	 * 
	 * @see yeti.environments.java.YetiJavaRoutine#makeCall(yeti.YetiCard[])
	 */
	public Object makeCall(YetiCard []arg){
		String log = null;

		try {

			try {
				makeEffectiveCall(arg);
			} catch(YetiCallException e) {
				log = e.getLog();
				throw e.getOriginalThrowable();
			}

		} catch (IllegalArgumentException e) {
			// should never happen
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			// should never happen
			// e.printStackTrace();
		} catch (InvocationTargetException e) {

			// if we are here, we found a bug.
			// we first print the log
			YetiLog.printYetiLog(log+");", this);
			// then print the exception
			if (e.getCause() instanceof RuntimeException || e.getCause() instanceof Error) {
				if (e.getCause() instanceof ThreadDeath) {
					YetiLog.printYetiLog("/**POSSIBLE BUG FOUND: TIMEOUT**/", this);
				} else {
					YetiLog.printYetiLog("/**BUG FOUND: RUNTIME EXCEPTION**/", this);
				}
			}
			else
				YetiLog.printYetiLog("/**NORMAL EXCEPTION:**/", this);
			YetiLog.printYetiThrowable(e.getCause(), this);
		} catch (Error e){
			// if we are here there was a serious error
			// we print it
			YetiLog.printYetiLog(log+");", this);
			YetiLog.printYetiLog("BUG FOUND: ERROR", this);
			YetiLog.printYetiThrowable(e.getCause(), this);

		}
		catch (Throwable e){
			// should never happen
			e.printStackTrace();
		}
		return this.lastCallResult;
	}
	
	@Override
	public String makeEffectiveCall(YetiCard[] arg) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
