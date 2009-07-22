package yeti.environments.csharp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;
import yeti.environments.csharp.YetiCsharpRoutine;


/**
 * Class that represents a constructor in Csharp.
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 21, 2009
 *
 */
public class YetiCsharpConstructor extends YetiCsharpRoutine {

	/**
	 * The actual constructor.  
	 */
	@SuppressWarnings("unchecked")
	private String c;
	
	
	/**
	 * The last result of a call to this constructor.
	 */
	public YetiVariable lastCallResult=null;
	
	/**
	 * Constructor to the constructor.
	 * 
	 * @param name the name of this constructor.
	 * @param openSlots the open slots for this constructor.
	 * @param returnType the returnType of this constructor.
	 * @param originatingModule the module in which it was defined.
	 * @param c the constructor itself.
	 */
	@SuppressWarnings("unchecked")
	public YetiCsharpConstructor(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, String c) {
		super(name, openSlots, returnType, originatingModule);
		this.c=c; //make it a string
	}
	

	/* (non-Javadoc)
	 * Returns the actual name of the constructor.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return c;
	}


	/* (non-Javadoc)
	 * 
	 * Makes the call to create a new instance.
	 * 
	 * @see yeti.environments.java.YetiJavaRoutine#makeCall(yeti.YetiCard[])
	 */
	public Object makeCall(YetiCard []arg){
		lastCallResult=null;
		Object []initargs=new Object[arg.length];

		// we start by unboxing the arguments boxed into the cards
		YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
		String log = returnType.toString() + " " + id.getValue() + "=new "+returnType.getName()+"(";
		for (int i=0;i<arg.length; i++){
			// note that we use getValue to get the actual value
			initargs[i]=arg[i].getValue();
			// we use toString() to make it pretty-print.
			log=log+arg[i].toString();
			if (i<arg.length-1){
				log=log+",";
			}
		}
		try {
			// we try to make the call
			Object o = c;
			// if it succeeds we create the new variable
			this.lastCallResult=new YetiVariable(id, returnType, o);
			log=log+");";
			// print the log
			YetiLog.printYetiLog(log, this);
		} catch (IllegalArgumentException e) {
			// should never happen
			// we ignore it
			// TODO check that
			// e.printStackTrace();
		} /* catch (InvocationTargetException e) {

			// if we are here, we found a bug.
			// we first print the log
			YetiLog.printYetiLog(log+");", this);
			// then print the exception
			if (e.getCause() instanceof RuntimeException || e.getCause() instanceof Error) {
				if (e.getCause() instanceof ThreadDeath) {
					//YetiLog.printYetiLog("/** POSSIBLE BUG FOUND: TIMEOUT **///", this);
				/*} else {
					//YetiLog.printYetiLog("/**BUG FOUND: RUNTIME EXCEPTION**///", this);
				/*}
			}
			else
				YetiLog.printYetiLog("/**NORMAL EXCEPTION:**///", this);
			//YetiLog.printYetiThrowable(e.getCause(), this);
		/*} catch (Error e){
			// if we are here there was a serious error
			// we print it
			YetiLog.printYetiLog(log+");", this);
			YetiLog.printYetiLog("BUG FOUND: ERROR", this);
			YetiLog.printYetiThrowable(e.getCause(), this);
			
		}*/
		return this.lastCallResult;	
			
			
			
		}

}
