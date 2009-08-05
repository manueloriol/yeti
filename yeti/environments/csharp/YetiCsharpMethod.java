package yeti.environments.csharp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;
import yeti.environments.csharp.YetiCsharpRoutine;
import yeti.environments.java.YetiJavaSpecificType;

/**
 * Class that represents a Csharp method.
 * 
 * @author Sotrios Tassis (st552@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiCsharpMethod extends YetiCsharpRoutine {

	/**
	 * a list of methods not to test. Typically will contain wait, notify, notifyAll
	 */
	public static HashMap<String,Object> methodsNotToAdd ;

	/**
	 * Result of the last call.
	 */
	public YetiVariable lastCallResult=null;

	/**
	 * The actual method to call.
	 */
	protected String m;

	/**
	 * Checks whether this method is a static method or not. 
	 */
	public boolean isStatic = false;


	/**
	 * Constructor to define a Csharp method
	 * 
	 * @param name the name of the method.
	 * @param openSlots the open slots of the method.
	 * @param returnType the return type of the method.
	 * @param originatingModule the module in which it was defined.
	 * @param m the method implementation.
	 * @param isStatic the parameter that says whether the method is static or not.
	 */
	public YetiCsharpMethod(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, String m, boolean isStatic) {
		super(name, openSlots, returnType, originatingModule);
		this.m=m;
		this.isStatic=isStatic;
		
	}


	/* (non-Javadoc)
	 * Returns the name of the method for pretty-print.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return m;
	}

	/**
	 * Makes the effective call (lets return the exceptions and Errors).
	 * 
	 * @param arg the arguments of the call.
	 * @return the logs.
	 * @throws YetiCallException the wrapped exception. 
	 */
	public String makeEffectiveCall(YetiCard[] arg) throws YetiCallException {
		String log="";
		String log1="";
		lastCallResult=null;
		String prefix;
		boolean isValue= false;
		String msg="Method:";

		// if the method is static, we need to adjust the arguments
		// there is no target in the open slots.
		if (isStatic){			
			prefix = this.originatingModule.getModuleName();
		}else{
			prefix = arg[0].getIdentity().toString();
		}
		
		//Object []initargs=new Object[length];
		// we start generating the log as well as the identifier to use to store the 
		// result if there is one.
		YetiIdentifier id=null;
		if (!("Void".equals(returnType.getName()))) {
			// if there is a result to be expected
			//YetiLog.printDebugLog("return type is "+returnType.getName(), this);
			id=YetiIdentifier.getFreshIdentifier();
			msg+=id+":"+originatingModule.getModuleName()+":"+returnType.getName()+":";
			msg+=m+":";
			
			boolean isSimpleReturnType=false;
			
			if (this.m.startsWith("__yetiValue_"))			
					isSimpleReturnType=true;						
			
			if (isSimpleReturnType) {
				isValue=true;
				log1 = this.returnType.toString()+" "+ id.getValue() + "=";
			} else
				log = this.returnType.toString()+" "+ id.getValue() + "="+ prefix +"."+ m+"(";			

			
		} else {
			// otherwise
			log = prefix + "."+m +"(";
		}

		// we adjust the number of arguments according 
		// to the fact that they are static or not.
		int offset=1;
		Object target=null;
		if (isStatic){
			offset=0;
		} 
		
		
		for (int i = offset;i<arg.length; i++){
			// if we should replace it by a null value, we do it
			if (YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>Math.random()&&!(((YetiCsharpSpecificType)arg[i].getType()).isSimpleType())) {
				//initargs[i-offset]=null;
				msg+="null";
				if(i<arg.length -1) msg+= ";";
				log=log+"null";
			} else {
				//initargs[i-offset]=arg[i].getValue();
				msg+=arg[i].getIdentity();
                if(i<arg.length -1) msg+= ";";				
				log=log+arg[i].toString();
			}
			if (i<arg.length-1){
				log=log+",";
			}

		}
		
		String valuestring="$$";
        try {
            YetiServerSocket.sendData(2400, msg);
           
            ArrayList<String> a = YetiServerSocket.getData(2300);
           
            for(String s : a)
            {
                String[] helps = s.split(":");
                System.out.println(helps[0]);
                System.out.println(helps[1].trim());
               
                valuestring = helps[1].trim();

            }
        } catch (IOException e) {
            
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if the return type is void, we look it up
		if ("Void".equals(this.returnType.getName()))
			returnType=YetiType.allTypes.get(returnType.getName());
		// if there is a result, we store it and create the variable
		if (id!=null&& "OK".equals(valuestring.trim())){
	        System.out.println("LastCallResult: --> "+id+" "+returnType+" "+valuestring);
			this.lastCallResult=new YetiVariable(id, returnType, valuestring);
		}
		if(isValue) log=log1+" "+valuestring;
		else	log=log+");";
		System.out.println(msg);
		// finally we print the log.
		//YetiLog.printYetiLog(log, this);
		return log;
	}

	/**
	 * Getter for the implementation of the method.
	 * 
	 * @return the implementation of the method.
	 */
	/*public Method getM() {
		return m;
	}

	/**
	 * Setter for the implementation of the method.
	 * 
	 * @param m the method to set.
	 */
	/*public void setM(Method m) {
		this.m = m;
	}*/


}
