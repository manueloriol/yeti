package yeti.strategies.GA;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;
import yeti.*;
import yeti.cloud.YetiMap;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiLoader;
import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiTestManager;
import yeti.environments.commandline.YetiCLInitializer;
import yeti.environments.commandline.YetiCLLogProcessor;
import yeti.environments.commandline.YetiCLProperties;
import yeti.environments.commandline.YetiCLTestManager;
import yeti.environments.csharp.*;
import yeti.environments.java.*;
import yeti.environments.jml.YetiJMLPrefetchingLoader;
import yeti.environments.pharo.YetiPharoCommunicator;
import yeti.environments.pharo.YetiPharoTestManager;
import yeti.monitoring.YetiGUI;
import yeti.stats.YetiDataSet;
import yeti.stats.YetiMichaelisMentenEquation;
import yeti.strategies.*;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Lucas Serpa Silva
 * Date: Apr 23, 2011
 * Time: 12:15:29 PM
 */
public class YetiSimpleFitnessFunction extends FitnessFunction {
    private YetiGAParameters parameters;


    public YetiSimpleFitnessFunction(YetiGAParameters parameters) {
        this.parameters = parameters;
    }

    public double evaluate(IChromosome a_subject) {
        String[] arguments={"-java","-time="+this.parameters.gaEvaluationTime,"-testModules="+this.parameters.gaEvaluationModules,"-nologs","-chromosome=test"};
        Yeti.chromosome = a_subject;
        Yeti.YetiRun(arguments);
        return Yeti.report.getnErrors();
    }
}
