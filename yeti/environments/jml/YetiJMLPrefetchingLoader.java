package yeti.environments.jml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.environments.java.YetiJavaPrefetchingLoader;

/**
 * 
 * Class that represents the custom class loader to load classes of the program.
 * It creates Yeti routines that form constructors and methods annotated with JML.
 * 
 * If the classes are not annotated with JML they are treated as normal Java classes.
 * 
 * @author Vasileios Dimitriadis (vd508@cs.york.ac.uk, vdimitr@hotmail.com)
 * @date 20 Jul 2009
 *
 */
public class YetiJMLPrefetchingLoader extends YetiJavaPrefetchingLoader {
	
	/**
	 * This character denotes that the 
	 */
	private static final char JML_SPECIFIC_INDICATOR = '$';
	
	/**
	 * Constructor that creates a new loader for loading JML annotated Java classes.
	 * 
	 * @param path the classpath to load classes.
	 */
	public YetiJMLPrefetchingLoader(String path) {
		super(path);
	}
	
	/**
	 * Create a Yeti routine for the JML annotated constructor to test
	 * 
	 * @param clazz the originating class.
	 * @param paramTypes the types of the parameters.
	 * @param type the type of the created object.
	 * @param mod the module to which we should add it.
	 * @param m the constructor.
	 * @return the Yeti routine for the constructor of the class c
	 */
	@SuppressWarnings("unchecked")
	protected YetiRoutine generateRoutineFromConstructor(Class clazz, YetiType[] paramTypes, YetiType type, YetiModule mod, Constructor con) {
		return new YetiJMLConstructor(YetiName.getFreshNameFrom(clazz.getName()), paramTypes , type, mod, con);
	}
	
	/**
	 * Create a Yeti routine for the JML annotated method to test
	 * 
	 * @param module the module to which add the method.
	 * @param method the method to add.
	 * @param paramTypes the types of the parameters.
	 * @param returnType the type returned by the method 
	 * @return a Yeti routine for this method
	 */
	protected YetiRoutine generateRoutineFromMethod(YetiModule module, Method method, YetiType[] paramTypes, YetiType returnType) {
		return new YetiJMLMethod(YetiName.getFreshNameFrom(method.getName()), paramTypes , returnType, module, method);
	}
	
	/**
	 * Add a method to the module if it is usable.
	 * 
	 * @param module the module to which add the method.
	 * @param method the method to add.
	 * @param usable True if it should be added.
	 * @param paramTypes the types of the parameters.
	 */
	public void addMethodToModuleIfUsable(YetiModule module, Method method, boolean usable, YetiType[] paramTypes) {
		if(!isJMLSpecific(method)) {
			super.addMethodToModuleIfUsable(module, method, usable, paramTypes);
		}
	}
	
	/**
	 * Check whether the method to add is a method generated from JML
	 * 
	 * @param method The method to test
	 * @return True if the method contains '$'
	 */
	protected boolean isJMLSpecific(Method method) {
		// if it is jml-specific, skip the method
		return method.getName().indexOf(JML_SPECIFIC_INDICATOR) >= 0; 
	}
}
