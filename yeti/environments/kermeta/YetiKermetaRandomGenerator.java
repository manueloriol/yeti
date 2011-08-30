package yeti.environments.kermeta;

import java.util.HashMap;
import java.util.Set;

import kyeti.util.KermetaSimplifiedInterpreter;
import fr.irisa.triskell.kermeta.runtime.RuntimeObject;

/**
 * 
 * Class that contains random generators for Kermeta Types.
 * Is added as a module in the kermeta testing procedures in the YetiKermetaLoader.
 * TODO We only have a random integer generator for now.... maybe other numeric types should have some as well.
 * 
 * @author Erwan Bousse
 * @date 1 juil. 2011
 *
 */
public class YetiKermetaRandomGenerator {

	public static int counter = 0;
	
	/**
	 * To find the kermeta return type of this class methods
	 */
	private static HashMap<String, String> _returnTypes;
	
	private static void initialize() {
		_returnTypes = new HashMap<String, String>();
		_returnTypes.put("randomInteger", "kermeta::standard::Integer");
		//_returnTypes.put("randomBoolean", "kermeta::standard::Boolean");
	}
	
	public static String getReturnTypeName(String methodName) {
		if (_returnTypes==null) {
			initialize();
		} 
		return _returnTypes.get(methodName);
	}
	
	public static  Set<String> getMethodsNames() {
		if (_returnTypes==null) {
			initialize();
		} 
		return _returnTypes.keySet();
	}
	
	
	
	public static RuntimeObject randomInteger() {
		counter++;
		KermetaSimplifiedInterpreter interpreter = YetiKermetaLoader.yetiLoader.getInterpreter();
		double d=Math.random();
		double d2=Math.random()*2-1.0d;
		int value = ((int) Math.floor(2147483647*d*d2));
		return interpreter.constructKermetaInteger(value);
	}
	
	
}
