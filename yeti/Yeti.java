package yeti;


import java.util.ArrayList;
import java.util.Date;

import yeti.environments.YetiInitializer;
import yeti.environments.YetiPrefetchingLoader;
import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiTestManager;
import yeti.environments.java.YetiJavaInitializer;
import yeti.environments.java.YetiJavaLogProcessor;
import yeti.environments.java.YetiJavaPrefetchingLoader;
import yeti.environments.java.YetiJavaProperties;
import yeti.environments.java.YetiJavaTestManager;
import yeti.environments.jml.YetiJMLInitializer;
import yeti.environments.jml.YetiJMLPrefetchingLoader;
import yeti.monitoring.YetiGUIFaultsOverTime;
import yeti.monitoring.YetiGUINumberOfCallsOverTime;
import yeti.monitoring.YetiGUINumberOfFailuresOverTime;
import yeti.monitoring.YetiGUINumberOfVariablesOverTime;
import yeti.strategies.YetiRandomPlusStrategy;
import yeti.strategies.YetiRandomStrategy;

/**
 * Class that represents the main launching class of Yeti
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 */
public class Yeti {

	/**
	 * The properties of the programming language.
	 */
	public static YetiProgrammingLanguageProperties pl;
	
	/**
	 * The strategy being used.
	 */
	public static YetiStrategy strategy = null;

	/**
	 * The tested modules.
	 */
	public static YetiModule testModule = null;

	
	/**
	 * Stores the path to use for testing.
	 */
	public static String yetiPath = System.getProperty("java.class.path");

	/**
	 * Main method of Yeti. Arguments are numerous. Here is a list of the current ones:
	 * 
	 * -java, -Java : for calling it on Java.
	 * -time=Xs, -time=Xmn : for calling Yeti for a given amount of time (X can be minutes or seconds, e.g. 2mn or 3s ).
	 * -nTests=X : for calling Yeti to attempt X method calls.
	 * -testModules=M1:M2:...:Mn : for testing one or several modules.
	 * -help, -h: prints the help out.
	 * -rawlogs : prints the logs directly instead of processing them at the end. 
	 * -nologs : does not print logs, only the final result.
	 * -msCalltimeout=X : sets the timeout (in milliseconds) for a method call to X. Note that too 
	 * low values may result in blocking Yeti (use at least 30ms for good performances).
	 * -yetiPath=X : stores the path that contains the code to test (e.g. for Java the classpath to consider)
	 * -newInstanceInjectionProbability=X : probability to inject new instances at each call (if relevant). Value between 0 and 100. 
	 * -probabilityToUseNullValue=X : probability to use a null instance at each variable (if relevant). Value between 0 and 100 default is 1.
	 * -randomPlus : uses the random+ strategy that injects interesting values every now and then.
	 * -gui : shows the standard graphical user interface for monitoring yeti.
	 * 
	 * @param args the arguments of the program
	 */
	public static void main(String[] args) {
		
		YetiEngine engine;
		boolean isJava = false;
		boolean isJML = false;
		boolean isTimeout = false;
		int timeOutSec=0;
		boolean isNTests = false;
		boolean isRawLog = false;
		boolean isNoLogs = false;
		boolean isRandomPlus = false;
		boolean showMonitoringGui = false;
		int nTests=0;
		String []modulesToTest=null;
		int callsTimeOut=75;
		
		
		// we parse all arguments of the program
		for (String s0: args) {
			// if it is printing help
			if (s0.equals("-help")||s0.equals("-h")) {
				Yeti.printHelp();
				return;
			}
			// if Java
			if (s0.equals("-java")||s0.equals("-Java")) {
				isJava = true;
				continue;
			}
			// if JML
			//TODO somebody could also set -java
			if (s0.toLowerCase().equals("-jml")) {
				isJML = true;
				continue;
			}
			
			// if testing for time value
			if (s0.startsWith("-time=")) {
				isTimeout=true;
				int size = s0.length();
				// if the time value is in seconds
				if (s0.substring(size-1).equals("s")) {
					timeOutSec=(Integer.parseInt(s0.substring(6, size-1)));
					continue;
				}
				// if the time value is in minutes
				if (s0.substring(size-2).equals("mn")) {
					timeOutSec=60*(Integer.parseInt(s0.substring(6, size-2)));
					continue;	
				}				
			}
			// if testing for time value
			if (s0.startsWith("-msCalltimeout=")) {
				int size = s0.length();
				// if the time value is in seconds
				callsTimeOut=(Integer.parseInt(s0.substring(15, size)));
				if (callsTimeOut<=0) {
					Yeti.printHelp();
					return;
				}
				continue;
			}
			// if testing for new instance injection probability
			if (s0.startsWith("-newInstanceInjectionProbability=")) {
				int size = s0.length();
				// if the time value is in seconds
				YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY=(Integer.parseInt(s0.substring(33, size)))/100d;
				if ((YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY>1.0)||(YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY<0)) {
					Yeti.printHelp();
					return;
				}
				continue;
			}

			// if testing for new instance injection probability
			if (s0.startsWith("-probabilityToUseNullValue=")) {
				int size = s0.length();
				// if the time value is in seconds
				YetiVariable.PROBABILITY_TO_USE_NULL_VALUE=(Integer.parseInt(s0.substring(27, size)))/100d;
				if ((YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>1.0)||(YetiVariable.PROBABILITY_TO_USE_NULL_VALUE<0)) {
					Yeti.printHelp();
					return;
				}
				continue;
			}

			// if it is for a number of tests
			if (s0.startsWith("-nTests=")) {
				isNTests=true;
				nTests=(Integer.parseInt(s0.substring(8)));
				continue;
			}
			// we want to test these modules
			if (s0.startsWith("-testModules=")) {
				String s1=s0.substring(13);
				modulesToTest=s1.split(":");
				continue;
			}
			// we want to have only logs in standard form
			if (s0.equals("-rawlogs")) {
				isRawLog = true;
				continue;	
			}

			// we want to have only logs in standard form
			if (s0.equals("-nologs")) {
				isNoLogs = true;
				isRawLog = true;
				continue;	
			}

			// we want to have only logs in standard form
			if (s0.equals("-gui")) {
				showMonitoringGui = true;
				continue;	
			}

			// we want to use the following path
			if (s0.startsWith("-yetiPath=")) {
				String s1=s0.substring(10);
				Yeti.yetiPath = s1;
				System.setProperty("java.class.path", System.getProperty("java.class.path")+":"+s1);
				continue;
			}

			// we can use the randomPlus strategy
			if (s0.equals("-randomPlus")) {
				isRandomPlus = true;
				continue;	
			}

			
			System.out.println("Yeti could not understand option: "+s0);
			Yeti.printHelp();
			return;

		}
		
		//test of options to set up the YetiProperties for Java
		if (isJava) {
			YetiPrefetchingLoader prefetchingLoader = new YetiJavaPrefetchingLoader(yetiPath);
			YetiInitializer initializer = new YetiJavaInitializer(prefetchingLoader);
			YetiTestManager testManager = new YetiJavaTestManager();
			YetiLogProcessor logProcessor = new YetiJavaLogProcessor();
			pl=new YetiJavaProperties(initializer, testManager, logProcessor);
		}
		
		//test of options to set up the YetiProperties for JML
		if (isJML) {
			YetiPrefetchingLoader prefetchingLoader = new YetiJMLPrefetchingLoader(yetiPath);
			YetiInitializer initializer = new YetiJMLInitializer(prefetchingLoader);
			YetiTestManager testManager = new YetiJavaTestManager();
			YetiLogProcessor logProcessor = new YetiJavaLogProcessor();
			pl=new YetiJavaProperties(initializer, testManager, logProcessor);
		}

		
		//if it is raw logs, then set it		
		if (isRawLog) {
			pl.setRawLog(isRawLog);
		}
		
		//if it is raw logs, then set it		
		if (isNoLogs) {
			pl.setNoLogs(isNoLogs);
		}

		// initializing Yeti
		try {
			pl.getInitializer().initialize(args);
		} catch (YetiInitializationException e) {
			//should never happen
			e.printStackTrace();
		}
		
		// create a YetiTestManager and 
		YetiTestManager testManager = pl.getTestManager(); 
	
		//sets the calls timeout
		if (!(callsTimeOut<=0)) {
			testManager.setTimeoutInMilliseconds(callsTimeOut);
		}
		
		// We set the strategy
		if (isRandomPlus)
			strategy= new YetiRandomStrategy(testManager);
		else
			strategy= new YetiRandomPlusStrategy(testManager);
			
			
		// getting the module(s) to test
		YetiModule mod=null;
		
		// if the modules to test is actually one module
		if (modulesToTest.length==1) {
			// we get the module
			mod=YetiModule.allModules.get(modulesToTest[0]);
			
			// if it does not exist we stop
			if(mod==null) {
				System.err.println(modulesToTest[0] + " was not found. Please check");
				System.err.println("Testing halted");
				printHelp();
				return;
			}
		} else {
			// if the modules to test are many
			ArrayList<YetiModule> modules=new ArrayList<YetiModule>(modulesToTest.length);
			// we iterate through the modules
			// if the module does not exist we omit it
			for(String moduleToTest : modulesToTest) {
				YetiModule yetiModuleToTest = YetiModule.allModules.get(moduleToTest);
				if(yetiModuleToTest==null) {
					System.err.println(moduleToTest + " was not found. Please check");
					System.err.println(moduleToTest + " is skipped from testing");
				} else {
					modules.add(yetiModuleToTest);
				}
			}
			// if none rests at the end, we stop the program
			if(modules.isEmpty()) {
				System.err.println("Testing halted");
				printHelp();
				return;
			}
			// otherwise, we combine all modules
			mod = YetiModule.combineModules(modules.toArray(new YetiModule[modules.size()]));
		}
		// we let everybody use the tested module
		Yeti.testModule = mod;
		
		// creating the engine object
		engine= new YetiEngine(strategy,testManager);
		
		// Creating the log processor
		if (showMonitoringGui) {
		YetiLog.proc=new YetiGUINumberOfVariablesOverTime(new YetiGUINumberOfFailuresOverTime(new YetiGUINumberOfCallsOverTime(new YetiGUIFaultsOverTime(pl.getLogProcessor(),100),100),100),100);
		} else {
			YetiLog.proc=pl.getLogProcessor();
		}
		
		// logging purposes:
		long startTestingTime = new Date().getTime();
		// depending of the options launch the testing
		if (isNTests)
			// if it is the number of states
			engine.testModuleForNumberOfTests(mod, nTests);
		else if (isTimeout) 
			// if it is according to a timeout
			engine.testModuleForNSeconds(mod, timeOutSec);
		else {
			printHelp();
			return;
		}
		// logging purposes:
		long endTestingTime = new Date().getTime();
	
		// for logging purposes
		if (isTimeout) {
			System.out.println("\n/** Testing Session finished, time: "+(endTestingTime-startTestingTime)+"ms **/");
		}

		boolean isProcessed = false;
		String aggregationProcessing = "";
		// presents the logs
		System.out.println("/** Testing Session finished, number of tests:"+YetiLog.numberOfCalls+", time: "+(endTestingTime-startTestingTime)+"ms , number of failures: "+YetiLog.numberOfErrors+"**/");
		if (!Yeti.pl.isRawLog()) {
			isProcessed = true;
			for (String log: YetiLog.proc.processLogs()) {
				System.out.println(log);
			}
			// logging purposes: (slightly wrong because of printing)
			long endProcessingTime = new Date().getTime();
			aggregationProcessing = "/** Processing time: "+(endProcessingTime-endTestingTime)+"ms **/";
		}
		if (!isProcessed) {
			YetiLogProcessor lp = (YetiLogProcessor)Yeti.pl.getLogProcessor();
			System.out.println("/** Unique relevant bugs: "+lp.listOfErrors.size()+" **/");			
		}
		if (isProcessed) {
			System.out.println("/** Testing Session finished, number of tests:"+YetiLog.numberOfCalls+", time: "+(endTestingTime-startTestingTime)+"ms , number of failures: "+YetiLog.numberOfErrors+"**/");
			System.out.println(aggregationProcessing);			
		}
		
	}
	
	/**
	 * This is a simple help printing utility function.
	 */
	public static void printHelp() {
		System.out.println("Yeti Usage:\n java yeti.Yeti [-java|-Java] [[-time=Xs|-time=Xmn]|[-nTests=X]][-testModules=M1:M2:...:Mn][-help|-h][-rawlog]");
		System.out.println("\t-java, -Java : for calling it on Java.");
		System.out.println("\t-jml, -JML : for calling it on JML annotated code.");
		System.out.println("\t-time=Xs, -time=Xmn : for calling Yeti for a given amount of time (X can be minutes or seconds, e.g. 2mn or 3s ).");
		System.out.println("\t-nTests=X : for calling Yeti to attempt X method calls.");
		System.out.println("\t-testModules=M1:M2:...:Mn : for testing one or several modules.");
		System.out.println("\t-help, -h: prints the help out.");
		System.out.println("\t-rawlogs: prints the logs directly instead of processing them at the end.");
		System.out.println("\t-nologs : does not print logs, only the final result.");
		System.out.println("\t-msCalltimeout=X : sets the timeout (in milliseconds) for a method call to X.Note that too low values may result in blocking Yeti (use at least 30ms for good performances)");
		System.out.println("\t-yetiPath=X : stores the path that contains the code to test (e.g. for Java the classpath to consider)");
		System.out.println("\t-newInstanceInjectionProbability=X : probability to inject new instances at each call (if relevant). Value between 0 and 100, default is 25.");
		System.out.println("\t-probabilityToUseNullValue=X : probability to use a null instance at each variable (if relevant). Value between 0 and 100, default is 1.");
		System.out.println("\t-randomPlus : uses the random+ strategy that injects interesting values every now and then.");
		System.out.println("\t-gui : shows the standard graphical user interface for monitoring yeti.");
	
	}

}
