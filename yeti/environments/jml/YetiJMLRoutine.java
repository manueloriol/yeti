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
 * Class that represents a routine in Yeti which is annotated with JML.
 * 
 * When making the call either to a constructor or a method  annotated 
 * with JML then the strategy for determining if there is an error is 
 * determined by the template method suggested in 
 * "JML and JUnit way of unit testing and its implementation" paper
 * by Y. Cheon and G. T. Leavens for running a test case and deciding 
 * the test output
 * 
 * @@author Vasileios Dimitriadis (vd508@cs.york.ac.uk, vdimitr@hotmail.com)
 * @date 20 Jul 2009
 *
 */
public abstract class YetiJMLRoutine extends YetiJavaRoutine {
	
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
			//TODO log can be null here if thread is killed
			YetiLog.printYetiLog(log+");", this);
			
			// then print the exception
			// if the thread was killed
			if (e.getCause() instanceof ThreadDeath) {
				YetiLog.printYetiLog("/**POSSIBLE BUG FOUND: TIMEOUT**/", this);
				YetiLog.printYetiThrowable(e.getCause(), this);
			} else if (e.getCause() instanceof org.jmlspecs.jmlrac.runtime.JMLEntryPreconditionError) {
				// if the cause is a precondition violation of a JML Assertion
				YetiLog.printYetiLog("/**MEANINGLESS: JMLEntryPreconditionError**/", this);
				
			} else if (e.getCause() instanceof org.jmlspecs.jmlrac.runtime.JMLAssertionError) {
				// if the cause is a violation of a JML Assertion other than precondition
				YetiLog.printYetiLog("/**BUG FOUND: JMLAssertionError**/", this);
				YetiLog.printYetiThrowable(e.getCause(), this);
			} else {
				// if the cause is a violation any other than JML Assertion
				if (isAcceptable(e.getCause())) {
					// if the cause is declared
					YetiLog.printYetiLog("/**DECLARED EXCEPTION:**/", this);
					YetiLog.printYetiThrowable(e.getCause(), this);
				} else {
					YetiLog.printYetiLog("/**UNDECLARED EXCEPTION:**/", this);
					YetiLog.printYetiThrowable(e.getCause(), this);
				}
			}
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
}
