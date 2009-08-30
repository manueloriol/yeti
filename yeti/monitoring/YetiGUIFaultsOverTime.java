package yeti.monitoring;

import java.util.Date;
import javax.swing.JFrame;
import yeti.YetiLogProcessor;

/**
 * Class that allows the wrapping of a YetiLogProcessor and shows the number of faults over time.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 27, 2009
 *
 */
public class YetiGUIFaultsOverTime extends YetiMonitoringLogProcessor{


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
	public YetiGUIFaultsOverTime(YetiLogProcessor lp, int nMilliseconds) {
		this.nMilliseconds = nMilliseconds;
		this.lp=lp;
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graph = new YetiGraph("Number of Relevant Failures, steps of "+nMilliseconds+" millisecond");
		f.add(graph);
		f.setSize(400,200);
		f.setLocation(200,200);
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
			graph.addValue(firstInstant,lp.getListOfErrorsSize()-getNumberOfNonErrors());
		} else {
			// otherwise, we remove the offset and add the offsetted value
			int currentSecond = (int) (new Date().getTime()/nMilliseconds);
			if (lastInstantShown+1<currentSecond) {
				graph.addValue(currentSecond-firstInstant,lp.getListOfErrorsSize()-getNumberOfNonErrors());				
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
			graph.addValue(firstInstant,lp.getListOfErrorsSize()-getNumberOfNonErrors());
		} else {
			// otherwise, we remove the offset and add the offsetted value
			int currentSecond = (int) (new Date().getTime()/nMilliseconds);
			if (lastInstantShown+1<currentSecond) {
				graph.addValue(currentSecond-firstInstant,lp.getListOfErrorsSize()-getNumberOfNonErrors());				
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
			graph.addValue(firstInstant,lp.getListOfErrorsSize()-getNumberOfNonErrors());
		} else {
			// otherwise, we remove the offset and add the offsetted value
			int currentSecond = (int) (new Date().getTime()/nMilliseconds);
			if (lastInstantShown+1<currentSecond) {
				graph.addValue(currentSecond-firstInstant,lp.getListOfErrorsSize()-getNumberOfNonErrors());				
				lastInstantShown=currentSecond;
			}
		}	
	}

}
