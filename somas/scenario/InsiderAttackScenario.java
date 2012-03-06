
package scenario;

import java.util.ArrayList;

import metric.*;

import simulator.*;
import visualization.StatisticsVisualizer;
import agent.Agent;

import common.*;

import environment.ComponentNetwork;
import environment.Location;
import metric.SOMetric;

public class InsiderAttackScenario extends Scenario {
	
	public static final int numObjectives = 5; // TODO : change back to 6, set to 5 right now for backwards compatibility
	public static final String[] metricNames = {"activity metric", "bad info", "insiders remaining", "self organization", 
		"feasibility",  "dead nodes"};
	
	public InsiderAttackScenario() {super();}

	public static void main(String[] args) { new InsiderAttackScenario().visualize(args[0], true); }

	private void setupSimulation() {
	
		/*
		  This array stores the components to be run in
		  the simulator, such as network nodes and
		  agents.
		*/
		
		components = new ArrayList<MASONComponent>(); 
		
		// Set up scheduling
		/*
		  Scheduling machines interact with the
		  simulator's schedule,
		  i.e. descheduling an agent component
		  once it has been deleted from the
		  network.  This way, inactive
		  components don't take up space, and
		  exceed the memory threshold.
		*/
		schedulingMachineList.clear(); 
		scheduler = new MASONSimScheduler();
		schedulingMachineList.add(scheduler);
		descheduler = new MASONSimDeScheduler();
		schedulingMachineList.add(descheduler);
		
		// This keeps the simulator from causing a memory exception
		Machine killTrigger = (Machine) (new MASONMemoryKillTrigger());
		killTrigger.setProperty("errorTypeContainer", errorTypeContainer);
		killTrigger.setNewBoxedProperty("memoryUsePercentageLimit", new Double(0.5));
		schedulingMachineList.add(killTrigger);
		components.add(new MASONComponent(killTrigger));
		components.get(components.size() - 1).order = 3; // Special purpose components execute last
		
		/*
		  ========================
		  Self Organization Metric
		  ========================
		*/
		
		/*
		  The metric logger saves a history of the
		  simulation.
		*/
		
		Thing metricContainer = new Thing();
		MetricLogger metricLogger = new MetricLogger();
		metricLogger.setProperty("metricContainer", metricContainer);
		metricLogger.setProperty("arffWriter", arffWriter);
		
		components.add(new MASONComponent(metricLogger));
		components.get(components.size() - 1).order = 3; // Special purpose components execute last
		
		
		/*
		  The metric aggregator collects data from each
		  simulation step and converts it into a
		  statistic.
		*/
		Thing metricAggregationContainersContainer = new Thing();
		metricAggregationContainers = new ArrayList<Thing>();
		metricAggregationContainersContainer.setProperty("object", metricAggregationContainers);
		MetricStatistic metricStatistic = new MetricStatistic();
		metricStatistic.setProperty("metricAggregationContainersContainer", metricAggregationContainersContainer);
		metricStatistic.setNewBoxedProperty("conelength", 2);
		metricStatistic.setProperty("resultContainer", metricContainer);
		
		components.add(new MASONComponent(metricStatistic));
		components.get(components.size() - 1).order = 3; // Special purpose components execute last
		
		
		/*
		  ==============
		  Create network
		  ==============
		  TODO [#C] remove hardcoding of network node numbers
		
		  In this section, the network's parts are each
		  created individually, then added to the whole
		  network.  There are 3 main parts for the network:
		
		  1. Backbone: the portion of the network connecting
		  all LANS to each other.  Often called the
		  municipal area network, or the MAN.
		
		  2. Local area networks: the smaller, more loosely
		  connected networks branching off of the backbone.
		  These tend to be star networks, though a wheel
		  shape is used here.
		
		  3. External nodes: these are nodes that outsiders
		  enter the network through, and are not part of the
		  network proper.
		
		  Additionally, there can be internal nodes that
		  malicious agents use to access the network.
		
		  TODO [#A] make scenario generation use a case based system,
		  where nodes are labelled by type, and agents installed by
		  type.  Smoother decoupling of network layout and agent
		  installation.
		
		  TODO [#B] implement a scenario with an insider threat
		*/

		int[] lanSizes = {8, 5, 15, 23, 2};
		ComponentNetwork componentNetwork = new ComponentNetwork(this, 5, 2, lanSizes);
		componentNetwork.addBadInfoReceiverAgents();
		componentNetwork.addInsiders(3);
		componentNetwork.addSOMASAgents(0.25);
		componentNetwork.finalizeGraph();
		network = componentNetwork;
		
		// setup statistics visualization
		if(visualize)
		{
			StatisticsVisualizer statisticsVisualizer = new StatisticsVisualizer(network.getGlobalGraph().vertexSet(), 
					this.getClass().getSimpleName());
			components.add(new MASONComponent(statisticsVisualizer));
		}

	}
		
	public void run() {
		
		/*
		  These variables make it easier to keep the code in a normalized form.
		*/
		Thing tempThing = new Thing();
		Machine tempMachine = null;
		Machine tempMachine2 = null;
		Double[] tempDoubleArray = null;
		
		schedulingMachineList.clear();
		errorTypeContainer.setProperty("object", null);
		
		objectiveValueContainers.clear();
		tempThing = (new Thing());
		tempThing.setProperty("object", new Double(0));
		objectiveValueContainers.add(tempThing); // activity metric
		tempThing = (new Thing());
		tempThing.setProperty("object", new Double(0));
		objectiveValueContainers.add(tempThing); // bad info
		tempThing = (new Thing());
		tempThing.setProperty("object", new Double(0));
		objectiveValueContainers.add(tempThing); // insiders remaining
		tempThing = (new Thing());
		tempThing.setProperty("object", new Double(0));
		objectiveValueContainers.add(tempThing); // self organization
		tempThing = (new Thing());
		tempThing.setProperty("object", new Double(0));
		objectiveValueContainers.add(tempThing); // feasibility (0 or 1)
		tempThing = (new Thing());
		tempThing.setProperty("object", new Double(0));
		objectiveValueContainers.add(tempThing); // dead nodes
		
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
		
		badInfoMetric.execute();
		
		Double infoExtracted = new Double(0);
		for(Location l : network.getGlobalGraph().vertexSet()) { // HACK since the receiver nodes are not counting the bad info
			if(l.hasBoxedProperty("externalNode")) {
				for(Agent a : (ArrayList<Agent>) l.getBoxedProperty("agentList")) {
					ArrayList<String> typeList = a.hasBoxedProperty("typeList") ? (ArrayList<String>) a.getBoxedProperty("typeList") : new ArrayList<String>();
					for(String type : typeList) {
						if(type.equals("badinfocomm")) {
							infoExtracted++;
						}
					}
				}
			}
		}
		
		objectiveValueContainers.get(1).setProperty("object", infoExtracted);
		
		// Count number of remaining insiders on nodes
		Double insidersRemaining = new Double(0);
		for(Location l : network.getGlobalGraph().vertexSet()) {
			if(l.hasBoxedProperty("insiderPresent")) {
				insidersRemaining++;
			}
		}
		
		objectiveValueContainers.get(2).setProperty("object", insidersRemaining);
		
		int currObjective = 3;
		
		// Load simulation history.
		tempMachine = ((Machine) (new MetricLogLoader().setProperty("arffWriter", arffWriter)));
		tempMachine.execute();
		
		// Analyze history for self organized content.
		tempMachine2 = ((Machine) (new SOMetric()));
		tempMachine2.setNewBoxedProperty("coneLength", 2); /*keep it short for quick calculation*/
		tempMachine2.setNewBoxedProperty("history", tempMachine.getBoxedProperty("result"));
		tempMachine2.setProperty("objectiveValueContainer", objectiveValueContainers.get(currObjective++));
		tempMachine2.execute();
		
		
		// Track whether the simulation executed successfully.  If
		// not, solution is infeasible, and feasibilty objective is
		// unset.
		if(errorType != null) {
		    tempThing = objectiveValueContainers.get(currObjective++);
		    tempThing.setProperty("object", new Double(1.0));
		} else {
		    tempThing = objectiveValueContainers.get(currObjective++);
		    tempThing.setProperty("object", new Double(0.0));
		}
		
		// Dead node metric, should minimize the number of nodes
		// destroyed
		Double deadNodes = 0.0;
		for(Location location : network.getGlobalGraph().vertexSet()){
		    // TODO [#B] HACK should use a different variable name for this, since it isn't about being a vital node 
		    Boolean vitalNodeSet = location.hasBoxedProperty("isVitalNode"); 
		
		    Boolean isVitalNode = (Boolean) location.getBoxedProperty("isVitalNode");
		    if(vitalNodeSet && isVitalNode)
			deadNodes++;
		}
		tempThing = objectiveValueContainers.get(currObjective++);              
		tempThing.setProperty("object", deadNodes);
		
		Double[] objectiveValues = new Double[numObjectives];
		for(int i = 0; i < numObjectives; i++) {
		    tempThing = objectiveValueContainers.get(i);
		    objectiveValues[i] = (Double) tempThing.getProperty("object");
		}
		
		this.objectiveValues = objectiveValues;
	}
}