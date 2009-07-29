package yeti.environments.java;

import java.io.FilePermission;
import java.security.Permission;

import yeti.YetiInitializationException;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiLoader;
import yeti.environments.YetiSecurityException;


/**
 * Class that represents the initialiser for Java.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaInitializer extends YetiInitializer {

	/**
	 * The custom class loader that is going to be used.
	 */
	protected YetiLoader prefetchingLoader = null;
	
	public YetiJavaInitializer(YetiLoader prefetchingLoader) {
		this.prefetchingLoader = prefetchingLoader;
	}
	
	/**
	 * A simple helper routine that ignores the parameter String.
	 * 
	 * @param s the string to be ignored.
	 */
	public void ignore(String s){

	}

	/* (non-Javadoc)
	 * Initializes the Java environment.
	 * 
	 * @see yeti.environments.YetiInitializer#initialize(java.lang.String[])
	 */
	@Override
	public void initialize(String []args) throws YetiInitializationException {
		// we initialize primitive types first
		YetiJavaSpecificType.initPrimitiveTypes();

		// we try to load classes that will certainly be used
		try {
			prefetchingLoader.loadClass("java.lang.String");
		} catch (ClassNotFoundException e1) {
			// should never happen
			e1.printStackTrace();
		}

		// we go through all arguments
		for(int i=0; i<args.length; i++) {
			if (args[i].equals("-java")) 
				ignore(args[i]);
			else {

				try {
					// we load all classes in path and String
					prefetchingLoader.loadAllClassesInPath();
					prefetchingLoader.loadClass("java.lang.String");
					// we want to test these modules
					String []modulesToTest=null;
					for (String s0: args) {
						if (s0.startsWith("-testModules=")) {
							String s1=s0.substring(13);
							modulesToTest=s1.split(":");
							break;
						}
					}

					// we iterate through the modules
					// if the module does not exist we load it
					for(String moduleToTest : modulesToTest) {
						YetiModule yetiModuleToTest = YetiModule.allModules.get(moduleToTest);
						if(yetiModuleToTest==null) {
							prefetchingLoader.loadClass(moduleToTest);
						} 
					}

				} catch (ClassNotFoundException e) {
					// Should not happen, but... we ignore it...
					YetiLog.printDebugLog(e.toString(), this, true);
					// e.printStackTrace();
				}
			}
		}
		System.setSecurityManager(new SecurityManager() {
			// we set this security manager that filters file output/execution for worker threads
			public void checkPermission(Permission perm) {
				// if we are in the thread group of worker threads
				if (this.getThreadGroup()==YetiJavaTestManager.workersGroup) {
					// if we are trying to access a file permission
					if (perm instanceof FilePermission) {
						String action = perm.getActions();
						// if any of those is in the permission requested, we throw the exception
						if ((action.indexOf("write")>=0) || (action.indexOf("execute")>=0) || (action.indexOf("delete")>=0)) {
							YetiLog.printDebugLog("Yeti did not grant permission: "+ perm, this);
							throw new YetiSecurityException("Yeti did not grant the following file permission: "+perm.toString());
						}
					}
				}
			}
		});

	}

}
