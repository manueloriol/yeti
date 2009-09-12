package yeti.cloud;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.sun.xml.internal.bind.v2.TODO;

/**
 * Class that represents the Reducer for Yeti to run on cloud. 
 * It will append the result of each MapJob to a file on disk at the output location specified at start of job.
 * 
 * @author Faheem Ullah (fu500@cs.york.ac.uk)
 * @date August 20, 2009
 */

public class YetiReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
	
	/**
	 * The reduce method, which will take the output of each Map and write it out to disk as final output after doing any processing
	 * 
	 */ 
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		String traces = "";
		int sum = values.next().get();

		traces+=key.toString();
		
		output.collect(new Text(traces+"\n\nNumber of Bugs Found in Job: "), new IntWritable(sum));
//		output.collect(new Text(YetiJob.uniqueListOfErrors.keySet()+"\nNon-Unique Errors: "+sum+"\nUnique Errors"), new IntWritable (YetiJob.uniqueListOfErrors.size()));	
	}
}
