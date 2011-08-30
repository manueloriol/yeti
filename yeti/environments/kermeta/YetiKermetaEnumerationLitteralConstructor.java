package yeti.environments.kermeta;

import kyeti.util.KermetaSimplifiedInterpreter;
import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;
import fr.irisa.triskell.kermeta.language.structure.Enumeration;
import fr.irisa.triskell.kermeta.runtime.RuntimeObject;

/**
 * 
 * Class that represents "constructor" of an instance of a Kermeta enumeration. 
 * 
 * @author Erwan Bousse (erwan.bousse@gmail.com)
 * @date 28 juil. 2011
 *
 */
public class YetiKermetaEnumerationLitteralConstructor extends YetiKermetaRoutine {

	/**
	 * The name of the literal to construct.
	 * Ex. enum{one,two,three} ; "two" is a litteral name. 
	 */
	private String literalName;
	
	/**
	 * The Kermeta enumeration to consider.
	 */
	private Enumeration enumeration;
	
	/**
	 * Constructor of the constructor. 
	 * @param name The YetiName of the routine.
	 * @param returnType The return type, which is the YetiType made from the kermeta enumeration.
	 * @param enumeration The Kermeta enumeration type.
	 * @param literalName The name of the litteral constructed by this constructor.
	 */
	public YetiKermetaEnumerationLitteralConstructor(YetiName name, YetiType returnType, Enumeration enumeration, String literalName) {
		super(name, new YetiType[0], returnType, null);
		this.literalName = literalName;
		this.enumeration=enumeration;
	}


	/**
	 * Makes the effective call (lets return the exceptions and Errors).
	 * 
	 * @param arg the arguments of the call.
	 * @return the logs.
	 * @throws YetiCallException the wrapped exception. 
	 */

	public String makeEffectiveCall(YetiCard[] arg)	throws YetiCallException {
		// We need the interpreter to create the instance
		KermetaSimplifiedInterpreter interpreter = YetiKermetaLoader.yetiLoader.getInterpreter();
		// The log to return
		String log = "";
		// The result, if any
		lastCallResult=null;
		// First we construct the object
		RuntimeObject o = null;	
		try {
			o = interpreter.getEnumerationLitteral(enumeration, literalName);
		} catch (Throwable t) { //useful ? is there any risk ?
			throw new YetiCallException(log,t);
		}
		// Log purpose
		YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
		log = returnType.toString() + " " + id.getValue() + "=new "+returnType.getName()+"(";
		// if it succeeds we create the new variable
		this.lastCallResult=new YetiVariable(id, returnType, o);
		log=log+");";
		// print the log
		YetiLog.printYetiLog(log, this);
		YetiLog.printDebugLog(log, this);
		return log;
	}
	
	public String toString() {
		return this.getName().getValue();
	}



}
