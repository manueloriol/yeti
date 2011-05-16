package yeti.strategies.GA;

import org.jgap.Chromosome;
import org.jgap.Gene;
import org.jgap.IChromosome;
import yeti.*;
import yeti.environments.YetiTestManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Lucas Serpa Silva
 * Date: Apr 19, 2011
 * Time: 11:15:14 PM
 */
public class YetiEvolutionaryStrategy extends YetiStrategy{


	/**
	 * The maximum number of recursions allowed for creating arguments to a method.
	 */
	public static int MAX_RECURSIVE_RANK=20;
    private YetiChromosomeInterpreter chromosomeInterpreter;
    private ArrayList<YetiRoutine> staticRoutineList;


	/**
	 * Creates this strategy.
	 *
	 * @param ytm the test manager used.
	 */
	public YetiEvolutionaryStrategy(YetiTestManager ytm, YetiChromosomeInterpreter chromosomeInterpreter) {
		super(ytm);
        this.chromosomeInterpreter = chromosomeInterpreter;
	}

    /* (non-Javadoc)
      *
      * Standard strategy, get the arguments.
      *
      * @see yeti.YetiStrategy#getAllCards(yeti.YetiRoutine)
      */
	public YetiCard[] getAllCards(YetiRoutine routine) throws ImpossibleToMakeConstructorException{
		int length = routine.getOpenSlots().length;
		YetiCard[] yt=new YetiCard[length];

		// for all open slots we try to get a corresponding instance
		for (int i=0; i<length; i++){
			YetiLog.printDebugLog("trying to get argument "+ i, this);
			yt[i]=getNextCard(routine, i, 0);
		}
		return yt;
	}

	/**
	 * Get all the arguments with the level of recursion.
	 *
	 * @param routine the routine to test.
	 * @param recursiveRank the rank of recursion
	 * @return an array with all arguments.
	 * @throws ImpossibleToMakeConstructorException when it is impossible to construct all argument givent hte level of recursion.
	 */
	public YetiCard[] getAllCards(YetiRoutine routine, int recursiveRank) throws ImpossibleToMakeConstructorException{

		// we test for the level of recursion
		if (recursiveRank >= MAX_RECURSIVE_RANK) {
			throw new ImpossibleToMakeConstructorException(routine.getClass().getName());
        }

		int length = routine.getOpenSlots().length;
		YetiCard[] yt = new YetiCard[length];

		// we try to get all arguments
		for (int i=0; i<length; i++){
			YetiLog.printDebugLog("trying to get argument "+ i, this);
			yt[i] = getNextCard(routine, i, recursiveRank+1);
		}
		return yt;
	}



	/**
	 * Tries to find or generate one argument.
	 *
	 * @param routine the routine to test.
	 * @param argumentNumber the slot number.
	 * @param recursiveRank the rank of recursion at which we are.
	 * @return the argument to use.
	 * @throws ImpossibleToMakeConstructorException when there is no possibility to generate the argument.
	 */
	public YetiCard getNextCard(YetiRoutine routine, int argumentNumber, int recursiveRank) throws ImpossibleToMakeConstructorException {

        // we test first
        if (recursiveRank >= MAX_RECURSIVE_RANK) {
            throw new ImpossibleToMakeConstructorException(routine.getClass().getName());
        }

        YetiType type = routine.getOpenSlots()[argumentNumber];
        YetiLog.printDebugLog(routine.getName().getValue(),this);

		// we try to get a random instance from the pool
        //TODO: Initialize the pool to a fixed size
        //TODO change 100 by a parameter
		if ((type.instances.size() > 0) && (100>NEW_INSTANCES_INJECTION_PROBABILITY)){
			return type.getDeterministicInstance(chromosomeInterpreter.getNextMethodCallParameter());
		}

        YetiRoutine creator;

		// we try to create that random instance
		try {
			creator = type.getDeterministicCreationRoutine(chromosomeInterpreter.getNextMethodCall());
		} catch (NoCreationRoutineInType e) {
			throw new ImpossibleToMakeConstructorException(e.getMessage());
		}

		YetiCard[] creatorCards = getAllCards(creator,recursiveRank);

		if (creatorCards.length>0) {
			YetiLog.printDebugLog("Getting creation routine of type "+type.getName()+" with arguments "+creatorCards[0]+"...", this);

        } else {
			YetiLog.printDebugLog("Getting creation routine of type "+type.getName()+" with no arguments ", this);
        }

		// we make a call to a constructor
		YetiVariable result = (YetiVariable)creator.makeCall(creatorCards);
		if (result!=null) {
			return result;
        } else {
			return getNextCard( routine, argumentNumber, recursiveRank+1);
        }
	}

	/* (non-Javadoc)
	 * Standard get next routine method.
	 *
	 * @see yeti.YetiStrategy#getNextRoutine(yeti.YetiModule)
	 */
	@Override
	public YetiRoutine getNextRoutine(YetiModule module) {
        if (this.staticRoutineList == null) {
            this.staticRoutineList = new ArrayList<YetiRoutine>(module.routinesInModule.values());

            Collections.sort(this.staticRoutineList, new Comparator(){
                public int compare(Object o1, Object o2) {
                    YetiRoutine p1 = (YetiRoutine) o1;
                    YetiRoutine p2 = (YetiRoutine) o2;
                    return p1.getName().getValue().compareTo(p2.getName().getValue());
                }

            });


        }
        int i = this.chromosomeInterpreter.getNextMethodCall() % staticRoutineList.size();
		YetiRoutine r = this.staticRoutineList.get(i);
        YetiLog.printDebugLog("Testing routine "+r+" module "+module.getModuleName(),this);
		return r;
	}

	/* (non-Javadoc)
	 * standard get next argument method.
	 *
	 * @see yeti.YetiStrategy#getNextCard(yeti.YetiRoutine, int)
	 */
	@Override
	public YetiCard getNextCard(YetiRoutine routine, int argumentNumber) throws ImpossibleToMakeConstructorException {

		YetiType type = routine.getOpenSlots()[argumentNumber];
		YetiLog.printDebugLog(routine.getName().getValue(),this);

		// we try to get a random argument.
		if (type.instances.size()>0){
			return type.getDeterministicInstance(chromosomeInterpreter.getNextMethodCallParameter());
		}
		YetiRoutine creator;
		try {
			// we get a creation routine at random.
			creator = type.getRandomCreationRoutine();
		} catch (NoCreationRoutineInType e) {
			throw new ImpossibleToMakeConstructorException(e.getMessage());
		}
		// we try to get all the arguments to the creation routine
		YetiCard[] creatorCards=getAllCards(creator);
		if (creatorCards.length>0)
			YetiLog.printDebugLog("Getting creation routine of type "+type.getName()+" with arguments "+creatorCards[0]+"...", this);
		else
			YetiLog.printDebugLog("Getting creation routine of type "+type.getName()+" with no arguments ", this);

		// we make the call
		YetiVariable result = (YetiVariable)creator.makeCall(creatorCards);
		if (result!=null) {
			return result;
        } else  {
			// we try to call again in case we could not generate the routine.
			return getNextCard( routine, argumentNumber);
        }
	}

	@Override
	public String getName() {
		return "Random Strategy";
	}
}
