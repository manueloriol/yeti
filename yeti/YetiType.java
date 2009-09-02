package yeti;

import java.util.HashMap;
import java.util.Vector;


/**
 * Class that represents types in Yeti.
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Jun 22, 2009
 */
public class YetiType {

	/**
	 *	True if there is a maximum to the number of instances by default.
	 */
	public static boolean defaultTypesHaveCapOnNumberOfDirectInstances = true;
	
	/**
	 *	The default maximum to the number of instances.
	 */
	public static int defaultMaximumNumberOfDirectInstances = 1000;
	
	/**
	 *	True if there is a maximum to the number of instances of this type.
	 */
	public boolean hasMaximumNumberOfDirectInstances;
	
	/**
	 *	The maximum to the number of instances of this type.
	 */
	public int maximumNumberOfDirectInstances;
	
	/**
	 * The name of the type. Note that this can be a class name,  a type with a generic instantiation, or an interface.
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
	 * Structure that stores all direct instances of this type (this type is their defining type)
	 */
	public Vector<YetiVariable> directInstances=new Vector<YetiVariable>();

	
	/**
	 * Adds an instance to the type in question and all supertypes
	 *
	 * @param v the instance to add.
	 */
	public synchronized void addInstance(YetiVariable v){
		instances.add(v);
		// if there is a cap on the number of instances, we will remove one at random to make room
		if (v.getType().equals(this)) {
			// if there is a cap and we got to it
			if (this.hasMaximumNumberOfDirectInstances&&directInstances.size()>=this.maximumNumberOfDirectInstances) {
				this.removeInstance(this.getRandomDirectInstance());
			}
			directInstances.add(v);
		}
		// we add the instance to all parents
		for (YetiType t: directSuperTypes.values()) 
			t.addInstance(v);
	}
	
	/**
	 * Removes an instance to the type in question and all supertypes
	 *
	 * @param v the instance to add.
	 */
	public synchronized void removeInstance(YetiVariable v){
		instances.remove(v);
		// if we are in the type that defined the instance
		if (v.getType().equals(this)) {
			// we remove it from its set of real instances
			directInstances.remove(v);
			YetiVariable.allId.remove(v.identity.value);
			YetiVariable.nVariables--;
		}
		for (YetiType t: directSuperTypes.values()) 
			t.removeInstance(v);
	}
	/**
	 * Returns all instances of this type
	 * @return  all instances of this type.
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
	 * Returns an instance of this type at random.
	 * 
	 * @return the chosen instance.
	 */
	public YetiVariable getRandomDirectInstance(){
		double d=Math.random();
		int i=(int) Math.floor(d*this.directInstances.size());
		return this.directInstances.get(i);
	}
	
	/**
	 * A HashMap that stores direct super types. This might be used 
	 * by specific implementations.
	 */
	public HashMap <String, YetiType> directSuperTypes=new HashMap<String,YetiType>();

	/**
	 * A HashMap that stores all instantiated subtypes. 
	 * 
	 */
	public HashMap <String, YetiType> allSubtypes=new HashMap<String,YetiType>();

	/**
	 * A HashMap that stores all instantiated types. 
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
		this.maximumNumberOfDirectInstances=YetiType.defaultMaximumNumberOfDirectInstances;
		this.hasMaximumNumberOfDirectInstances = defaultTypesHaveCapOnNumberOfDirectInstances;
	}

	/**
	 * Adds a subtype that was instantiated
	 * 
	 * @param yt type to add
	 */
	public synchronized void addSubtype(YetiType yt){
		allSubtypes.put(yt.getName(), yt);
	}
	
	/**
	 * Returns the name of the YetiType.
	 * @return  the name of the type
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
	 * The vector of interestingValues.
	 */
	private Vector<Object> interestingValues = new Vector<Object>();
	
	/**
	 * Simple getter for interesting values.
	 * @return  the vector of interesting values.
	 */
	public Vector<Object> getInterestingValues() {
		return interestingValues;
	}

	/**
	 * Adds an interesting value on this type.
	 * 
	 * @param interestingValue the value to add.
	 */
	public void addInterestingValues(Object interestingValue) {
		if (!this.hasInterestingValues())
			this.setHasInterestingValues(true);
		this.interestingValues.add(interestingValue);
	}

	/**
	 * Resets interesting values on this type.
	 * 
	 * @param interestingValue the value to add.
	 */
	public void resetInterestingValues() {
		this.interestingValues=new Vector<Object>();
		this.setHasInterestingValues(false);
	}
	/**
	 * Returns an interesting value and removes it from the list of interesting values.
	 * 
	 * @param interestingValue the value to add.
	 */
	public Object removeInterestingValue() {
		if (interestingValues.size()==1) 		
			this.setHasInterestingValues(false);

		return this.interestingValues.remove(interestingValues.size()-1);
	}

	/**
	 * A simple setter to say that a type has interesting values.
	 * @param hasInterestingValues  true if it has interesting values, false otherwise.
	 */
	public void setHasInterestingValues(boolean hasInterestingValues) {
		this.hasInterestingValues = hasInterestingValues;
	}

	/**
	 * Indicates whether this type has interesting values.
	 */
	private boolean hasInterestingValues = false;
	
	/**
	 * Defines whether this type has interesting values.
	 * 
	 * @return true if it has interesting values.
	 */
	public boolean hasInterestingValues() {
		return hasInterestingValues;
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