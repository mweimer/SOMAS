/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package scenario;

import java.util.ArrayList;

import metric.*;

import simulator.*;
import visualization.StatisticsVisualizer;

import common.*;

import environment.VEFlatNetwork;
import metric.SOMetric;

public class VitalElementScenario extends Scenario {
   
    public static final int numObjectives = 6;
    public static final String[] metricNames = {"activity metric", "marked nodes", "shortest path diff", 
    	"disconnects diff", "self organization",  "feasibility"};
    
    public VitalElementScenario() {super();}

    public static void main(String[] args) { new VitalElementScenario().visualize(args[0], true); }

    private void setupSimulation() {

		components = new ArrayList<MASONComponent>();
	
		// Set up scheduling
		schedulingMachineList.clear();
		scheduler = new MASONSimScheduler();
		schedulingMachineList.add(scheduler);
		descheduler = new MASONSimDeScheduler();
		schedulingMachineList.add(descheduler);
	
		// This keeps the simulator from causing a memory exception
		Machine killTrigger = (Machine) (new MASONMemoryKillTrigger())
		    .setProperty("errorTypeContainer", errorTypeContainer)
		    .setNewBoxedProperty("memoryUsePercentageLimit", new Double(0.5));
		schedulingMachineList.add(killTrigger);
		components.add(new MASONComponent(killTrigger));
		components.get(components.size() - 1).order = 3; // Special purpose components execute last
	
		// Set up SO metric
		Thing metricContainer = new Thing();
		MetricLogger metricLogger = new MetricLogger();
		metricLogger.setProperty("metricContainer", metricContainer);
		metricLogger.setProperty("arffWriter", arffWriter);
		
		components.add(new MASONComponent(metricLogger));
		components.get(components.size() - 1).order = 3; // Special purpose components execute last
		
		
		Thing metricAggregationContainersContainer = new Thing();
		metricAggregationContainers = new ArrayList<Thing>();
		metricAggregationContainersContainer.setProperty("object", metricAggregationContainers);
		MetricStatistic metricStatistic = new MetricStatistic();
		metricStatistic.setProperty("metricAggregationContainersContainer", metricAggregationContainersContainer);
		metricStatistic.setNewBoxedProperty("conelength", 2);
		metricStatistic.setProperty("resultContainer", metricContainer);
	
		components.add(new MASONComponent(metricStatistic));
		components.get(components.size() - 1).order = 3; // Special purpose components execute last
	
		network = new VEFlatNetwork(this, 50, 150);
	
		// setup statistics visualization
		if(visualize)
		{
			StatisticsVisualizer statisticsVisualizer = new StatisticsVisualizer(network.getGlobalGraph().vertexSet(), 
					this.getClass().getSimpleName());
			components.add(new MASONComponent(statisticsVisualizer));
		}
		
		//		Machine vitalComponentIDMetric = (Machine) (new VitalComponentIDMetric())
		//		.setProperty("graph",graph)
		//		.setProperty(", v)
		//		.setProperty("objectiveValueContainer", objectiveValueContainers.get(1));
		//		;
		//		components.add(new MASONComponent(vitalComponentIDMetric));
		//		components.get(components.size() - 1).order = 2; // Happens after the agents and the network locations do their thing 
	
    }

	public void run() {
		
		schedulingMachineList.clear();
		errorTypeContainer.setProperty("object", null);
	
		objectiveValueContainers.clear();
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // activity metric
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // marked nodes
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // shortest path diff
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // disconnects diff
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // self organization
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // feasibility (binary value)
	
		String errorType = null;
		try {
			
		    // Initialize and execute simulation.
			MASONSimClassContext masonSimClassContext = new MASONSimClassContext();
			setupSimulation();
			masonSimClassContext.setProperty("componentList", components);
			masonSimClassContext.setProperty("schedulingMachineList", schedulingMachineList);
			masonSimClassContext.setProperty("graph", network.getGlobalGraph());
		    Machine simulation = ((Machine) ((Thing) simClass.newInstance()));
		    simulation.setProperty("errorTypeContainer", errorTypeContainer);
		    simulation.setProperty("simClassContext", masonSimClassContext);
		    simulation.setProperty("argList", new String[] {"-for", "100"}); // Do simulation for 100 steps
		    Thing result = simulation.execute();
	
		    errorType = (String) result.getBoxedProperty("errorType");
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		}
	
		int currObjective = 1;
	
		((Machine)
		 (new MarksMetric())
		 .setNewBoxedProperty("graph", network.getGlobalGraph())
		 .setProperty("objectiveValueContainer", objectiveValueContainers.get(currObjective++)))
		    .execute();
	
		((Machine)
		 (new ShortestPathMetric())
		 .setNewBoxedProperty("graph", network.getGlobalGraph())
		 .setProperty("objectiveValueContainer", objectiveValueContainers.get(currObjective++)))
		    .execute();
	
		((Machine)
		 (new DisconnectMetric())
		 .setNewBoxedProperty("graph", network.getGlobalGraph())
		 .setProperty("objectiveValueContainer", objectiveValueContainers.get(currObjective++)))
		    .execute();
	
		((Machine)
		 (new SOMetric())
		 .setNewBoxedProperty("coneLength", 2) /*keep it short for quick calculation*/
		 .setNewBoxedProperty("history", ((Machine)
						  new MetricLogLoader().setProperty("arffWriter", arffWriter))
				      .execute()
				      .getBoxedProperty("result"))
		 .setProperty("objectiveValueContainer", objectiveValueContainers.get(currObjective++)))
		    .execute();
		
		// Track whether the simulation executed successfully
		if(errorType != null)
		    objectiveValueContainers.get(currObjective++).setProperty("object", new Double(1.0));
		else 
		    objectiveValueContainers.get(currObjective++).setProperty("object", new Double(0.0));
			
		Double[] objectiveValues = new Double[7];
		objectiveValues[0] = (Double) objectiveValueContainers.get(0).getProperty("object");
		objectiveValues[1] = (Double) objectiveValueContainers.get(1).getProperty("object");
		objectiveValues[2] = (Double) objectiveValueContainers.get(2).getProperty("object");
		objectiveValues[3] = (Double) objectiveValueContainers.get(3).getProperty("object");
		objectiveValues[4] = (Double) objectiveValueContainers.get(4).getProperty("object"); 
		objectiveValues[5] = (Double) objectiveValueContainers.get(5).getProperty("object"); // feasibility value
	
		this.objectiveValues = objectiveValues;
    }
}


