package yeti.cloud;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.io.MapWritable;

import yeti.Yeti;
import yeti.YetiLogProcessor;

/**
 * Class that represents the Maper for Yeti to run on cloud.
 * 
 * @author Faheem Ullah (fu500@cs.york.ac.uk)
 * @date August 20, 2009
 */

public class YetiMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>  {
	public static String traces;
	

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		//spliting the lines into parameters based on whitespaces
		String params [] = value.toString().split("\\x20");
		
		//passing parameters (Command line arguments) to Yeti
		Yeti.YetiRun(params);
		
		YetiLogProcessor lp = (YetiLogProcessor)Yeti.pl.getLogProcessor();
		
		//You can not duplicate the key anyway, so only unique exception traces would go in
		YetiJob.uniquelistOfErrors.putAll(lp.listOfErrors);
		
//		String[] trace = (String[])lp.listOfErrors.keySet().toArray();
		
//		traces = trace.toString();
				
		//	output.collect(out, new IntWritable(lp.listOfErrors.size()));
		output.collect(new Text ("Non-Unique"), new IntWritable(lp.listOfErrors.size()));
		
		
	}
}
