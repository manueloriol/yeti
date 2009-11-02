package yeti.strategies;

import yeti.Yeti;
import yeti.YetiEngine;
import yeti.YetiModule;
import yeti.YetiRoutine;
import yeti.YetiStrategy;
import yeti.YetiVariable;
import yeti.environments.YetiTestManager;

public class YetiRandomPlusDecreasing extends YetiRandomPlusStrategy {

	YetiEngine ye = null;

	int lastValue = 100;

	public YetiRoutine getNextRoutine(YetiModule module) {

		// we check whether the engine is null or not
		if (ye == null) {
			ye = Yeti.engine;
		} 
		// if it is still null we abandon...
		// otherwise we set the value of the probailities to the state of the progress
		if (ye!=null){
			// we sample the value
			int currentValue = ye.getProgress();
			// if the previous and current values are the same we don't do anything
			// otherwise, we change the values...
			if (lastValue!=currentValue) {
				double val = ((double)(100-((double)currentValue)))/(100.0d);
				YetiVariable.PROBABILITY_TO_USE_NULL_VALUE=val;											
				YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY=val;											
				YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY=val;											
			}
		}
		return super.getNextRoutine(module);
	}

	/**
	 * We set the initial values to 100%
	 * 
	 * @param ytm the test manager for this strategy
	 */
	public YetiRandomPlusDecreasing(YetiTestManager ytm) {
		super(ytm);
		YetiVariable.PROBABILITY_TO_USE_NULL_VALUE=1.0;											
		YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY=1.0;											
		YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY=1.0;											

	}

	@Override
	public String getName() {
		return "Random+ Decreasing Strategy";
	}


}
