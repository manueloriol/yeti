package yeti.strategies;

import yeti.ImpossibleToMakeConstructorException;
import yeti.YetiCard;
import yeti.YetiRoutine;
import yeti.environments.YetiTestManager;

public class YetiRandomPlusStrategy extends YetiRandomStrategy {

	public YetiRandomPlusStrategy(YetiTestManager ytm) {
		super(ytm);
	}

	@Override
	public YetiCard getNextCard(YetiRoutine routine, int argumentNumber)
			throws ImpossibleToMakeConstructorException {
		// TODO Auto-generated method stub
		return null;
	}

}
