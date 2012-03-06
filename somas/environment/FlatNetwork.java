package environment;

import java.util.ArrayList;

import metric.ActivityMetric;

import org.jgrapht.Graph;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.graph.ClassBasedVertexFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

import cern.jet.random.Uniform;

import actuators.Actuator;
import actuators.SetLocationPheromone;
import agent.Agent;
import agentGenerator.CompromiseAgentGenerator;
import agentGenerator.DDoSAgentGenerator;
import agentGenerator.SOMASAgentGenerator;

import common.Machine;
import common.Thing;

import rules.ExecuteAllRule;
import rules.Rule;
import scenario.*;
import simulator.MASONComponent;
import unknown.Chromosome;
import unknown.GraphUpdateComponent;
import util.NormalSampler;
import util.SOMAUtil;

public class FlatNetwork extends Network {
	
	ArrayList<Location> targetList; // target list for ddos agents
	ArrayList<Location> externalNodeList; // gateway nodes

	int numNodes;
	
	public FlatNetwork(Scenario scenario, int numNodes, int numEdges) 
	{
		this.scenario = scenario;
		this.numNodes = numNodes;
		
		sampler = new NormalSampler();
		sampler.setNewBoxedProperty("mean", Uniform.staticNextDoubleFromTo(-100.0, 100.0));
		sampler.setNewBoxedProperty("variance", new Double(10.0));	


		networkGraph = new DefaultDirectedGraph<Location, Edge>(Edge.class);
		
		// setup backbone and populate the backbone node list
		DefaultDirectedGraph<Location, Edge> backboneGraph = new DefaultDirectedGraph<Location, Edge>(Edge.class);
		(new RandomGraphGenerator<Location, Edge>(numNodes, numEdges)).generateGraph(backboneGraph, new ClassBasedVertexFactory<Location>(Location.class), null);
		ArrayList<Location> nodeList = new ArrayList<Location>(backboneGraph.vertexSet());

		// add backbone graph to entire network graph
		SOMAUtil.combineGraphs(networkGraph, backboneGraph);

		// external node list starts out with no external nodes
		externalNodeList = new ArrayList<Location>();
		
		
	}
	

	public void addCompromiseAgents(int numCompromisedAgents, boolean targetNetwork)
	{	
		if(targetNetwork)
			targetList = new ArrayList<Location>(networkGraph.vertexSet());
		else
			targetList = new ArrayList<Location>(); // DDoS agents don't send anything, only sit there and intrude
		
		for(int i = 0; i < numCompromisedAgents; i++) {
			Location location = new ArrayList<Location>(networkGraph.vertexSet()).get(new Double(Math.random() * (numNodes - 1)).intValue());
			
			ArrayList<Agent> agentList = new ArrayList<Agent>();
			location.setNewBoxedProperty("agentList", agentList);

			Agent agent = (Agent) ((Machine) (new CompromiseAgentGenerator())
					.setNewBoxedProperty("location", location)
					.setNewBoxedProperty("targetList", targetList) // DEBUG
					.setNewBoxedProperty("infoDepotList", externalNodeList)
					.setProperty("graphContainer", graphContainer)
					.setNewBoxedProperty("scheduler", scenario.scheduler)
					.setNewBoxedProperty("descheduler", scenario.descheduler))
					.execute()
					.getBoxedProperty("result");
			agentList.add(agent);
			
			scenario.components.add(new MASONComponent(agent));
			scenario.components.get(scenario.components.size() - 1).order = 0; 
		}
	}
	
	public void addDDoSAgents(int numDDoSAgents, int numTargets)
	{

		targetList = new ArrayList<Location>(); // DDoS agents don't send anything, only sit there and intrude
		
		for(int i = 0; i < numDDoSAgents; i++) {
			Location location = new ArrayList<Location>(networkGraph.vertexSet()).get(new Double(Math.random() * (numNodes - 1)).intValue());

			ArrayList<Agent> agentList = location.hasBoxedProperty("agentList") ? (ArrayList<Agent>) location.getBoxedProperty("agentList") : new ArrayList<Agent>();
			location.setNewBoxedProperty("agentList", agentList);
			
			Agent agent = (Agent) ((Machine) (new DDoSAgentGenerator())
					.setNewBoxedProperty("location", location)
					.setNewBoxedProperty("agentList", agentList)
					.setNewBoxedProperty("targetList", targetList) // DEBUG
					.setProperty("graphContainer", graphContainer)
					.setNewBoxedProperty("scheduler", scenario.scheduler)
					.setNewBoxedProperty("descheduler", scenario.descheduler))
					.execute()
					.getBoxedProperty("result");
			agentList.add(agent);

			scenario.components.add(new MASONComponent(agent));
			scenario.components.get(scenario.components.size() - 1).order = 1; 
		}
	
		for(int i = 0; i < numTargets; i++) {
			Location location = new ArrayList<Location>(networkGraph.vertexSet()).get(new Double(Math.random() * (numNodes - 1)).intValue());
			
			targetList.add(location);
		}
	}
	
	public ArrayList<Location> getTargetList()
	{
		return targetList;
	}
	
	private void initializeLocations()
	{
		ArrayList<Location> globalLocationList = new ArrayList<Location>(networkGraph.vertexSet());
		globalLocationList.removeAll(externalNodeList);
		int numGlobalLocations = globalLocationList.size();
		
		for(int i = 0; i < numGlobalLocations; i++) {

			// Set up chromosome decoder
			ArrayList<Chromosome> chromosomeList = scenario.createChromoList();

			Location location = (Location) (globalLocationList.get(i));
			
			if(Math.random() < this.observerPercent){
				location.setProperty("observerID", new Integer(i));
			}

			ArrayList<Agent> agentList = new ArrayList<Agent>();
			if(random.nextDouble() <= somasAgentPercent)
		    {
				SOMASAgentGenerator somasAgentGenerator = new SOMASAgentGenerator(chromosomeList, scenario.scheduler, scenario.descheduler);
				Agent agent = somasAgentGenerator.generateAgent();
	
				agent.setBoxedProperty("location", location);
	
				// All agents use the same scheduler and descheduler
				// TODO : this doesn't really need to be boxed
				agent.setNewBoxedProperty("scheduler", scenario.scheduler);
				agent.setNewBoxedProperty("descheduler", scenario.descheduler); 
	
				// Set up location
			
				agentList.add(agent);
				
				scenario.components.add(new MASONComponent(agent));
				scenario.components.get(scenario.components.size() - 1).order = 1;  
		    }
			location
			.setNewBoxedProperty("isVitalNode", new Boolean(false))
			.setNewBoxedProperty("agentList", agentList);
					

		    // Location pheromone update 
		    location.setNewBoxedProperty("pheromone", new Double(0));
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
	public void finalizeGraph() 
	{
		initializeLocations();
		globalGraph = (Graph<Location, Edge>) networkGraph.clone();
		graphContainer.setProperty("object", globalGraph);
		
		Machine graphUpdateComponent = (Machine) (new GraphUpdateComponent())
		.setNewBoxedProperty("originalGraph", networkGraph)
		.setNewBoxedProperty("descheduler", scenario.descheduler)
		.setProperty("resultContainer", graphContainer);
		scenario.components.add(new MASONComponent(graphUpdateComponent));
		scenario.components.get(scenario.components.size() - 1).order = 3; 
		
		setupLocations(targetList);
	}
}
