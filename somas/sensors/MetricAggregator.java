/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package sensors;

import java.util.ArrayList;

import metric.Metric;

import common.*;

public class MetricAggregator extends Sensor {
    public MetricAggregator() {}
    MetricAggregator(Thing t) {super(t);} 
    
    public void run() {
	ArrayList<Thing> metricContainers = (ArrayList<Thing>) this.getBoxedProperty("metricContainers");

	ArrayList<Object> metricAggregation = new ArrayList<Object>();
	for(Thing metricContainer : metricContainers)
	    metricAggregation.add(metricContainer.getProperty("object"));

	this.setBoxedProperty("result", metricAggregation);
    }
}

