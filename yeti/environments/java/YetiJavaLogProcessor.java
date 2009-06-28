package yeti.environments.java;

import java.util.Date;
import java.util.Vector;

import yeti.YetiLog;
import yeti.YetiLogProcessor;

/**
 * Class that represents a log processor for Java. 
 * <code>processLog</code> generates test cases in each cell of the array.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jun 22, 2009
 *
 */
public class YetiJavaLogProcessor extends YetiLogProcessor {

	/**
	 * A constructor for the YetiJavaLogProcessor.
	 */
	public YetiJavaLogProcessor() {	
		
	}

	/* (non-Javadoc)
	 * Adds a timestamp on the log
	 * 
	 * @see yeti.YetiLogProcessor#appendToCurrentLog(java.lang.String)
	 */
	@Override
	public void appendToCurrentLog(String newLog) {
		// substantific gains (2-3x) in execution time can be done by NOT adding the timestamp
		// super.appendToCurrentLog(newLog);
		super.appendToCurrentLog(newLog+" // time:"+(new Date()).getTime());
	}

	/**
	 * Generates a Vector<String> that a test case for each cell.
	 * 
	 * @see yeti.YetiLogProcessor#processLogs()
	 */
	@Override
	public Vector<String> processLogs() {		
		Vector<String> tmp = YetiJavaLogProcessor.sliceStatically(this.getCurrentLog());
		Vector<String> result = new Vector<String>();
		int i = 0;
		for (String tc: tmp) {
			i++;
			result.add("public static void test_"+i+"() {\n"+tc+"\n}");
		}
		
		return result;
	}
	
	/**
	 * Generates the kill value for this line.
	 * 
	 * @param loc the line of code to treat.
	 * @return the String value for the variable to kill.
	 */
	public static String kill(String loc){
		boolean isAssignment = (loc.indexOf("=")>0);
		int indexOfSpace = loc.indexOf(" ");
		

		YetiLog.printDebugLog("loc: "+loc, YetiJavaLogProcessor.class);
		if (isAssignment){
			YetiLog.printDebugLog("kill: "+ loc.substring(indexOfSpace+1,loc.indexOf("=")), YetiJavaLogProcessor.class);
			return loc.substring(indexOfSpace+1,loc.indexOf("="));
		}else {
			YetiLog.printDebugLog("no kill", YetiJavaLogProcessor.class);
			return null;
		}
	}
	
	/**
	 * Generates the vector of variables that are used by this line of code.
	 * 
	 * @param loc the line of code to treat.
	 * @return a vector containing all the variables that should be added to the 
	 * list of values that matter.
	 */
	public static Vector<String> gen(String loc){
		
		boolean isAssignment = (loc.indexOf("=")>0);
		boolean isCreation = (loc.indexOf("new ")>0);
		boolean isMethodCall = (loc.indexOf("(")>0);
		boolean isComment = loc.startsWith("/**");

		// if this is a comment we return no gen
		if (isComment)
			return new Vector<String>();
		
		// we initialize the values
		String localLoc = loc;
		Vector<String> valuesThatMatter = new Vector<String>();
		YetiLog.printDebugLog("loc: "+loc, YetiJavaLogProcessor.class);
		
		// if it is not a creation method but it is a method call
		if (!isCreation&&isMethodCall) {
			String target;
			// we find the target
			if (isAssignment)
				target = loc.substring(loc.indexOf("=")+1,loc.lastIndexOf('.'));
			else
				target = loc.substring(0,loc.lastIndexOf('.'));
			YetiLog.printDebugLog("target: "+target, YetiJavaLogProcessor.class);
			
			// we add it to the values that matter
			valuesThatMatter.add(target);
		}
		// for all method calls we extract arguments
		if (isMethodCall) {
			int indexOfAfterOpenParenthesis = loc.indexOf("(")+1;
			int indexOfCloseParenthesis = loc.indexOf(")");
			localLoc = localLoc.substring(indexOfAfterOpenParenthesis, indexOfCloseParenthesis);

			// we add all arguments one after he other
			for (String var: localLoc.split(",")){
				YetiLog.printDebugLog("arg: "+var, YetiJavaLogProcessor.class);
				valuesThatMatter.add(var);
			}
		}
		// we return the result
		return valuesThatMatter;
	}
	
	/**
	 * Checks whether the line contains kills or gen that matter for the variables
	 * 
	 * @param loc the line of code to consider
	 * @param varNames the variable names
	 * @return <code>true</code> it it contains a gen or a kill, <code>false</code> otherwise.
	 */
	public static boolean containsKillsOrGens(String loc, Vector<String> varNames){
		Vector<String> gen0 = gen(loc);
		String kill0=kill(loc);
		
		// we iterate through all names
		for (String var: varNames) {
			if (kill0!=null)
				if (kill0.equals(var)) return true;
			for (String geni: gen0) {
				if (geni.equals(var)) return true;
			}
			
		}
		return false;
		
	}
	
	/**
	 * Slices the code of the test case statically and conervatively.
	 * 
	 * Does not make any assumption on command-query separation.
	 * 
	 * @param log the log to slice
	 * @return a vector with all generated test cases.
	 */
	public static Vector<String> sliceStatically(String log){
		Vector<String> testCases = new Vector<String>();
		
		// we split the lines of code
		String []linesOfTest = log.split("\n");
		
		// we make the list of errors
		Vector<Integer> listOfErrors= new Vector<Integer>();
		// we look for all errors up
		for (int i = 0; i<linesOfTest.length; i++){
			if (linesOfTest[i].startsWith("/**BUG FOUND:")){
				listOfErrors.add(i-1);
			}
		}
		
		// for each error:
		for(int i: listOfErrors){
			int finalLength = 0;
			String currentTestCase = linesOfTest[i]+"\n"+linesOfTest[i+1];
			Vector<String> variables = gen(linesOfTest[i]);
			boolean ignoreNext = false;
			// for all lines previously executed:
			for (int j = i-1; j>=0 ; j--){
				// if there is an error, we the call
				if (ignoreNext) {
					ignoreNext = false;
					continue;
				}
				if (linesOfTest[j].startsWith("/**BUG FOUND:")) {
					ignoreNext=true;
					continue;
				}
				
				// if there is no active variable we stop here
				if (variables.isEmpty()) break;
				
				// if the line contains meaningful kills or gen 
				// then we include it in the trace
				// Note we cannot take an aggressive stance on command-query 
				// separation with Java
				if (containsKillsOrGens(linesOfTest[j], variables)) {
					String kill0 = kill(linesOfTest[j]);
					// we remove the kill
					if (kill0!=null)
						for (int k=0;k<variables.size();k++){
							if (variables.get(k).equals(kill0)){
								variables.remove(k--);
							}
						}
					// we add the gens
					variables.addAll(gen(linesOfTest[j]));
					// we add the line to the test case
					currentTestCase = linesOfTest[j] +"\n"+ currentTestCase;
					finalLength++;
				}
			}
			// we aggregate the results and give some output
			currentTestCase=currentTestCase+"\n/** original locs: "+i+" minimal locs: "+finalLength+"**/";
			testCases.add(currentTestCase);
		}
		
		YetiLog.printDebugLog("Number of Errors: "+listOfErrors.size()+" Number of test cases: "+testCases.size(), YetiJavaLogProcessor.class);
		//testCases.add("Number of Errors: "+listOfErrors.size()+" Number of test cases: "+testCases.size());
		
		return testCases;
		
	}
	

}
