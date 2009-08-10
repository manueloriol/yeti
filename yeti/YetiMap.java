package yeti;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import yeti.Yeti;
import yeti.YetiLogProcessor;

public class YetiMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>  {

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		String line = value.toString();
		Text out = new Text("MyResult");
		output.collect(out, new IntWritable(Integer.parseInt(line)));
		
		
	}
}
