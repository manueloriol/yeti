package yeti.environments.java;

import yeti.YetiInitializationException;
import yeti.YetiLog;
import yeti.environments.YetiInitializer;


/**
 * Class that represents the initialiser for Java.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaInitializer extends YetiInitializer{

	/**
	 * The custom class loader that is going to be used.
	 */
	YetiJavaPrefetchingLoader cl= new YetiJavaPrefetchingLoader(System.getProperty("java.class.path"));
	
	/**
	 * A simpel helper routine that ignores the parameter String.
	 * 
	 * @param s teh string to be ignored.
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
			cl.loadClass("java.lang.String");
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
					// we load all classes in path
					cl.loadAllClassesInPath();
					
					// TODO we load the classes defined in the module to test
					cl.loadClass("yeti.test.YetiTest");

				} catch (ClassNotFoundException e) {
					// Should not happen, but... we ignore it...
					YetiLog.printDebugLog(e.toString(), this);
					// e.printStackTrace();
				}
			}
		}
		
		
	}

}
