package yeti;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/**
 * Class that represents the Maper for Yeti to run on cloud.
 * 
 * @author Faheem Ullah (fu500@cs.york.ac.uk)
 * @date August 20, 2009
 */

public class YetiMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>  {
	

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		//spliting the lines into parameters based on whitespaces
		String params [] = value.toString().split("\\x20");
		
		//passing parameters (Command line arguments) to Yeti
		Yeti.YetiRun(params);
		
		YetiLogProcessor lp = (YetiLogProcessor)Yeti.pl.getLogProcessor();
		
//		Collection<String> col= lp.listOfErrors.keySet();
//		Iterator<String> itr = col.iterator();
//				
//		while(itr.hasNext()){
//			String yetiKey = itr.next();
//			System.out.println(yetiKey);
//			if (!YetiJob.uniquelistOfErrors.containsKey(yetiKey)) {
//				System.out.println("placed in unique list of Errors: "+yetiKey);
//				YetiJob.uniquelistOfErrors.put(lp.listOfErrors.get(yetiKey).toString(), new Date());
//			}
//		}
		
		Text out = new Text("Non-Unique Bugs");
		output.collect(out, new IntWritable(lp.listOfErrors.size()));
		
	}
}
