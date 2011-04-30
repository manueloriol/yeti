package yeti.strategies.GA;

import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;

/**
 * Created by IntelliJ IDEA.
 * User: slucas
 * Date: 4/28/11
 * Time: 9:31 PM
 */
public class YetiChromosomeInterpreter {
    int currentIndex;
    IChromosome chromosome;

    public YetiChromosomeInterpreter(IChromosome aChromosome) {
        this.chromosome = aChromosome;
        currentIndex = 0;

    }

    public int getNextMethodCall() {
        int result = 0;
        if (currentIndex < chromosome.size()) {
            IntegerGene gene = (IntegerGene)chromosome.getGene(currentIndex);
            result = gene.intValue();
            currentIndex++;
        }

        return result;
    }


}
