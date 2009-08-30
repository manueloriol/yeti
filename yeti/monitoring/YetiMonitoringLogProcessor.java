package yeti.monitoring;

import java.util.Vector;

import yeti.YetiLogProcessor;

public abstract class YetiMonitoringLogProcessor extends YetiLogProcessor {

	/**
	 * The processor taht we wrap here.
	 */
	public YetiLogProcessor lp;

	/* (non-Javadoc)
	 * 
	 * The call is wrapped. 
	 * 
	 * @see yeti.YetiLogProcessor#appendFailureToCurrentLog(java.lang.String)
	 */
	@Override
	public void appendFailureToCurrentLog(String newLog) {
		lp.appendFailureToCurrentLog(newLog);
	}

	/* (non-Javadoc)
	 * 
	 * The call is wrapped. 
	 * 
	 * @see yeti.YetiLogProcessor#appendToCurrentLog(java.lang.String)
	 */
	@Override
	public void appendToCurrentLog(String newLog) {
		lp.appendToCurrentLog(newLog);
	}


	/* (non-Javadoc)
	 * 
	 * The call is wrapped. 
	 * 
	 * @see yeti.YetiLogProcessor#printMessageNoLogs(java.lang.String)
	 */
	@Override
	public void printMessageNoLogs(String message) {
		lp.printMessageNoLogs(message);
	}

	/*
	 * The call is wrapped. 
	 * 
	 * @see yeti.YetiLogProcessor#printMessageRawLogs(java.lang.String)
	 */
	@Override
	public void printMessageRawLogs(String message) {
		lp.printMessageRawLogs(message);
	}

	/* 
	 * The call is wrapped and visual output requested.
	 * 
	 * @see yeti.YetiLogProcessor#printThrowableNoLogs(java.lang.Throwable)
	 */
	@Override
	public void printThrowableNoLogs(Throwable t) {
		lp.printThrowableNoLogs(t);
	}

	/* 
	 * Shows the correct value of the list of errors and call the regular RawLogs. 
	 * 
	 * (non-Javadoc)
	 * @see yeti.YetiLogProcessor#printThrowableRawLogs(java.lang.Throwable)
	 */
	@Override
	public void printThrowableRawLogs(Throwable t) {
		lp.printThrowableRawLogs(t);
	}
	
	/* 
	 * Shows the correct value of the list of errors and call the regular RawLogs. 
	 * 
	 * (non-Javadoc)
	 * @see yeti.YetiLogProcessor#printThrowableLogs(java.lang.Throwable)
	 */
	@Override
	public void printThrowableLogs(Throwable t) {
		lp.printThrowableLogs(t);
	}

	/* 
	 * Returns the wrapped call
	 * 
	 * @see yeti.YetiLogProcessor#processLogs()
	 */
	@Override
	public Vector<String> processLogs() {
		return lp.processLogs();
	}
	
	/**
	 * Getter for the number of non-errors;
	 * 
	 * @return the number of non errors;
	 */
	public int getNumberOfNonErrors() {
		return lp.getNumberOfNonErrors();
	}
	
	/**
	 * Getter for the number of unique faults.
	 * 
	 * @return the number of non errors;
	 */
	public int getNumberOfUniqueFaults() {
		return lp.getNumberOfUniqueFaults();
	}	
}
