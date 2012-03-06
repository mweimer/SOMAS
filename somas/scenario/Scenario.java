/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import metric.ARFFWriter;

import org.jgrapht.graph.*;

import classification.*;

import com.sun.org.apache.bcel.internal.generic.NEW;

import representations.IntBitRep;
import rules.CreateInsiderProbabilisticallyRule;
import rules.ExecuteAllRule;
import rules.Rule;
import sensors.ContextPheromoneSensor;
import sensors.LocationAgentsCumulativePheromoneSensor;
import sensors.LocationMarkedSensor;
import sensors.LocationNumberAgentsSensor;
import sensors.LocationNumberNeighborsSensor;
import sensors.MetricAggregator;
import sensors.Sensor;
import sim.util.Double2D;
import simulator.*;
import unknown.Chromosome;
import unknown.IntBitToDelimitedDoubleChromosomeDecoder;
import util.NormalSampler;
import util.SOMAUtil;
import util.Sampler;
import visualization.ChromoVisualizer;
import actuators.Actuator;
import actuators.SetLocationPheromone;
import agent.Agent;
import agentGenerator.SOMASAgentGenerator;

import cern.jet.random.Uniform;
import common.*;

import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import environment.Edge;
import environment.Location;
import environment.Network;

import common.Thing;

import edu.uci.ics.jung.visualization.contrib.KKLayout;

abstract public class Scenario implements Runnable{
	
	private ArrayList<Integer[]> decisionSpaceList = new ArrayList<Integer[]>();
	protected Double[] objectiveValues = null;
	
	public MASONSimDeScheduler descheduler = null;
	public MASONSimScheduler scheduler = null;
	protected boolean visualize = false;
	public ARFFWriter arffWriter = new ARFFWriter();
	
	protected Network network;
	
	public ClassificationMetrics classmets = null;
	public static final Integer numclasssteps = new Integer(100);
	
	protected static Class simClass = MASONSim.class;
	protected ArrayList<Machine> schedulingMachineList = new ArrayList<Machine>();
    protected Thing errorTypeContainer = new Thing();
    public ArrayList<Thing> objectiveValueContainers = new ArrayList<Thing>();
    public static Machine goodInfoMetric = null;
    public static Machine badInfoMetric = null;
    public ArrayList<MASONComponent> components;
    public ArrayList<Thing> metricAggregationContainers;
    
    private Random random = new Random();
    
	private static Map<String, Class> nameScenClassMap = new HashMap<String, Class>();
	static {
		nameScenClassMap.put("CompetitionScenario", CompetitionScenario.class);
		nameScenClassMap.put("DDoSScenario", DDoSScenario.class);
		nameScenClassMap.put("IntrusionEliminationScenario", IntrusionEliminationScenario.class);
		nameScenClassMap.put("VitalElementScenario", VitalElementScenario.class);
		nameScenClassMap.put("InfoWarScenario", InfoWarScenario.class);
		nameScenClassMap.put("DataExtractionScenario", DataExtractionScenario.class);
		nameScenClassMap.put("InsiderAttackScenario", InsiderAttackScenario.class);
	}
	
	private static Map<String, Class> nameClassificationMap = new HashMap<String, Class>();
	static {
		nameClassificationMap.put("CompetitionClassification", CompetitionClassification.class);
		nameClassificationMap.put("DDoSClassification", DDoSClassification.class);
		nameClassificationMap.put("IntrusionEliminationClassification", IntrusionEliminationClassification.class);
		nameClassificationMap.put("InfoWarClassification", InfoWarClassification.class);
		nameClassificationMap.put("DataExtractionClassification", DataExtractionClassification.class);
		nameClassificationMap.put("InsiderAttackClassification", InsiderAttackClassification.class);
	}
	
	private static Map<String, Integer> numObjScenMap = new HashMap<String, Integer>();
	static {
		numObjScenMap.put("CompetitionScenario", CompetitionScenario.numObjectives);
		numObjScenMap.put("DDoSScenario", DDoSScenario.numObjectives);
		numObjScenMap.put("IntrusionEliminationScenario", IntrusionEliminationScenario.numObjectives);
		numObjScenMap.put("VitalElementScenario", VitalElementScenario.numObjectives);
		numObjScenMap.put("InfoWarScenario", InfoWarScenario.numObjectives);
		numObjScenMap.put("DataExtractionScenario", DataExtractionScenario.numObjectives);
		numObjScenMap.put("InsiderAttackScenario", InsiderAttackScenario.numObjectives);
	}
	
	private static Map<String, String[]> scenMetricNamesMap = new HashMap<String, String[]>();
	static {
		scenMetricNamesMap.put("CompetitionScenario", CompetitionScenario.metricNames);
		scenMetricNamesMap.put("DDoSScenario", DDoSScenario.metricNames);
		scenMetricNamesMap.put("IntrusionEliminationScenario", IntrusionEliminationScenario.metricNames);
		scenMetricNamesMap.put("VitalElementScenario", VitalElementScenario.metricNames);
		scenMetricNamesMap.put("InfoWarScenario", InfoWarScenario.metricNames);
		scenMetricNamesMap.put("DataExtractionScenario", DataExtractionScenario.metricNames);
		scenMetricNamesMap.put("InsiderAttackScenario", InsiderAttackScenario.metricNames);
	}
		
	public static int getNumObjectives(String scenarioName) 
	{ 
		if(numObjScenMap.containsKey(scenarioName))
			return numObjScenMap.get(scenarioName); 
		else
			return 5;
	}
	
	public static String[] getMetricNames(String scenarioName)
	{
		if(scenMetricNamesMap.containsKey(scenarioName))
			return scenMetricNamesMap.get(scenarioName);
		else
		{
			String s[] = {"metric 0", "metric 1", "metric 2", "metric 3", "metric 4",  "metric 5"};
			return s;
		}
	}
	
	public Double[] getObjectiveValues() { return objectiveValues; }
	
	public void addDecisionSpace(Integer[] decisionSpace) { this.decisionSpaceList.add(decisionSpace); }
	
	public void setDecisionSpaceList(ArrayList<Integer[]> decisionSpaceList) { this.decisionSpaceList = decisionSpaceList; }
	
	public Integer[] getDecisionSpace()
	{
		if(decisionSpaceList.size() == 1)
			return decisionSpaceList.get(0);
		else
		{
			int index = random.nextInt(decisionSpaceList.size());
			return decisionSpaceList.get(index);
		}
	}
	
	public static Scenario getNewScenario(String scenarioName) {
		try { 
			return (Scenario) nameScenClassMap.get(scenarioName).newInstance();
		} 
		catch (Exception e) { 
			return null;
		} 
	}
	
	public static Scenario getNewClassicatiocScenario(String name) {
		try { 
			return (Scenario) nameClassificationMap.get(name).newInstance();
		} 
		catch (Exception e) { 
			return null;
		} 
	}
	
	public void visualize(String chromo, boolean showChromo) {
		int[] decisionSpace = new int[chromo.length()];

		for(int i = 0; i < chromo.length(); i++) {
			decisionSpace[i] = chromo.charAt(i) == '1' ? 1:0;
		}

		Integer[] boxedDecisionSpace = new Integer[decisionSpace.length];
		for(int i = 0; i < decisionSpace.length; i++)
			boxedDecisionSpace[i] = new Integer(decisionSpace[i]);

		simClass = MASONSimWithUI.class;
		
		SOMASAgentGenerator.NV = false;
		SOMASAgentGenerator.NE = false;
		SOMASAgentGenerator.NCDA = true;

		if(showChromo)
			new ChromoVisualizer(boxedDecisionSpace);
		
		addDecisionSpace(boxedDecisionSpace);
		this.visualize = true;
		this.run();
	}
	
	public void visualize(ArrayList<String> chromoList) {
		
		ArrayList<Integer[]> boxedDecisionSpaceList = new ArrayList<Integer[]>();
		
		for(String chromo : chromoList)
		{
			int[] decisionSpace = new int[chromo.length()];
	
			for(int i = 0; i < chromo.length(); i++) {
				decisionSpace[i] = chromo.charAt(i) == '1' ? 1:0;
			}
	
			Integer[] boxedDecisionSpace = new Integer[decisionSpace.length];
			for(int i = 0; i < decisionSpace.length; i++)
				boxedDecisionSpace[i] = new Integer(decisionSpace[i]);
			
			boxedDecisionSpaceList.add(boxedDecisionSpace);
		}

		simClass = MASONSimWithUI.class;
		
		SOMASAgentGenerator.NV = false;
		SOMASAgentGenerator.NE = false;
		SOMASAgentGenerator.NCDA = true;

		
		setDecisionSpaceList(boxedDecisionSpaceList);
		this.visualize = true;
		this.run();
	}
	
	public ArrayList<Chromosome> createChromoList()
	{
		Integer[]  decisionSpace = getDecisionSpace();
		
		/*
		  General parameter initialization
		*/
		int numChromosomes = 3;
		assert((decisionSpace.length % numChromosomes) == 0); // Make sure the decision space can be equally divided
		int chromosomeSize = decisionSpace.length/numChromosomes;
		
		/*
		  This variable points to the location of the pheromone, it
		  should probably note be hardcoded.
		*/
		int pheromoneGene = 0; 	// HACK
		
		ArrayList<Chromosome> chromosomeList = new ArrayList<Chromosome>();
		
		// Split up decision space into chromosome,
	    // extract pheromone values from chromosome
		for(int j = 0; j < numChromosomes; j++) {
			int[] chromosomeBits = new int[chromosomeSize];

			for(int k = 0; k < chromosomeSize; k++)
				chromosomeBits[k] = decisionSpace[(j* numChromosomes) + k];

			IntBitRep iBR = new IntBitRep(chromosomeBits);

			// Add chromosome bits to chromosome .
			Chromosome chromosome = (Chromosome) (new Chromosome())
			.setNewBoxedProperty("representation", iBR);

			Thing pheromoneContainer = new Thing();
			pheromoneContainer.setProperty("object", new Object());

			// Set pheromone value of chromosome.
			chromosome
			.setNewBoxedProperty
			("pheromone",
					((Double[])
							((Machine)
									(new IntBitToDelimitedDoubleChromosomeDecoder())
									.setNewBoxedProperty
									("chromosome", chromosome)
									.setNewBoxedProperty
									("result", new Object()))
									.execute()
									.getBoxedProperty("result"))
									[pheromoneGene]);

			// Add chromosome to list of agent's chromosomes.
			chromosomeList.add(chromosome);
		}
		return chromosomeList;
	}
}
