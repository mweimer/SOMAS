/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package metric;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import weka.clusterers.EM;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.AddCluster;

import common.Thing;

public class SOMetric extends Metric {
    public SOMetric() {}
    public SOMetric(Thing t) {super(t);} 

    @SuppressWarnings("deprecation")
	public void run() {
        Instances history = (Instances) this.getBoxedProperty("history");
        
        if(history.numInstances() == 0) {
            this.setBoxedProperty("objectiveValue", new Double(0));
            return; 
        }
        
        int numInstances = history.numInstances();

        Integer coneLength = (Integer) this.getBoxedProperty("coneLength");

        EM EMClusterer = new EM();
        AddCluster ac = new AddCluster();

        try {
            EMClusterer.buildClusterer(history);

            ac.setClusterer(EMClusterer);
            ac.setInputFormat(history);
            for(int i = 0; i < numInstances; i++)
                ac.input(history.instance(i));
            ac.batchFinished();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Instances clusteredHistory = ac.outputFormat();
        Instance processed;
        while ((processed = ac.output()) != null)
            clusteredHistory.add(processed);

        Integer classAttribute = clusteredHistory.numAttributes()-1;
        clusteredHistory.setClassIndex(classAttribute);

        double[] classifiedUnboxTemp = clusteredHistory.attributeToDoubleArray(classAttribute); 
        Double[] classifiedBoxTemp = new Double[classifiedUnboxTemp.length];
        for(int i = 0; i < classifiedUnboxTemp.length; i++)
            classifiedBoxTemp[i] = classifiedUnboxTemp[i];
        
        ArrayList<Double> classifiedHistory = new ArrayList<Double>(Arrays.asList(classifiedBoxTemp));

        FastVector attInfo = new FastVector();

        for(Integer i = 0; i < coneLength; i++)
            attInfo.addElement(new Attribute(i.toString()));

        Instances pasts = new Instances("pasts", attInfo, coneLength);

        Instances futures = new Instances("futures", attInfo, coneLength);

        HashMap<Instance, Instance> historyMap = new HashMap<Instance, Instance>();
        
        for(int i = 0; i < classifiedHistory.size() - (coneLength * 2) + 1; i++) {
            Object[] pastValues = (Object[]) classifiedHistory.subList(i, i+coneLength).toArray();
            double[] pastUnboxTemp = new double[pastValues.length];
            for(int j = 0; j < pastValues.length; j++)
                pastUnboxTemp[j] = (Double) pastValues[j];
                
            Instance past = new Instance(1.0, pastUnboxTemp);
            pasts.add(past);
            
            Object[] futureValues = (Object[]) classifiedHistory.subList(i+coneLength, i+(coneLength * 2)).toArray();
            double[] futureUnboxTemp = new double[pastValues.length];
            for(int j = 0; j < futureValues.length; j++)
                futureUnboxTemp[j] = (Double) futureValues[j];
                
            Instance future = new Instance(1.0, futureUnboxTemp);
            futures.add(future);
        }

        Double SOMeasure = calcSO(pasts, futures);

        this.setBoxedProperty("objectiveValue", SOMeasure);
    }
    
    Double calcSO(Instances pasts, Instances futures) {
        EM EMClusterer = new EM();
        AddCluster ac = new AddCluster();

        try {
            EMClusterer.buildClusterer(pasts);

            ac.setClusterer(EMClusterer);
            ac.setInputFormat(pasts);
                
            int numInstances = pasts.numInstances();
            for(int i = 0; i < numInstances; i++)
                ac.input(pasts.instance(i));
            ac.batchFinished();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            // e1.printStackTrace();
            System.err.println("SOMetric:calcSO() infeasible solution"); // HACK
            return 1.0; // HACK
        }

        Instances pastClusters = ac.outputFormat();
        Instance processed;
        while ((processed = ac.output()) != null)
            pastClusters.add(processed);

        pastClusters.setClassIndex(pastClusters.numAttributes()-1);

        ArrayList<Instances> futuresGroups = new ArrayList<Instances>();

        for(int i = 0; i < pastClusters.numClasses(); i++)
            futuresGroups.add(new Instances(futures, 10));

        for(int i = 0; i < pastClusters.numInstances(); i++)
            futuresGroups.get((new Double(pastClusters.instance(i).classValue())).intValue()).add(futures.instance(i)); // Assume pasts and futures are always lined up, and the clustering does not reorder the past list

        Instances futureDistributions = new Instances(futures, 0);

        for(int i = 0; i < pastClusters.numClasses(); i++) {
            int numAttributes = futures.instance(0).numAttributes();
            double[] meanValues = new double[numAttributes];

            for(int j = 0; j < numAttributes; j++)
                meanValues[j] = futuresGroups.get(i).meanOrMode(j);
            
            Instance futureDistribution = new Instance(1.0, meanValues);
            futureDistributions.add(futureDistribution);
        }

        try {
            EMClusterer.buildClusterer(futureDistributions);

            ac.setClusterer(EMClusterer);
            ac.setInputFormat(futureDistributions);
            for(int i = 0; i < futureDistributions.numInstances(); i++)
                ac.input(futureDistributions.instance(i));
            ac.batchFinished();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Instances futureDistributionClusters = ac.outputFormat();
        while ((processed = ac.output()) != null)
            futureDistributionClusters.add(processed);

        futureDistributionClusters.setClassIndex(futureDistributionClusters.numAttributes()-1);

        return 1.0/(Math.log(futureDistributionClusters.numClasses()) + 1.0);
    }
}    

