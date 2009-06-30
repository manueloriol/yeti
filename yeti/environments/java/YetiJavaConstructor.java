package yeti.environments.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;

/**
 * Class that represents a constructor in Java.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaConstructor extends YetiJavaRoutine {

	/**
	 * The actual constructor.  
	 */
	@SuppressWarnings("unchecked")
	private Constructor c;
	
	
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
	public YetiJavaConstructor(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, Constructor c) {
		super(name, openSlots, returnType, originatingModule);
		this.c=c;
	}
	

	/* (non-Javadoc)
	 * Returns the actual name of the constructor.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return c.getName();
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
			Object o = c.newInstance(initargs);
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
		} catch (InstantiationException e) {
			// should never happen
			// we ignore it
			// TODO check that
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// can happen
			// ignored!
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			// should never happen
			//e.printStackTrace();
		}
		return this.lastCallResult;
	}

}
