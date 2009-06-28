package yeti;


import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiTestManager;
import yeti.environments.java.YetiJavaLogProcessor;
import yeti.environments.java.YetiJavaProperties;
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
	 * Main method of Yeti. Arguments are numerous. Here is a list of the current ones:
	 * 
	 * -java : for calling it on Java
	 * -time=Xs / -time=Xmn : for calling Yeti for a given amount of time (X can be minutes or seconds, e.g. 2mn | 3s )
	 * -nTest=X : for calling Yeti to attempt X method calls
	 * -testModules=M1:M2:...:Mn : for testing one or several modules
	 * 
	 * @param args the arguments of the program
	 */
	//TODO: implement more than -java
	public static void main(String[] args) {
		
		YetiEngine engine;
		boolean isJava = false;
		boolean isTimeout = false;
		int timeOutSec=0;
		int timeOutMin=0;
		boolean isNTests = false;
		
		
		
		//test of options to set up the YetiProperties
		if (args[0].equals("-java")) {
			pl=new YetiJavaProperties();
		};
		
		// initializing Yeti
		try {
			pl.getInitializer().initialize(args);
		} catch (YetiInitializationException e) {
			//should never happen
			e.printStackTrace();
		}
		
		// create a YetiTestManager and 
		YetiTestManager testManager = pl.getTestManager(); 
		strategy= new YetiRandomStrategy(testManager);

		// getting the module(s) to test
		YetiModule mod=YetiModule.allModules.get("yeti.test.YetiTest");
		//YetiModule mod=YetiModule.allModules.get("java.lang.String");
		
		// creating the engine object
		engine= new YetiEngine(strategy,testManager);
		
		// Creating the log processor
		YetiLog.proc=new YetiJavaLogProcessor();
		
		// depending of the options launch the testing
		engine.testModuleForNumberOfTests(mod, 50);
		//engine.testModuleForNMinutes(mod, 1);
		//engine.testModuleForNSeconds(mod, 1);
		
		// presents the logs
		for (String log: YetiLog.proc.processLogs()) {
			System.out.println(log);
		}
		
		
		
	}

}
