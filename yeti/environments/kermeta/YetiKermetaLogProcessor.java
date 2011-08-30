package yeti.environments.kermeta;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Vector;

import yeti.Yeti;
import yeti.YetiLog;
import yeti.YetiLogProcessor;
import fr.irisa.triskell.kermeta.interpreter.KermetaRaisedException;

/**
 * 
 * LogProcessor for Kermeta. Used to store the traces of errors.
 * 
 * @author Erwan Bousse (erwan.bousse@gmail.com)
 * @date 28 juil. 2011
 *
 */
public class YetiKermetaLogProcessor extends YetiLogProcessor {

	@Override
	public Vector<String> processLogs() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	/**
	 * Store the trace in the log processor if relevant for the considered module.
	 * @param trace The trace to store.
	 * @return True if it's relevant, otherwise false.
	 */
	private boolean storeTraceIfRelevant(String trace, boolean printException) {
		// Is the trace relevant ?
		boolean test = Yeti.testModule.isThrowableInModule(trace);
		// if the trace is actually relevant for the considered module...
		if (test) {
			// Remove the ID number of objects and some other differences
			trace = trace.replaceAll("\\d+]", "]");
			trace = trace.replaceAll(": \\d+ =", "=");
			trace = trace.replaceAll(" = \"\"]", "]");
			trace = trace.replaceAll(" : ]", "]");
			// Then, if it's not already an error for Yeti, we add it to the list of errors
			if (!this.listOfErrorsContainsTrace(trace)) {
				this.putNewTrace(trace,new Date());
				// If asked, we also print some information
				if (printException) {
					System.out.println("Exception "+this.getListOfErrorsSize()+"\n"+trace); // a little different from the java version
				}
			}
		}
		// So that other methods can use this result
		return test;
		
	}
	
	
	
	/**
	 * Printer for throwables in raw logs
	 * 
	 * @parameter t the throwable log to print.
	 */
	public void printThrowableRawLogs(Throwable t) {
		// Begining of the raw log 
		System.err.print("YETI EXCEPTION - START ");
		// We first extract the trace from the throwable
		String exceptionTrace = getTraceFromThrowable(t);
		// We store the trace if relevant
		boolean isRelevant = storeTraceIfRelevant(exceptionTrace, false);
		// And if it's not relevant, we say it
		if (isRelevant) {
			System.err.println();
		} else {
			System.err.println("- NOT IN TESTED MODULE");
		}
		// We print the trace in any case
		System.err.println(exceptionTrace);
		// End of the raw log
		System.err.println("YETI EXCEPTION - END ");
	}

	/**
	 * Printer for throwables in no logs
	 * 
	 * @parameter t the throwable log not to print.
	 */
	public void printThrowableNoLogs(Throwable t) {
		// We first extract the trace from the throwable
		String exceptionTrace = getTraceFromThrowable(t);
		// We store the trace if relevant
		storeTraceIfRelevant(exceptionTrace, true);
	}
	
	
	/**
	 * Printer for throwables in logs
	 * 
	 * @parameter t the throwable log to print.
	 */
	public void printThrowableLogs(Throwable t) {
		YetiLog.printDebugLog("Logs printed", this);
		String exceptionTrace = getTraceFromThrowable(t);
		YetiLog.printDebugLog(exceptionTrace, this);
		storeTraceIfRelevant(exceptionTrace, true);
		this.appendFailureToCurrentLog(exceptionTrace);
	}

	
	/**
	 * A routine that extracts a trace from a throwable.
	 * 
	 * @param t the throwable to get the trace from.
	 * @return the corresponding String
	 */
	public String getTraceFromThrowable(Throwable t) {

		// Here we make a string out of the Throwable
		String throwableLog = "";
		if (t!=null) {
			if (t instanceof KermetaRaisedException) {
				throwableLog = ((KermetaRaisedException)t).toString();
				//System.out.println(((KermetaRaisedException)t).);
			} else {
				OutputStream os=new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(os);
				t.printStackTrace(ps);
				throwableLog = os.toString();
			}
		}

		// we split the lines of code
		String []linesOfTest = throwableLog.split("\n");
		// we continue until the end of the exception trace
		int k = 0;
		String exceptionTrace = null;
		while (k<linesOfTest.length){

			// if we arrive to the reflexive call (java exception), we cut
			if (linesOfTest[k].contains("sun.reflect.")) {
				break;
			}
			// if we arrive to the end of the stack (kermeta exception), we cut
			if (linesOfTest[k].contains("------------END OF STACK TRACE------------")) {
				break;
			}
			if (linesOfTest[k].contains("\n")) {
				continue;
			}
			// in other cases, we just read the trace
			if (exceptionTrace == null) {
				exceptionTrace = linesOfTest[k++];
			} else {
				exceptionTrace=exceptionTrace+"\n"+linesOfTest[k++];
			}
		}
		return exceptionTrace;
	}

}