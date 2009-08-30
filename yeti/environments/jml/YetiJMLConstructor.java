package yeti.environments.jml;

import java.lang.reflect.Constructor;

import yeti.YetiCard;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.environments.java.YetiJavaConstructor;

/**
 * 
 * Class that represents a constructor in Java annotated with JML.
 * 
 * @author Vasileios Dimitriadis (vd508@cs.york.ac.uk, vdimitr@hotmail.com)
 * @date 20 Jul 2009
 *
 */
public class YetiJMLConstructor extends YetiJMLRoutine {
	
	/**
	 * The wrapped Java constructor.
	 */
	private YetiJavaConstructor javaConstructor = null;
	
	/**
	 * Constructor to define a JML constructor.
	 * 
	 * @param name the name of this constructor.
	 * @param openSlots the open slots for this constructor.
	 * @param returnType the returnType of this constructor.
	 * @param originatingModule the module in which it was defined.
	 * @param constructor the constructor itself.
	 */
	public YetiJMLConstructor(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, Constructor<?> constructor) {
		super(name, openSlots, returnType, originatingModule);
		javaConstructor = new YetiJavaConstructor(name, openSlots, returnType, originatingModule, constructor);
		acceptableExceptionTypes = javaConstructor.getAcceptableExceptionTypes();
	}
	
	@Override
	public String makeEffectiveCall(YetiCard[] yetiCards) throws Throwable {
		return javaConstructor.makeEffectiveCall(yetiCards);
	}
	
	@Override
	public String toString() {
		return javaConstructor.toString();
	}
}
