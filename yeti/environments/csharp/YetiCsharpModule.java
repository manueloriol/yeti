package yeti.environments.csharp;

//import yeti.YetiLog;
import yeti.YetiModule;


/**
 * Class that represents a Csharp module (typically in Csharp either a class or an assembly).
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 20, 2009
 *
 */

public class YetiCsharpModule extends YetiModule {
	
	/**
	 * In this implementation we consider it to be a class name.
	 * 
	 * @param className the name of the class of this module.
	 */
	public YetiCsharpModule(String className){
		super(className);
	}
	
	/**
	 * Checks that the trace contains refers to the module(s) in its trace.
	 * 
	 * @param trace the trace to check.
	 * @return true if the throwable is relevant.
	 */
	public boolean isThrowableInModule(String trace) {
		String moduleName = this.getModuleName();
		int endExe = moduleName.indexOf(".exe");
		int endDLL = moduleName.indexOf(".dll");
		int end = trace.length();
		
		// if it is neither a dll nor a exe we return false
		if ((endExe<0) && (endDLL<0)) return false;
		
		// if it is one of those, we will cut it
		if (endExe>=0) end = endExe;
		if (endDLL>=0) end = endDLL;
		
		// we check that the trace contains the module name
		return (trace.indexOf(moduleName.substring(0,end))>=0);
	}

	
}
