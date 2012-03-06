/*
   Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
  package metric;
    
import java.util.ArrayList;

import common.*;

import weka.clusterers.EM;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;

public class MetricStatistic extends Metric {
    public void run() {
        ArrayList<Object> metricAggregationContainers = (ArrayList<Object>) this.getBoxedProperty("metricAggregationContainers");

        FastVector attInfo = new FastVector();

        int numMetrics = ((ArrayList<Object>) ((Thing) metricAggregationContainers.get(0)).getProperty("object")).size();
                
        for(Integer i = 0; i < numMetrics; i++)
            attInfo.addElement(new Attribute(i.toString()));

        Instances metricAggregations = new Instances("metricAggregations", attInfo, numMetrics);

        for(Object metricAggregationContainer : metricAggregationContainers) {
            ArrayList<Object> objectAggregation = (ArrayList<Object>) ((Thing) metricAggregationContainer).getProperty("object");

            double[] metricAggregation = new double[objectAggregation.size()];

            for(int i = 0; i < objectAggregation.size(); i++)
                metricAggregation[i] = (Double) objectAggregation.get(i);

            metricAggregations.add(new Instance(1.0, metricAggregation));
        }

        ArrayList<Double> metricAggregationStatistic = new ArrayList<Double>();

        for(int i = 0; i < metricAggregations.numAttributes(); i++)
            metricAggregationStatistic.add(metricAggregations.meanOrMode(i));

        this.setBoxedProperty("result", metricAggregationStatistic);
    }
}

