package yeti.cloud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
 * It reads the contents of file containing all the parameters for YETI one line at a time and passes them on to YETI 
 * 
 * @author Faheem Ullah (fu500@cs.york.ac.uk)
 * @date August 20, 2009
 */

public class YetiMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>  {
	
	/**
	 * The map method, this will serve as a single map job and will be run on a single machine within the cloud
	 * Each machine will get a map job to run YETI with the specified line of parameters
	 * The output is written to disk by YETI and read back here to report the number of bugs and exception traces.
	 * @param 
	 * key = the line number read<br>
	 * value = the actual line contents<br>
	 * OutputCollector = Output will be collected in the form of <Text, Int><br>
	 * Reporter = to report the status of current process<br>
	 */
	public synchronized void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		//spliting the lines into parameters based on whitespaces
		String params [] = value.toString().split("\\x20");
		
		//passing parameters (Command line arguments) to Yeti
		Yeti.YetiRun(params);
		
		String outputKey="";

		//Reading the files back from disk		
		ArrayList<String> traces = YetiLogProcessor.readTracesFromFile("YetiTraces");
		
		for (String str: traces)
			outputKey+=str+"\n";
				
		//output the number of traces, actual traces for this job (i.e. one line of parameters)
		output.collect(new Text (outputKey), new IntWritable(traces.size()));
		
		
	}
}
