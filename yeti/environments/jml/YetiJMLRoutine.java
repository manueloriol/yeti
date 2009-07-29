package yeti.environments.jml;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.environments.java.YetiJavaRoutine;

/**
 * Class that represents a routine in Yeti which is annotated with JML
 * 
 * @@author Vasileios Dimitriadis (vd508@cs.york.ac.uk, vdimitr@hotmail.com)
 * @date 20 Jul 2009
 *
 */
public class YetiJMLRoutine extends YetiJavaRoutine {

	/**
	 * Create a JML Routine
	 * 
	 * @param name
	 * @param openSlots
	 * @param returnType
	 * @param originatingModule
	 */
	public YetiJMLRoutine(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule) {
		super(name, openSlots, returnType, originatingModule);
	}
	
	@Override
	public Object makeCall(YetiCard[] arg) {
		String log = null;
		
		// invoke the constructor with the arguments
		// if there is an exception then
		// depending on its type 
		// decide the outcome of the test
		try {
			
			try {
				makeEffectiveCall(arg);
			} catch(YetiCallException e) {
				log = e.getLog();
				throw e.getOriginalThrowable();
			}
		} catch (InstantiationException e) {
			// should never happen
			//e.printStackTrace();
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
			// if the thread was killed
			if (e.getCause() instanceof ThreadDeath) {
				YetiLog.printYetiLog("/**POSSIBLE BUG FOUND: TIMEOUT**/", this);
			}
			
			// if the cause is a violation of a JML Assertion
			if (isJMLAssertionError(e.getCause())) {
				YetiLog.printYetiLog("/**BUG FOUND: JMLAssertionError**/", this);
			}
			
			YetiLog.printYetiThrowable(e.getCause(), this);
		} catch (Error e) {
			// if we are here there was a serious error
			// we print it
			YetiLog.printYetiLog(log+");", this);
			YetiLog.printYetiLog("BUG FOUND: ERROR", this);
			YetiLog.printYetiThrowable(e.getCause(), this);
		}
		catch (Throwable e){
			// should never happen
			System.out.println("This should never happen!!!");
			System.out.println("Trying to call " + this + " with these args=" + Arrays.toString(arg));
			e.printStackTrace();
		}
		return this.lastCallResult;
	}
	
	/**
	 * This method is correspondent to the template method suggested
	 * in "JML and JUnit way of unit testing and its implementation" paper
	 * by Yoosnik Cheon and Gary T. Leavens 
	 * for running a test case and deciding the test output
	 * 
	 * @param t The cause to check
	 * @return True if <code>t</code> is instance of org.jmlspecs.jmlrac.runtime.JMLAssertionError
	 * and not instance of org.jmlspecs.jmlrac.runtime.JMLEntryPreconditionError
	 */
	private boolean isJMLAssertionError(Throwable t) {
		if (t instanceof org.jmlspecs.jmlrac.runtime.JMLEntryPreconditionError) {
            // meaningless test input
			return false;
		}
        if (t instanceof org.jmlspecs.jmlrac.runtime.JMLAssertionError) {
        	// test failure
            return true;
        } 
        // test success
        return false;
	}

}
