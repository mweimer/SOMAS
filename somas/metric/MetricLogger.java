/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package metric;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import common.*;

public class MetricLogger extends Metric {
	public void run() {
		ArrayList<Double> metric = (ArrayList<Double>) this.getBoxedProperty("metric");
		StringWriter writer = ((ARFFWriter) this.getProperty("arffWriter")).getSW();
				
		writeDataARFF(writer, metric);
	}

	public void writeDataARFF(Writer writer, ArrayList<Double> metric) {
		try {
			if (metric != null) { // HACK I'm not sure why this would ever be
									// null

				String record = "";

				for (Double value : metric)
					record += value.toString() + ",";

				record = record.substring(0, record.length() - 1) + '\n';

				writer.append(record);

				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
