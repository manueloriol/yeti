package yeti.environments.java;

/**
 
 YETI - York Extensible Testing Infrastructure
 
 Copyright (c) 2009-2010, Manuel Oriol <manuel.oriol@gmail.com> - University of York
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
 must display the following acknowledgement:
 This product includes software developed by the University of York.
 4. Neither the name of the University of York nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY <COPYRIGHT HOLDER> ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 **/ 

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import yeti.YetiCallException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.YetiVariable;

/**
 * Class that represents a Java method.
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Jun 22, 2009
 */
public class YetiJavaMethod extends YetiJavaRoutine {

	/**
	 * a list of methods not to test. Typically will contain wait, notify, notifyAll
	 */
	public static HashMap<String,Object> methodsNotToAdd ;


	/**
	 * The actual method to call.
	 */
	protected Method method;

	/**
	 * Checks whether this method is a static method or not. 
	 */
	public boolean isStatic = false;

	/**
	 * Constructor to define a Java method. By default the method is non-static.
	 * 
	 * @param name the name of the method.
	 * @param openSlots the open slots of the method.
	 * @param returnType the return type of the method.
	 * @param originatingModule the module in which it was defined.
	 * @param m the method implementation.
	 */
	@SuppressWarnings("unchecked")
	public YetiJavaMethod(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, Method m) {
		super(name, openSlots, returnType, originatingModule);
		isStatic = Modifier.isStatic((m.getModifiers()));
		this.method=m;
		for (Class cl: m.getExceptionTypes()) {
			this.addAcceptableExceptionType(cl.getName());			
		}

	}

	/**
	 * Constructor to define a Java method
	 * 
	 * @param name the name of the method.
	 * @param openSlots the open slots of the method.
	 * @param returnType the return type of the method.
	 * @param originatingModule the module in which it was defined.
	 * @param m the method implementation.
	 * @param isStatic the parameter that says whether the method is static or not.
	 */
	@SuppressWarnings("unchecked")
	public YetiJavaMethod(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, Method m, boolean isStatic) {
		super(name, openSlots, returnType, originatingModule);
		this.method=m;
		this.isStatic=isStatic;
		for (Class cl: m.getExceptionTypes()) {
			this.addAcceptableExceptionType(cl.getName());			
		}

	}


	/**
	 * A method to initialize the set of methods not to add.
	 */
	public static void initMethodsNotToAdd(){
		methodsNotToAdd = new HashMap<String,Object>();
		methodsNotToAdd.put("wait", null);
		methodsNotToAdd.put("notify", null);
		methodsNotToAdd.put("notifyAll", null);
	}

	/**
	 * Checks whether the method corresponds to a method not to test.
	 * 
	 * @param methodName the name of the method.
	 * @return <code>true</code> if the method should not be tested. <code>false</code> Otherwise.
	 */
	public static boolean isMethodNotToAdd(String methodName){
		if (methodsNotToAdd==null)
			initMethodsNotToAdd();
		return methodsNotToAdd.containsKey(methodName);
	}

	/* (non-Javadoc)
	 * Returns the name of the method for pretty-print.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return method.getName();
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
		int length = 0;
		String prefix;
		boolean isValue= false;

		// use this to monitor a method if needed...
//		boolean isIndexOf = false;
//		int k = 0;
//		if (this.toString().equals("indexOf")) {
//			isIndexOf=true;
//		}
//		YetiLog.printDebugLog("indexOf "+k++, this, isIndexOf);
		
		// if the method is static, we need to adjust the arguments
		// there is no target in the open slots.
		if (isStatic){
			length = arg.length;
			prefix = this.originatingModule.getModuleName();
		}else{
			length = arg.length-1;
			prefix = arg[0].getIdentity().toString();
		}


		Object []initargs=new Object[length];
		// we start generating the log as well as the identifier to use to store the 
		// result if there is one.
		YetiIdentifier id=null;


		if (method.getReturnType()!= void.class) {
			// if there is a result to be expected
			YetiLog.printDebugLog("return type is "+method.getReturnType().getName(), this);
			id=YetiIdentifier.getFreshIdentifier();
			String s0=method.getName();
			// this is a hack still decide whether it is a simple type 
			// (in which case, we will print the result rather than the sequence to construct it) 
			boolean isSimpleReturnType=false;
			if (this.returnType instanceof YetiJavaSpecificType) {
				YetiJavaSpecificType rt = (YetiJavaSpecificType) this.returnType;
				if (rt.isSimpleType()) {
					isSimpleReturnType=true;
				}

			}
			if (s0.startsWith("__yetiValue_")||isSimpleReturnType) {//||(this.returnType instanceof YetiJavaSpecificType)){ //(this.returnType instanceof YetiJavaSpecificType)
				isValue=true;
				log1 = this.returnType.toString()+" "+ id.getValue() + "=";
				log = this.returnType.toString()+" "+ id.getValue() + "="+ prefix +"."+ method.getName()+"(";			
			} else
				log = this.returnType.toString()+" "+ id.getValue() + "="+ prefix +"."+ method.getName()+"(";			

		} else {
			// otherwise
			log = prefix + "."+method.getName()+"(";
		}

		// we adjust the number of arguments according 
		// to the fact that they are static or not.
		int offset=1;
		Object target=null;
		if (isStatic){
			offset=0;
		} else {
			target= arg[0].getValue();
		}

		for (int i = offset;i<arg.length; i++){
			// if we should replace it by a null value, we do it
			if ((YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>Math.random())&&!(((YetiJavaSpecificType)arg[i].getType()).isSimpleType())) {
				initargs[i-offset]=null;
				log=log+"null";
			} else {
				initargs[i-offset]=arg[i].getValue();
				log=log+arg[i].toString();
			}
			if (i<arg.length-1){
				log=log+",";
			}

		}


		// we  make the call
		if (target!=null)
			YetiLog.printDebugLog("trying to call "+method.getName()+" on a "+target.getClass().getName()+", target ="+target, this);
		else 
			YetiLog.printDebugLog("trying to call statically "+method.getName()+" of "+method.getDeclaringClass().getName(), this);				

		Object o=null;
		try {
			o = method.invoke(target,initargs);
		} catch (Throwable t) {	
			throw new YetiCallException(log,t);
		}

		// if the return type is void, we look it up
		if (returnType==null)
			returnType=YetiType.allTypes.get(method.getReturnType().getName());
		// if there is a result, we store it and create the variable
		if (id!=null&&o!=null){
			this.lastCallResult=new YetiVariable(id, returnType, o);
		}

		// if this is a value, we print it directly
		if (isValue) {
			log = generateLogForValues(log1, o);
		}
		else
			log=log+");";
		
		// finally we print the log.
		YetiLog.printYetiLog(log, this);
		return log;
	}

	/**
	 * Takes a log and adds the litteral value in the case this is a value type.
	 * 
	 * @param log1
	 * @param o
	 * @return
	 */
	public static String generateLogForValues(String log1, Object o) {
		String log;
		// we escape the values
		if (o instanceof Character){
			String value;
			// in case we have space characters
			switch (((Character)o).charValue()){

			case '\r': {
				value = "\\r"; 
				break;
			}

			case ' ': {
				value = " "; 
				break;
			}

			case '\f': {
				value = "\\f"; 
				break;
			}
			case '\t': {
				value = "\\t"; 
				break;
			}
			case '\n': {
				value = "\\n";
				break;
			}
			default :{
				// if this is not a standard character from the old time ISO set
				int i = ((Character)o).charValue();
				if (!(i<128 && Character.isLetter(i))) {
					value = "\\u";
					String value0="";
					// we have to reconstruct the correct value
					char hexDigit[] = {
							'0', '1', '2', '3', '4', '5', '6', '7',
							'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
					};
					// we iterate 4 times (only)
					for (int j = 0;j<4;j++){
						value0 = hexDigit[i & 0x0f]+value0;
						i = i>>4;
					}
					value = value+value0;
				} else
					// otherwise, we simply show it as is
					value = ""+((Character)o).charValue();
			}

			}
			log1=log1+"'"+value+"'"+";";
		} else {
			// just in case we have a NaN value we are able to make it again...
			// we also add the correct modifier to indicate Longs, floats, and double
			if (o instanceof Float) {
				if (((Float)o).isNaN()) {
					log1 = log1+"0.0/0.0f;";
				} else
					log1 = log1+o.toString()+"f;";
			} else
				if (o instanceof Double) {
					if (((Double)o).isNaN()) {
						log1 = log1+"0.0/0.0d;";
					} else
						log1 = log1+o.toString()+"d;";
				} else
					if (o instanceof Long) {
						log1 = log1+o.toString()+"L;";
					} else
						log1=log1+o.toString()+";";
		}
		log = log1;
		return log;
	}
	
	
	

	/**
	 * Getter for the implementation of the method.
	 * @return  the implementation of the method.
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Setter for the implementation of the method.
	 * @param m  the method to set.
	 */
	public void setMethod(Method m) {
		this.method = m;
	}

	/**
	 * Resets the Java methods.
	 */
	public static void reset() {
		initMethodsNotToAdd();
	}

}
