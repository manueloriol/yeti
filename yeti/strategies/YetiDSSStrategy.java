package yeti.strategies;

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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import yeti.ImpossibleToMakeConstructorException;
import yeti.Yeti;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiLog;
import yeti.YetiName;
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.YetiVariable;
import yeti.environments.YetiTestManager;
import yeti.monitoring.YetiGUI;
import yeti.monitoring.YetiUpdatableSlider;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class that represents an improved random strategy.
 * The goal is to also maintain a set of interesting values within the types.
 * With a given probability, the strategy injects one of these values instead
 * of a pure random approach.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk), Mian Asbat Ahmad (ma@cs.york.ac.uk)
 * @date April 11, 2011
 *
 */


public class YetiDSSStrategy extends YetiRandomStrategy {

	/**
	 * The probability to inject an interesting value (default is 10%).
	 * 
	 */
	private static double INTERESTING_VALUE_INJECTION_PROBABILITY = 0.60;


	/**
	 * HashMap to store the type name with the interesting values.
	 */
	public static HashMap<String, Object> hashMapToStoreTypeNameWithInterestingValues = new HashMap<String, Object>();



	/**
	 * Creates the RandomPlusStrategy using a test manager.
	 * 
	 * @param ytm the test manager to create the strategy.
	 */
	public YetiDSSStrategy(YetiTestManager ytm) {
		super(ytm);

		// This statement is added to remove the existing interesting values from the Vector.
		YetiType.interestingValues.clear();
	}


	long oldFaults1=0;

	static int  number = 1;

	long oldFaults2=0;

	YetiCard[] oldyt=null;

	int lengthOfParameters;

	public YetiCard[] getAllCards(YetiRoutine routine) throws ImpossibleToMakeConstructorException{

		long currentErrors = YetiLog.numberOfErrors;

		YetiLog.printDebugLog("Number of Errors "+currentErrors, this);

		if (currentErrors>oldFaults1){
			YetiLog.printDebugLog("found bug in the strategy", this);
			oldFaults1 = currentErrors;


			for(YetiCard yc: oldyt){
				yc.getValue();

				if (yc.getType().getName().equals("int"))
				{	

					String typeName = "IntegerType"+ number++; 
					ArrayList<Integer> arrayListToStoreDSSInterestingIntegerValues = new ArrayList<Integer>();

					int intFaultValue = ((Integer)(yc.getValue())).intValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Integer)intFaultValue);
					arrayListToStoreDSSInterestingIntegerValues.add((Integer)intFaultValue);

					int temp = 0;
					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						temp = intFaultValue + i;

						//http://www.cafeaulait.org/course/week2/02.html

						if((temp > -2147483648)&&(temp < 2147483467))
						{
							yc.getType().addInterestingValues((Integer)temp);
							arrayListToStoreDSSInterestingIntegerValues.add((Integer)temp);
						}
					}

					// This for loop will add values lesser than the fault value.
					for (int j = 1; j < 5; j++)
					{
						temp = intFaultValue - j;

						if((temp > -2147483648)&&(temp < 2147483467))
						{
							yc.getType().addInterestingValues((Integer)temp);
							arrayListToStoreDSSInterestingIntegerValues.add((Integer)temp);
						}
					}

					hashMapToStoreTypeNameWithInterestingValues.put(typeName, arrayListToStoreDSSInterestingIntegerValues);

				}


				else if (yc.getType().getName().equals("double"))
				{

					String typeName = "DoubleType"+ number++; 
					ArrayList<Double> arrayListToStoreDSSInterestingDoubleValues = new ArrayList<Double>();

					double doubleFaultValue = ((Double)(yc.getValue())).doubleValue();

					double temp;	
					// This statement will add the fault value first
					yc.getType().addInterestingValues((Double)doubleFaultValue);
					arrayListToStoreDSSInterestingDoubleValues.add((Double)doubleFaultValue);

					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						temp = doubleFaultValue + i;

						if((temp > -4.94065645841246544e-324d)&&(temp < 1.79769313486231570e+308d))
						{	
							yc.getType().addInterestingValues((Double)temp);
							arrayListToStoreDSSInterestingDoubleValues.add((Double)temp);
						}
					}

					// This for loop will add values lesser than the fault value.
					for (int j = 1; j < 5; j++)
					{
						temp = doubleFaultValue - j;

						if((temp > -4.94065645841246544e-324d)&&(temp < 1.79769313486231570e+308d))
						{
							yc.getType().addInterestingValues((Double)temp);
							arrayListToStoreDSSInterestingDoubleValues.add((Double)temp);
						}
					}

					hashMapToStoreTypeNameWithInterestingValues.put(typeName, arrayListToStoreDSSInterestingDoubleValues);
				}	


				else if(yc.getType().getName().equals("String")){

					String typeName = "StringType"+ number++; 
					ArrayList<String> arrayListToStoreDSSInterestingStringValues = new ArrayList<String>();

					String c = (String) yc.getValue();

					yc.getType().addInterestingValues((String)c);
					arrayListToStoreDSSInterestingStringValues.add((String)c);

					yc.getType().addInterestingValues((String)c+" ");
					arrayListToStoreDSSInterestingStringValues.add((String)c+" ");

					yc.getType().addInterestingValues((String)" "+c);
					arrayListToStoreDSSInterestingStringValues.add((String)" "+c);

					yc.getType().addInterestingValues((String)c.toUpperCase());
					arrayListToStoreDSSInterestingStringValues.add((String)c.toUpperCase());

					yc.getType().addInterestingValues((String)c.toLowerCase());
					arrayListToStoreDSSInterestingStringValues.add((String)c.toLowerCase());

					yc.getType().addInterestingValues((String)c.trim());
					arrayListToStoreDSSInterestingStringValues.add((String)c.trim());

					yc.getType().addInterestingValues((String)c.substring(2));
					arrayListToStoreDSSInterestingStringValues.add((String)c.substring(2));

					yc.getType().addInterestingValues((String)c.substring(1,c.length()-1));
					arrayListToStoreDSSInterestingStringValues.add((String)c.substring(1,c.length()-1));

					hashMapToStoreTypeNameWithInterestingValues.put( typeName, arrayListToStoreDSSInterestingStringValues);

				}


				else if(yc.getType().getName().equals("byte"))
				{
					String typeName = "ByteType"+ number++; 
					ArrayList<Byte> arrayListToStoreDSSInterestingByteValues = new ArrayList<Byte>();


					byte byteFaultValue = ((Byte)(yc.getValue())).byteValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Byte)byteFaultValue);
					arrayListToStoreDSSInterestingByteValues.add((Byte)byteFaultValue);
					int temp;
					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{	
						temp = byteFaultValue + i;

						if((temp > -128)&&(temp < 127))
						{
							yc.getType().addInterestingValues((Byte)(byte)temp);
							arrayListToStoreDSSInterestingByteValues.add((Byte)(byte)temp);
						}
					}

					// This for loop will add values lesser than the fault value.
					for (int j = 1; j < 5; j++)
					{
						temp = byteFaultValue - j;
						if((temp > -128)&&(temp < 127))
						{
							yc.getType().addInterestingValues((Byte)(byte) temp);
							arrayListToStoreDSSInterestingByteValues.add((Byte)(byte)temp);
						}
					}

					hashMapToStoreTypeNameWithInterestingValues.put(typeName, arrayListToStoreDSSInterestingByteValues);

				}


				else if(yc.getType().getName().equals("short"))
				{
					String typeName = "ShortType"+ number++; 
					ArrayList<Short> arrayListToStoreDSSInterestingShortValues = new ArrayList<Short>();


					short shortFaultValue = ((Short)(yc.getValue())).shortValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Short)shortFaultValue);
					arrayListToStoreDSSInterestingShortValues.add((Short)shortFaultValue);
					int temp;

					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						temp = shortFaultValue + i;
						if((temp > -32768)&&(temp < 32767))
						{
							yc.getType().addInterestingValues((Short)(short)temp);
							arrayListToStoreDSSInterestingShortValues.add((Short)(short)temp);
						}
					}

					// This for loop will add values lesser than the fault value.
					for (int j = 1; j < 5; j++)
					{
						temp = shortFaultValue - j;
						if((temp > -32768)&&(temp < 32767))
						{
							yc.getType().addInterestingValues((Short)(short)temp);
							arrayListToStoreDSSInterestingShortValues.add((Short)(short)temp);
						}
					}

					hashMapToStoreTypeNameWithInterestingValues.put(typeName, arrayListToStoreDSSInterestingShortValues);
				}


				else if(yc.getType().getName().equals("long"))
				{
					String typeName = "LongType"+ number++; 
					ArrayList<Long> arrayListToStoreDSSInterestingLongValues = new ArrayList<Long>();

					long longFaultValue = ((Long)(yc.getValue())).longValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Long)longFaultValue);
					arrayListToStoreDSSInterestingLongValues.add((Long)longFaultValue);

					long temp;	
					
					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						temp = longFaultValue + i;
						// This is giving error and I dont understand why therefore I am commenting this check.
						//if((temp > -9223372036854775808)&&(temp < 9223372036854775807))
						{
							yc.getType().addInterestingValues((Long)temp);
							arrayListToStoreDSSInterestingLongValues.add((Long)temp);
						}
					}

					// This for loop will add values lesser than the fault value.
					for (int j = 1; j < 5; j++)
					{
						temp = longFaultValue - j;
						//if((temp > -9223372036854775808)&&(temp < 9223372036854775807))
						{
							yc.getType().addInterestingValues((Long)temp);
							arrayListToStoreDSSInterestingLongValues.add((Long)temp);
						}
					}

					hashMapToStoreTypeNameWithInterestingValues.put(typeName, arrayListToStoreDSSInterestingLongValues);
				}


				else if(yc.getType().getName().equals("char"))
				{

					String typeName = "CharType"+ number++; 
					ArrayList<Character> arrayListToStoreDSSInterestingCharValues = new ArrayList<Character>();

					char charFaultValue = ((Character)(yc.getValue())).charValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Character)charFaultValue);
					arrayListToStoreDSSInterestingCharValues.add((Character)charFaultValue);
					int temp;
					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						temp = charFaultValue + i;
						if((temp > 0)&&(temp < 65535))
						{
							yc.getType().addInterestingValues((Character)(char)temp);
							arrayListToStoreDSSInterestingCharValues.add((Character)(char)temp);
						}
					}

					// This for loop will add values lesser than the fault value.
					for (int j = 1; j < 5; j++)
					{
						temp = charFaultValue - j;
						if((temp > 0)&&(temp < 65535))
						{
							yc.getType().addInterestingValues((Character)(char)temp);
							arrayListToStoreDSSInterestingCharValues.add((Character)(char)temp);
						}
					}
					hashMapToStoreTypeNameWithInterestingValues.put(typeName, arrayListToStoreDSSInterestingCharValues);
				}


				else if(yc.getType().getName().equals("float"))
				{
					String typeName = "FloatType"+ number++; 
					ArrayList<Float> arrayListToStoreDSSInterestingFloatValues = new ArrayList<Float>();

					float floatFaultValue = ((Float)(yc.getValue())).floatValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Float)floatFaultValue);
					arrayListToStoreDSSInterestingFloatValues.add((Float)floatFaultValue);
					float temp;
					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						temp = floatFaultValue + i;
						if((temp > 1.40129846432481707e-45)&&(temp < 3.40282346638528860e+38))
						{
							yc.getType().addInterestingValues((Float)temp);
							arrayListToStoreDSSInterestingFloatValues.add((Float)temp);
						}
					}

					// This for loop will add values lesser than the fault value.
					for (int j = 1; j < 5; j++)
					{
						temp = floatFaultValue - j;
						if((temp > 1.40129846432481707e-45)&&(temp < 3.40282346638528860e+38))
						{
							yc.getType().addInterestingValues((Float)temp);
							arrayListToStoreDSSInterestingFloatValues.add((Float)temp);
						}
					}

					hashMapToStoreTypeNameWithInterestingValues.put(typeName, arrayListToStoreDSSInterestingFloatValues);

				}
			}
		}


		oldyt=super.getAllCards(routine);
		return oldyt;
	} 







	/**
	 * This overrides the strategy and chooses to pick from a set of 
	 * interesting values IF AVAILABLE.
	 * 
	 * @see yeti.strategies.YetiRandomStrategy#getNextCard(yeti.YetiRoutine, int, int)
	 */
	@Override
	public YetiCard getNextCard(YetiRoutine routine, int argumentNumber)
	throws ImpossibleToMakeConstructorException {
		YetiType cardType = routine.getOpenSlots()[argumentNumber];
		if (cardType.hasInterestingValues())
			if (Math.random()<INTERESTING_VALUE_INJECTION_PROBABILITY) {
				Object value =cardType.getRandomInterestingValue();   
				YetiLog.printDebugLog("Interesting value: "+value, this);
				YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
				return new YetiVariable(id, cardType, value);
			}

		return super.getNextCard(routine, argumentNumber);
	}



	/**
	 * This overrides the strategy and chooses to pick from a set of 
	 * interesting values if available.
	 * 
	 * @see yeti.strategies.YetiRandomStrategy#getNextCard(yeti.YetiRoutine, int, int)
	 */
	@Override
	public YetiCard getNextCard(YetiRoutine routine, int argumentNumber,
			int recursiveRank) throws ImpossibleToMakeConstructorException {
		YetiType cardType = routine.getOpenSlots()[argumentNumber];
		if (cardType.hasInterestingValues())
			if (Math.random()<INTERESTING_VALUE_INJECTION_PROBABILITY) {
				return cardType.getRandomInterestingVariable();
			}

		return super.getNextCard(routine, argumentNumber, recursiveRank);
	}




	@SuppressWarnings("serial")
	@Override
	public JPanel getPreferencePane() {
		// we generate a panel to contain both the label and the slider
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		JLabel txt = new JLabel("% DSS interesting values: ");
		p.add(txt);
		txt.setAlignmentX(0);

		// we create the slider, this slider is updated both ways
		YetiUpdatableSlider useInterestingValuesSlider = new YetiUpdatableSlider(JSlider.HORIZONTAL, 
				0, 100, (int) YetiDSSStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY*100) {

			/* (non-Javadoc)
			 * Updates the value by taking its value from the variable
			 * 
			 * @see yeti.monitoring.YetiUpdatableSlider#updateValues()
			 */
			public void updateValues() {
				super.updateValues();
				this.setValue((int)(YetiDSSStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY*100));

			}
		};

		// we set up the listener that updates the value
		useInterestingValuesSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int nullValuesP = (int)source.getValue();
					YetiDSSStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY = ((double)nullValuesP)/100;
				}			
			}
		});

		//Turn on labels at major tick marks.
		useInterestingValuesSlider.setMajorTickSpacing(25);
		useInterestingValuesSlider.setMinorTickSpacing(5);
		useInterestingValuesSlider.setPaintTicks(true);
		useInterestingValuesSlider.setPaintLabels(true);

		YetiGUI.allComponents.add(useInterestingValuesSlider);
		useInterestingValuesSlider.setMaximumSize(new Dimension(130,50));
		useInterestingValuesSlider.setAlignmentX(0);
		p.add(useInterestingValuesSlider);


		TitledBorder title = BorderFactory.createTitledBorder(Yeti.strategy.getName()+" Panel");
		p.setBorder(title);
		p.setMinimumSize(new Dimension(300,250));
		p.setMaximumSize(new Dimension(300,250));
		p.setAlignmentX(Component.CENTER_ALIGNMENT);

		return p;

	}






	@Override
	public String getName() {
		return "Dirt Spot Sweeping Strategy";
	}

}
