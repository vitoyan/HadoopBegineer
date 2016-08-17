import org.apache.hadoop.io.*;
import java.util.*;

public class WritablesTest
{
	public static class IntArraryWritable extends ArrayWritable
	{
		public IntArraryWritable()
		{
			super(IntWritable.class);
		}	
	}

	public static void main(String[] args)
	{
		System.out.println("***Primitive Writable***");
		BooleanWritable bool1 = new BooleanWritable(true);
		ByteWritable byte1 = new ByteWritable((byte)3);
		System.out.printf("Boolean: %s Byte: %d\n", bool1, byte1.get());
		IntWritable i1 = new IntWritable(5);
		IntWritable i2 = new IntWritable(17);
		System.out.printf("I1: %d I2: %d\n", i1.get(), i2.get());
		i1.set(i2.get());
		System.out.printf("I1: %d I2: %d\n", i1.get(), i2.get());
		Integer i3 = new Integer(23);
		i1.set(i3);
		System.out.printf("I1: %d I2: %d\n", i1.get(), i2.get());

		System.out.println("***Array Writable***");
		ArrayWritable a = new ArrayWritable(IntWritable.class);
		a.set(new IntWritable[]{new IntWritable(1), new IntWritable(3), new IntWritable(5)});
		IntWritable[] values = (IntWritable[])a.get();
		for(IntWritable i: values)
			System.out.println(i);
		IntArraryWritable ia = new IntArraryWritable();
		ia.set(new IntWritable[]{new IntWritable(1), new IntWritable(3), new IntWritable(5)});
		IntWritable[] ivalues = (IntWritable[])ia.get();
		for(IntWritable iv: ivalues)
			System.out.println(iv);
		IntArraryWritable la = new IntArraryWritable();
		la.set(new LongWritable[]{new LongWritable(10001), new LongWritable(10002)});
		LongWritable[] lvalues = (LongWritable[])la.get();
		for(LongWritable lv: lvalues)
			System.out.println(lv);

		System.out.println("** Map Writalbes ***");
		MapWritable m = new MapWritable();
		IntWritable key1 = new IntWritable(5);
		NullWritable value1 = NullWritable.get();
		m.put(key1, value1);
		System.out.println(m.containsKey(key1));
		System.out.println(m.get(key1));
		m.put(new LongWritable(1000000000), key1);
		Set<Writable> keys = m.keySet();
		for(Writable w: keys)
			System.out.println(w.getClass());
	}
}