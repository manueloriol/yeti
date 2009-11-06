package yeti.cloud;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import yeti.Yeti;
import yeti.YetiLogProcessor;

/**
 * Class that represents the Maper for Yeti to run on cloud. 
 * It reads the contents of file(s) containing all the parameters for YETI one file at a time and passes them on to YETI 
 * 
 * @author Faheem Ullah (fu500@cs.york.ac.uk)
 * @date August 20, 2009
 */

public class YetiMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>  {
	
	public static HashMap<String, Object> listOfExceptions= new HashMap<String,Object>();
	public static String moduleName;
	/**
	 * The map method, this will serve as a single map job and will be run on a single machine within the cloud
	 * Each machine will get a map job to run YETI with the specified line of parameters
	 * @param 
	 * key = the line number read<br>
	 * value = the actual line contents<br>
	 * OutputCollector = Output will be collected in the form of <Text, Int><br>
	 * Reporter = to report the status of current process<br>
	 */
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		//splitting the lines into parameters based on white spaces
		String params [] = value.toString().split("\\x20");
		
		//set up the distributed mode
		Yeti.isDistributed=true;
		
		//passing parameters (Command line arguments) to Yeti
		Yeti.YetiRun(params);
		
		String outputKey="";

		YetiLogProcessor lp = (YetiLogProcessor)Yeti.pl.getLogProcessor();
		int uniqueBugs= lp.getNumberOfUniqueFaults();

		Iterator it =listOfExceptions.keySet().iterator(); 
		while(it.hasNext())
			outputKey+=it.next().toString()+"@\n";
		
		output.collect(new Text (moduleName+"\n"+outputKey), new IntWritable(uniqueBugs));
	}
}
