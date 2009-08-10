
package yeti;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.io.*;
import org.apache.hadoop.filecache.DistributedCache;

import yeti.Yeti;

public class YetiJob {
	
	public static String dfsInput;
	public static String dfsOutput;

public static void main(String[] args) {

		Yeti.YetiRun(args);
				
		
		JobClient client = new JobClient();
		JobConf conf = new JobConf(yeti.YetiJob.class);
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.setInputPaths(conf, new Path (dfsInput));
		FileOutputFormat.setOutputPath(conf, new Path(dfsOutput));

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
