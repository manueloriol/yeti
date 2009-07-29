package yeti.environments.java;

import yeti.YetiType;
import yeti.environments.YetiPrefetchingLoader;

/**
 * Class that represents primitive types.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaSpecificType extends YetiType {

	/**
	 * A version of the type name actually nice to read 
	 */
	String prettyPrintTypeName = null;

	/**
	 * Is it an array type? True if yes.
	 */
	@SuppressWarnings("unused")
	private boolean isArrayType = false;

	/**
	 * The number of levels of array if it is an array type.
	 */
	private int numLevelsArray = 0;


	/**
	 * The base type if it is an array type.
	 */
	private String baseTypeForArrayName="";

	/**
	 * Is it a simple type?
	 */
	private boolean isSimpleType=false;



	/**
	 * Is this type a simple type?
	 * 
	 * @return the fact it is a simple type.
	 */
	public boolean isSimpleType() {
		return isSimpleType;
	}


	/**
	 * Sets the reason it is a simple type.
	 * 
	 * @param isSimpleType true if it is a simple type.
	 */
	public void setSimpleType(boolean isSimpleType) {
		this.isSimpleType = isSimpleType;
	}


	/**
	 * Initialize the primitive types (as they are not classes, they need to be initialized in a specific way).
	 */
	public static void initPrimitiveTypes(){

		// we create the primitive types
		YetiJavaSpecificType tBoolean=new YetiJavaSpecificType("boolean");
		tBoolean.setSimpleType(true);
		YetiJavaSpecificType tByte=new YetiJavaSpecificType("byte");
		tByte.setSimpleType(true);
		YetiJavaSpecificType tShort=new YetiJavaSpecificType("short");
		tShort.setSimpleType(true);
		YetiJavaSpecificType tInt=new YetiJavaSpecificType("int");
		tInt.setSimpleType(true);
		YetiJavaSpecificType tLong=new YetiJavaSpecificType("long");
		tLong.setSimpleType(true);
		YetiJavaSpecificType tDouble=new YetiJavaSpecificType("double");
		tDouble.setSimpleType(true);
		YetiJavaSpecificType tChar=new YetiJavaSpecificType("char");
		tChar.setSimpleType(true);
		YetiJavaSpecificType tFloat=new YetiJavaSpecificType("float");
		tFloat.setSimpleType(true);

		// we add the helper class that has creating procedures.
		YetiPrefetchingLoader.yetiLoader.addDefinition(YetiJavaSpecificType.class);

	}


	/**
	 * A boolean random generator.
	 * 
	 * @return a random boolean.
	 */
	public static boolean __yetiValue_createRandomBoolean(){
		return (Math.random()<.5);
	}

	/**
	 * A byte random generator.
	 * 
	 * @return a random byte.
	 */
	public static byte __yetiValue_createRandomByte(){
		double d=Math.random();
		return ((byte) Math.floor(257*d));
	}

	/**
	 * A short random generator.
	 * 
	 * @return a random short.
	 */
	public static short __yetiValue_createRandomShort(){
		double d=Math.random();
		return((short) (Math.floor(65535*d)-32767));
	}

	/**
	 * A int random generator.
	 * 
	 * @return a random int.
	 */
	public static int __yetiValue_createRandomInt(){
		double d=Math.random();
		double d2=Math.random()*2-1.0d;
		return ((int) Math.floor(2147483647*d*d2));
	}

	/**
	 * A long random generator.
	 * 
	 * @return a random long.
	 */
	public static long __yetiValue_createRandomLong(){
		Double d=new Double(YetiJavaSpecificType.__yetiValue_createRandomDouble());
		return d.longValue();
	}

	/**
	 * A char random generator.
	 * 
	 * @return a random char.
	 */
	public static char __yetiValue_createRandomChar(){
		double d=Math.random();
		return (char)(Math.floor(0xFFFF*d));
	}

	/**
	 * A float random generator.
	 * 
	 * @return a random float.
	 */
	public static float __yetiValue_createRandomFloat(){
		int i = (int) Math.floor(11*Math.random());
		return ((float)Math.random()*(10^i));
	}

	/**
	 * A double random generator.
	 * 
	 * @return a random double.
	 */
	public static double __yetiValue_createRandomDouble(){
		int i = (int) Math.floor(15*Math.random());
		return (Math.random()*(10^i));
	}

	/**
	 * Constructor that creates an empty type.
	 * 
	 */
	public YetiJavaSpecificType(String name) {
		super(name);
		String uglyName=name, prettyName="";
		while (uglyName.startsWith("[")){
			this.isArrayType=true;
			this.numLevelsArray++;
			prettyName=prettyName+"[]";
			uglyName=uglyName.substring(1);
		}
		if (uglyName.contains(";")){
			this.baseTypeForArrayName=uglyName.substring(1, uglyName.indexOf(';'));
			prettyName=this.baseTypeForArrayName+prettyName;
		} else{
			if (uglyName.equals("I")) prettyName="int"+prettyName;
			else if (uglyName.equals("Z")) prettyName="boolean"+prettyName;
			else if (uglyName.equals("C")) prettyName="char"+prettyName;
			else if (uglyName.equals("B")) prettyName="byte"+prettyName;
			else if (uglyName.equals("D")) prettyName="double"+prettyName;
			else if (uglyName.equals("F")) prettyName="float"+prettyName;
			else if (uglyName.equals("J")) prettyName="long"+prettyName;
			else if (uglyName.equals("S")) prettyName="short"+prettyName;
			else
				prettyName=uglyName+prettyName;
		}
		this.prettyPrintTypeName=prettyName;
	}


	/* (non-Javadoc)
	 * 
	 * Returns the prettyPrint version of the name
	 * 
	 * @see yeti.YetiType#toString()
	 */
	@Override
	public String toString() {
		return this.prettyPrintTypeName;
	}

}
