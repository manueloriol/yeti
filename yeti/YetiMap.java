package yeti;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class YetiMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>  {

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		String rawParams = value.toString();
		String params [] = rawParams.split("\\x20");
				
		Yeti.YetiRun(params);
		YetiLogProcessor lp = (YetiLogProcessor)Yeti.pl.getLogProcessor();
		
		Text out = new Text("MyResult");
		output.collect(out, new IntWritable(lp.listOfErrors.size()));
		
	}
}
