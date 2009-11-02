package yeti;

import java.util.Date;

import yeti.environments.YetiTestManager;

/**
 * This class is here to manage how the tests are run. It contains several methods that have have several ways of stopping testing. 
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Jun 22, 2009
 */
public class YetiEngine {
	
		/**
		 * A progress value between 1 and 100.
		 */
		int progress = 0;
	
		/**
		 * Simple getter for the progress.
		 * 
		 * @return the value of the progress variable (between 0 and 100).
		 */
		public int getProgress() {
			return progress;
		}


		/**
		 * A simple setter for the progress variable.
		 * 
		 * @param progress the value of the progress variable.
		 */
		public void setProgress(int progress) {
			this.progress = progress;
			YetiLog.printDebugLog("YETI Testing session: "+progress+"%", this);
		}


		/**
		 * The strategy that will be used
		 */
		protected YetiStrategy strategy;
		
		/**
		 * The manager to use for making calls
		 */
		protected YetiTestManager manager;
		
		
		/**
		 * A constructor for the engine.
		 * Simply assigns the two parameters.
		 * 
		 * @param strategy the strategy to use for this testing session
		 * @param manager the manager for making calls
		 */
		public YetiEngine(YetiStrategy strategy, YetiTestManager manager){
			this.strategy=strategy;
			this.manager=manager;
		}
		
	
		/**
		 * Method allowing to test a module for a number of minutes.
		 * 
		 * Performance: on the Java implementation, 1 mn generate hundreds of thousands of method calls.
		 * 
		 * @param mod the module to test
		 * @param minutes the number of minutes to test it
		 */
		public void testModuleForNMinutes(YetiModule mod, int minutes){
			this.testModuleForNSeconds(mod, 60*minutes);
		}

		/**
		 * Method allowing to test a module for a number of seconds.
		 * 
		 * Performance: on the Java implementation, 10 seconds generate more than a hundred thousand method calls.
		 * 
		 * @param mod the module to test
		 * @param seconds the number of seconds to test it
		 */
		public void testModuleForNSeconds(YetiModule mod, int seconds){
			// we take the start time
			long startTime = new Date().getTime();
			// we generate the end time using a long representation
			long endTime = startTime+seconds*1000;
			
			// steps
			long step = (endTime-startTime)/100L;
			
			// the last step
			long lastStep = startTime;
			
			// the current time
			long currentTime = startTime;
			
			// at each iteration we test the current time
			while (currentTime<endTime){
				if (currentTime>lastStep+step) {
					this.setProgress((int)((100L*(currentTime-startTime))/(endTime-startTime)));
					lastStep = lastStep+step;
				}
				manager.makeNextCall(mod, strategy);
				currentTime=new Date().getTime();
			}
			this.setProgress(100);
			manager.stopTesting();
		}

		
		/**
		 * Method allowing to test a module for a number of tests.
		 * 
		 * Performance: on the Java implementation, 500 tests is almost immediate.

		 * @param mod the module to test
		 * @param number the number of tests to make
		 */
		public void testModuleForNumberOfTests(YetiModule mod, int number){
			int nTests=number;
			
			// steps
			long step = number/100;
			
			// the last step
			long lastStep = 0;
	
			// we simplty iterate through the calls
			while (nTests-->0){
				if ((number-nTests)>(lastStep+step)) {
					this.setProgress(((number-nTests)*100)/number);
					lastStep = lastStep+step;
				}
				manager.makeNextCall(mod, strategy);
			}
			this.setProgress(100);
			manager.stopTesting();
		}


		/**
		 * Getter for the manager.
		 * @return  the manager of this engine
		 */
		public YetiTestManager getManager() {
			return manager;
		}


		/**
		 * Setter for the manager.
		 * @param manager  the manager to set.
		 */
		public void setManager(YetiTestManager manager) {
			this.manager = manager;
		}


		/**
		 * Getter for the strategy.
		 * @return  the strategy.
		 */
		public YetiStrategy getStrategy() {
			return strategy;
		}


		/**
		 * Setter for the strategy.
		 * @param strategy  the strategy to set.
		 */
		public void setStrategy(YetiStrategy strategy) {
			this.strategy = strategy;
		}

}
