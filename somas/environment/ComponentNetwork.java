package environment;

import java.util.ArrayList;
import java.util.Collections;

import metric.ActivityMetric;
import metric.MetricAdder;

import org.jgrapht.Graph;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.generate.WheelGraphGenerator;
import org.jgrapht.graph.ClassBasedVertexFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

import cern.jet.random.Uniform;

import actuators.Actuator;
import actuators.SetLocationPheromone;
import agent.Agent;
import agentGenerator.CompromiseAgentGenerator;
import agentGenerator.DDoSAgentGenerator;
import agentGenerator.ReceiverAgentGenerator;
import agentGenerator.SOMASAgentGenerator;
import agentGenerator.SenderAgentGenerator;

import common.Machine;
import common.Thing;

import rules.ExecuteAllRule;
import rules.Rule;
import scenario.Scenario;
import simulator.MASONComponent;
import unknown.Chromosome;
import unknown.GraphUpdateComponent;
import util.NormalSampler;
import util.SOMAUtil;
import util.Sampler;

public class ComponentNetwork extends Network
{
	
	ArrayList<Location> backboneNodeList;
	ArrayList<Location> externalNodeList = new ArrayList<Location>();
	ArrayList<Location> hubNodes = new ArrayList<Location>();
	ArrayList<Location> receiverList = new ArrayList<Location>();
	ArrayList<Location> senderList = new ArrayList<Location>();
	ArrayList<Location> insiderLocationList = new ArrayList<Location>();
	
	ArrayList<Thing> goodInfoMetricContainerList = new ArrayList<Thing>();
	Thing goodInfoMetricContainerListContainer = new Thing();
	 // The bad info metric records how many information
	// packets the malicious agents are able to steal and
	// send off network.
	ArrayList<Thing> badInfoMetricContainerList = new ArrayList<Thing>();
	Thing badInfoMetricContainerListContainer = new Thing();
	{
		badInfoMetricContainerListContainer.setProperty("object", badInfoMetricContainerList);
		goodInfoMetricContainerListContainer.setProperty("object", goodInfoMetricContainerList);
	}
	
	
	int numBackboneNodes;
	int numReceivers = 0;
	int numSenders = 0;
	
	public ComponentNetwork(Scenario scenario, int numBackboneNodes, int numExternalNodes, int[] lanSizes) 
	{
		this.scenario = scenario;
		this.numBackboneNodes = numBackboneNodes;
		
		sampler = new NormalSampler();
	    sampler.setNewBoxedProperty("mean", Uniform.staticNextDoubleFromTo(Double.MIN_VALUE, Double.MAX_VALUE));
	    sampler.setNewBoxedProperty("variance", new Double(10.0));
		
		networkGraph = new DefaultDirectedGraph<Location, Edge>(Edge.class);
		
		/*
		  --------
		  Backbone
		  --------
		*/
		int backboneEdges = numBackboneNodes * numBackboneNodes; // Backbone is fully interconnected, as is usual.
		DefaultDirectedGraph<Location, Edge> backbone = new DefaultDirectedGraph<Location, Edge>(Edge.class);
		(new RandomGraphGenerator<Location, Edge>(numBackboneNodes, backboneEdges)).generateGraph(backbone, new ClassBasedVertexFactory<Location>(Location.class), null);

	
		SOMAUtil.combineGraphs(networkGraph, backbone);
		
		backboneNodeList = new ArrayList<Location>(networkGraph.vertexSet());
		for(Location location : backboneNodeList)
			location.setNewBoxedProperty("backboneNode", new Boolean(true));
		
		addExternalNodes(numExternalNodes);
		addLans(lanSizes);
		
		
		
	}
	
	private void addExternalNodes(int numExternalNodes)
	{
		/*
		  ----------------------------------------
		  Create external links and external nodes
		  ----------------------------------------
		*/

		int externalNodesStart = 0;
		for(int i = externalNodesStart; i < externalNodesStart+numExternalNodes; i++) {
		    ArrayList<Agent> agentList = new ArrayList<Agent>();
		    Location location = (Location) (new Location());
		    location.setNewBoxedProperty("isVitalNode", new Boolean(false));
		    location.setNewBoxedProperty("compromised", new Boolean(true));
		    location.setNewBoxedProperty("agentList", agentList);
		    location.setNewBoxedProperty("externalNode", new Boolean(true)); // HACK so external nodes can be identified
	
		    // Attach new node to network.
		    externalNodeList.add(location);
		    networkGraph.addVertex(location);
		    networkGraph.addEdge(backboneNodeList.get(i), location);
		}
	}
	
	public void addCompromiseAgents(boolean disableCreateAgent)
	{
		for(Location location : externalNodeList)
		{
			ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");
			
			// Compromise agent : steals information and install DDoS agents on nodes.
		    CompromiseAgentGenerator compromiseAgentGenerator = new CompromiseAgentGenerator();
		    compromiseAgentGenerator.setNewBoxedProperty("location", location);
		    compromiseAgentGenerator.setNewBoxedProperty("targetList", senderList); // DEBUG
		    compromiseAgentGenerator.setNewBoxedProperty("infoDepotList", externalNodeList);
		    compromiseAgentGenerator.setProperty("graphContainer", graphContainer);
		    compromiseAgentGenerator.setNewBoxedProperty("scheduler", scenario.scheduler);
		    compromiseAgentGenerator.setNewBoxedProperty("descheduler", scenario.descheduler);
		    if(disableCreateAgent)
		    	compromiseAgentGenerator.setNewBoxedProperty("disableCreateAgentActuator", new Boolean(true));
		    compromiseAgentGenerator.execute();
		    Agent agent = (Agent) compromiseAgentGenerator.getBoxedProperty("result");
		    agentList.add(agent);
	
		    scenario.components.add(new MASONComponent(agent));
		    scenario.components.get(scenario.components.size() - 1).order = 1; 
			
		}
	}
	
	public void addBadInfoReceiverAgents()
	{
		for(Location location : externalNodeList)
		{
			ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");
			
			// Bad info receiver agent :  collects and count stolen information.
		    Thing badInfoContainer = new Thing();
		    ReceiverAgentGenerator receiverAgentGenerator = new ReceiverAgentGenerator();
		    receiverAgentGenerator.setNewBoxedProperty("location", location);
		    receiverAgentGenerator.setProperty("infoCounterContainer", badInfoContainer);
		    receiverAgentGenerator.setNewBoxedProperty("scheduler", scenario.scheduler);
		    receiverAgentGenerator.setNewBoxedProperty("descheduler", scenario.descheduler);
		    receiverAgentGenerator.execute();
		    Agent agent = (Agent) receiverAgentGenerator.getBoxedProperty("result");
		    agentList.add(agent); 
		    badInfoMetricContainerList.add(badInfoContainer);
	
		    scenario.components.add(new MASONComponent(agent));
		    scenario.components.get(scenario.components.size() - 1).order = 1; 
			
		}
	}
	
	public void addDDoSAgents()
	{
		for(Location location : externalNodeList)
		{
			ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");
			
			// DDoS agent : sends packets target nodes, attempting to overload and deactivate node.
		    DDoSAgentGenerator ddosAgentGenerator = new DDoSAgentGenerator();
		    ddosAgentGenerator.setNewBoxedProperty("location", location);
		    ddosAgentGenerator.setNewBoxedProperty("targetList", senderList); // DEBUG
		    ddosAgentGenerator.setProperty("graphContainer", graphContainer);
		    ddosAgentGenerator.setNewBoxedProperty("scheduler", scenario.scheduler);
		    ddosAgentGenerator.setNewBoxedProperty("descheduler", scenario.descheduler);
		    ddosAgentGenerator.execute();
				
		    Agent agent = (Agent) ddosAgentGenerator.getBoxedProperty("result");
		    agentList.add(agent);
	
		    scenario.components.add(new MASONComponent(agent));
		    scenario.components.get(scenario.components.size() - 1).order = 1; 
			
		}
	}
	
	
	private void addLans(int[] lanSizes)
	{
		/*
		  -------------------
		  Local Area Networks
		  -------------------
		*/
	
		// Generate the LAN nodes
		for(int i = 0; i < numBackboneNodes; i++) {
		    DefaultDirectedGraph<Location, Edge> lan = new DefaultDirectedGraph<Location, Edge>(Edge.class);
		    WheelGraphGenerator<Location, Edge> wheelGraphGenerator = new WheelGraphGenerator<Location, Edge>(lanSizes[i]);
		    wheelGraphGenerator.generateGraph(lan, new ClassBasedVertexFactory<Location>(Location.class), null);
	
		    Location newNode = (new ArrayList<Location>(lan.vertexSet())).get(0);
		    hubNodes.add(newNode);
		    networkGraph.addVertex(newNode);
		    networkGraph.addEdge(backboneNodeList.get(i), newNode);
	
		    SOMAUtil.combineGraphs(networkGraph, lan);
		}
	}

	public void initializeLocations()
	{
		/* Set up rest of graph by adding rest of agents.
		
		   1. SOMAS agents 
	
		   These agents are the network defense, and are generated
		   from the decision space.  The decision space is converted
		   into a set of chromosomes, and the first chromosome is used
		   to generate the SOMAS agent parameters.  The same agent
		   parameter values are used for each SOMAS agent.
	
		   2. Receiver and sender agents
	
		   These two agents establish the information flows on the
		   network which the malicious agents attempt to disrupt and
		   exploit.
		*/
		ArrayList<Location> globalLocationList = new ArrayList<Location>(networkGraph.vertexSet());
		globalLocationList.removeAll(externalNodeList);
		int numGlobalLocations = globalLocationList.size();
	
	
		for(int i = 0; i < numGlobalLocations; i++) {
		    /*
		      ------------
		      SOMAS Agents
		      ------------
		    */
	
		    // Add SOMAS agents to each network location.
		    Location location = (Location) (globalLocationList.get(i));
		    
		    if(Math.random() < this.observerPercent){
				location.setProperty("observerID", new Integer(i));
			}
	
		    ArrayList<Agent> agentList;
		    if(location.hasBoxedProperty("agentList"))
		    	agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");
		    else
		    	agentList = new ArrayList<Agent>();
		    if(random.nextDouble() <= somasAgentPercent)
		    {
		    	// Initialize agent's chromosome store.
			    ArrayList<Chromosome> chromosomeList = scenario.createChromoList();
		
				SOMASAgentGenerator somasAgentGenerator = new SOMASAgentGenerator(chromosomeList, scenario.scheduler, scenario.descheduler);
				Agent agent = somasAgentGenerator.generateAgent();
		
			    agent.setBoxedProperty("location", location);
		
			    // All agents use the same scheduler and descheduler
			    // TODO [#B] this doesn't really need to be boxed
			    agent.setNewBoxedProperty("scheduler", scenario.scheduler);
			    agent.setNewBoxedProperty("descheduler", scenario.descheduler); 
		
			    scenario.components.add(new MASONComponent(agent));
			    scenario.components.get(scenario.components.size() - 1).order = 1;            
		
			    agentList.add(agent);
		    }
		    
		    /*
		      -------------------------------
		      Generic location initialization
		      -------------------------------
		    */
		    // Set up location.  Adds the agentlist, and initializes
		    // parameters.
		    // 
		    // "isVitalNode" : whether the node has been
		    // deactivated.
		    // 
		    // "compromised" : whether a compromise agent
		    // has installed a DDoS agent on node.
		    // 
		    // "agentList" : current set of agents at
		    // node.
		    
		    location.setNewBoxedProperty("isVitalNode", new Boolean(false));
		    location.setNewBoxedProperty("compromised", new Boolean(false));
		    location.setNewBoxedProperty("agentList", agentList);
	
		    // Location pheromone update.
		    // Randomly initialize a random variable.  Used for noisy
		    // reading of pheromone.
		    location.setNewBoxedProperty("pheromone", new Double(0));
	
		    ArrayList<Actuator> locationActuatorList = new ArrayList<Actuator>();
		    SetLocationPheromone setLocationPheromone = (SetLocationPheromone) (new SetLocationPheromone());
		    setLocationPheromone.setNewBoxedProperty("sampler", sampler);
		    setLocationPheromone.setProperty("pheromoneContainer", location.getProperty("pheromoneContainer"));
		    setLocationPheromone.setProperty("agentListContainer", location.getProperty("agentListContainer"));
		    locationActuatorList.add(setLocationPheromone);
	
		    // Location actuators
		    Thing actuatorsContainer = new Thing();
		    actuatorsContainer.setProperty("object", locationActuatorList);
		    location.setProperty("actuatorsContainer", actuatorsContainer);
	
		    // Add rules to location
		    ArrayList<Rule> locationRules = new ArrayList<Rule>();
		    Rule rule = new ExecuteAllRule();
		    rule.setProperty("actuatorsContainer", actuatorsContainer);
		    locationRules.add((Rule) rule);
	
		    /*
		      ---------------
		      Activity metric
		      ---------------
		    */
		    // The activity metric measures the difference between
		    // current and previous count of agents on node.
		    ActivityMetric activityMetric = new ActivityMetric();
		    activityMetric.setNewBoxedProperty("location", location);
		    activityMetric.setNewBoxedProperty("previousAgentCount", new Double(0));
		    activityMetric.setNewBoxedProperty("currentAgentCount", new Double(0));
		    activityMetric.setProperty("objectiveValueContainer", scenario.objectiveValueContainers.get(0));
		    scenario.components.add(new MASONComponent(activityMetric));
		    scenario.components.get(scenario.components.size() - 1).order = 2; // Ditto
		}
	}
	
	public void addGoodInfoReceivers(int numReceivers)
	{
		this.numReceivers += numReceivers;
	}
	public void initializeGoodInfoReceivers()
	{
		/*
		  ---------------------------
		  Receiver and Sender Agents 
		  ---------------------------
		*/
		// Add receiver and sender agents to the network.  These
		// agents relay information, and the goal of the SOMAS agents
		// is to maximize their information flow, i.e. keep DDoS
		// agents from deactivating the sender nodes and compromise
		// agents from stealing the relayed information.
		ArrayList<Location> potentialReceiverList = new ArrayList<Location>(networkGraph.vertexSet());
		
		// The following keeps receiver agents from being placed on the
		// external nodes, since the external nodes are not
		// communication nodes on a network.
		potentialReceiverList.removeAll(externalNodeList);
		
	
		// The following keeps receiver agents from being placed on the
		// backbone, since the backbone nodes are usually not
		// communication nodes on a network.
		potentialReceiverList.removeAll(backboneNodeList);
	
	
		// Randomly select a node to hold the receiver agent, without
		// replacement.
		for(int i = 0; i < numReceivers; i++) {
		    Integer receiverID = (new Double(Math.random() * (potentialReceiverList.size() - 1))).intValue();
		    Location location = potentialReceiverList.get(receiverID);
		    receiverList.add(location);
		    potentialReceiverList.remove(receiverID);
		    ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");

		    Thing goodInfoCounterContainer = new Thing();
		    ReceiverAgentGenerator receiverAgentGenerator = new ReceiverAgentGenerator();
		    receiverAgentGenerator.setNewBoxedProperty("location", location);
		    receiverAgentGenerator.setProperty("infoCounterContainer", goodInfoCounterContainer);
		    receiverAgentGenerator.setProperty("graphContainer", graphContainer);
		    receiverAgentGenerator.setNewBoxedProperty("scheduler", scenario.scheduler);
		    receiverAgentGenerator.setNewBoxedProperty("descheduler", scenario.descheduler);
		    receiverAgentGenerator.execute();
		    Agent receiverAgent = (Agent) receiverAgentGenerator.getBoxedProperty("result");
		    agentList.add(receiverAgent);
		    location.setBoxedProperty("agentList", agentList);
	
		    scenario.components.add(new MASONComponent(receiverAgent));
		    scenario.components.get(scenario.components.size() - 1).order = 1; 
	
		    Thing badInfoCounterContainer = new Thing();
		    location.setProperty("badInfoCounterContainer", badInfoCounterContainer);
	
		    goodInfoMetricContainerList.add(goodInfoCounterContainer);
		    badInfoMetricContainerList.add(badInfoCounterContainer);
		}
	}
	public void addGoodInfoSenders(int numSenders)
	{
		this.numSenders += numSenders;
	}
	public void initializeGoodInfoSenders()
	{
		// As with the receiver agent, the following keeps sender agents
		// from being placed on the backbone and external nodes, since they are
		// not communication nodes on a network.
		ArrayList<Location> potentialSenderList = new ArrayList<Location>(networkGraph.vertexSet());
		potentialSenderList.removeAll(backboneNodeList);
		potentialSenderList.removeAll(externalNodeList);
	
		// Again, as with the receiver agents, randomly select a node
		// to hold the sender agent, without replacement.
		for(int i = 0; i < numSenders; i++) {
		    Integer senderID = (new Double((Math.random() * (potentialSenderList.size() - 1)))).intValue();
		    Location location = potentialSenderList.get(senderID);
		    potentialSenderList.remove(senderID);
		    senderList.add(location);  // DEBUG
		    ArrayList<Agent> agentList  = (ArrayList<Agent>) location.getBoxedProperty("agentList");
	
		    SenderAgentGenerator senderAgentGenerator = new SenderAgentGenerator();
		    senderAgentGenerator.setNewBoxedProperty("location", location);
		    senderAgentGenerator.setNewBoxedProperty("targetList", receiverList);
		    senderAgentGenerator.setNewBoxedProperty("lifeCounter", new Double(140));
		    senderAgentGenerator.setProperty("graphContainer", graphContainer);
		    senderAgentGenerator.setNewBoxedProperty("scheduler", scenario.scheduler);
		    senderAgentGenerator.setNewBoxedProperty("descheduler", scenario.descheduler);
		    senderAgentGenerator.execute();
		    Agent senderAgent = (Agent)  senderAgentGenerator.getBoxedProperty("result");
	
		    agentList.add(senderAgent);
	
		    scenario.components.add(new MASONComponent(senderAgent));
		    scenario.components.get(scenario.components.size() - 1).order = 1; 
		}
	}
	
	
	public void addInsiders(int numInsiders)
	{
		// Initialize insider locations, where insiders will inject compromise agents onto the network
		ArrayList<Location> locationPopulation = new ArrayList<Location>(networkGraph.vertexSet());
		locationPopulation.removeAll(externalNodeList);
		
		Collections.shuffle(locationPopulation);
		for(int i = 0; i < numInsiders; i++) {
			insiderLocationList.add(locationPopulation.get(i));
		}
		for(Location l : insiderLocationList) {
			l.setNewBoxedProperty("insiderPresent", new Boolean(true));
		}
	}
	
	@Override
	public void finalizeGraph() {
		
		initializeLocations();
		initializeGoodInfoReceivers();
		initializeGoodInfoSenders();
		/*
		  ==================
		  Initialize metrics
		  ==================
		*/
		// Initialize all the info metrics' counters.
		for(Thing container : goodInfoMetricContainerList)
		    container.setProperty("object", new Double(0));
		scenario.goodInfoMetric = (Machine) (new MetricAdder());
		scenario.goodInfoMetric.setProperty("metricContainerListContainer", goodInfoMetricContainerListContainer);
		goodInfoMetricContainerListContainer.setProperty("resultContainer", scenario.objectiveValueContainers.get(1));
	
		for(Thing container : badInfoMetricContainerList)
		    container.setProperty("object", new Double(0));
		scenario.badInfoMetric = (Machine) (new MetricAdder());
		scenario.badInfoMetric.setProperty("metricContainerListContainer", badInfoMetricContainerListContainer);
		badInfoMetricContainerListContainer.setProperty("resultContainer", scenario.objectiveValueContainers.get(2));
	
		globalGraph = (Graph<Location, Edge>) networkGraph.clone();
		graphContainer.setProperty("object", globalGraph);
	
		Machine graphUpdateComponent = (Machine) (new GraphUpdateComponent());
		graphUpdateComponent.setNewBoxedProperty("originalGraph", networkGraph);
		graphUpdateComponent.setNewBoxedProperty("descheduler", scenario.descheduler);
		graphUpdateComponent.setProperty("resultContainer", graphContainer);
		scenario.components.add(new MASONComponent(graphUpdateComponent));
		scenario.components.get(scenario.components.size() - 1).order = 3; 
		
		
		setupLocations(externalNodeList);
		
	}

}
