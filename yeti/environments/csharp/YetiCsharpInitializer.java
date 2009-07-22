package yeti.environments.csharp;

import java.io.IOException;
//import java.lang.reflect.Modifier;
//import java.lang.reflect.Constructor;
import java.util.ArrayList;

import yeti.YetiInitializationException;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiServerSocket;
//import yeti.environments.csharp.YetiCsharpPrefetchingLoader;
import yeti.environments.csharp.YetiCsharpSpecificType;

/**
 * Class that represents the initialiser for Csharp.
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 21, 2009
 *
 */

public class YetiCsharpInitializer extends YetiInitializer {

	private ArrayList<String> strTypes;
	private ArrayList<String> cons,meths,inters;
	
	public YetiCsharpInitializer()
	{
		strTypes=new ArrayList<String>();
		cons=new ArrayList<String>();
		meths=new ArrayList<String>();
		inters=new ArrayList<String>();
	
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
	public void initialize(String[] args) throws YetiInitializationException {
		
		YetiServerSocket soc = new YetiServerSocket();
		
		// we initialize primitive types first
		// the primitives hav the typ names that C# has
		YetiCsharpSpecificType.initPrimitiveTypes();
		
		try {
			strTypes = soc.getData(2000);
			cons = soc.getData(2100);
			meths = soc.getData(2200);
			inters = soc.getData(2300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//For each type we do what the Prefetching Loader does	
		for(String s: strTypes)
		{
			//st[0]: stores types names, st[1]: stores base types names
			//e.g. YetiTest:System.Object
			System.out.println(s);
			String[] st = s.split(":");
			System.out.println("st[0]: "+ st[0]);
			System.out.println("st[1]: "+ st[1]);
			YetiType type=new YetiCsharpSpecificType(st[0].trim());
			YetiType.allTypes.put(type.getName(), type);
			YetiLog.printDebugLog("adding "+type.getName()+" to yeti types ", this);
			//If the base class is not object we add it to the subtypes 
			//of that base type
			String parent = st[1].trim();
			if (!parent.equalsIgnoreCase("Object") && YetiType.allTypes.containsKey(parent)){
				YetiLog.printDebugLog("linking "+type.getName()+" to "+parent, this);
				YetiType.allTypes.get(parent).allSubtypes.put(st[0], type);
			}
							
			// we create the YetiModule out of the type
			// which exists in st[0] each time
			YetiModule mod = this.makeModuleFromClass(st[0]);
			YetiModule.allModules.put(st[0], mod);
			
		}
		// we link an interface with its type to the parent interfaces
		for (String i: inters ) 
		{
			String[] st = i.split(":");
			System.out.println("st[0]: "+ st[0]);
			System.out.println("st[1]: "+ st[1]);
			YetiType type=new YetiCsharpSpecificType(st[1].trim());
			if (YetiType.allTypes.containsKey(st[0])){
				YetiLog.printDebugLog("linking "+type.getName()+" to "+st[0], this);
				YetiType.allTypes.get(st[0]).allSubtypes.put(st[1], type);
				System.out.println("---------\n"+i+"\n----------");
			}
		}
		// Here we add the constructors of the assemblies
		// Each constructor is put to the Module and Type (i.e. class)
		// that it belongs to and we do that by getting the types/modules from
		// the fields that store them in Java (allTypes,allModules)
		for(String cs: cons)
		{
			String[] st = cs.split(":");
			YetiType t = YetiType.allTypes.get(st[0].trim());
			YetiModule m = YetiModule.allModules.get(st[0].trim());
			addConstructors(cs, t, m);
			//System.out.println(cs);
		}
		
		// Here we add the methods of the assemblies
		// Each method is put to the Module and Type(i.e. class)
		// that it belongs to and we do that by getting the types/modules from
		// the fields that store them in Java (allTypes,allModules)
		for(String ms: meths)
		{
			String[] st = ms.split(":");
			YetiType t = YetiType.allTypes.get(st[0].trim());
			YetiModule m = YetiModule.allModules.get(st[0].trim());
			addMethods(ms,t,m);
			//System.out.println(ms);
		}
		
		//TODO connect to C#
		
//
//		// we try to load classes that will certainly be used
//		try {
//			cl.loadClass("java.lang.String");
//		} catch (ClassNotFoundException e1) {
//			// should never happen
//			e1.printStackTrace();
//		}
//		
//		// we go through all arguments
//		for(int i=0; i<args.length; i++) {
//			if (args[i].equals("-java")) 
//				ignore(args[i]);
//			else {
//				
//				try {
//					// we load all classes in path
//					cl.loadAllClassesInPath();
//					
//					// TODO we load the classes defined in the module to test
//					cl.loadClass("yeti.test.YetiTest");
//
//				} catch (ClassNotFoundException e) {
//					// Should not happen, but... we ignore it...
//					YetiLog.printDebugLog(e.toString(), this);
//					// e.printStackTrace();
//				}
//			}
//		}
	}
	
	
	private void addConstructors(String c, YetiType type, YetiModule mod) {
		String[] st = c.split(":");
		System.out.println("st[0]: "+ st[0]);
		System.out.println("st[1]: "+ st[1]);
		String[] pars = st[1].split(";");
		boolean usable = true;
		YetiType []paramTypes;
		//Check if there are any parameters or not
		if("\n".equals(pars[0])) paramTypes=new YetiType[0];
		else paramTypes=new YetiType[pars.length];
		// for all types we box the types.
		for (int i=0; i<paramTypes.length; i++){
			
			if (YetiType.allTypes.containsKey(pars[i].trim())){				
				paramTypes[i]=YetiType.allTypes.get(pars[i]);						
			} else {
				usable = false;
			}
		}
		
		// if we don't know a type from the constructor we don't add it
		if (usable){
			System.out.println("+++++++++++++++++++++++");
			YetiLog.printDebugLog("adding constructor to "+type.getName()+" in module "+mod.getModuleName(), this);
			YetiCsharpConstructor construct = new YetiCsharpConstructor(YetiName.getFreshNameFrom(st[0]), paramTypes , type, mod,"");		
			// add it as a creation routine for the type
			type.addCreationRoutine(construct);
			// add the constructor as a routines to test
			mod.addRoutineInModule(construct);
			System.out.println(c);
			System.out.println("=======================");
		}
	}
	
	
	private void addMethods(String c, YetiType type, YetiModule mod) {
		String[] st = c.split(":");
		/*System.out.println("st[0]: "+ st[0]); //The name of the class that has the method
		System.out.println("st[1]: "+ st[1]); //The name of the method
		System.out.println("st[2]: "+ st[2]); //The parameters of the method
		System.out.println("st[3]: "+ st[3]); //The return type of the method e.g.Void,Boolean...
		System.out.println("st[4]: "+ st[4]); //Boolean flag indicating if it is static or not*/
		String[] pars = st[2].split(";"); //We further split the parameters
		boolean usable = true;
		YetiType []paramTypes;
		int numberParameters=-1;
		//Check if there are any parameters or not
		if("\n".equals(pars[0])) numberParameters=0;
		else numberParameters=pars.length;
		
		boolean isStatic;
		if("True".equals(st[4].trim())) isStatic = true;
		else isStatic = false;
	    //System.out.println("Static: "+isStatic+"Pars: "+pars.length);
		// if the method is static we do not introduce a slot for the target.
		int offset = 0;
		if (isStatic){
			paramTypes=new YetiType[numberParameters];
		} else {
			paramTypes=new YetiType[numberParameters+1];
			offset = 1;
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			if (YetiType.allTypes.containsKey(st[0])){
				paramTypes[0]=YetiType.allTypes.get(st[0]);						
			} else {
				usable = false;
			}
		}
		
		// for all types we box the types.
		//System.out.println("ParamTypes: "+paramTypes.length);
		for (int i=0; i<pars.length; i++){
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			if (YetiType.allTypes.containsKey(st[0])){
				paramTypes[i+offset]=YetiType.allTypes.get(st[0]);						
			} else {
				usable = false;
			}
		}
		
		// if we don't know a type from the constructor we don't add it
		if (usable && (!YetiCsharpMethod.isMethodNotToAdd(st[1]))){
			System.out.println(c);
			YetiLog.printDebugLog("adding method "+st[1]+" in module "+st[0], this);
			// add it as a creation routine for the return type
			YetiType returnType = YetiType.allTypes.get(st[3]);
			if (returnType==null)
				returnType = new YetiCsharpSpecificType(st[3]);
			YetiCsharpMethod method = new YetiCsharpMethod(YetiName.getFreshNameFrom(st[1]), paramTypes , returnType, mod,st[1],isStatic);
			returnType.addCreationRoutine(method);
			// add the constructor as a routines to test
			mod.addRoutineInModule(method);
		}
	}
	
	
	public YetiModule makeModuleFromClass(String c){
		YetiModule mod=new YetiCsharpModule(c);
		
		return mod;
	}
	
	public static void main(String[] args)
	{
		YetiCsharpInitializer init = new YetiCsharpInitializer();
		try {
			String[] a = null;
			
			init.initialize(a);
		} catch (YetiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
