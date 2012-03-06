/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package scenario;

import java.util.ArrayList;

import metric.*;

import simulator.*;
import visualization.StatisticsVisualizer;

import common.*;

import environment.FlatNetwork;
import environment.Location;
import metric.SOMetric;

public class CompetitionScenario extends Scenario {
	
	public static final int numObjectives = 5; // TODO : change back to 6, set to 5 right now for backwards compatibility
	public static final String[] metricNames = {"activity metric", "good agent deletion count", "bad agent deletion count", 
		"self organization", "feasibility",  "dead nodes"};
	
	public CompetitionScenario() {super();}

    public static void main(String[] args) { new CompetitionScenario().visualize(args[0], true); }

	private void setupSimulation() {
		
		components = new ArrayList<MASONComponent>();

		// Set up scheduling
		schedulingMachineList.clear();
		scheduler = new MASONSimScheduler();
		schedulingMachineList.add(scheduler);

		ArrayList<Machine> deschedulerActuators = new ArrayList<Machine>();
		descheduler = (MASONSimDeScheduler) new MASONSimDeScheduler()
		.setNewBoxedProperty("actuators", deschedulerActuators);
		schedulingMachineList.add(descheduler);

		AgentTypeCounterMetric goodAgentTypeCounterMetric = (AgentTypeCounterMetric) new AgentTypeCounterMetric()
		.setNewBoxedProperty("descheduler", descheduler)
		.setNewBoxedProperty("type", "somas")
		.setProperty("objectiveValueContainer", objectiveValueContainers.get(1));
		deschedulerActuators.add(goodAgentTypeCounterMetric);
		
		AgentTypeCounterMetric badAgentTypeCounterMetric = (AgentTypeCounterMetric) new AgentTypeCounterMetric()
		.setNewBoxedProperty("descheduler", descheduler)
		.setNewBoxedProperty("type", "malicious")
		.setProperty("objectiveValueContainer", objectiveValueContainers.get(2));
		deschedulerActuators.add(badAgentTypeCounterMetric);
		
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

	
		FlatNetwork flatNetwork = new FlatNetwork(this, 50, 150);
		flatNetwork.addCompromiseAgents(2, true);
		flatNetwork.addSOMASAgents(1.0);
		flatNetwork.finalizeGraph();
		network = flatNetwork;
		
		// setup statistics visualization
		if(visualize)
		{
			StatisticsVisualizer statisticsVisualizer = new StatisticsVisualizer(network.getGlobalGraph().vertexSet(), 
					this.getClass().getSimpleName());
			components.add(new MASONComponent(statisticsVisualizer));
		}
	}

	public void run() {
	
		schedulingMachineList.clear();
		errorTypeContainer.setProperty("object", null);

		objectiveValueContainers.clear();
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // activity metric
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // good agent deletion count
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // bad agent deletion count
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // self organization
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // feasibility (0 or 1)
		objectiveValueContainers.add((new Thing()).setProperty("object", new Double(0))); // dead nodes

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

		int currObjective = 3;

		// Maximize the bad agent deletion 
		Double badAgentDeletionCount = (Double) objectiveValueContainers.get(2).getProperty("object");
		badAgentDeletionCount = 1.0 / (badAgentDeletionCount + 1.0);
		objectiveValueContainers.get(2).setProperty("object", badAgentDeletionCount);
		
		((Machine)
				(new SOMetric())
				.setNewBoxedProperty("coneLength", 2) /*keep it short for quick calculation*/
				.setNewBoxedProperty("history", ((Machine)
						new MetricLogLoader()
						.setProperty("arffWriter", arffWriter))
						.execute()
						.getBoxedProperty("result"))
						.setProperty("objectiveValueContainer", objectiveValueContainers.get(currObjective++)))
						.execute();

		// Track whether the simulation executed successfully
		if(errorType != null)
			objectiveValueContainers.get(currObjective++).setProperty("object", new Double(1.0));
		else 
			objectiveValueContainers.get(currObjective++).setProperty("object", new Double(0.0));

		// Dead node metric, should minimize the number of nodes destroyed
		Double deadNodes = 0.0;
		for(Location location : network.getGlobalGraph().vertexSet()){
			if(location.hasBoxedProperty("isVitalNode") && (Boolean) location.getBoxedProperty("isVitalNode"))
				deadNodes++;
		}
		objectiveValueContainers.get(currObjective++).setProperty("object", deadNodes);
		
		Double[] objectiveValues = new Double[numObjectives];
		for(int i = 0; i < numObjectives; i++)
			objectiveValues[i] = (Double) objectiveValueContainers.get(i).getProperty("object");

		this.objectiveValues = objectiveValues;
	}
}


