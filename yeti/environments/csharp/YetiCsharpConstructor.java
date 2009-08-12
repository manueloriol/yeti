package yeti.environments.csharp;

import java.io.IOException;
import java.util.ArrayList;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;



/**
 * Class that represents a constructor in Csharp.
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
        lastCallResult=null;
        boolean successCall=true;
        //Object []initargs=new Object[arg.length];
       
        msg+="Constructor:";
        // we start by unboxing the arguments boxed into the cards
        YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
        msg+=id+":"+c+":";
        
        log = returnType.toString() + " " + id.getValue() + "=new "+returnType.getName()+"(";
        for (int i=0;i<arg.length; i++){
            // if we should replace it by a null value, we do it
        	
            if (YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>Math.random()&& !(((YetiCsharpSpecificType)arg[i].getType()).isSimpleType())) {
                //initargs[i]=null;
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
        String valuestring="";
        boolean communicationflag=true;
        try {
        	System.out.println(msg);
            YetiServerSocket.sendData(2400, msg);
           
            ArrayList<String> a = YetiServerSocket.getData(2300);
            int i=0;
            for(String s : a)
            {
            	i=s.indexOf("FAIL!");
            	System.out.println("The S in Cons ----: "+s);
            	if(i==-1)
        		{
        			String[] helps = s.split(":");
        			if(helps.length>=2)
        			{
        				System.out.println("Constructor help 1: "+helps[0]);
        				System.out.println("Constructor help 2: "+helps[1].trim());

        				valuestring = helps[1].trim();
        			}
        			else System.out.println("Constructor help 1: "+helps[0]);
        		}
            	else
            	{
            		System.out.println(s);
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
        	// if it succeeds we create the new variable
        	//System.out.println("LastCallResult: --> "+id+" "+returnType+" "+valuestring);
        	if(successCall)
        	this.lastCallResult=new YetiVariable(id, returnType, valuestring);
        	log=log+");";
        	if(!successCall) throw new YetiCallException(log+"<>"+msg,new Throwable());
        	
        	if(successCall)
        	{
        		System.out.println(log);
        		//System.out.println("The Msg is "+msg);
        		YetiLog.printYetiLog(log, this);
        		
        	}
        }
              
        // print the log
        
        return log;
    }     

}
