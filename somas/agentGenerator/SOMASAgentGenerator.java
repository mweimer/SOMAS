/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package agentGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import rules.Rule;
import rules.WeightedRule;
import sensors.AgentOrderSensor;
import sensors.AgentSensor;
import sensors.ArcTanFilterSensor;
import sensors.LocalChromosomeOrderSensor;
import sensors.LocationAgentsCumulativePheromoneSensor;
import sensors.LocationChromosomeOrderSensor;
import sensors.LocationMarkedSensor;
import sensors.LocationNumberAgentsSensor;
import sensors.LocationNumberNeighborsSensor;
import sensors.LocationOrderSensor;
import sensors.ContextPheromoneSensor;
import sensors.RandomValueSensor;
import sensors.SelectThingSensor;
import sensors.Sensor;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import unknown.Chromosome;
import unknown.IntBitCrossOverOperator;
import unknown.IntBitMutationOperator;
import unknown.IntBitToDelimitedDoubleChromosomeDecoder;
import actuators.Actuator;
import actuators.ChangeLocation;
import actuators.CreateAgent;
import actuators.CrossoverLocalChromosomeWithLocationChromosome;
import actuators.DecreasePheromone;
import actuators.DeleteAgent;
import actuators.IncreasePheromone;
import actuators.MarkLocation;
import actuators.MutateSelfChromosome;
import actuators.NullActuator;
import actuators.SetPheromoneOnSelfChromosome;
import actuators.UnMarkLocation;
import agent.Agent;

import common.Machine;
import common.Thing;

public class SOMASAgentGenerator  {

    static public Boolean NV = false; // Does not set whether node is vital or not
    static public Boolean NE = false; // Does not evolve chromosomes
    static public Boolean NCDA = false; // Does not create or delete SOMAS agents
    static public Boolean NMAD = false; // Does not delete malicious agents
    static public Boolean NS = false; // Non searching agent (still creates and deletes agents)
    
    private ArrayList<String> typeList = new ArrayList<String>();
    private ArrayList<Chromosome> chromosomeList = null;
    private Thing schedulerContainer = new Thing();
    private Thing deschedulerContainer = new Thing();

    public SOMASAgentGenerator(ArrayList<Chromosome> chromosomeList, MASONSimScheduler scheduler, 
    		MASONSimDeScheduler descheduler) {
    	this.chromosomeList = chromosomeList;
    	this.schedulerContainer.setProperty("object", scheduler);
    	this.deschedulerContainer.setProperty("object", descheduler);
    }
    
	public Agent generateAgent() {
			
        Machine tempMachine = null;

        /*
          ==========================
          Basic agent initialization
          ==========================
        */

        typeList.add("somas");

        Agent agent = new Agent();
        Thing agentContainer = new Thing();
        agentContainer.setProperty("object", agent);
        ArrayList<Sensor> sensors = null;
        ArrayList<Rule> rules = null;
        ArrayList<Actuator> actuators = null;

        sensors = new ArrayList<Sensor>();
        rules = new ArrayList<Rule>();
        actuators = new ArrayList<Actuator>();

        agent.setNewBoxedProperty("typeList", typeList);
        agent.setNewBoxedProperty("sensors", sensors);
        agent.setNewBoxedProperty("rules", rules);
        agent.setNewBoxedProperty("actuators", actuators);

        /*
          ==================
          Initialize sensors
          ==================
        */
        LocationOrderSensor locationOrderSensor = new LocationOrderSensor();
        sensors.add(locationOrderSensor);
        AgentOrderSensor agentOrderSensor = new AgentOrderSensor();
        sensors.add(agentOrderSensor);
        AgentSensor agentSensor = new AgentSensor();
        sensors.add(agentSensor);
        LocalChromosomeOrderSensor localChromosomeOrderSensor = new LocalChromosomeOrderSensor();
        sensors.add(localChromosomeOrderSensor);
        LocationChromosomeOrderSensor locationChromosomeOrderSensor = new LocationChromosomeOrderSensor();
        sensors.add(locationChromosomeOrderSensor);
        LocationNumberAgentsSensor locationNumberAgentsSensor = new LocationNumberAgentsSensor();
        sensors.add(locationNumberAgentsSensor);
        LocationNumberNeighborsSensor locationNumberNeighborsSensor = new LocationNumberNeighborsSensor();
        sensors.add(locationNumberNeighborsSensor);
        ContextPheromoneSensor locationPheromoneSensor = new ContextPheromoneSensor();
        sensors.add(locationPheromoneSensor);
        ContextPheromoneSensor selfPheromoneSensor = new ContextPheromoneSensor();
        sensors.add(selfPheromoneSensor);
        LocationAgentsCumulativePheromoneSensor locationAgentsCumulativePheromoneSensor = new LocationAgentsCumulativePheromoneSensor();
        sensors.add(locationAgentsCumulativePheromoneSensor);
        LocationMarkedSensor locationMarkedSensor = new LocationMarkedSensor();
        sensors.add(locationMarkedSensor);
        SelectThingSensor selectMarkLocationSensor = new SelectThingSensor();
        sensors.add(selectMarkLocationSensor);
        SelectThingSensor selectUnmarkLocationSensor = new SelectThingSensor();
        sensors.add(selectUnmarkLocationSensor);

        RandomValueSensor randomValueSensor = new RandomValueSensor();
        sensors.add(randomValueSensor);

        /*
          ====================
          Initialize actuators
          ====================
        */
        NullActuator nullActuator = (NullActuator) new NullActuator();

        CreateAgent createAgent = new CreateAgent();
        DeleteAgent deleteAgent = new DeleteAgent();
        if(NCDA) {
            actuators.add(nullActuator);
            actuators.add(nullActuator);
        } else {
            actuators.add(createAgent);
            actuators.add(deleteAgent);
        }
        IncreasePheromone increasePheromone = new IncreasePheromone();
        actuators.add(increasePheromone);
        DecreasePheromone decreasePheromone = new DecreasePheromone();
        actuators.add(decreasePheromone);
        SetPheromoneOnSelfChromosome setPheromoneOnSelfChromosome = new SetPheromoneOnSelfChromosome();
        actuators.add(setPheromoneOnSelfChromosome);
        MutateSelfChromosome mutateSelfChromosome = new MutateSelfChromosome();
        CrossoverLocalChromosomeWithLocationChromosome crossoverLocalChromosomeWithLocationChromosome = new CrossoverLocalChromosomeWithLocationChromosome();
        if(!NE && !NS) {
            actuators.add(mutateSelfChromosome);
            actuators.add(crossoverLocalChromosomeWithLocationChromosome);
        } else
            {
                actuators.add(nullActuator);
                actuators.add(nullActuator);
            }
        MarkLocation markLocation = new MarkLocation();
        UnMarkLocation unMarkLocation = new UnMarkLocation();
        if(!NV) {
            actuators.add(markLocation);
            actuators.add(unMarkLocation);
        } else
            {
                actuators.add(nullActuator);
                actuators.add(nullActuator);
            }
        // This has to be last so it doesn't screw up agent deletion, etc.
        ChangeLocation changeLocation = new ChangeLocation();
        actuators.add(changeLocation);

        /*
          =======================================
          Extract pheromone gene from chromosome 
          =======================================

          The gene extraction code is divided across the source file.
          This is because the self pheremone sensor depends on the
          pheremone gene, and the rest of the gene extraction code
          depends on the number of actuators and sensors in the agent,
          which isn't completely known until the parameters for the
          actuators and sensors are all declared.  Since there isn't a
          set number of parameters per actuator (though there is only
          one parameter per sensor), the parameter declaration has
          been grouped with the rest of the actuator initialization
          code.

          Such organization is not ideal, and there is no strict
          reason why it must be kept.

          TODO [#B] reorganize file so gene extraction code is all
          grouped in one location.
        */
        // These variables are used by the rules to know when to trigger an actuator.
        // The preloaded ones exist if the environment has specialized rules already.
        int numActuators = actuators.size();
        ArrayList<Thing> decisionVariableContainers = new ArrayList<Thing>();
        for(int i = 0; i < numActuators; i++)
            decisionVariableContainers.add(new Thing());
        agent.setProperty("decisionVariableContainers", decisionVariableContainers);
        int numDecisionVariables = decisionVariableContainers.size();

        // First part of gene initialization
        int miscStart = 0;
        int pheromoneGene = 0;
        int numMisc = 1;

        Chromosome chromosome = chromosomeList.get(0);

        Thing chromosomeContainer = new Thing();
        chromosomeContainer.setProperty("object", chromosome);

        int numChromosomes = chromosomeList.size(); 
        if(NS) {
            numChromosomes = 1;
        }
                
        ArrayList<Chromosome> localChromosomeList = new ArrayList<Chromosome>(chromosomeList.subList(1,numChromosomes));
        Thing localChromosomeListContainer = new Thing();
        localChromosomeListContainer.setProperty("object", localChromosomeList);

	// Turns the allele into a double value, scaled by DOUBLE_MAX
	// so it is within (0,1).
        tempMachine = (new IntBitToDelimitedDoubleChromosomeDecoder());
        tempMachine.setNewBoxedProperty("chromosome", chromosome);
        tempMachine.setNewBox("result");
        tempMachine.execute();
                
        Double[] genes = (Double[]) tempMachine.getBoxedProperty("result");

        /*
          ===================================
          Create actuators and set parameters
          ===================================
        */

        // Collect actuators' variables.
        ArrayList<Thing> globalParameterVariableList = new ArrayList<Thing>();

        /*
          ------------------------
          Change Location actuator
          ------------------------
        */
        Thing locationContainer = new Thing();
        agent.setProperty("locationContainer", locationContainer);
        changeLocation.setProperty("locationContainer", locationContainer);
        changeLocation.setProperty("agentContainer", agentContainer);
        Thing locationListContainer = new Thing();
        changeLocation.setProperty("locationListContainer", locationListContainer);
        locationOrderSensor.setProperty("agentContainer", agentContainer);
        locationOrderSensor.setProperty("locationListContainer", locationListContainer);

        ArrayList<Thing> parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        changeLocation.setNewBoxedProperty("parameterList", parameterList);

        changeLocation.setNewBoxedProperty("context", agent);

        /*
          ---------------------
          Create Agent actuator
          ---------------------
        */
        createAgent.setProperty("locationContainer", locationContainer);
        Thing agentListContainer = new Thing();
        agent.setProperty("agentListContainer", agentListContainer); 
        createAgent.setProperty("agentListContainer", agentListContainer);
        createAgent.setProperty("chromosomeContainer", chromosomeContainer);

        agent.setProperty("localChromosomeListContainer", localChromosomeListContainer);
        createAgent.setProperty("localChromosomeListContainer", localChromosomeListContainer);

        parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        createAgent.setProperty("parameterList", parameterList);

        createAgent.setProperty("schedulerContainer", schedulerContainer); // This is already set outside of this context

        Thing agentGeneratorContainer = (new Thing())
            .setProperty("object", this); 
        createAgent.setProperty("agentGeneratorContainer", agentGeneratorContainer);
        createAgent.setProperty("locationContainer", locationContainer);
        createAgent.setProperty("agentContainer", agentContainer);

        /*
          ---------------------
          Delete Agent actuator
          ---------------------
        */
        ArrayList<String> filteredTypeList = new ArrayList<String>();
        if(!NCDA) {
            filteredTypeList.add("somas");
        }
        if(!NMAD) {
            filteredTypeList.add("malicious");
        }
        Thing deleteAgentListContainer = new Thing();
        agentSensor.setProperty("agentListContainer", agentListContainer);
        agentSensor.setProperty("resultContainer", deleteAgentListContainer);
        agentSensor.setNewBoxedProperty("filteredTypeList", filteredTypeList);

        deleteAgent.setProperty("locationContainer", locationContainer); // locationContainer created previously
        agentOrderSensor.setProperty("agentListContainer", agentListContainer);
        agentOrderSensor.setProperty("agentContainer", agentContainer);
        deleteAgent.setProperty("agentListContainer", deleteAgentListContainer);

        deleteAgent.setProperty("deschedulerContainer", deschedulerContainer); // This is already set outside of this context

        parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        deleteAgent.setProperty("parameterList", parameterList);

        /*
          ---------------------------
          Increase Pheromone actuator
          ---------------------------
        */
        Thing pheromoneContainer = new Thing();
        pheromoneContainer.setProperty("object", genes[pheromoneGene]);
        agent.setProperty("pheromoneContainer", pheromoneContainer);
        increasePheromone.setProperty("pheromoneContainer", pheromoneContainer);

        parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        increasePheromone.setProperty("parameterList", parameterList);

        /*
          ---------------------------
          Decrease Pheromone actuator
          ---------------------------
        */
        decreasePheromone.setProperty("pheromoneContainer", pheromoneContainer);

        parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        decreasePheromone.setProperty("parameterList", parameterList);

        /*
          -----------------------------------------
          Set Pheromone On Self Chromosome actuator
          -----------------------------------------
        */
        chromosomeContainer.setProperty("object", chromosome); // TODO : get this from whereever it ends up being decoded
        agent.setProperty("chromosomeContainer", chromosomeContainer);
        setPheromoneOnSelfChromosome.setProperty("chromosomeContainer", chromosomeContainer);

        setPheromoneOnSelfChromosome.setProperty("pheromoneContainer", pheromoneContainer);

        /*
          -----------------------------------------------------------------------
          Local Chromosome Order Sensor actuator (used by the next two actuators)
          -----------------------------------------------------------------------
        */
        localChromosomeOrderSensor.setProperty("agentContainer", agentContainer);
        localChromosomeOrderSensor.setProperty("localChromosomeListContainer", localChromosomeListContainer);

        /*
          -------------------------------
          Mutate Self Chromosome actuator
          -------------------------------
        */
        mutateSelfChromosome.setProperty("localChromosomeListContainer", localChromosomeListContainer);
        mutateSelfChromosome.setProperty("chromosomeContainer", chromosomeContainer);

        Thing mutateOperatorContainer = new Thing(); 
        mutateOperatorContainer.setProperty("object", new IntBitMutationOperator());
        mutateSelfChromosome.setProperty("mutateOperatorContainer", mutateOperatorContainer);

        parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        mutateSelfChromosome.setProperty("parameterList", parameterList);

        /*
          ------------------------------------------------------------
          Crossover Local Chromosome With Location Chromosome actuator
          ------------------------------------------------------------
        */
        Thing crossoverOperatorContainer = new Thing(); 
        crossoverOperatorContainer.setProperty("object", new IntBitCrossOverOperator());
        crossoverLocalChromosomeWithLocationChromosome.setProperty("crossoverOperatorContainer", crossoverOperatorContainer);
        crossoverLocalChromosomeWithLocationChromosome.setProperty("localChromosomeListContainer", localChromosomeListContainer);

        Thing locationChromosomeListContainer = new Thing();
        locationChromosomeOrderSensor.setProperty("agentContainer", agentContainer);
        locationChromosomeOrderSensor.setProperty("locationChromosomeListContainer", locationChromosomeListContainer);
        crossoverLocalChromosomeWithLocationChromosome.setProperty("locationChromosomeListContainer", locationChromosomeListContainer);

        parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        crossoverLocalChromosomeWithLocationChromosome.setProperty("parameterList", parameterList);

        /*
          ----------------------
          Mark Location actuator
          ----------------------
        */
        Thing markLocationContainer = new Thing();
        markLocation.setProperty("locationContainer", markLocationContainer);

        parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        selectMarkLocationSensor.setProperty("thingListContainer", locationListContainer);
        selectMarkLocationSensor.setProperty("resultContainer", markLocationContainer);
        selectMarkLocationSensor.setNewBoxedProperty("parameterList", parameterList);

        /*
          ------------------------
          Unmark Location actuator
          ------------------------
        */
        Thing unmarkLocationContainer = new Thing();
        unMarkLocation.setProperty("locationContainer", unmarkLocationContainer);

        parameterList = new ArrayList<Thing>();
        parameterList.add(new Thing());
        globalParameterVariableList.addAll(parameterList);
        selectUnmarkLocationSensor.setProperty("thingListContainer", locationListContainer);
        selectUnmarkLocationSensor.setProperty("resultContainer", unmarkLocationContainer);
        selectUnmarkLocationSensor.setNewBoxedProperty("parameterList", parameterList);

        /*
          =======================
          Set sensors' parameters
          =======================

          Since the SOMAS agent should know no semantics about the
          sensors, each sensor's parameters are initialized in the
          same way.

          As described in Holloway_2009, Section 4.6.2, each sensor
          initially receives an unbounded number as its input
          (although, only a finite range is implemented here).  This
          number is then scaled using a scaling function.  Here, the
          scaling function is the inverse tangent function, since it
          gives a sinusoidal curve.  For graphical description see
          Holloway_2009, Section 4.6.2.
        */

        // Collect sensors' variables.
        ArrayList<Thing> globalSensorVariableList = new ArrayList<Thing>();

        Thing temp = null;

        ArrayList<Thing> setParametersList = new ArrayList<Thing>();

        /*
          -----------------------------
          Location Number Agents Sensor
          -----------------------------
        */
        temp = new Thing();
        locationNumberAgentsSensor.setProperty("agentListContainer", agentListContainer);
        locationNumberAgentsSensor.setProperty("sensorVariableContainer", temp);
        ArcTanFilterSensor arcTanFilterSensor = new ArcTanFilterSensor();
        arcTanFilterSensor.setProperty("valueContainer", temp);
        setParametersList.add(new Thing());
        setParametersList.add(new Thing());
        arcTanFilterSensor.setProperty("scalarNumeratorContainer", setParametersList.get(setParametersList.size()-2));
        arcTanFilterSensor.setProperty("scalarDenominatorContainer", setParametersList.get(setParametersList.size()-1));
        globalSensorVariableList.add(new Thing());
        arcTanFilterSensor.setProperty("sensorVariableContainer", globalSensorVariableList.get(globalSensorVariableList.size()-1));

        /*
          --------------------------------
          Location Number Neighbors Sensor
          --------------------------------
        */
        temp = new Thing();
        locationNumberNeighborsSensor.setProperty("locationListContainer", locationListContainer);
        locationNumberNeighborsSensor.setProperty("sensorVariableContainer", temp);
        arcTanFilterSensor = new ArcTanFilterSensor();
        arcTanFilterSensor.setProperty("valueContainer", temp);
        setParametersList.add(new Thing());
        setParametersList.add(new Thing());
        arcTanFilterSensor.setProperty("scalarNumeratorContainer", setParametersList.get(setParametersList.size()-2));
        arcTanFilterSensor.setProperty("scalarDenominatorContainer", setParametersList.get(setParametersList.size()-1));
        globalSensorVariableList.add(new Thing());
        arcTanFilterSensor.setProperty("sensorVariableContainer", globalSensorVariableList.get(globalSensorVariableList.size()-1));

        /*
          -------------------------
          Location Pheromone Sensor
          -------------------------
        */
        temp = new Thing();
        locationPheromoneSensor.setProperty("contextContainer", locationContainer);
        locationPheromoneSensor.setProperty("sensorVariableContainer", temp);
        arcTanFilterSensor = new ArcTanFilterSensor();
        arcTanFilterSensor.setProperty("valueContainer", temp);
        setParametersList.add(new Thing());
        setParametersList.add(new Thing());
        arcTanFilterSensor.setProperty("scalarNumeratorContainer", setParametersList.get(setParametersList.size()-2));
        arcTanFilterSensor.setProperty("scalarDenominatorContainer", setParametersList.get(setParametersList.size()-1));
        globalSensorVariableList.add(new Thing());
        arcTanFilterSensor.setProperty("sensorVariableContainer", globalSensorVariableList.get(globalSensorVariableList.size()-1));

        /*
          ---------------------
          Self Pheromone Sensor
          ---------------------
        */
        temp = new Thing();
        selfPheromoneSensor.setProperty("contextContainer", agentContainer);
        selfPheromoneSensor.setProperty("sensorVariableContainer", temp);
        arcTanFilterSensor = new ArcTanFilterSensor();
        arcTanFilterSensor.setProperty("valueContainer", temp);
        setParametersList.add(new Thing());
        setParametersList.add(new Thing());
        arcTanFilterSensor.setProperty("scalarNumeratorContainer", setParametersList.get(setParametersList.size()-2));
        arcTanFilterSensor.setProperty("scalarDenominatorContainer", setParametersList.get(setParametersList.size()-1));
        globalSensorVariableList.add(new Thing());
        arcTanFilterSensor.setProperty("sensorVariableContainer", globalSensorVariableList.get(globalSensorVariableList.size()-1));

        /*
          --------------------------------------------
          Location Agents' Cumulative Pheromone Sensor
          --------------------------------------------
        */
        temp = new Thing();
        locationAgentsCumulativePheromoneSensor.setProperty("agentListContainer", agentListContainer);
        locationAgentsCumulativePheromoneSensor.setProperty("sensorVariableContainer", temp);
        arcTanFilterSensor = new ArcTanFilterSensor();
        arcTanFilterSensor.setProperty("valueContainer", temp);
        setParametersList.add(new Thing());
        setParametersList.add(new Thing());
        arcTanFilterSensor.setProperty("scalarNumeratorContainer", setParametersList.get(setParametersList.size()-2));
        arcTanFilterSensor.setProperty("scalarDenominatorContainer", setParametersList.get(setParametersList.size()-1));
        globalSensorVariableList.add(new Thing());
        arcTanFilterSensor.setProperty("sensorVariableContainer", globalSensorVariableList.get(globalSensorVariableList.size()-1));

        locationMarkedSensor.setProperty("contextContainer", locationContainer);
        globalSensorVariableList.add(new Thing());
        locationMarkedSensor.setProperty("sensorVariableContainer", globalSensorVariableList.get(globalSensorVariableList.size()-1));

        /*
          -------------------------------
          Static and random value sensors
          -------------------------------

          Add extra variables for the set value and random value
          sensor.  These sensors give static and stochastic elements
          to the agent behavior, independent of what they observe.
        
	  The static value is set by the chromosome.
	*/

	// Set value sensor is just a variable in the sensor list.
	// It doesn't require a special sensor class, or the rest of
	// the configuration.
	int numSetValues = 1; // TODO [#C] should not be hardcoded
	ArrayList<Thing> setValueVariableContainers = new ArrayList<Thing>(Arrays.asList(new Thing[]{new Thing()}));
	globalSensorVariableList.addAll(setValueVariableContainers); 

	// Random value sensor, also a special case, but requires an
	// actuator since can change each simulation iteration.
	int numRandomValues = 1; // TODO [#C] should not be hardcoded
	ArrayList<Thing> randomVariableContainers = new ArrayList<Thing>();
	randomValueSensor.setProperty("randomVariableContainers", randomVariableContainers); 
	for(int i = 0; i < numRandomValues; i++)
	    randomVariableContainers.add(new Thing());
	globalSensorVariableList.addAll(randomVariableContainers);

	/*
	  =====================================
	  Extract rest of genes from chromosome
	  =====================================

	  First, the alleles are grouped into variable size genes
	  based on how the SOMAS agent has been configured.  

	  For example, all parameters for actuators have genes of the
	  same size, but the gene for rule weights is a different
	  size.
	*/

	/*
	  -----------------------
	  Determine size of genes
	  -----------------------
	*/
	int numParameterVariables = globalParameterVariableList.size();
	int numSetParameters = setParametersList.size();
	int numSensorVariables = globalSensorVariableList.size();
	int numWeights = numDecisionVariables + numParameterVariables;
	int numElements = numSensorVariables;
	int weightsStart = miscStart + numMisc;
	int setValuesStart = weightsStart + (numWeights * numElements);
	int setParametersStart = setValuesStart + numSetValues;

	/*
	  ---------------------------------------------
	  Initialize rule(s) & rest of agent from genes
	  ---------------------------------------------
	*/
	// Add the agent's weighted rule
	Rule weightedRule = new WeightedRule();
	rules.add(weightedRule);

	// WeightedRule's variables need to be set.

	// Weights
	Thing weightsContainer = new Thing();
	weightedRule.setProperty("weightsContainer", weightsContainer);
	// SensorVariables
	for(Thing variable : globalSensorVariableList)
	    variable.setProperty("object", new Double(0));
	weightedRule.setProperty("sensorVariableContainers", globalSensorVariableList);
	// DecisionVariables
	for(Thing variable : decisionVariableContainers)
	    variable.setProperty("object", new Double(0));
	weightedRule.setProperty("decisionVariableContainers", decisionVariableContainers);
	// ParameterVariables
	for(Thing variable : globalParameterVariableList)
	    variable.setProperty("object", new Double(0));
	weightedRule.setProperty("parameterVariableContainers", globalParameterVariableList);
	// Actuators to trigger
	weightedRule.setProperty("actuators", actuators);

	// Create the weights from the genes
	// The format of the genes is:
	// - (numParameterVariables + numDecisionVariables) * (numSensorVariables) genes for the weights
	// - (numParameterVariables + numDecisionVariables) genes for the set values
	Double[][] weights = new Double[numWeights][numElements];

	for(int i = 0; i < numWeights; i++)
	    for(int j = 0; j < numElements; j++)
		weights[i][j] = genes[weightsStart + (numElements * i) + j];

	weightsContainer.setProperty("weights", weights);

	for(int i = 0; i < numSetValues; i++)
	    setValueVariableContainers.get(i).setNewBoxedProperty("object", genes[setValuesStart + i]);

	for(int i = 0; i < numSetParameters; i++)
	    setParametersList.get(i).setNewBoxedProperty("object", genes[setParametersStart + i]);

	return agent;
    }


}



