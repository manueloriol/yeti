package yeti.environments.csharp;

import yeti.YetiType;
import yeti.environments.csharp.YetiCsharpSpecificType;
//import yeti.environments.java.YetiJavaPrefetchingLoader;

/**
 * Class that represents primitive types.
 * 
 * @author Sotirios Tassis (st552@cs.york.ac.uk)
 * @date Jul 21, 2009
 *
 */
public class YetiCsharpSpecificType extends YetiType {
	
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
		
		YetiCsharpSpecificType tBoolean=new YetiCsharpSpecificType("Boolean");
		tBoolean.setSimpleType(true);
		YetiType.allTypes.put(tBoolean.getName(), tBoolean);

		YetiCsharpSpecificType tByte=new YetiCsharpSpecificType("Byte");
		tByte.setSimpleType(true);
		YetiType.allTypes.put(tByte.getName(), tByte);
		
		YetiCsharpSpecificType tShort=new YetiCsharpSpecificType("Int16");
		tShort.setSimpleType(true);
		YetiType.allTypes.put(tShort.getName(), tShort);
		
		YetiCsharpSpecificType tInt=new YetiCsharpSpecificType("Int32");
		tInt.setSimpleType(true);
		YetiType.allTypes.put(tInt.getName(), tInt);
		
		YetiCsharpSpecificType tLong=new YetiCsharpSpecificType("Int64");
		tLong.setSimpleType(true);
		YetiType.allTypes.put(tLong.getName(), tLong);
		
		YetiCsharpSpecificType tDouble=new YetiCsharpSpecificType("Double");
		tBoolean.setSimpleType(true);
		YetiType.allTypes.put(tDouble.getName(), tDouble);
		
		YetiCsharpSpecificType tChar=new YetiCsharpSpecificType("Char");
		tChar.setSimpleType(true);
		YetiType.allTypes.put(tChar.getName(), tChar);
		
		YetiCsharpSpecificType tFloat=new YetiCsharpSpecificType("Single");
		tFloat.setSimpleType(true);
		YetiType.allTypes.put(tFloat.getName(), tFloat);
		
		YetiCsharpSpecificType tString=new YetiCsharpSpecificType("String");
		tString.setSimpleType(true);
		YetiType.allTypes.put(tString.getName(), tString);
				
		}
		


	
	/**
	 * Constructor that creates an empty type.
	 * 
	 */	
	public YetiCsharpSpecificType(String name) {
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
