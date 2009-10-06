package yeti.environments.csharp;


import java.util.ArrayList;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;
import yeti.Yeti;



/**
 * Class that represents a constructor of a .NET compatible language.
 *
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 21, 2009
 *
 */
public class YetiCsharpConstructor extends YetiCsharpRoutine {

    /**
     * The name of the constructor. 
     */
   
    private String c;
  //shows when a call was successful on the CsharpReflexiveLayer
    public boolean successCall=true;



    /**
     * Constructor to the constructor.
     *
     * @param name the name of this constructor.
     * @param openSlots the open slots for this constructor.
     * @param returnType the returnType of this constructor.
     * @param originatingModule the module in which it was defined.
     * @param c the constructor itself.
     */
   
    public YetiCsharpConstructor(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, String c) {
        super(name, openSlots, returnType, originatingModule);
        this.c=c; //make it a string
    }
   

    /* (non-Javadoc)
     * Returns the actual name of the constructor.
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return c;
    }


    /**
     * Makes the effective call (lets return the exceptions and Errors).
     *
     * @param arg the arguments of the call.
     * @return the logs.
     * @throws YetiCallException the wrapped exception.
     */

    public String makeEffectiveCall(YetiCard[] arg)
    throws YetiCallException {
        String log;
        String msg="";
        this.lastCallResult=null;
        Yeti.testCaseCount++;
        //Object []initargs=new Object[arg.length];
       
        msg+="Constructor:";
        // we start by unboxing the arguments boxed into the cards
        YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
        msg+=id+":"+c+":";
        
        log = returnType.toString() + " " + id.getValue() + "=new "+returnType.getName()+"(";
        for (int i=0;i<arg.length; i++){
            // if we should replace it by a null value, we do it
        	
            if (YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>Math.random()&& !(((YetiCsharpSpecificType)arg[i].getType()).isSimpleType())) {                
                msg+="null";
                if(i<arg.length -1) msg+= ";";
                log=log+"null";
                
            } else {
                // note that we use getValue to get the actual value
                //initargs[i]=arg[i].getValue();
                msg+=arg[i].getIdentity();
                if(i<arg.length -1) msg+= ";";
                // we use toString() to make it pretty-print.
                log=log+arg[i].toString();
            }
            if (i<arg.length-1){
                log=log+",";
            }
        }
        
        log=log+");";
        msg+=":"+log;
        String valuestring="";       
        	YetiLog.printDebugLog(msg,this);	
            YetiServerSocket.sendData(msg);
           
            ArrayList<String> a = YetiServerSocket.getData();
            String s=a.get(0);
            if (s.indexOf("FAIL!")>=0){
            	successCall = false;              	
            	msg="";
            	for (String s0: a)
            		msg=msg+s0+"\n";
            	YetiLog.printDebugLog("The LOG: "+log, this);
            	//we throw the exception of an not successful call   
            	//YetiLog.printDebugLog(log+"><"+msg, this, true);
            	throw new YetiCallException(log+"><"+msg,new Throwable());
            	
            } else{
            	YetiLog.printDebugLog(log+"><"+msg+"^^^^"+s, this);
            	String[] helps = s.split(":");
     			if(helps.length>=2)
    			{
    				valuestring = helps[1].trim();
    			}
            }


        	
        	//if the call is successful we store to the pool the id
        	//the value for now is not the valid one because of syncronization
        	//problem with the working threads and the non-asynchronous socket
        	//communication
        	if(successCall)
        	{          		        		
        		this.lastCallResult=new YetiVariable(id, returnType, valuestring);
        	}
        	
        	// print the log
    		YetiLog.printDebugLog("The LOG: "+log, this);        		
        	YetiLog.printYetiLog(log, this);        		                            
        
        return log;
    }     

}
