package yeti.monitoring;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Class that represents a sampling class. 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Sep 4, 2009
 *
 */
public class YetiSampler implements Runnable {

	/**
	 * A vector of instances to sample.
	 */
	ArrayList<YetiSamplable> samplables = new ArrayList<YetiSamplable>();	

	/**
	 * A boolean variable to stop the thread in case it is not needed anymore.
	 */
	public boolean isToUpdate = true;
	
	/**
	 * A simple setter for is to update.
	 * 
	 * @param isToUpdate the new value. Put false to stop sampling.
	 */
	public void setToUpdate(boolean isToUpdate) {
		this.isToUpdate = isToUpdate;
	}

	/**
	 * The timeout between updates. 
	 */
	public long nMSBetweenUpdates;

	/**
	 * Add the samplable to the list of samplables.
	 * 
	 * @param s the samplable to add.
	 */
	public void addSamplable(YetiSamplable s) {
		samplables.add(s);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		long startSampledPoint = new Date().getTime();
		long currentSamplePoint = 0;
		long waitTime = 0;
		long i = 0;
		
		// sampling loop
		while (isToUpdate) {
			// we take samples on all samplables
			for (YetiSamplable u: samplables) {
				u.sample();
			}
			i++;
			// we take the current time
			currentSamplePoint=new Date().getTime();
			// the time to wait is calculated theoretically
			waitTime=(startSampledPoint+nMSBetweenUpdates*i)-currentSamplePoint;
			try {
				// we check that the time to wait is positive
				// otherwise, we iterate directly.
				if (waitTime>=0) {
					Thread.sleep(waitTime);
				}
			} catch (InterruptedException e) {
				// Should never happen
				// e.printStackTrace();
			}

		}
	}

	/**
	 * Simple constructor for the sampler.
	 * 
	 * @param nMSBetweenUpdates the number of milliseconds between two updates.
	 */
	public YetiSampler(long nMSBetweenUpdates) {
		super();
		this.nMSBetweenUpdates = nMSBetweenUpdates;
	}
	

}
