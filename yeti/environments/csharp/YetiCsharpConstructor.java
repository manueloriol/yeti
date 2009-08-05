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
import yeti.environments.java.YetiJavaSpecificType;


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
        Object []initargs=new Object[arg.length];
       
        msg+="Constructor:";
        // we start by unboxing the arguments boxed into the cards
        YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
        msg+=id+":"+c+":";
        System.out.println(msg);
        log = returnType.toString() + " " + id.getValue() + "=new "+returnType.getName()+"(";
        for (int i=0;i<arg.length; i++){
            // if we should replace it by a null value, we do it
            if (YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>Math.random()&&!(((YetiJavaSpecificType)arg[i].getType()).isSimpleType())) {
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        // if it succeeds we create the new variable
        System.out.println("LastCallResult: --> "+id+" "+returnType+" "+valuestring);
        this.lastCallResult=new YetiVariable(id, returnType, valuestring);
        log=log+");";
        // print the log
        //YetiLog.printYetiLog(log, this);
        return log;
    }

}
