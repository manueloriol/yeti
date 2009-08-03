package yeti.strategies;

import yeti.ImpossibleToMakeConstructorException;
import yeti.YetiCard;
import yeti.YetiIdentifier;
import yeti.YetiRoutine;
import yeti.YetiType;
import yeti.YetiVariable;
import yeti.environments.YetiTestManager;

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
	public static double INTERESTING_VALUE_INJECTION_PROBABILITY = 0.10;
	
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
				Object value =cardType.removeInterestingValue();
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
				Object value =cardType.removeInterestingValue();
				YetiIdentifier id=YetiIdentifier.getFreshIdentifier();
				return new YetiVariable(id, cardType, value);
			}
				
		return super.getNextCard(routine, argumentNumber);
	}

}
