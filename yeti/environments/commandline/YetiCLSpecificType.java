package yeti.environments.commandline;

import yeti.YetiCard;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.environments.java.YetiJavaSpecificType;

public class YetiCLSpecificType extends YetiType {

	public class YetiCLRoutine extends YetiRoutine{
		public YetiCLRoutine(String name, YetiType type) {
			this.name = new YetiName(name);
			this.openSlots = new YetiType[0];
			this.returnType = type;
		}
		
		@Override
		public boolean checkArguments(YetiCard[] arg) {
			return true;
		}

		@Override
		public Object makeCall(YetiCard[] arg) {
			return null;
			
		}
		
		@Override
		public String makeEffectiveCall(YetiCard[] arg) throws Throwable {
			// TODO fix
			//if (returnType.getName().equals("int"))
			//	return __yetiValue_createRandomInt();
			return "";
		}
		
	}
	
	
	public YetiCLSpecificType(String name) {
		super(name);
		// FIX
		//this.addCreationRoutine(name, this);
		
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
		return((short) (Math.floor(65535*d)-32768));
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


	public static void initPrimitiveTypes() {

		// we create the primitive types		
		YetiCLSpecificType tBoolean=new YetiCLSpecificType("boolean");
		tBoolean.addInterestingValues(true);
		tBoolean.addInterestingValues(false);
		
		YetiCLSpecificType tByte=new YetiCLSpecificType("byte");
		tByte.addInterestingValues((byte)0);
		tByte.addInterestingValues((byte)1);
		tByte.addInterestingValues((byte)2);
		tByte.addInterestingValues((byte)255);
		tByte.addInterestingValues((byte)254);
		tByte.addInterestingValues((byte)253);

		YetiCLSpecificType tShort=new YetiCLSpecificType("short");
		for (short j = -10; j<11;j++)
			tShort.addInterestingValues((short)j);
		tShort.addInterestingValues(Short.MAX_VALUE);
		tShort.addInterestingValues((short) (Short.MAX_VALUE-1));
		tShort.addInterestingValues((short)(Short.MAX_VALUE-2));
		tShort.addInterestingValues((short)(Short.MIN_VALUE+2));
		tShort.addInterestingValues((short)(Short.MIN_VALUE+1));
		tShort.addInterestingValues(Short.MIN_VALUE);


		YetiCLSpecificType tInt=new YetiCLSpecificType("int");
		for (int j = -10; j<11;j++)
			tInt.addInterestingValues(j);
		tInt.addInterestingValues(Integer.MAX_VALUE);
		tInt.addInterestingValues(Integer.MAX_VALUE-1);
		tInt.addInterestingValues(Integer.MAX_VALUE-2);
		tInt.addInterestingValues(Integer.MIN_VALUE+2);
		tInt.addInterestingValues(Integer.MIN_VALUE+1);
		tInt.addInterestingValues(Integer.MIN_VALUE);

		YetiCLSpecificType tLong=new YetiCLSpecificType("long");
		for (long j = -10; j<11;j++)
			tLong.addInterestingValues(j);
		tLong.addInterestingValues(Long.MAX_VALUE);
		tLong.addInterestingValues(Long.MAX_VALUE-1);
		tLong.addInterestingValues(Long.MAX_VALUE-2);
		tLong.addInterestingValues(Long.MIN_VALUE+2);
		tLong.addInterestingValues(Long.MIN_VALUE+1);
		tLong.addInterestingValues(Long.MIN_VALUE);
		tLong.addInterestingValues(Integer.MAX_VALUE);
		tLong.addInterestingValues(Integer.MAX_VALUE-1);
		tLong.addInterestingValues(Integer.MAX_VALUE-2);
		tLong.addInterestingValues(Integer.MIN_VALUE+2);
		tLong.addInterestingValues(Integer.MIN_VALUE+1);
		tLong.addInterestingValues(Integer.MIN_VALUE);



		YetiCLSpecificType tDouble=new YetiCLSpecificType("double");
		tDouble.addInterestingValues(Double.MAX_VALUE);
		tDouble.addInterestingValues(Double.MIN_VALUE);
		tDouble.addInterestingValues(Double.NaN);
		tDouble.addInterestingValues(Double.POSITIVE_INFINITY);
		tDouble.addInterestingValues(Double.NEGATIVE_INFINITY);
		tDouble.addInterestingValues(Float.MAX_VALUE);
		tDouble.addInterestingValues(Float.MIN_VALUE);
		tDouble.addInterestingValues(Float.NaN);
		tDouble.addInterestingValues(Float.POSITIVE_INFINITY);
		tDouble.addInterestingValues(Float.NEGATIVE_INFINITY);


		YetiCLSpecificType tChar=new YetiCLSpecificType("char");
		tChar.addInterestingValues('\0');
		tChar.addInterestingValues('\1');
		tChar.addInterestingValues('\2');
		tChar.addInterestingValues('\n');
		tChar.addInterestingValues('\255');
		tChar.addInterestingValues('\254');
		tChar.addInterestingValues('\253');



		YetiCLSpecificType tFloat=new YetiCLSpecificType("float");
		tFloat.addInterestingValues(Float.MAX_VALUE);
		tFloat.addInterestingValues(Float.MIN_VALUE);
		tFloat.addInterestingValues(Float.NaN);
		tFloat.addInterestingValues(Float.POSITIVE_INFINITY);
		tFloat.addInterestingValues(Float.NEGATIVE_INFINITY);		
	}
}
