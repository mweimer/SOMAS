package environment;

import java.util.ArrayList;
import java.util.HashMap;

import metric.ActivityMetric;

import org.jgrapht.Graph;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.ClassBasedVertexFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

import actuators.Actuator;
import actuators.SetLocationPheromone;
import agent.Agent;
import agentGenerator.SOMASAgentGenerator;

import rules.ExecuteAllRule;
import rules.Rule;
import scenario.Scenario;
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
import unknown.Chromosome;
import util.SOMAUtil;
import util.Sampler;

import common.Thing;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.contrib.KKLayout;

public class VEFlatNetwork extends Network
{

	public VEFlatNetwork(Scenario scenario, int numVertices, int numEdges) 
	{
		this.scenario = scenario;
		networkGraph = new DefaultDirectedGraph<Location, Thing>(new ClassBasedEdgeFactory<Location, Thing>(Thing.class));
		
		RandomGraphGenerator<Location, Thing> randomGraphGenerator = new RandomGraphGenerator(numVertices, numEdges);
		randomGraphGenerator.generateGraph(networkGraph, new ClassBasedVertexFactory<Location>(Location.class), null);
		
		ArrayList<Location> globalLocationList = new ArrayList<Location>(networkGraph.vertexSet());
		int numGlobalLocations = globalLocationList.size();
		
		Graph<Location, Thing> graph = globalGraph;
		
		// Use JUNG's layout manager to make network look pretty
		SparseGraph jungGraph = new SparseGraph();
		HashMap<Location, SparseVertex> lvm = new HashMap<Location, SparseVertex>();
	
		for(Location vertex : graph.vertexSet()) {
			SparseVertex jv = new SparseVertex();
			lvm.put(vertex, jv);
			jungGraph.addVertex(jv);
		}
	
		for(Thing edge : graph.edgeSet()) {
			Location v1 = graph.getEdgeSource(edge);
			Location v2 = graph.getEdgeTarget(edge);
			jungGraph.addEdge(new UndirectedSparseEdge(lvm.get(v1), lvm.get(v2)));
		}
	
		KKLayout layout = new KKLayout(jungGraph);
		layout.setDisconnectedDistanceMultiplier(.50);
		layout.setLengthFactor(1.15);
		Renderer r = new PluggableRenderer();
		VisualizationViewer vv = new VisualizationViewer( layout, r);
		layout.restart();
		while(!layout.incrementsAreDone())
			layout.advancePositions();
		
	    for(int i = 0; i < numGlobalLocations; i++) {
	
		    // Set up chromosome decoder
		    ArrayList<Chromosome> chromosomeList = scenario.createChromoList();
	
		    Location location = globalLocationList.get(i);
	
		    ArrayList<Agent> agentList = new ArrayList<Agent>();

			SOMASAgentGenerator somasAgentGenerator = new SOMASAgentGenerator(chromosomeList, scenario.scheduler, scenario.descheduler);
			Agent agent = somasAgentGenerator.generateAgent();
	
		    agent.setBoxedProperty("location", location);
	
		    // All agents use the same scheduler and descheduler
		    // TODO : this doesn't really need to be boxed
		    agent.setNewBoxedProperty("scheduler", scenario.scheduler);
		    agent.setNewBoxedProperty("descheduler", scenario.descheduler); 
	
		    
		    agentList.add(agent);
		    
			Thing agentListContainer = new Thing();
		    agentListContainer.setProperty("object", agentList);
		    location.setProperty("agentListContainer", agentListContainer);
	
		    Thing neighborListContainer = new Thing();
		    neighborListContainer.setProperty("object",  SOMAUtil.getNeighbors(graph, location));
		    location.setProperty("locationListContainer", neighborListContainer);
	
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
		    
		    Thing temp = null;
		    temp = new Thing();
		    selfLocationNumberAgentsSensor.setProperty("agentListContainer", agentListContainer);
		    selfLocationNumberAgentsSensor.setProperty("sensorVariableContainer", temp);
		    metricContainers.add(temp);
		
		    temp = new Thing();
		    selfLocationNumberNeighborsSensor.setProperty("locationListContainer", neighborListContainer);
		    selfLocationNumberNeighborsSensor.setProperty("sensorVariableContainer", temp);
		    metricContainers.add(temp);
	
		    Thing locationContainer = new Thing();
		    locationContainer.setProperty("object", location);
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
	
		    // Set up location
		    Thing agentContainer = new Thing(); // HACK
		    agentContainer.setProperty("object", agent);
		    location.setNewBoxedProperty("draw", (new MASONLocationDraw())
						 .setNewBoxedProperty("agentList", agentList));
	
		    // Location pheromone update 
		    location.setNewBoxedProperty("pheromone", new Double(0));
		    Sampler sampler = (Sampler) this.sampler;
		    ArrayList<Actuator> locationActuatorList = new ArrayList<Actuator>();
		    SetLocationPheromone setLocationPheromone = (SetLocationPheromone)
			(new SetLocationPheromone())
			.setNewBoxedProperty("sampler", sampler)
			.setProperty("pheromoneContainer", location.getProperty("pheromoneContainer"))
			.setProperty("agentListContainer", location.getProperty("agentListContainer"));
		    locationActuatorList.add(setLocationPheromone);
	
		    // Location actuators
		    Thing actuatorsContainer = new Thing();
		    actuatorsContainer.setProperty("object", locationActuatorList);
		    location.setProperty("actuatorsContainer", actuatorsContainer);
		    location.setNewBoxedProperty("isVitalNode", new Boolean(false));
	
		    // Add items to location
		    ArrayList<Rule> locationRules = new ArrayList<Rule>();
		    locationRules.add((Rule) (new ExecuteAllRule())
				      .setProperty("actuatorsContainer", actuatorsContainer));
	
		    location.setNewBoxedProperty("rules", locationRules);
		    location.setNewBoxedProperty("sensors", selfLocationSensors);
	
		    scenario.components.add(new MASONComponent(agent));
		    scenario.components.get(scenario.components.size() - 1).order = 1;		
	
		    scenario.components.add(new MASONComponent(location));
		    scenario.components.get(scenario.components.size() - 1).order = 0; // Locations update themselves first, b/c agents depend on up to date location variables
		    scenario.components.get(scenario.components.size() - 1).location = 
				new Double2D( layout.getCoordinates(lvm.get(location)) );
	
				
		    ActivityMetric activityMetric = new ActivityMetric();
		    activityMetric.setNewBoxedProperty("location", location);
		    activityMetric.setNewBoxedProperty("previousAgentCount", new Double(0));
		    activityMetric.setNewBoxedProperty("currentAgentCount", new Double(0));
		    activityMetric.setProperty("objectiveValueContainer", scenario.objectiveValueContainers.get(0));
		    scenario.components.add(new MASONComponent(activityMetric));
		    scenario.components.get(scenario.components.size() - 1).order = 2; // Ditto
		}
	}
	@Override
	public void finalizeGraph() {
		globalGraph = (Graph<Location, Edge>) networkGraph.clone();
		graphContainer.setProperty("object", globalGraph);
	}
	
}
