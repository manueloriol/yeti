package yeti.environments.csharp;

import java.io.IOException;

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
	public static int count=0;

	/**
	 * The actual method to call.
	 */
	protected String m;
	protected String originatingClass;

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
	public YetiCsharpMethod(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, String m, boolean isStatic, String cls) {
		super(name, openSlots, returnType, originatingModule);
		this.m=m;
		this.originatingClass = cls;
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
		boolean isValue= false, successCall=true;
		String msg="Method:";

		// if the method is static, we need to adjust the arguments
		// there is no target in the open slots.
		if (isStatic){		
			//System.out.println("It is Static");
			prefix = this.originatingClass;			
		}else{
			
			prefix = arg[0].getIdentity().toString();
		}
		
		//Object []initargs=new Object[length];
		// we start generating the log as well as the identifier to use to store the 
		// result if there is one.
		YetiIdentifier id=null;
		if (!("Void".equals(returnType.getName()))) {
			// if there is a result to be expected
			YetiLog.printDebugLog("return type is "+returnType.getName(), this);
			id=YetiIdentifier.getFreshIdentifier();
			msg+=id+":"+originatingClass+":"+returnType.getName()+":";
			msg+=m+":";
			
			boolean isSimpleReturnType=false;
			
			if (this.m.startsWith("__yetiValue_"))			
					isSimpleReturnType=true;						
			
			if (isSimpleReturnType) {
				isValue=true;
				log1 = this.returnType.toString()+" "+ id.getValue();
			} else
				log = this.returnType.toString()+" "+ id.getValue() + "="+ prefix +"."+ m+"(";			

			
		} else {
			// otherwise
			//System.out.println("IT RETURNS VOID");
			msg+="void"+":"+originatingClass+":"+returnType.getName()+":";
			msg+=m+":";
			log = prefix + "."+m +"(";
		}

		// we adjust the number of arguments according 
		// to the fact that they are static or not.
		int offset=1;
		if (isStatic){
			offset=0;
		} 
		//System.out.println("OFFSET IS: "+offset);
		
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
			//System.out.println("THE INDEX IS: "+i);

		}
		
		log=log+");";
		msg+=":"+log;
		String valuestring="";
		boolean communicationflag=true;
        try {
        	//System.out.println(msg);
            YetiServerSocket.sendData(2400, msg);
           
            ArrayList<String> a = YetiServerSocket.getData(2300);
            int i=0;
            for(String s : a)
            {
            	//System.out.println("The S in Meths ----: "+s);
            		i=s.indexOf("FAIL!");
            		if(i==-1)
            		{
            			String[] helps = s.split(":");
            			if(helps.length>=2)
            			{
            				//System.out.println("Method help 1: "+helps[0]);
            				//System.out.println("Method help 2: "+helps[1].trim());

            				valuestring = helps[1].trim();
            			}
            			else System.out.println("Method help 1: "+helps[0]);
            		}
            		else
            		{            		            		
            			//System.out.println(s);
                		successCall=false;
                		msg=s;
            		}            	

            }
        } catch (IOException e) {
            communicationflag=false;
            //e.printStackTrace();
        } catch (Exception e) {
            communicationflag=false;
			//e.printStackTrace();
		} 
        
        if(communicationflag)
        {
        	// if the return type is void, we look it up
        	if ("Void".equals(this.returnType.getName()))
        		returnType=YetiType.allTypes.get(returnType.getName());
        	
        	
        	// if there is a result, we store it and create the variable
        	//if(successCall) System.out.println("LastCallResult: --> "+id+" "+returnType+" "+valuestring);
        	if (id!=null && successCall){        		        		
        		this.lastCallResult=new YetiVariable(id, returnType, valuestring);
        	}
        	
        	if(isValue) log=log1;        	
        	//System.out.println(msg);
        	count++;
        	if(!successCall) throw new YetiCallException(log+"<>"+msg,new Throwable());
        	//System.out.println("The COUNT is : ---> "+count);        	
        	if(successCall)
        	{
        		System.out.println("The LOG: "+log);
        		//System.out.println("The Msg is "+msg);
        		YetiLog.printYetiLog(log, this);
        	}
        }
		// finally we print the log.
		
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
