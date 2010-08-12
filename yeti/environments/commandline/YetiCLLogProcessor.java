package yeti.environments.commandline;

import java.util.Vector;

import yeti.YetiLogProcessor;

/**
 * Class that represents a log processor for command lines.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 23, 2010
 *
 */
public class YetiCLLogProcessor extends YetiLogProcessor {

	Vector<String> Errors = new Vector<String>();
	
	/* (non-Javadoc)
	 * @see yeti.YetiLogProcessor#processLogs()
	 */
	@Override
	public Vector<String> processLogs() {
		return Errors;
	}

	@Override
	public void appendToCurrentLog(String newLog) {
		
		// no need to do anything...
	}
	/* 
	 * A nicer printer for Yeti and the command-line.
	 * 
	 * (non-Javadoc)
	 * @see yeti.YetiLogProcessor#appendFailureToCurrentLog(java.lang.String)
	 */
	public void appendFailureToCurrentLog(String newLog){
		Errors.add(newLog);
	}
}
