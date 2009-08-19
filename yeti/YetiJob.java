
package yeti;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;


public class YetiJob {
	
	public static String dfsInput;
	public static String dfsOutput;
	
	
	public static void main(String[] args) {

		JobClient client = new JobClient();
		JobConf conf = new JobConf(yeti.YetiJob.class);
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.setInputPaths(conf, new Path (args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		
		conf.setMapperClass(yeti.YetiMap.class);
		conf.setReducerClass(yeti.YetiReducer.class);
		
		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
