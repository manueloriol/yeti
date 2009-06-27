package yeti;

import java.util.HashMap;
import java.util.Vector;


/**
 * Class that represents types in Yeti.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiType {

	/**
	 * The name of the type. Note that this can be a class name, 
	 * a type with a generic instanciation, or an interface.
	 */
	protected String name;

	/**
	 * Structure that stores all routines that return this type
	 */
	public Vector<YetiRoutine> creationRoutines=new Vector<YetiRoutine>();

	/**
	 * adds a routine to the type in question and all supertypes
	 * 
	 * @param v the creation routine to add to this type.
	 */
	public synchronized void addCreationRoutine(YetiRoutine v){
		creationRoutines.add(v);
		for (YetiType t: directSuperTypes.values()) 
			t.addCreationRoutine(v);
	}
	
	/**
	 * Return the creation routines of this type.
	 * 
	 * @return
	 */
	public Vector<YetiRoutine> getCreationRoutines(){
		return creationRoutines;
	}
	
	/**
	 * Returns a routine of this type at random.
	 * 
	 * @return the routine found.
	 * @throws NoCreationRoutineInType Just in case we don't know how to create the type.
	 */
	public YetiRoutine getRandomCreationRoutine() throws NoCreationRoutineInType{
		if (creationRoutines.size()==0) throw new NoCreationRoutineInType("no creation routine for: "+this.name);
		double d=Math.random();
		int i=(int) Math.floor(d*(double)(creationRoutines.size()));
		YetiLog.printDebugLog("trying to get routine for: "+this.name, this);
		return creationRoutines.get(i);
	}
	
	/**
	 * Structure that stores all instances of this type
	 */
	public Vector<YetiVariable> instances=new Vector<YetiVariable>();

	/**
	 * Adds an instance to the type in question and all supertypes
	 *
	 * @param v the instance to add.
	 */
	public synchronized void addInstance(YetiVariable v){
		instances.add(v);
		for (YetiType t: directSuperTypes.values()) 
			t.addInstance(v);
	}
	
	/**
	 * Returns all instances of this type
	 * 
	 * @return all instances of this type.
	 */
	public Vector<YetiVariable> getInstances(){
		return instances;
	}
	
	/**
	 * Returns an instance of this type at random.
	 * 
	 * @return the chosen instance.
	 */
	public YetiVariable getRandomInstance(){
		double d=Math.random();
		int i=(int) Math.floor(d*instances.size());
		return instances.get(i);
	}
	
	/**
	 * A HashMap that stores direct super types. This might be used 
	 * by specific implementations.
	 */
	public HashMap <String, YetiType> directSuperTypes=new HashMap<String,YetiType>();

	/**
	 * A HashMap that stores all instanciated subtypes. 
	 * 
	 */
	public HashMap <String, YetiType> allSubtypes=new HashMap<String,YetiType>();

	/**
	 * A HashMap that stores all instanciated types. 
	 * 
	 */
	public static HashMap <String, YetiType> allTypes=new HashMap<String,YetiType>();

	/**
	 * Constructor of YetiTypes.
	 * 
	 * @param name The name of the YetiType
	 */
	public YetiType(String name){
		this.name=name;
		allTypes.put(name, this);
	}

	/**
	 * Adds a subtype that was instanciated
	 * 
	 * @param yt type to add
	 */
	public synchronized void addSubtype(YetiType yt){
		allSubtypes.put(yt.getName(), yt);
	}
	
	/**
	 * Returns the name of the YetiType.
	 * 
	 * @return the name of the type
	 */
	public String getName(){
		return name;
	}
	
	
	
	/* (non-Javadoc)
	 * 
	 * Used for pretty-print of the type
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Returns true if the present type is a subtype of the argument.
	 * 
	 * @param yt the type to compare to.
	 */
	public boolean isSubtype(YetiType yt){
		if (directSuperTypes.containsValue(yt)) return true;
		for (YetiType t: directSuperTypes.values()) 
			if (t.isSubtype(yt)) return true; 
			
		return false;
		
	}
	
	/**
	 * Prints all creation routines listed for all types.  
	 */
	public static void printCreationProcedureList(){
		YetiLog.printDebugLog("Constructors per type: ", YetiType.class);			
		for(YetiType yt: YetiType.allTypes.values()){
			YetiLog.printDebugLog(" "+yt.name+":", YetiType.class);
			for (YetiRoutine r : yt.creationRoutines){
				YetiLog.printDebugLog("     "+r.name.value, YetiType.class);
			}
		}
	}

	
}