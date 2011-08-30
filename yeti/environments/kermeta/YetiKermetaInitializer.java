package yeti.environments.kermeta;


import javax.swing.JOptionPane;

import yeti.Yeti;
import yeti.YetiInitializationException;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.environments.YetiInitializer;
import yeti.environments.java.YetiJavaSecurityManager;


/**
 * Initialiser for the YETI Kermeta binding.
 * 
 * @author  Erwan bousse
 */
public class YetiKermetaInitializer extends YetiInitializer {

	/**
	 * The kermeta class loader that is going to be used.
	 */
	protected YetiKermetaLoader loader = null;

	/**
	 * Default constructor 
	 * @param loader the class loader to use.
	 */
	public YetiKermetaInitializer(YetiKermetaLoader loader) {
		this.loader = loader;
	}

	/**
	 * A simple helper routine that ignores the parameter String.
	 * 
	 * @param s the string to be ignored.
	 */
	public void ignore(String s){

	}

	/* (non-Javadoc)
	 * Initializes the Kermeta environment.
	 * 
	 * @see yeti.environments.YetiInitializer#initialize(java.lang.String[])
	 */
	@Override
	public void initialize(String []args) throws YetiInitializationException {
		// we go through all arguments
		for(int i=0; i<args.length; i++) {
			if (!args[i].equals("-kermeta"))
				ignore(args[i]);
			else {
				
				// little hack : overrides the option -testModules
				// indeed, kermeta requires ":" to write a full class name (as "." in Java)
				// so we consider "," to split instead
				// Problem : contradictory with the help provided with Yeti
				// TODO find another way ? add another option ? 
				String []modulesToTest=null;
				for (String s0: args) {
					if (s0.startsWith("-testModules=")) {
						String s1=s0.substring(new String("-testModules=").length());
						modulesToTest=s1.split(",");
						Yeti.testModulesName = modulesToTest;
						break;
					}
				}
				
				try {
					// Initializes the Kermeta interpreter with all the kermeta files in the path
					loader.prepareInterpreterWithPath();
					// Loads important classes from the kermeta framework, and adds interesting values
					YetiKermetaType.initImportantTypes();
					// some random generators
					loader.addRandomGenerators();
					// we load all classes in path
					loader.loadAllClassesInPath();
				}catch (Throwable t) {
					YetiLog.printDebugLog("A problem occured during initialization ! Error : "+t.getMessage(), this, true);
				}
	
				// we iterate through the modules
				// if the module is not found we warn the user
				if(modulesToTest!=null) {
					for(String moduleToTest : modulesToTest) {
						YetiModule yetiModuleToTest = YetiModule.allModules.get(moduleToTest);
						if(yetiModuleToTest==null) {
							YetiLog.printDebugLog("The module "+moduleToTest+" can't be found in the yetipath !", this, true);
						} 
					}
				} else {
					YetiLog.printDebugLog("You must specify modules to test.", this, true);
				}


			}
		}
		// We use the java test manager, so we use the same security manager
		System.setSecurityManager(new YetiJavaSecurityManager());
	}

	@Override
	public void addModule(String s) {
		// We don't allow to add a module in the testing procedure  at runtime
		JOptionPane.showMessageDialog(null, "impossible to add a module in kermeta testing");
	}

}
