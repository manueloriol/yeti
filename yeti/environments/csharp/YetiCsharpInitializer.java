package yeti.environments.csharp;

import java.io.IOException;
import java.io.InputStream;
//import java.lang.reflect.Modifier;
//import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiInitializationException;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.environments.YetiInitializer;
import yeti.environments.csharp.YetiServerSocket;
//import yeti.environments.csharp.YetiCsharpPrefetchingLoader;
import yeti.environments.csharp.YetiCsharpSpecificType;
import yeti.strategies.YetiRandomPlusStrategy;
import java.lang.Thread;

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
	public static boolean initflag=false;
	
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
	@SuppressWarnings("static-access")
	@Override
	public void initialize(final String[] args) throws YetiInitializationException {
		
		YetiServerSocket soc = new YetiServerSocket();
		
		Thread th = new Thread(new Runnable()
		{
			String[] as = args;
			
			public void run() {
				Runtime run = Runtime.getRuntime();
				String command = "C:\\Users\\st552\\Documents\\Visual Studio 2008\\Projects\\CsharpReflexiveLayer\\CsharpReflexiveLayer\\bin\\Debug\\CsharpReflexiveLayer.exe";
				 
				//String command = "C:\\Users\\st552\\test\\CsharpReflexiveLayer.exe";
				try {
					Process p = run.exec(command);
					
					InputStream in = p.getInputStream();
					//PrintStream ps = new PrintStream(in);
					//ps.println();
				    int c;
				    while ((c = in.read()) != -1) {
				      //System.out.print((char) c);
				    }
				} catch (IOException e) {					
					YetiCsharpInitializer.initflag=true;
				}
			}
			} );
		
		th.start();
		
		
		if(YetiCsharpInitializer.initflag) throw new YetiInitializationException("C#ReflexiveLayer Unable To Start");
		// we initialize primitive types first
		// the primitives have the type names that C# has
		YetiCsharpSpecificType.initPrimitiveTypes();
		try {
		@SuppressWarnings("unused")
		ArrayList<String> a = YetiServerSocket.getData(2300);	    		
		System.out.println("**************************************");
		
			YetiServerSocket.sendData(2400, "ContrExample1.exe");
		} catch (IOException e1) {
			throw new YetiInitializationException(e1.getMessage());
		} catch (Exception e) {
			throw new YetiInitializationException(e.getMessage());
		}
		System.out.println("**************************************");
		
		try {
			YetiServerSocket.sendData(2400, "reached read point");
			System.out.println("1");
			strTypes = soc.getData(2300);
			System.out.println("2");
			cons = soc.getData(2300);
			System.out.println("3");
			meths = soc.getData(2300);
			System.out.println("4");
			inters = soc.getData(2300);
			System.out.println("5");
		} catch (IOException e) {			
			throw new YetiInitializationException(e.getMessage());
		} catch (Exception e) {
			throw new YetiInitializationException(e.getMessage());
		}
		System.out.println("**************************************");
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
				YetiType.allTypes.get(parent).allSubtypes.put(st[0].trim(), type);
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
			YetiType type=new YetiCsharpSpecificType(st[0].trim());
			if (YetiType.allTypes.containsKey(st[0])){
				YetiLog.printDebugLog("linking "+type.getName()+" to "+st[0], this);
				YetiType.allTypes.get(st[0]).allSubtypes.put(st[1].trim(), type);
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
			System.out.println("$$$$$"+t);
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
			System.out.println(ms);
		}
		
	}
	
	
	private void addConstructors(String c, YetiType type, YetiModule mod) {
		String[] st = c.split(":");
		System.out.println("st[0]: "+ st[0]);
		System.out.println("st[1]: "+ st[1]);
		String[] pars = st[1].split(";");
		boolean usable = true;
		YetiType []paramTypes=null;
		int numberPars=0; //The number of parameters the constructor has
		//Checks if there are any parameters or not
		if("\n".equals(pars[0]))
			paramTypes=null;					
		else 
		{
			paramTypes=new YetiType[pars.length];
			numberPars=pars.length;
		}
		// for all types we box the types.
		for (int i=0; i<numberPars; i++){
			
			if (YetiType.allTypes.containsKey(pars[i].trim())){				
				paramTypes[i]=YetiType.allTypes.get(pars[i].trim());						
			} else {
				usable = false;
			}
		}
		
		// if we don't know a type from the constructor we don't add it
		if (usable){
			System.out.println("+++++++++++++++++++++++");
			YetiLog.printDebugLog("adding constructor to "+type.getName()+" in module "+mod.getModuleName(), this);
			YetiCsharpConstructor construct = new YetiCsharpConstructor(YetiName.getFreshNameFrom(st[0]), paramTypes , type, mod,type.getName());		
			// add it as a creation routine for the type
			type.addCreationRoutine(construct);
			// add the constructor as a routines to test
			mod.addRoutineInModule(construct);
			System.out.println("O constructor is: "+ type);
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
		if("".equals(pars[0].trim())) numberParameters=0;
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
			if (YetiType.allTypes.containsKey(st[0].trim())){
				paramTypes[0]=YetiType.allTypes.get(st[0].trim());						
			} else {
				usable = false;
			}
		}
		
		// for all types we box the types.
		//System.out.println("ParamTypes: "+paramTypes.length);
		for (int i=0; i<numberParameters; i++){
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			if (YetiType.allTypes.containsKey(pars[i].trim())){
				paramTypes[i+offset]=YetiType.allTypes.get(pars[i].trim());						
			} else {
				usable = false;
			}
		}
		
		// if we don't know a type from the constructor we don't add it
		if (usable){
			System.out.println(c);
			YetiLog.printDebugLog("adding method "+st[1]+" in module "+st[0], this);
			// add it as a creation routine for the return type
			YetiType returnType = YetiType.allTypes.get(st[3].trim());
			if (returnType==null)
				returnType = new YetiCsharpSpecificType(st[3].trim());
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
		
		System.out.println("=========================================");
		System.out.println("=========================================");
		System.out.println("THE ALLTYPES HASMAP: ");
		Set entries = YetiType.allTypes.entrySet();
	    Iterator it = entries.iterator();
	    
	    while (it.hasNext()) {
	        Map.Entry entry = (Map.Entry) it.next();
	        System.out.println(entry.getKey() + "-->" + entry.getValue());
	      }
	    System.out.println("=========================================");
		System.out.println("=========================================");
	    System.out.println("THE ALLMODULES HASMAP: ");
		Set entries2 = YetiModule.allModules.entrySet();
	    Iterator it2 = entries2.iterator();
	    
	    while (it2.hasNext()) {
	        Map.Entry entry2 = (Map.Entry) it2.next();
	        System.out.println(entry2.getKey() + "-->" + entry2.getValue());
	      }
	    System.out.println("*****************************************");
	    System.out.println("*****************************************");
	    
	    /*Set e = YetiType.allTypes.get("Interface1").allSubtypes.entrySet();
	    Iterator eit = e.iterator();
	    while (eit.hasNext()) {
	    	Map.Entry entry3 = (Map.Entry) eit.next();
	        System.out.println(entry3.getKey() + "-->" + entry3.getValue());
	    }*/
	    
	    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	    System.out.println("THE CREATIONROUTINES : ");
	    
	    //YetiType rType = YetiType.allTypes.get("Rational");
	    YetiType rType2 = YetiType.allTypes.get("Int32");
	    
	    /*Iterator itr = rType.creationRoutines.iterator();
	    
	    while (itr.hasNext()){
	    	Object o = (Object) itr.next();
	    	o.toString();
	    	
	    	YetiCsharpRoutine r = (YetiCsharpRoutine) o;
	    	
	    		int offset=0;
	    		//if(r.isStatic)
	    		//{
	    		//	offset=0;
	    		//}
	    		System.out.print(r+": ");
	    		if(r.getOpenSlots()!=null)
	    		{
	    			YetiType[] yt= r.getOpenSlots();
	    			for(int i=0+offset; i< yt.length; i++)
	    			{
	    				System.out.print(yt[i]+";");

	    			}
	    		}
	    		System.out.println();
	    	
	    	//System.out.println(itr.next());
	    		  
	    }*/
	    
	    System.out.println("Send Data to C# Reflexive Layer");
	    /*int i =0;	   
	    try {
	    	ArrayList<String> a = YetiServerSocket.getData(2300);

	    	while(true)
	    	{
	    		System.out.println("**************************************");
	    		if(i<9)
	    			YetiServerSocket.sendData(2400, "TESTING MESSAGE FROM JAVA PART --> "+i);
	    		else
	    		{
	    			YetiServerSocket.sendData(2400, "! STOP TESTING !");
	    			break;
	    		}
	    		a = YetiServerSocket.getData(2300);
	    		for(String s : a)
	    		{
	    			String[] helps = s.split(":");
	    			System.out.println(helps[0]);
	    			System.out.println(helps[1]);

	    		}
	    		System.out.println("**************************************");	    		
	    		i++;
	    	}
	    } catch (IOException e1) {
	    	
	    	e1.printStackTrace();
	    }*/
	    
	    
	    /*Iterator itr2 =rType.creationRoutines.iterator();
        System.out.println("**************************************");
        try
        {
            ArrayList<String> a = YetiServerSocket.getData(2300);
            while(itr2.hasNext())
            {
                Object o = (Object) itr2.next();
                int i=0;
                
                YetiCsharpRoutine r = (YetiCsharpRoutine) o;
                
                	if(r.getOpenSlots().length==0)
                	{
                		while(i<5)
                        {
                		YetiCard[] arg = new YetiCard[0];
                		String result = r.makeEffectiveCall(arg);                    
                		System.out.println("The Result is: ");
                		System.out.println(result);
                		i++;
                        }
                	}
                
            }
            
        }
        catch(Throwable e)
        {
            System.out.println(e);
        }
        System.out.println("**************************************");*/
	    
        
        
       
        
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        Iterator itr3 =rType2.creationRoutines.iterator();
        try
        {
        	ArrayList<String> a = YetiServerSocket.getData(2300);
            while(itr3.hasNext())
            {
                Object o = (Object) itr3.next();
                int i=0;
                
                YetiCsharpMethod r = (YetiCsharpMethod) o;
                System.out.println("---> "+r.m);
                	if(r.m.equals("ComputeResult"))
                	{
                		while(i<5)
                        {
                			System.out.println("100");
                		YetiCard[] arg = new YetiCard[1];
            			System.out.println("101");
                		YetiIdentifier h1 = new YetiIdentifier("v110");
            			System.out.println("102");
                		YetiType tp = new YetiType("Int32");
            			System.out.println("103");
                		Object ob = null;
                		arg[0] = new YetiCard(h1,tp,ob);
            			System.out.println("104");
                		    
                		String result = r.makeEffectiveCall(arg);                    
                		System.out.println("The Result is: ");
                		System.out.println(result);
                		i++;
                        }
                	}
                
            }
            
							            
        }
        catch(Throwable e)
        {
            System.out.println(e);
        }
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	    
        try {
			YetiServerSocket.sendData(2400, "! STOP TESTING !");
			 ArrayList<String> a = YetiServerSocket.getData(2300);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    System.out.println(YetiCsharpSpecificType.__yetiValue_createRandomBoolean());
	    System.out.println(YetiCsharpSpecificType.__yetiValue_createRandomChar());
	    System.out.println(YetiCsharpSpecificType.__yetiValue_createRandomDouble());
	    System.out.println(YetiCsharpSpecificType.__yetiValue_createRandomFloat());
	    System.out.println(YetiCsharpSpecificType.__yetiValue_createRandomInt());
	    System.out.println(YetiCsharpSpecificType.__yetiValue_createRandomLong());
	    System.out.println(YetiCsharpSpecificType.__yetiValue_createRandomByte());
	    System.out.println(YetiCsharpSpecificType.__yetiValue_createRandomShort());

	    /*try {
	    	ArrayList<String> a = YetiServerSocket.getData(2400);	    		    		
    		YetiServerSocket.sendData(2400, "Constructor:v112:Rational:null;v110");
    		a = YetiServerSocket.getData(2400);
    		
    		for(String s : a)
    		{    		
    			String[] sh = s.split("!");
    			
    			if("FAIL".equalsIgnoreCase(sh[0]))
    			{
    				System.out.println("Call FAILED");
    				System.out.println(sh[1]);
    			}
    			else System.out.println("Call SUCCEEDED");

    		}
    	} catch (IOException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}*/

	}

}
