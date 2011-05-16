package yeti.strategies.GA;

/**
 * Created by IntelliJ IDEA.
 * User: slucas
 * Date: 4/28/11
 * Time: 10:02 PM
 */

//TODO: Make these parameters configurable Property file?

public class YetiGAParameters {
    public static int GA_CHROMOSOME_SIZE = 10000;
    public static int GA_POPULATION_SIZE = 1;
    public static int GA_NUMBER_GENERATION = 0;

    public String gaEvaluationTime;
    public String gaEvaluationModules;
    public String gaEvaluationModulesClassPath;

    public String getGaFittestChromosomeOutPutPath() {
        return gaFittestChromosomeOutPutPath;
    }

    public void setGaFittestChromosomeOutPutPath(String gaFittestChromosomeOutPutPath) {
        this.gaFittestChromosomeOutPutPath = gaFittestChromosomeOutPutPath;
    }

    public String gaFittestChromosomeOutPutPath;


    public String getGaEvaluationTime() {
        return gaEvaluationTime;
    }

    public void setGaEvaluationTime(String gaEvaluationTime) {
        this.gaEvaluationTime = gaEvaluationTime;
    }

    public String getGaEvaluationModules() {
        return gaEvaluationModules;
    }

    public void setGaEvaluationModules(String gaEvaluationModules) {
        this.gaEvaluationModules = gaEvaluationModules;
    }

    public String getGaEvaluationModulesClassPath() {
        return gaEvaluationModulesClassPath;
    }

    public void setGaEvaluationModulesClassPath(String gaEvaluationModulesClassPath) {
        this.gaEvaluationModulesClassPath = gaEvaluationModulesClassPath;
    }
}
