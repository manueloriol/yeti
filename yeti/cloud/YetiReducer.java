package yeti.cloud;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * Class that represents the Reducer for Yeti to run on cloud. 
 * It will append the result of each MapJob to a file on disk at the output location specified at start of job.
 * 
 * @author Faheem Ullah (fu500@cs.york.ac.uk)
 * @date August 20, 2009
 */

public class YetiReducer extends MapReduceBase implements Reducer<Text, Text, Text, IntWritable> {
	
	/**
	 * The reduce method, which will take the output of each Map and write it out to disk as final output after performing any processing
	 * 
	 */ 
	public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		String[] list= null;
		String traces = "";
		
		Text className = key;
			
		while(values.hasNext())
			traces+=values.next();
		
		list = traces.split("@");
		
		output.collect(new Text(className+"\n"+traces+"\n\n"), new IntWritable(list.length));
	}
	
}
