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
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.YetiVariable;
import yeti.environments.YetiTestManager;
import yeti.monitoring.YetiGUI;
import yeti.monitoring.YetiUpdatableSlider;

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
public class YetiDSSRStrategy extends YetiRandomStrategy {

	/**
	 * The probability to inject an interesting value (default is 10%).
	 * 
	 */
	public static double INTERESTING_VALUE_INJECTION_PROBABILITY = 0.1;

	/**
	 * Creates the RandomPlusStrategy using a test manager.
	 * 
	 * @param ytm the test manager to create the strategy.
	 */
	public YetiDSSRStrategy(YetiTestManager ytm) {
		super(ytm);
		
//		for (YetiType yt: YetiType.allTypes.values()){
//			yt.interestingValues.clear();
//		}
		//YetiType.interestingValues.clear();
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

	/**
	 * This overrides the strategy and chooses to pick from a set of 
	 * interesting values if available.
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
				0, 100, (int) YetiDSSRStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY*100) {

			/* (non-Javadoc)
			 * Updates the value by taking its value from the variable
			 * 
			 * @see yeti.monitoring.YetiUpdatableSlider#updateValues()
			 */
			public void updateValues() {
				super.updateValues();
				this.setValue((int)(YetiDSSRStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY*100));

			}
		};
		
		// we set up the listener that updates the value
		useInterestingValuesSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int nullValuesP = (int)source.getValue();
					YetiDSSRStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY = ((double)nullValuesP)/100;
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
	
	
	long oldFaults1=0;
	
	YetiCard[] oldyt=null;

	public YetiCard[] getAllCards(YetiRoutine routine) throws ImpossibleToMakeConstructorException{

		long currentErrors = YetiLog.numberOfErrors;

		YetiLog.printDebugLog("nErrors "+currentErrors, this);

		if (currentErrors>oldFaults1){
			YetiLog.printDebugLog("found bug in the strategy", this);
			oldFaults1 = currentErrors;


			for(YetiCard yc: oldyt){
				yc.getValue();

				if (yc.getType().getName().equals("int"))
				{
					int intFaultValue = ((Integer)(yc.getValue())).intValue();

					// This statement will add the fault value first
					
					yc.getType().addInterestingValues((Integer)intFaultValue);

					// for loop will add values greater and lesser than the fault value.

					for (int i =1; i < 5; i++)
					{
						yc.getType().addInterestingValues((Integer)intFaultValue + i);
						yc.getType().addInterestingValues((Integer)intFaultValue - i);
					}
				}


				if (yc.getType().getName().equals("double"))
				{
					double doubleFaultValue = ((Double)(yc.getValue())).doubleValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Double)doubleFaultValue);

					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						yc.getType().addInterestingValues((Double)doubleFaultValue + i);					
						yc.getType().addInterestingValues((Double)doubleFaultValue - i);
					}
				}	


				if(yc.getType().getName().equals("String")){

					String c = (String) yc.getValue();

					yc.getType().addInterestingValues((String)c);
					yc.getType().addInterestingValues((String)c+" ");
					yc.getType().addInterestingValues((String)" "+c);
					yc.getType().addInterestingValues((String)c.toUpperCase());
					yc.getType().addInterestingValues((String)c.toLowerCase());
					yc.getType().addInterestingValues((String)c.trim());
					yc.getType().addInterestingValues((String)c.substring(2));
					yc.getType().addInterestingValues((String)c.substring(1,c.length()-1));

				}


				if(yc.getType().getName().equals("byte"))
				{
					byte byteFaultValue = ((Byte)(yc.getValue())).byteValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Byte)byteFaultValue);

					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						yc.getType().addInterestingValues((Byte)byteFaultValue+i);
						yc.getType().addInterestingValues((Byte)byteFaultValue-i);
					}
				}


				if(yc.getType().getName().equals("short"))
				{
					short shortFaultValue = ((Short)(yc.getValue())).shortValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Short)shortFaultValue);

					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						yc.getType().addInterestingValues((Short)shortFaultValue + i);
						yc.getType().addInterestingValues((Short)shortFaultValue - i);
					}
				}


				if(yc.getType().getName().equals("long"))
				{
					long longFaultValue = ((Long)(yc.getValue())).longValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Long)longFaultValue);

					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						yc.getType().addInterestingValues((Long)longFaultValue + i);
						yc.getType().addInterestingValues((Long)longFaultValue - i);
					}
				}


				if(yc.getType().getName().equals("char"))
				{
					char charFaultValue = ((Character)(yc.getValue())).charValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Character)charFaultValue);

					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						yc.getType().addInterestingValues((Character)(char)(charFaultValue+i));
						yc.getType().addInterestingValues((Character)(char)(charFaultValue-i));
					}
				}


				if(yc.getType().getName().equals("float"))
				{
					float floatFaultValue = ((Float)(yc.getValue())).floatValue();

					// This statement will add the fault value first
					yc.getType().addInterestingValues((Float)floatFaultValue);

					// This for loop will add values greater than the fault value.
					for (int i =1; i < 5; i++)
					{
						yc.getType().addInterestingValues((Float)floatFaultValue + i);
						yc.getType().addInterestingValues((Float)floatFaultValue - i);
					}
				}
			}
		}


		oldyt = super.getAllCards(routine);
		return oldyt;
	} 
	
	
	
	@Override
	public String getName() {
		return "Dirt Spot Sweeping Strategy";
	}

}
