/**
 * 
 */
package yeti.environments.java;

import yeti.YetiModule;


/**
 * Class that represents a Java module (typically in Java either a class or a package).
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaModule extends YetiModule {
	
	/**
	 * In this implementation we consider it to be a class name.
	 * 
	 * @param className the name of the class of this module.
	 */
	public YetiJavaModule(String className){
		super(className);
	}

	private String []moduleNames=null;
	
	/**
	 * Checks that the throwable trace contains the name (or one of the names) of the module(s) in its trace.
	 * 
	 * @param throwableTrace the trace to check.
	 * @return true if the throwable is relevant.
	 */
	public boolean isThrowableInModule(String throwableTrace) {
		String trace = throwableTrace.substring(throwableTrace.indexOf('\t')+1);
		if (moduleNames == null) {
			if (this.getCombiningModules()!=null) {
				YetiModule []modules = this.getCombiningModules();
				moduleNames = new String[modules.length];
				int i = 0;
				for (YetiModule m: modules) {
					moduleNames[i++]=m.getModuleName();
				}
			} else {
				moduleNames = new String[1];
				moduleNames[0] = this.getModuleName();
			}
		}
		for (String modName: moduleNames) {
			if (trace.contains(modName))
				return true;
		}
		return false;
	}
}
