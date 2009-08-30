package yeti.environments.jml;

import java.lang.reflect.Method;

import yeti.YetiCard;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.environments.java.YetiJavaMethod;

/**
 * 
 * Class that represents a Java method annotated with JML.
 * 
 * @author Vasileios Dimitriadis (vd508@cs.york.ac.uk, vdimitr@hotmail.com)
 * @date 20 Jul 2009
 *
 */
public class YetiJMLMethod extends YetiJMLRoutine {
	
	/**
	 * The wrapped Java method
	 */
	private YetiJavaMethod javaMethod = null;
	
	/**
	 * Constructor to define a JML method. By default the method is non-static.
	 * 
	 * @param name the name of the method.
	 * @param openSlots the open slots of the method.
	 * @param returnType the return type of the method.
	 * @param originatingModule the module in which it was defined.
	 * @param method the method implementation.
	 */
	public YetiJMLMethod(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, Method method) {
		super(name, openSlots, returnType, originatingModule);
		javaMethod = new YetiJavaMethod(name, openSlots, returnType, originatingModule, method);
		acceptableExceptionTypes = javaMethod.getAcceptableExceptionTypes();
	}

	/**
	 * Constructor to define a JML method
	 * 
	 * @param name the name of the method.
	 * @param openSlots the open slots of the method.
	 * @param returnType the return type of the method.
	 * @param originatingModule the module in which it was defined.
	 * @param method the method implementation.
	 * @param isStatic the parameter that says whether the method is static or not.
	 */
	public YetiJMLMethod(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, Method method, boolean isStatic) {
		super(name, openSlots, returnType, originatingModule);
		javaMethod = new YetiJavaMethod(name, openSlots, returnType, originatingModule, method, isStatic);
		acceptableExceptionTypes = javaMethod.getAcceptableExceptionTypes();
	}
	
	@Override
	public String makeEffectiveCall(YetiCard[] yetiCards) throws Throwable {
		return javaMethod.makeEffectiveCall(yetiCards);
	}
	
	@Override
	public String toString() {
		return javaMethod.toString();
	}
}
