/**
 * 
 */
package yeti;

import java.util.HashMap;

/**
 * Class that represents a unit of testing. Typically for Java this would be a class or a package, for C a header file.
 * A module contains a list of routines to test. The strategy will iterate through them.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public abstract class YetiModule {
	
	/**
	 * The name of the module
	 */
	protected String moduleName;

	
	/**
	 * A HashMap of all existing modules.
	 */
	public static HashMap <String, YetiModule> allModules =new HashMap<String,YetiModule>();

	/**
	 * A HashMap of all routines in this module.
	 */
	public HashMap <String, YetiRoutine> routinesInModule =new HashMap<String,YetiRoutine>();
	
	/**
	 * Add a routine to the list of routine in module.
	 * 
	 * @param routine the routien to add.
	 */
	public void addRoutineInModule(YetiRoutine routine){
		routinesInModule.put(routine.name.toString(),routine);
	}
	
	/**
	 * Return a routine from this module with a given name.
	 * 
	 * @param name the name of the routine asked
	 * @return the routine selected
	 */
	public YetiRoutine getRoutineFromModuleWithName(String name){
		return routinesInModule.get(name);
	}

	
	/**
	 * Adds a module to the general list of modules.
	 * 
	 * @param module the module to add.
	 */
	public void addModuleToAllModules(YetiModule module){
		allModules.put(module.getModuleName(),module);
	}
	
	/**
	 * Get a routine at random.
	 * 
	 * @return the routine selected.
	 */
	public YetiRoutine getRoutineAtRandom(){
		double d=Math.random();
		int i=(int) Math.floor(d*routinesInModule.size());
		
		return (YetiRoutine)(routinesInModule.values().toArray()[i]);
	}
	
	
	/**
	 * Getter for the module name.
	 * 
	 * @return the module name.
	 */
	public String getModuleName(){
		return moduleName;
	}

	/**
	 * Setter for the module name.
	 * 
	 * @param name the name to set.
	 */
	public void setModuleName(String name){
		moduleName=name;
	}
}
