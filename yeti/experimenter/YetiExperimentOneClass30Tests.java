package yeti.experimenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import yeti.Yeti;

/**
 * Class that represents... 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date May 3, 2011
 *
 */
public class YetiExperimentOneClass30Tests extends YetiExperiment {
	
	/**
	 * A String representing the classpath
	 */
	String classPath;

	/**
	 * A string representing the name of the class to test
	 */
	String className;

	/**
	 * The percentage of Null values
	 */
	int nullValues;

	/**
	 * The percentage of new values.
	 */
	private int newValues;

	/**
	 * Simple constructor for the YetiExperiment30PicksRandom
	 * 
	 * @param ps the PrintStream where to store results.
	 * @param staticOptions the options that are standard.
	 * @param arch the archive explorer.
	 * @param onlyPrint true if only to print the commands. 
	 */
	public YetiExperimentOneClass30Tests(PrintStream ps, String[] staticOptions,boolean onlyPrint, String classPath, String className, int nullValues, int newValues) {
		super(ps, staticOptions,onlyPrint);
		this.className = className;
		this.classPath = classPath;
		this.nullValues = nullValues;
		this.newValues = newValues;
	}

	@Override
	public void make() {
			for (int i = 0; i<30; i++) {
					this.makeCall(classPath, className);
			}
	}

	/**
	 * Make an individual call to YETI.
	 * 
	 * @param yetiPath the yetiPath to provide.
	 * @param modules the modules to test.
	 */
	public void makeCall(String yetiPath, String modules) {
		// we first initialize the arguments.
		String []args = new String[staticOptions.length+4];
		for (int i = 0;i<staticOptions.length;i++) {
			args[i]=staticOptions[i];
		}
		args[staticOptions.length]="-yetiPath="+yetiPath;
		args[staticOptions.length+1]="-testModules="+modules;
		args[staticOptions.length+2]="-newInstanceInjectionProbability="+newValues;
		args[staticOptions.length+3]="-probabilityToUseNullValue="+nullValues;
		
		// We then make the call
		if (!onlyPrint) {
			Yeti.YetiRun(args);
			Yeti.reset();
		} else {
			String call = "java -ea yeti.Yeti ";
			for (String opt: args) {
				call=call+" "+opt;
			}
			System.out.println(call);
		}
	}
	/**
	 * Main method allowing to launch experiments.
	 * 
	 * Example of use: java yeti.experimenter.YetiExperimentOneClass30Tests . java.lang.String 10 10 -onlyPrint
	 * 
	 * @param args
	 */
	public static void main(String []args) {
		String classPath = args[0];
		String className = args[1];
		int nullValues = Integer.parseInt(args[2]);
		int newValues = Integer.parseInt(args[3]);
		
		boolean onlyPrint = false;
		if (args.length>4&&args[4].equals("-onlyPrint"))
			onlyPrint = true;
		YetiTestArchiveExplorer expl = new YetiTestArchiveExplorer(".");
		PrintStream ps = null;
		String []staticOptions = {"-Java","-nTests=100000","-nologs","-approximate","-compactReport=results.csv"};
		YetiExperimentOneClass30Tests exp = new YetiExperimentOneClass30Tests(ps, staticOptions,onlyPrint, classPath, className, nullValues, newValues);
		exp.make();

	}


}
