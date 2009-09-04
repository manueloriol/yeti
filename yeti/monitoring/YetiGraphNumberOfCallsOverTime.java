package yeti.monitoring;

import java.util.Date;

import javax.swing.JFrame;

import yeti.YetiLog;
import yeti.YetiLogProcessor;

/**
 * Class that allows the wrapping of a YetiLogProcessor and shows the number of calls over time.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 27, 2009
 *
 */
public class YetiGraphNumberOfCallsOverTime extends YetiGraph {

	/**
	 * The log processor associated with this component
	 */
	public YetiLogProcessor lp=null;

	/**
	 * The number of ms between updates.
	 */
	public long nMSBetweenUpdates = 0;
	
	/**
	 * A constructor for this GUI.
	 * 
	 * @param lp the processor that we wrap inside it.
	 * @param nMilliseconds number of milliseconds second between failures.
	 */
	public YetiGraphNumberOfCallsOverTime(YetiLogProcessor lp, long nMSBetweenUpdates) {
		super("Number of Calls, steps of "+nMSBetweenUpdates+" milliseconds");
		this.nMSBetweenUpdates=nMSBetweenUpdates;
		this.lp=lp;
	}


	/**
	 * true if this GUI has been initialized.
	 */
	public boolean called = false;

	/**
	 * The value of the last time that was shown.
	 */
	public long lastInstantShown = 0;
	
	/**
	 * the value of the starting time.
	 */
	public long firstInstant = 0;

	/* (non-Javadoc)
	 * Taking a sample of the 
	 * 
	 * @see yeti.monitoring.YetiSamplable#sample()
	 */
	public void sample() {
		// if this is the first time we call it
		if (!called) {
			// we start by setting the value of the first second
			firstInstant = (long) (new Date().getTime());
			called = true;
			// and add the value
			addValue(0,YetiLog.numberOfCalls);
		} else {
			// otherwise, we remove the offset and add the offsetted value
			lastInstantShown = new Date().getTime();
			addValue(((double)(lastInstantShown-firstInstant))/nMSBetweenUpdates,YetiLog.numberOfCalls);				

		}
	}	

}
