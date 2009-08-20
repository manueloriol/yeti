package yeti;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * Class that represents the Reducer for Yeti to run on cloud.
 * 
 * @author Faheem Ullah (fu500@cs.york.ac.uk)
 * @date August 20, 2009
 */

public class YetiReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		int total = 0;
		//we sum all the intermediate values (i.e. number of bugs found by each Mapjob for yeti)
		while (values.hasNext())
			total += values.next().get();

		//This is the sum of all the bugs found by running each line of arguments, 
		//that is why the Bugs would be non-unique
		output.collect(new Text("Non-Unique Bugs"), new IntWritable(total));

//		output.collect(new Text("Unique Errors"), new IntWritable (YetiJob.uniquelistOfErrors.size()));
		
	}
}
