package yeti.strategies;

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
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 22, 2009
 *
 */
public class YetiRandomPlusStrategy extends YetiRandomStrategy {

	/**
	 * The probability to inject an interesting value (default is 10%).
	 * 
	 */
	public static double INTERESTING_VALUE_INJECTION_PROBABILITY = 0.50;

	/**
	 * Creates the RandomPlusStrategy using a test manager.
	 * 
	 * @param ytm the test manager to create the strategy.
	 */
	public YetiRandomPlusStrategy(YetiTestManager ytm) {
		super(ytm);
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
				Object value =cardType.getRandomInterestingValue();
				YetiLog.printDebugLog("Interesting value: "+value, this);
				YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
				return new YetiVariable(id, cardType, value);
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
		JLabel txt = new JLabel("% interesting values: ");
		p.add(txt);
		txt.setAlignmentX(0);
		
		// we create the slider, this slider is updated both ways
		YetiUpdatableSlider useInterestingValuesSlider = new YetiUpdatableSlider(JSlider.HORIZONTAL, 
				0, 100, (int) YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY*100) {

			/* (non-Javadoc)
			 * Updates the value by taking its value from the variable
			 * 
			 * @see yeti.monitoring.YetiUpdatableSlider#updateValues()
			 */
			public void updateValues() {
				super.updateValues();
				this.setValue((int)(YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY*100));

			}
		};
		
		// we set up the listener that updates the value
		useInterestingValuesSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int nullValuesP = (int)source.getValue();
					YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY = ((double)nullValuesP)/100;
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
		return "Random Plus Strategy";
	}

}
