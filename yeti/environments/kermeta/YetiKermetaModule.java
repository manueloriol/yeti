package yeti.environments.kermeta;

import yeti.YetiModule;

/**
 * 
 * Class that represents a YETI module for the Kermeta binding.
 * It contains all the routines of a single Kermeta class.
 * 
 * @author Erwan Bousse (erwan.bousse@gmail.com)
 * @date 28 juil. 2011
 *
 */
public class YetiKermetaModule extends YetiModule {


	/**
	 * In this implementation we consider it to be a class name.
	 * 
	 * @param className the name of the class of this module.
	 */
	public YetiKermetaModule(String className){
		super(className);
	}



	/**
	 * Checks that the throwable trace contains the name (or one of the names) of the module(s) in its trace.
	 * 
	 * @param throwableTrace the trace to check.
	 * @return true if the throwable is relevant.
	 */
	public boolean isThrowableInModule(String throwableTrace) {
		// First we split the trace in lines
		String []lines = throwableTrace.split("\n");

		boolean result = false;
		
		// If this module was combined with others, we check every module name
		if(getCombiningModules() != null) {
			for (YetiModule module : this.getCombiningModules()) {
				String moduleName = module.getModuleName();
				// We want the last line to contain the module name
				result = result || lines[lines.length-1].contains("in \'["+moduleName);
			}
			return result;
		}
		// If not, we check its name directly
		else {
			// We want the last line to contain the module name
			result =  lines[lines.length-1].contains("in \'["+moduleName);
		}
		return result;
	}


}
