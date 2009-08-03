package yeti.environments.csharp;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;
import yeti.environments.java.YetiJavaSpecificType;


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


	/**
	 * Makes the effective call (lets return the exceptions and Errors).
	 * 
	 * @param arg the arguments of the call.
	 * @return the logs.
	 * @throws YetiCallException the wrapped exception. 
	 */

	public String makeEffectiveCall(YetiCard[] arg)
	throws YetiCallException {
		String log;
		String msg="";
		lastCallResult=null;
		Object []initargs=new Object[arg.length];
		
		msg+=c+":";
		// we start by unboxing the arguments boxed into the cards
		YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
		log = returnType.toString() + " " + id.getValue() + "=new "+returnType.getName()+"(";
		for (int i=0;i<arg.length; i++){
			// if we should replace it by a null value, we do it
			if (YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>Math.random()&&!(((YetiJavaSpecificType)arg[i].getType()).isSimpleType())) {
				initargs[i]=null;
				log=log+"null";
			} else {
				// note that we use getValue to get the actual value
				initargs[i]=arg[i].getValue();
				msg+=initargs[i].toString();
				
				// we use toString() to make it pretty-print.
				log=log+arg[i].toString();
			}
			if (i<arg.length-1){
				log=log+",";
			}
		}
		// we try to make the call
		Object o = null;
		try {
			//o = c.newInstance(initargs);
		} catch (Throwable t) {
			throw new YetiCallException(log,t);
		}
		// if it succeeds we create the new variable
		this.lastCallResult=new YetiVariable(id, returnType, o);
		log=log+");";
		// print the log
		YetiLog.printYetiLog(log, this);
		return log;
	}

}
