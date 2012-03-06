package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import actuators.Actuator;
import actuators.SetLocationPheromone;
import agent.Agent;

import common.Machine;
import common.Thing;

import cern.jet.random.Uniform;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.contrib.KKLayout;

import rules.CreateInsiderProbabilisticallyRule;
import rules.ExecuteAllRule;
import rules.Rule;
import scenario.*;
import sensors.ContextPheromoneSensor;
import sensors.LocationAgentsCumulativePheromoneSensor;
import sensors.LocationMarkedSensor;
import sensors.LocationNumberAgentsSensor;
import sensors.LocationNumberNeighborsSensor;
import sensors.MetricAggregator;
import sensors.Sensor;
import sim.util.Double2D;
import simulator.MASONComponent;
import simulator.MASONLocationDraw;
import util.SOMAUtil;
import util.Sampler;

public abstract class Network {
	
	protected DefaultDirectedGraph networkGraph;
	protected Graph globalGraph;
	
	protected Scenario scenario;
	protected Thing graphContainer = new Thing();
	
	protected double somasAgentPercent = 0.0;
	
	protected double observerPercent = 0.05;
	
	protected Random random = new Random();

	protected Sampler sampler;

	public abstract void finalizeGraph();

	public Thing getGraphContainer()
	{
		return graphContainer;
	}
	
	public Graph<Location, Object> getGlobalGraph()
	{
		return globalGraph;
	}
	
	public DefaultDirectedGraph<Location, Object> getNetworkGraph()
	{
		return networkGraph;
	}
	
	/**
	 * Set up the network: nodes & connections
	 * @param components
	**/
	protected void setupLocations(ArrayList<Location> infoDepotList) 
	{
		DefaultDirectedGraph<Location, Edge> network = networkGraph;
		// Use JUNG's layout manager to make network look pretty
		SparseGraph jungGraph = new SparseGraph();
		HashMap<Location, SparseVertex> lvm = new HashMap<Location, SparseVertex>();

		for(Location vertex : network.vertexSet()) {
			SparseVertex jv = new SparseVertex();
			lvm.put(vertex, jv);
			jungGraph.addVertex(jv);
		}

		for(Edge edge : network.edgeSet()) {
			Location v1 = network.getEdgeSource(edge);
			Location v2 = network.getEdgeTarget(edge);
			jungGraph.addEdge(new UndirectedSparseEdge(lvm.get(v1), lvm.get(v2)));
		}

// 		FRLayout layout = new FRLayout(jungGraph);
//		layout.setAttractionMultiplier(0.5);
//		layout.setRepulsionMultiplier(0.5);
 		KKLayout layout = new KKLayout(jungGraph);
		layout.setDisconnectedDistanceMultiplier(.50);
		layout.setLengthFactor(1.15);
		layout.setMaxIterations(1000);
		Renderer r = new PluggableRenderer();
		VisualizationViewer vv = new VisualizationViewer( layout, r);
		layout.restart();
		while(!layout.incrementsAreDone())
			layout.advancePositions();

		{ // DEBUG
			for(Location vertex : network.vertexSet()) { 
				Thing neighborListContainer = new Thing();
				neighborListContainer.setProperty("object",  SOMAUtil.getEdgeNeighbors(network, vertex));

				scenario.components.add(new MASONComponent(vertex));
				scenario.components.get(scenario.components.size() - 1).order = 0; // Locations update themselves first, b/c agents depend on up to date location variables
				scenario.components.get(scenario.components.size() - 1).location =  
					new Double2D( layout.getCoordinates(lvm.get(vertex)) );

				ArrayList<Agent> agentList = null;
				if(vertex.hasBoxedProperty("agentList")) {				
				 agentList = (ArrayList<Agent>) vertex.getBoxedProperty("agentList");
				} else { // HACK
					agentList = new ArrayList<Agent>();
					vertex.setNewBoxedProperty("agentList", agentList);
				}

				vertex.setProperty("locationListContainer", neighborListContainer)
				.setNewBoxedProperty("draw", (new MASONLocationDraw())
						.setNewBoxedProperty("context", vertex)
						.setNewBoxedProperty("agentList", agentList))

						.setNewBoxedProperty("compromiseProbability", new Double(0.5))
						.setNewBoxedProperty("compromised", new Boolean(false));

				// Add location sensors to SO metric
				Thing metricAggregationContainer = new Thing();
				scenario.metricAggregationContainers.add(metricAggregationContainer);

				ArrayList<Thing> metricContainers = new ArrayList<Thing>();

				MetricAggregator metricAggregator = new MetricAggregator();
				metricAggregator.setNewBoxedProperty("metricContainers", metricContainers);
				metricAggregator.setProperty("resultContainer", metricAggregationContainer);


				ArrayList<Sensor> selfLocationSensors = new ArrayList<Sensor>();

				LocationNumberAgentsSensor selfLocationNumberAgentsSensor = new LocationNumberAgentsSensor();
				selfLocationSensors.add(selfLocationNumberAgentsSensor);

				LocationNumberNeighborsSensor selfLocationNumberNeighborsSensor = new LocationNumberNeighborsSensor();
				selfLocationSensors.add(selfLocationNumberNeighborsSensor);

				ContextPheromoneSensor selfLocationPheromoneSensor = new ContextPheromoneSensor();
				selfLocationSensors.add(selfLocationPheromoneSensor);

				LocationAgentsCumulativePheromoneSensor selfLocationAgentsCumulativePheromoneSensor = new LocationAgentsCumulativePheromoneSensor();
				selfLocationSensors.add(selfLocationAgentsCumulativePheromoneSensor);

				LocationMarkedSensor selfLocationMarkedSensor = new LocationMarkedSensor();
				selfLocationSensors.add(selfLocationMarkedSensor);

				scenario.arffWriter.init(selfLocationSensors.size());

				selfLocationSensors.add(metricAggregator); // added last so it has values to aggregate

				Thing agentListContainer = (Thing) vertex.getProperty("agentListContainer");

				Thing temp = null;
				temp = new Thing();
				selfLocationNumberAgentsSensor.setProperty("agentListContainer", agentListContainer);
				selfLocationNumberAgentsSensor.setProperty("sensorVariableContainer", temp);
				metricContainers.add(temp);

				temp = new Thing();
				selfLocationNumberNeighborsSensor.setProperty("locationListContainer", neighborListContainer);
				selfLocationNumberNeighborsSensor.setProperty("sensorVariableContainer", temp);
				metricContainers.add(temp);

				vertex.setNewBoxedProperty("pheromone", new Double(0));
				ArrayList<Actuator> locationActuatorList = new ArrayList<Actuator>();
				SetLocationPheromone setLocationPheromone = (SetLocationPheromone)
				(new SetLocationPheromone())
				.setNewBoxedProperty("sampler", sampler)
				.setProperty("pheromoneContainer", vertex.getProperty("pheromoneContainer"))
				.setProperty("agentListContainer", vertex.getProperty("agentListContainer"));
				locationActuatorList.add(setLocationPheromone);
				Thing actuatorsContainer = new Thing();
				actuatorsContainer.setProperty("object", locationActuatorList);
				vertex.setProperty("actuatorsContainer", actuatorsContainer);
				vertex.setNewBoxedProperty("isVitalNode", new Boolean(false));

			    // Add rules to location
				Machine tempMachine = null;
				
				ArrayList<Rule> locationRules = new ArrayList<Rule>();

				tempMachine = (Rule) (new ExecuteAllRule());
			    tempMachine.setProperty("actuatorsContainer", actuatorsContainer);
			    locationRules.add((Rule) tempMachine);

			    if(vertex.hasBoxedProperty("insiderPresent")) { // HACK
	    	
			    	tempMachine = (Rule) (new CreateInsiderProbabilisticallyRule());
			    	tempMachine.setNewBoxedProperty("creationProbability", new Double(0.05));
			        tempMachine.setNewBoxedProperty("scheduler", scenario.scheduler);
			        tempMachine.setNewBoxedProperty("descheduler", scenario.descheduler);
			        tempMachine.setNewBoxedProperty("location", vertex);
			        tempMachine.setProperty("graphContainer", graphContainer);
			        tempMachine.setNewBoxedProperty("infoDepotList", infoDepotList);
			        locationRules.add((Rule) tempMachine);
			    }
				vertex.setNewBoxedProperty("rules", locationRules);
				vertex.setNewBoxedProperty("sensors", selfLocationSensors);

				Thing locationContainer = new Thing();
				locationContainer.setProperty("object", vertex);
				temp = new Thing();
				selfLocationPheromoneSensor.setProperty("contextContainer", locationContainer);
				selfLocationPheromoneSensor.setProperty("sensorVariableContainer", temp);
				metricContainers.add(temp);

				temp = new Thing();
				selfLocationAgentsCumulativePheromoneSensor.setProperty("agentListContainer", agentListContainer);
				selfLocationAgentsCumulativePheromoneSensor.setProperty("sensorVariableContainer", temp);
				metricContainers.add(temp);

				temp = new Thing();
				selfLocationMarkedSensor.setProperty("contextContainer", locationContainer);
				selfLocationMarkedSensor.setProperty("sensorVariableContainer", temp);
				metricContainers.add(temp);

			}
		} // DEBUG
		
	}
	
	public void addSOMASAgents(double percentOfNodes)
	{
		somasAgentPercent = percentOfNodes;
	}
	

}
