package yeti.environments.csharp;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.YetiVariable;
//import yeti.environments.YetiSecurityException;


/**
 * Class that represents... 
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Aug 09, 2009
 *
 */
public class YetiCsharpRoutine extends YetiRoutine {

	
	/**
	 * Result of the last call.
	 */
	protected YetiVariable lastCallResult=null;


	/**
	 * 
	 * Creates a Csharp routine.
	 * 
	 * @param name the name of the routine.
	 * @param openSlots the open slots for the routine.
	 * @param returnType the type of the returned value.
	 * @param originatingModule the module in which it was defined
	 */
	public YetiCsharpRoutine(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule) {
		super();
		this.name = name;
		this.openSlots = openSlots;
		this.returnType = returnType;
		this.originatingModule = originatingModule;
	}

	/* (non-Javadoc)
	 * 
	 * Checks the arguments. In the case of Java, arguments match (TODO: change that for generics).
	 * 
	 * @see yeti.YetiRoutine#checkArguments(yeti.YetiCard[])
	 */
	@Override
	public boolean checkArguments(YetiCard[] arg) {
			
		return true;
	}

	/* (non-Javadoc)
	 * Method used to perform the actual call
	 * 
	 * @see yeti.environments.java.YetiCsharpRoutine#makeCall(yeti.YetiCard[])
	 */
	public Object makeCall(YetiCard []arg){
		String log = null;
		super.makeCall(arg);
		

			try {
				makeEffectiveCall(arg);
			} catch(YetiCallException e) {
				String temp = e.getLog();
				String[] results = temp.split("<>");
				log = results[0];
				//System.out.println("The LOG ---> "+temp);
				String reasonException="";
				int tmp=0;
				if(results[1]!=null)
				{
					//System.out.println("The result[1]= "+results[1]);
					reasonException = results[1];
					tmp = results[1].indexOf("PRECONDITION");
				}
				
				YetiLog.printYetiLog(log, this);				
				if(tmp==-1)
				{									
					String[] exception = reasonException.split("@");
					//exception[1] has the Buggy Log
					//exception[0] has the Exception Message and StackTrace
					String exceMessage = exception[2].trim()+"\n"+exception[1].trim();
					System.out.println("BUG FOUND: ERROR");
					System.out.println(exceMessage);
					YetiLog.printYetiLog("BUG FOUND: ERROR"+"\n"+exceMessage, this);
					YetiLog.printYetiLog(exceMessage, this);
				}
				else
				{
					String[] exception = reasonException.split("@");
					//exception[1] has the Buggy Log
					//exception[0] has the Exception Message and StackTrace
					String exceMessage = exception[1].trim();
					System.out.println("/**NORMAL EXCEPTION:**/");
					System.out.println(exceMessage);
					YetiLog.printYetiLog("/**NORMAL EXCEPTION:**/"+"\n"+exceMessage, this);
				}
			} catch (Throwable e) {
				
			}

		
		return this.lastCallResult;
	}
	
	
	/* (non-Javadoc)
	 * A stub for sublasses.
	 * 
	 * @see yeti.YetiRoutine#makeEffectiveCall(yeti.YetiCard[])
	 */
	@Override
	public String makeEffectiveCall(YetiCard[] arg) throws Throwable {
		// by default this one does not do anything
		return null;
	}

}
