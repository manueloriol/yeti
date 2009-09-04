package yeti.monitoring;

/**
 * Class that represents a data that can be sampled.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Sep 4, 2009
 *
 */
public interface YetiSamplable {
	
	/**
	 * The method to sample the values.
	 */
	public void sample();
}
