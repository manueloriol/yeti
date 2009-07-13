package yeti.environments.java;


import yeti.ImpossibleToMakeConstructorException;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiRoutine;
import yeti.YetiStrategy;
import yeti.environments.YetiTestManager;

/**
 * Class that represents a test manager for Java
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaTestManager extends YetiTestManager {

	/**
	 * Class that represents a thread that makes the next method call.
	 * 
	 * @author Manuel Oriol (manuel@cs.york.ac.uk)
	 * @date Jul 9, 2009
	 *
	 */
	private static class CallerThread extends Thread{


		/**
		 * The strategy used to pick the next method call and the instances.
		 */
		private YetiStrategy strategy = null;


		/**
		 * The module containing the routines to test.
		 */
		private YetiModule mod = null;


		private Object callLock;

		/**
		 * Simple constructor.
		 * 
		 * @param strategy the strategy to use.
		 * @param mod the module to test.
		 */
		public CallerThread() {
			super();
		}


		/**
		 * Gets the YetiStrategy for the next method call.
		 * 
		 * @return the strategy previously set.
		 */
		@SuppressWarnings("unused")
		public YetiStrategy getStrategy() {
			return strategy;
		}


		/**
		 * Sets the YetiStrategy for the next method call.
		 * 
		 * @param strategy the new strategy.
		 */
		@SuppressWarnings("unused")
		public void setStrategy(YetiStrategy strategy) {
			this.strategy = strategy;
		}

		/**
		 * Sets both Module and strategy.
		 * 
		 * @param strategy the new strategy.
		 * @param mod the YetiModule to set.
		 */
		public void setModAndStrategyAndCallLock(YetiModule mod, YetiStrategy strategy, Object callLock) {
			this.strategy = strategy;
			this.mod = mod;
			this.callLock=callLock;
		}


		/**
		 * Gets the YetiModule to use to make the call.
		 * 
		 * @return the YetiModule used to pick the next method to test.
		 */
		@SuppressWarnings("unused")
		public YetiModule getMod() {
			return mod;
		}


		/**
		 * Sets the YetiModule for picking the next method to call.
		 * 
		 * @param mod the YetiModule to set.
		 */
		@SuppressWarnings("unused")
		public void setMod(YetiModule mod) {
			this.mod = mod;
		}

		/**
		 * Used to know whether the thread is waiting for incoming tasks.
		 */
		boolean isWaiting = false;

		/**
		 * Used to know whether the thread has started or not.
		 */
		boolean hasStarted = false;



		/**
		 * This object is used to synchronize with the manager.
		 * Originally the synchronization was made on the CallerThread but
		 * stop() waits for the lock to be released on the thread itself!!!!
		 * 
		 *  Real issue: this is not documented in SUN's API.
		 */
		public Object thisLock = new Object();


		private long lastCallNumber;



		/* (non-Javadoc)
		 * 
		 * Simple caller for running the call in a separate thread.
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			lastCallNumber = 0;

			// we basically have a double handshake
			try {
				// beginning of the double handshake
				synchronized (thisLock) {
					hasStarted=true;

					// it takes requests indefinitely until it is interrupted
					while (true) {
						// we print the logs
						YetiLog.printDebugLog("Worker thread will wait on the lock", this);
						// we wait
						thisLock.wait();
						YetiLog.printDebugLog("Worker thread has waited the lock", this);

						// we pick the routine
						YetiRoutine r = strategy.getNextRoutine(mod);

						// if there is a routine...
						if (r != null) {
							try {
								// we make the actual call
								r.makeCall(strategy.getAllCards(r));
							} catch (ImpossibleToMakeConstructorException e) {
								// Ignore calls that do not allow to make new instances
								//e.printStackTrace();
							}
							lastCallNumber++;
						}
						YetiLog.printDebugLog("Worker thread will notify testmanager",this);
						// we wake the main thread up
						synchronized(callLock) {
							callLock.notifyAll();
						}
					}
				}
			} catch (InterruptedException e) {
				// Normal thread stop
				// triggered by the stopTesting
			}
		}

	}

	/**
	 * The current caller thread. 
	 */
	private CallerThread ct;

	/**
	 * Number of worker threads started by the infrastructure
	 */
	public static long nThreadsStarted = 0;

	/**
	 * Number of worker threads stopped by the infrastructure
	 */
	public static long nThreadsStopped = 0;

	/* (non-Javadoc)
	 * 
	 * We make the call.
	 * 
	 * @see yeti.environments.YetiTestManager#makeNextCall(yeti.YetiModule, yeti.YetiStrategy)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void makeNextCall(YetiModule mod, YetiStrategy strategy) {

		Object callLock = new Object();

		// we start the double handshake
		YetiLog.printDebugLog("Main thread synchronizes on the test manager",this);
		synchronized(callLock) {

			// we get the time for the timeout
			long timeout = getTimeoutInMilliseconds();

			// if this is the first time we make a call, we actually
			// create the worker thread
			if (ct==null || !ct.isAlive()) {
				YetiLog.printDebugLog("Restarting Worker thread",this);
				ct=new CallerThread();
				ct.start();
				nThreadsStarted++;
			}


			// we wait until the worker thread has started
			while(!ct.hasStarted) {
				try {
					YetiLog.printDebugLog("Waiting for the thread to start",this);
					// Default value 250 nanoseconds
					Thread.sleep(0,250);
				} catch (InterruptedException e) {
					// should never happen
					e.printStackTrace();
				}
			}

			long callNumber;
			synchronized (ct.thisLock) {
				// we set the module and the strategy
				ct.setModAndStrategyAndCallLock(mod, strategy, callLock);

				// we wake up the worker thread
				YetiLog.printDebugLog("Main thread has synchronized on the lock",this);
				ct.thisLock.notifyAll();
				callNumber=ct.lastCallNumber;
			}

			// we wait until the timeout 
			try {
				YetiLog.printDebugLog("Waiting with timeout: "+timeout,this);
				callLock.wait(timeout);
			} catch (InterruptedException e) {
				// should never happen
				YetiLog.printDebugLog("Main thread Interrupted", this);

				//e.printStackTrace();
			}
			// if the call has not done another call and the thread is alive
			// we stop it!
			if (ct.isAlive()&&(ct.lastCallNumber==callNumber)) {
				try {
					YetiLog.printDebugLog("Stopping the Worker Thread", this);
					ct.interrupt();
					Thread.sleep(0,250);
					ct.stop();
					nThreadsStopped++;
				} catch (Throwable t) {
					// Should not really matter here.
					//t.printStackTrace();
				}
				ct=null;

			}
		}

	}



	/* (non-Javadoc)
	 * Will stop the caller thread.
	 * 
	 * @see yeti.environments.YetiTestManager#stopTesting()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void stopTesting() {

		// we offset the timeout time.
		long timeout = getTimeoutInMilliseconds()/10;
		if (timeout>50L) {
			timeout=50L;
		}
		// we wait for the timeout time
		// then we interrupt the thread if it is waiting 
		//(which it should be doing even if it was just restarted)
		try {
			Thread.sleep(timeout);
			if (ct.isWaiting) {
				ct.interrupt();
				Thread.sleep(0,250);
			}
			// we stop the thread anyway!
			ct.stop();
			nThreadsStopped++;
		} catch (Throwable e) {
			// Should never happen
			// e.printStackTrace();
		}

		YetiLog.printDebugLog(" nThreadStarted: "+nThreadsStarted+" nThreadStopped: "+nThreadsStopped, this, false);
	}


}
