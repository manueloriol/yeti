package yeti;

import java.util.HashMap;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;

/**
 * Class that represents the main launching class of Yeti in distributed mode
 * 
 * @author Faheem Ullah (fu500@cs.york.ac.uk)
 * @date August 20, 2009
 */

public class YetiJob {
	
	public static HashMap<String,Object> uniquelistOfErrors = new HashMap<String, Object>();

	/**
	 * The main method of yeti in distributed mode.
	 *  It receives two arguments. These are:
	 *  -The Input Path for YetiMap
	 *  -The output path for YetiReducer.
	 *  @param args the arguments of the program
	 */
	public static void main(String[] args) {
		
		if (args.length<2){
			System.err.println("Error! Arguments must be provided for input and output path");
			System.err.println("Usage: bin/hadoop jar yeti.jar input_path output_path");
			return;
		}
		
		//The job client to interact with the JobTracker for this job
		JobClient client = new JobClient();
		//setting up the job configuration for YetiJob
		JobConf conf = new JobConf(yeti.YetiJob.class);
		
		//the output of Yeti would be Text for the Key
		conf.setOutputKeyClass(Text.class);
		//the output of Yeti would be a Number for the Value
		conf.setOutputValueClass(IntWritable.class);
		
		//The Input path for YetiMap to read file(s) containingthe input parameters
		FileInputFormat.setInputPaths(conf, new Path (args[0]));
		//The output path for YetiReducer to write the final output
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		
		//The input data would be read as a line of text at at time, separated by newlines
		conf.setInputFormat(TextInputFormat.class);
		//The output data would be written in the form of text - new line for each unique key, value pair
		conf.setOutputFormat(TextOutputFormat.class);
		
		//we set the Mapper class for Yeti
		conf.setMapperClass(yeti.YetiMap.class);
		//we set the Reducer class for Yeti
		conf.setReducerClass(yeti.YetiReducer.class);
		
		client.setConf(conf);
		try {
			//Finally we run the job
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

