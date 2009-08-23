package yeti.environments.csharp;



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

	/**
	 * The actual method to call.
	 */
	protected String m;
	protected String originatingClass;

	/**
	 * Checks whether this method is a static method or not. 
	 */
	public boolean isStatic = false;
	//shows when a call was successful on the CsharpReflexiveLayer
	public boolean successCall=true;


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
		this.lastCallResult=null;
		String prefix,target="";
		boolean isValue= false;
		String msg="Method:";

		// if the method is static, we need to adjust the arguments
		// there is no target in the open slots.
		if (isStatic){					
			prefix = this.originatingClass;			
		}else{
			
			prefix = arg[0].getIdentity().toString();
		}
		
		// we start generating the log as well as the identifier to use to store the 
		// result if there is one.
		YetiIdentifier id=null;
		if (!("Void".equals(returnType.getName()))) {
			// if there is a result to be expected
			YetiLog.printDebugLog("return type is "+returnType.getName(), this);
			id=YetiIdentifier.getFreshIdentifier();
			//constructing the message to send to the CsharpreflexiveLayer
			msg+=id+":"+originatingClass+":"+returnType.getName()+":";
			msg+=m+":";
			
			boolean isSimpleReturnType=false;
			
			if (this.m.startsWith("__yetiValue_"))			
					isSimpleReturnType=true;						
			
			if (isSimpleReturnType) {
				isValue=true;
				log1 = this.returnType.toString()+" "+ id.getValue()+";";
			} else
				log = this.returnType.toString()+" "+ id.getValue() + "="+ prefix +"."+ m+"(";			

			
		} else {
			// otherwise		
			msg+="void"+":"+originatingClass+":"+returnType.getName()+":";
			msg+=m+":";
			log = prefix + "."+m +"(";
		}

		// we adjust the number of arguments according 
		// to the fact that they are static or not.
		int offset=1;
		if (isStatic){
			offset=0;
			target="null";
		} 
		else target = arg[0].getIdentity().toString();		
		
		for (int i = offset;i<arg.length; i++){
			// if we should replace it by a null value, we do it
			if (YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>Math.random()&&!(((YetiCsharpSpecificType)arg[i].getType()).isSimpleType())) {		
				msg+="null";
				if(i<arg.length -1) msg+= ";";
				log=log+"null";
			} else {
				msg+=arg[i].getIdentity();
                if(i<arg.length -1) msg+= ";";				
				log=log+arg[i].toString();
			}
			if (i<arg.length-1){
				log=log+",";
			}

		}
		
		log=log+");";
		msg+=":"+log+":"+target;
		
		//we throw the exception of an not successful call
		if ("Void".equals(this.returnType.getName()))
    		returnType=YetiType.allTypes.get(returnType.getName());
    	
    	if(isValue) log=log1;
    	
		String valuestring="";	
		
			//sending the call to the other part
            YetiServerSocket.sendData(msg);
           //Receiving results
            ArrayList<String> a = YetiServerSocket.getData();
            String s=a.get(0);
            if (s.indexOf("FAIL!")>=0){            	
            	successCall = false;
            	
            	msg="";
            	for (String s0: a)
            		msg=msg+s0+"\n";
            	//we throw the exception of an not successful call
            	throw new YetiCallException(log+"><"+msg,new Throwable());
            	
            } else{            	
            	
            	String[] helps = s.split(":");
     			if(helps.length>=2)
    			{
    				valuestring = helps[1].trim();
    			}
            }
            YetiLog.printDebugLog(msg, this);                            	        	        	        	              
        	
        	if (id!=null && successCall){
        		//if the call is successful we store to the pool the id
            	//the value for now is not the valid one because of syncronization
            	//problem with the working threads and the non-asynchronous socket
            	//communication 
        		
        		this.lastCallResult=new YetiVariable(id, returnType, valuestring);
        	}        	                	    
        	
        	// finally we print the log.
        		System.out.println("The LOG: "+log);        		
        		YetiLog.printYetiLog(log, this); 

		return log;
	}	


}
