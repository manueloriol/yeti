package yeti.monitoring;

import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;

import yeti.YetiLog;
import yeti.YetiLogProcessor;

/**
 * Class that allows the wrapping of a YetiLogProcessor and shows the number of failures over time.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 27, 2009
 *
 */
public class YetiGUINumberOfFailuresOverTime extends YetiLogProcessor {

	/**
	 * The processor taht we wrap here.
	 */
	public YetiLogProcessor lp;

	/**
	 * The graph in which we show the values
	 */
	public YetiGraph graph;
	
	/**
	 * The step at which we consider adding a value for the sampling
	 */
	public int nMilliseconds;
	
	/**
	 * A constructor for this GUI.
	 * 
	 * @param lp the processor that we wrap inside it.
	 * @param nMilliseconds number of milliseconds second between failures.
	 */
	public YetiGUINumberOfFailuresOverTime(YetiLogProcessor lp, int nMilliseconds) {
		this.nMilliseconds = nMilliseconds;
		this.lp=lp;
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graph = new YetiGraph("Number of Failures, steps of "+nMilliseconds+" millisecond");
		f.add(graph);
		f.setSize(400,200);
		f.setLocation(200,400);
		f.setVisible(true);
	}

	
	/**
	 * true if this GUI has been initialized.
	 */
	public boolean called = false;

	/**
	 * The value of the last time that was shown.
	 */
	public int lastInstantShown = 0;
	public int firstInstant = 0;

	
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
		// if this is the first time we call it
		if (!called) {
			// we start by setting the value of the first second
			lastInstantShown = (int) (new Date().getTime()/nMilliseconds);
			firstInstant = lastInstantShown;
			called = true;
			// and add the value
			graph.addValue(firstInstant,YetiLog.numberOfErrors);
		} else {
			// otherwise, we remove the offset and add the offsetted value
			int currentSecond = (int) (new Date().getTime()/nMilliseconds);
			if (lastInstantShown+1<currentSecond) {
				graph.addValue(currentSecond-firstInstant,YetiLog.numberOfErrors);				
				lastInstantShown=currentSecond;
			}
		}
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
		// if this is the first time we call it
		if (!called) {
			// we start by setting the value of the first second
			lastInstantShown = (int) (new Date().getTime()/nMilliseconds);
			firstInstant = lastInstantShown;
			called = true;
			// and add the value
			graph.addValue(firstInstant,YetiLog.numberOfErrors);
		} else {
			// otherwise, we remove the offset and add the offsetted value
			int currentSecond = (int) (new Date().getTime()/nMilliseconds);
			if (lastInstantShown+1<currentSecond) {
				graph.addValue(currentSecond-firstInstant,YetiLog.numberOfErrors);				
				lastInstantShown=currentSecond;
			}
		}
	}

	/*
	 * The call is wrapped. 
	 * 
	 * @see yeti.YetiLogProcessor#printMessageRawLogs(java.lang.String)
	 */
	@Override
	public void printMessageRawLogs(String message) {
		lp.printMessageRawLogs(message);
		// if this is the first time we call it
		if (!called) {
			// we start by setting the value of the first second
			lastInstantShown = (int) (new Date().getTime()/nMilliseconds);
			firstInstant = lastInstantShown;
			called = true;
			// and add the value
			graph.addValue(firstInstant,YetiLog.numberOfErrors);
		} else {
			// otherwise, we remove the offset and add the offsetted value
			int currentSecond = (int) (new Date().getTime()/nMilliseconds);
			if (lastInstantShown+1<currentSecond) {
				graph.addValue(currentSecond-firstInstant,YetiLog.numberOfErrors);				
				lastInstantShown=currentSecond;
			}
		}
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
	 * Returns the wrapped call
	 * 
	 * @see yeti.YetiLogProcessor#processLogs()
	 */
	@Override
	public Vector<String> processLogs() {
		return lp.processLogs();
	}

}
