package yeti.strategies.GA;

import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.SwappingMutationOperator;
import yeti.YetiLog;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Lucas Serpa Silva
 * Date: Apr 20, 2011
 * Time: 10:44:40 PM
 */
public class YetiStrategyOptimizer {

    public Configuration gaConfiguration;

    public void evolveStrategy() {
        YetiLog.printDebugLog("Evolution started",this);
        Genotype genotype = null;
        try {
            genotype = configureJGAP();
            doEvolution(genotype);
            YetiLog.printDebugLog("Evolution finished", this);
            YetiLog.printDebugLog("The chromosome is " + genotype.getFittestChromosome().getFitnessValue(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the GA and Chromosome.
     * @return Genotype
     * @throws Exception
     */
    public Genotype configureJGAP()  throws Exception {

       this.gaConfiguration = new DefaultConfiguration();
        Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);

        gaConfiguration.setFitnessEvaluator(new DeltaFitnessEvaluator());
        // Just use a swapping operator instead of mutation and others.
        // ------------------------------------------------------------
        gaConfiguration.getGeneticOperators().clear();
        SwappingMutationOperator swapper = new SwappingMutationOperator(gaConfiguration);
        gaConfiguration.addGeneticOperator(swapper);
        // Setup some other parameters.
        // ----------------------------
        gaConfiguration.setPreservFittestIndividual(true);
        gaConfiguration.setKeepPopulationSizeConstant(false);
        // Set number of individuals (=tries) per generation.
        // --------------------------------------------------
        gaConfiguration.setPopulationSize(YetiGAParameters.GA_POPULATION_SIZE);
        Genotype genotype = null;
        try {
            //At the moment the chromosome is very simple and only includes
            //the routine callse.

            gaConfiguration.setSampleChromosome(createChromosome(gaConfiguration));
            //At the moment the fitness function is only using the number of faults
            gaConfiguration.setFitnessFunction(new YetiSimpleFitnessFunction());

            genotype = Genotype.randomInitialGenotype(gaConfiguration);
            //FIXME: lssilva this alleles have to be specified according to
            //the chromosome.
            List chromosomes = genotype.getPopulation().getChromosomes();
            for (int i = 0; i < chromosomes.size(); i++) {
                IChromosome chrom = (IChromosome) chromosomes.get(i);
                for (int j = 0; j < chrom.size(); j++) {
                    Gene gene = (Gene) chrom.getGene(j);
                    gene.setAllele(j);
                }
            }
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            System.exit( -2);
        }
        return genotype;
    }

    /**
     * Does the evolution until finished.
     */
    public void doEvolution(Genotype a_genotype) {
        int progress = 0;
        int percentEvolution = YetiGAParameters.GA_NUMBER_GENERATION / 100;
        for (int i = 0; i < YetiGAParameters.GA_NUMBER_GENERATION; i++) {
            a_genotype.evolve();
            // Print progress.
            // ---------------
            if (percentEvolution > 0 && i % percentEvolution == 0) {
                progress++;
                IChromosome fittest = a_genotype.getFittestChromosome();
                double fitness = fittest.getFitnessValue();
                System.out.println("Currently best solution has fitness " + fitness);
                printSolution(fittest);
            }
        }
        // Print summary.
        // --------------
        IChromosome fittest = a_genotype.getFittestChromosome();
        try {
            YetiStrategyPersistenceManager.saveChromosome(fittest, "/tmp/test.c");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Best solution has fitness " +  fittest.getFitnessValue());
        printSolution(fittest);
    }

    /**
     * @param a_solution of the best chromosome.
     */
    public void printSolution(IChromosome a_solution) {
        YetiLog.printDebugLog("Printing the chromosome of the Solution with size" + a_solution.size(),this);
        String chromosome = "";
        for (int i = 0; i < a_solution.size(); i++) {
            IntegerGene gene = (IntegerGene)a_solution.getGene(i);
            chromosome = chromosome.concat(gene.intValue() +",");
        }

        YetiLog.printDebugLog("Chromosome = "+chromosome,this);

    }

    /**
     * @param gaConf to create the chromosome
     * @return a chromosome
     */
    public IChromosome createChromosome(Configuration gaConf) {

        IChromosome sampleChromosome = null;
        try {
            sampleChromosome = new Chromosome(gaConf,YetiGAParameters.GA_CHROMOSOME_SIZE);
            Gene[] gene  = new IntegerGene[YetiGAParameters.GA_CHROMOSOME_SIZE];
            for (int i = 0; i < gene.length; i++) {
                gene[i] = new IntegerGene(gaConf, 0, 1000); //Alleles max num methods
            }
            sampleChromosome.setGenes(gene);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return sampleChromosome;
    }
}
