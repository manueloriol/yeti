package yeti.environments.commandline;

import yeti.ImpossibleToMakeConstructorException;
import yeti.YetiCard;
import yeti.YetiModule;
import yeti.YetiRoutine;
import yeti.YetiStrategy;
import yeti.environments.YetiTestManager;

public class YetiCLTestManager extends YetiTestManager {

	@Override
	public void makeNextCall(YetiModule mod, YetiStrategy strategy) {
		YetiRoutine yr = strategy.getNextRoutine(mod);
		try {
			YetiCard[] ycs = strategy.getAllCards(yr);
			yr.makeEffectiveCall(ycs);
			
		} catch (ImpossibleToMakeConstructorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
