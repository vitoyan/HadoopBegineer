import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Iterator;
import java.util.regex.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.*;

public class UFOLocation2
{
	public static class MapClass extends MapReduceBase implements Mapper<LongWritable, Text, Text, LongWritable>
	{
		private final static LongWritable one = new LongWritable(1);
		private static Pattern locationPattern = Pattern.compile("[a-zA-z]{2}[^a-zA-z]*$");
		private Map<String, String> stateNames;

		@Override
		public void configure(JobConf job)
		{
			try
			{
				Path[] cacheFiles = DistributedCache.getLocalCacheFiles(job);
				setupStateMap(cacheFiles[0].toString());
			} 
			catch (IOException e)
			{
				System.err.println("Error reading state file.");
				System.exit(1);
			}
		}

		private void setupStateMap(String fileName) throws IOException
		{
			Map<String, String> states = new HashMap<String, String>();
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();
			while(line != null)
			{
				String[] split = line.split("\t");
				states.put(split[0], split[1]);
				line = reader.readLine();
			}

			stateNames = states;
		}

		public void map(LongWritable key, Text value, OutputCollector<Text, LongWritable> output, Reporter reporter) throws IOException
		{
			String line = value.toString();
			String[] fields = line.split("\t");
			String location = fields[2].trim();

			if(location.length() >= 2)
			{
				Matcher matcher = locationPattern.matcher(location);
				if(matcher.find())
				{
					int start = matcher.start();
					String state = location.substring(start, start+2);

					output.collect(new Text(lookupState(state.toUpperCase())), one);
				}
			}
		}

		private String lookupState(String state)
		{
			String fullName = stateNames.get(state);
			return fullName == null?"Other": fullName;
		}
	}

	public static void main(String[] args) throws Exception
	{
		Configuration config = new Configuration();
		JobConf conf = new JobConf(config, UFOLocation2.class);
		conf.setJobName("UFOLocation");
		DistributedCache.addCacheFile(new URI("/user/will/states.txt"), conf);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(LongWritable.class);

		JobConf mapconf1 = new JobConf(false);
		ChainMapper.addMapper(conf, UFORecordValidationMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, true,mapconf1);

		JobConf mapconf2 = new JobConf(false);
		ChainMapper.addMapper(conf, MapClass.class, LongWritable.class, Text.class, Text.class, LongWritable.class, true, mapconf2);

		conf.setMapperClass(ChainMapper.class);
		conf.setCombinerClass(LongSumReducer.class);
		conf.setReducerClass(LongSumReducer.class);

		FileInputFormat.setInputPaths(conf, args[0]);
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}
}