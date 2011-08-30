package yeti.environments.kermeta;

import java.util.ArrayList;

import kyeti.util.KermetaSimplifiedInterpreter;
import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;
import yeti.environments.kermeta.YetiKermetaLoader;
import fr.irisa.triskell.kermeta.language.structure.ClassDefinition;
import fr.irisa.triskell.kermeta.language.structure.Type;
import fr.irisa.triskell.kermeta.language.structure.TypeVariable;
import fr.irisa.triskell.kermeta.runtime.RuntimeObject;

/**
 * 
 * Class that represents a "constructor" of a Kermeta type, even if constructors don't exist in Kermeta.
 * Calling this kermeta routine will create a Kermeta instance of its module's class. 
 * 
 * @author Erwan Bousse (erwan.bousse@gmail.com)
 * @date 28 juil. 2011
 *
 */
public class YetiKermetaConstructor extends YetiKermetaRoutine {

	/**
	 * The properties names of the concerned Kermeta class. Required hack in order to be able to use the "getProperties"
	 * method of the created object, so that we can set its properties.
	 */
	private String[] paramNames;

	/**
	 * Constructor of the constructor.  
	 * @param name
	 * @param openSlots The types slots. Decide how many parameters of the Kermeta object will be set by this constructor. The types of "openslots" must be in the same order as the names in "paramNames".
	 * @param paramNames Names of the parameters set by the constructor. The names in "paramNames" must be in the same order as the types of "openslots".
	 * @param returnType The return type of the routine, which is the type of the constructed kermeta instance.
	 * @param originatingModule The module it comes from. The module's name will be used as a class name, so it is very important.
	 */
	public YetiKermetaConstructor(YetiName name, YetiType[] openSlots, String[] paramNames,
			YetiType returnType, YetiModule originatingModule) {
		super(name, openSlots, returnType, originatingModule);
		this.paramNames = paramNames;
	}




	/**
	 * Makes the effective call (lets return the exceptions and Errors).
	 * 
	 * @param arg the arguments of the call.
	 * @return the logs.
	 * @throws YetiCallException the wrapped exception. 
	 */
	public String makeEffectiveCall(YetiCard[] arg)	throws YetiCallException {
		// The simplified interpreter will create the instance
		KermetaSimplifiedInterpreter interpreter = YetiKermetaLoader.yetiLoader.getInterpreter();
		// The classname is found using the module name (because modules are classes)
		String className = originatingModule.getModuleName();
		// The log we will return
		StringBuffer logbuf = new StringBuffer("");		
		// The call result, if any
		lastCallResult=null;
		// First we construct the object
		RuntimeObject o = null;	
		try {
			// If the class is parameterized, then we use a specific constructor
			// But first we need the classes to use to parameterize
			if(interpreter.testParameterizedClass(className)) {
				ClassDefinition classDef = interpreter.getClassDefinition(className);
				ArrayList<ClassDefinition> paramClasses = new ArrayList<ClassDefinition>();
				for(TypeVariable type : classDef.getTypeParameter()) {
					paramClasses.add((ClassDefinition)YetiKermetaLoader.yetiLoader.typeToTypeDefinition((Type)type));
				}
				o = interpreter.constructKermetaObjectForParameterizedClassWithSimpleClasses(className, paramClasses);
			}
			// If it is not, a simple constructor
			else {
				o = interpreter.constructKermetaObject(className);
			}
		} catch (Throwable t) { //useful ? is there any risk ?
			throw new YetiCallException(logbuf.toString(),t);
		}

		// we start by unboxing the arguments boxed into the cards
		YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
		logbuf.append(returnType.toString() + " " + id.getValue() + "=new "+returnType.getName()+"(");
		for (int i=0;i<arg.length; i++){
			// if we should replace it by a null value, we do it
			if (YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>Math.random()) {
				o.getProperties().put(this.paramNames[i], interpreter.getVoid());
				logbuf.append("null");
			} else {
				// note that we use getValue to get the actual value
				o.getProperties().put(this.paramNames[i], (RuntimeObject)arg[i].getValue());
				// we use toString() to make it pretty-print.
				logbuf.append(arg[i].toString());
			}
			if (i<arg.length-1){
				//log+=",";
				logbuf.append(",");
			}
		}

		// if it succeeds we create the new variable
		this.lastCallResult=new YetiVariable(id, returnType, o);

		logbuf.append(");");
		// print the log
		String log = logbuf.toString();
		YetiLog.printYetiLog(log, this);
		YetiLog.printDebugLog(log, this);
		return log;
	}


	
	public String toString() {
		return this.getName().getValue();
	}



}
