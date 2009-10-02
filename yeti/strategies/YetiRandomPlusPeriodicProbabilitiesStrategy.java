package yeti.strategies;

import yeti.YetiModule;
import yeti.YetiRoutine;
import yeti.YetiStrategy;
import yeti.YetiVariable;
import yeti.environments.YetiTestManager;

public class YetiRandomPlusPeriodicProbabilitiesStrategy extends YetiRandomPlusStrategy {

	static int routinesCalledSinceLastNullValueChangePeriod = 500;
	static int routinesCalledSinceLastNewGenChangePeriod = 250;
	static int routinesCalledSinceLastInterestingValuesChangePeriod = 1000;


	int routinesCalledSinceLastNullValueChange = 0;
	int routinesCalledSinceLastNewGenChange = 0;
	int routinesCalledSinceLastInterestingValuesChange = 0;

	public YetiRoutine getNextRoutine(YetiModule module) {
		routinesCalledSinceLastNullValueChange++;
		routinesCalledSinceLastNewGenChange++;
		routinesCalledSinceLastInterestingValuesChange++;

		if (routinesCalledSinceLastNullValueChange>routinesCalledSinceLastNullValueChangePeriod) {
			if (0==YetiVariable.PROBABILITY_TO_USE_NULL_VALUE) {
				YetiVariable.PROBABILITY_TO_USE_NULL_VALUE=1.0;						
			} else {
				YetiVariable.PROBABILITY_TO_USE_NULL_VALUE=YetiVariable.PROBABILITY_TO_USE_NULL_VALUE-.01;											
			}

			routinesCalledSinceLastNullValueChange=0;
		}
		if (routinesCalledSinceLastNewGenChange>routinesCalledSinceLastNewGenChangePeriod) {
			if (0==YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY) {
				YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY=1.0;						
			} else {
				YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY=YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY-.01;											
			}
			routinesCalledSinceLastNewGenChange=0;
		}
		if (routinesCalledSinceLastInterestingValuesChange>routinesCalledSinceLastInterestingValuesChangePeriod) {
			if (0==YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY) {
				YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY=1.0;						
			} else {
				YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY=YetiRandomPlusStrategy.INTERESTING_VALUE_INJECTION_PROBABILITY-.01;											
			}
			routinesCalledSinceLastInterestingValuesChange=0;
		}

		return super.getNextRoutine(module);
	}

	public YetiRandomPlusPeriodicProbabilitiesStrategy(YetiTestManager ytm) {
		super(ytm);
	}

	@Override
	public String getName() {
		return "Random Plus Periodic Strategy";
	}

	
}
