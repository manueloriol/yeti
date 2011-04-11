package yeti.experimenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class YetiExperimentTestAll extends YetiExperiment {

	/**
	 * The archive explorer where the archive is located.
	 */
	public YetiTestArchiveExplorer archive;

	/**
	 * Simple constructor for the YetiExperiment30PicksRandom
	 * 
	 * @param ps the PrintStream where to store results.
	 * @param staticOptions the options that are standard.
	 * @param arch the archive explorer.
	 * @param onlyPrint true if only to print the commands. 
	 */
	public YetiExperimentTestAll(PrintStream ps, String[] staticOptions, YetiTestArchiveExplorer arch, boolean onlyPrint) {
		super(ps, staticOptions,onlyPrint);
		archive = arch;
	}

	@Override
	public void make() {
		int archiveSize = archive.allModules.size();

		for (YetiTestArchiveModule mod:archive.allModules) {
			this.makeCall(mod.getClasspath(), mod.getClassName());
		}
	}

	/**
	 * Main method allowing to launch experiments.
	 * 
	 * @param args
	 */
	public static void main(String []args) {
		String yexpFile = args[0];
		boolean onlyPrint = false;
		if (args.length>1&&args[1].equals("-onlyPrint"))
			onlyPrint = true;
		YetiTestArchiveExplorer expl = new YetiTestArchiveExplorer(".");
		try {
			expl.loadFromFile(yexpFile);

			//PrintStream ps = new PrintStream("results.csv");
			PrintStream ps = null;
			String []staticOptions = {"-Java","-nTests=100000","-nologs","-approximate","-compactReport=results.csv"};
			YetiExperimentTestAll exp = new YetiExperimentTestAll(ps, staticOptions, expl,onlyPrint);
			exp.make();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
