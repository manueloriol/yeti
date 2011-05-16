package yeti.strategies.GA;

import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;
import yeti.YetiLog;

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
        printSolution(aChromosome);

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

    //TODO make the sections in the chromosome
    public int getNextMethodCallParameter() {
        int result = 0;
        if (currentIndex < chromosome.size()) {
            IntegerGene gene = (IntegerGene)chromosome.getGene(currentIndex);
            result = gene.intValue();
            currentIndex++;
        }

        return result;

    }


     public void printSolution(IChromosome a_solution) {
        YetiLog.printDebugLog("Printing the chromosome of the Solution with size" + a_solution.size(), this);
        String chromosome = "";
        for (int i = 0; i < a_solution.size(); i++) {
            IntegerGene gene = (IntegerGene)a_solution.getGene(i);
            chromosome = chromosome.concat(gene.intValue() +",");
        }

        System.out.println("Chromosome = "+chromosome);
    }

}
