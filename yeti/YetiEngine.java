package yeti;

import java.util.Date;

import yeti.environments.YetiTestManager;

/**
 * This class is here to manage how the tests are run.
 * It contains several methods that have have several ways of stopping testing. 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiEngine {
	
		
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
			// we generate the end time using a long representation
			long endTime = new Date().getTime()+minutes*60*1000;
			
			// at each iteration we test the current time
			while (new Date().getTime()<endTime){
				manager.makeNextCall(mod, strategy);
			}

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
			// we generate the end time using a long representation
			long endTime = new Date().getTime()+seconds*1000;
			
			// at each iteration we test the current time
			while (new Date().getTime()<endTime){
				manager.makeNextCall(mod, strategy);
			}

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
			
			// we simplty iterate through the calls
			while (nTests-->0){
				manager.makeNextCall(mod, strategy);
			}
		}


		/**
		 * Getter for the manager.
		 * 
		 * @return the manager of this engine
		 */
		public YetiTestManager getManager() {
			return manager;
		}


		/**
		 * Setter for the manager.
		 * 
		 * @param manager the manager to set.
		 */
		public void setManager(YetiTestManager manager) {
			this.manager = manager;
		}


		/**
		 * Getter for the strategy.
		 * 
		 * @return the strategy.
		 */
		public YetiStrategy getStrategy() {
			return strategy;
		}


		/**
		 * Setter for the strategy.
		 * 
		 * @param strategy the strategy to set.
		 */
		public void setStrategy(YetiStrategy strategy) {
			this.strategy = strategy;
		}

}
