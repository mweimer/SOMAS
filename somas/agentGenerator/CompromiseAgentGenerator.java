/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package agentGenerator;

import sensors.*;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import rules.*;
import actuators.*;
import unknown.*;
import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class CompromiseAgentGenerator extends MaliciousAgentGenerator {
    public CompromiseAgentGenerator() {super();}
    public CompromiseAgentGenerator(Thing t) {super(t);}
    public void run() {
        /*
	  ==========================
          Basic agent initialization
	  ==========================
        */
        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        ArrayList<Rule> rules = new ArrayList<Rule>();
        ArrayList<Actuator> actuators = new ArrayList<Actuator>();

	// Set agent types, for recognition by type sensors
        ArrayList<String> typeList = this.hasProperty("typeList") ? (ArrayList<String>) this.getBoxedProperty("typeList") : new ArrayList<String>();
        typeList.add("compromise");
        typeList.add("malicious");

        Location location = (Location) this.getBoxedProperty("location");

        Thing graphContainer = (Thing) this.getProperty("graphContainer");

        Thing locationContainer = (new Thing());
        locationContainer.setProperty("object", location);
        Thing nextHopContainer = new Thing();

        Sampler sampler = (Sampler) this.getBoxedProperty("sampler");
        sampler.execute();
        Double pheromone = (Double) sampler.getBoxedProperty("result");
        
        Agent agent = (Agent) (new Agent());
        agent.setProperty("locationContainer", locationContainer);
        agent.setNewBoxedProperty("typeList", typeList);
        agent.setNewBoxedProperty("sensors", sensors);
        agent.setNewBoxedProperty("rules", rules);
        agent.setNewBoxedProperty("pheromone", pheromone);
        agent.setNewBoxedProperty("actuators", actuators);

        /*
          =============
          Agent sensors
          =============
        */
        /*
          ---------------
          Next hop sensor
          ---------------

          Sensor to determine which node to visit next.
        */
        NextNetworkSearchHopSensor nextHopSensor = (NextNetworkSearchHopSensor) (new NextNetworkSearchHopSensor());
        nextHopSensor.setProperty("graphContainer", graphContainer);
        nextHopSensor.setProperty("locationContainer", locationContainer);
        nextHopSensor.setProperty("resultContainer", nextHopContainer);
        sensors.add(nextHopSensor);
        
        /*
          ------------------------
          Compromised node sensor 
          ------------------------

          Checks whether a node is compromised, with accuracy
          determined by detection probability parameter.
        */
        Thing detectionProbabilityContainer = (new Thing());
        detectionProbabilityContainer.setProperty("object", new Double(1.0)); // Probabilitiy that the agent can detect whether the node is compromised or not
        Thing compromiseContainer = new Thing();
        Thing compromiseCheckContainer = new Thing();

        CompromisedNodeSensor compromisedNodeSensor = (CompromisedNodeSensor) (new CompromisedNodeSensor());
        compromisedNodeSensor.setProperty("detectionProbabilityContainer", detectionProbabilityContainer);
        compromisedNodeSensor.setProperty("locationContainer", locationContainer);
        compromisedNodeSensor.setProperty("compromiseContainer", compromiseContainer);
        compromisedNodeSensor.setProperty("resultContainer", compromiseCheckContainer);
        sensors.add(compromisedNodeSensor);

        /*
          ------------------
          Agent order sensor
          ------------------

          Returns an ordered list of the agents on the node.  Used by
          the following agent type sensors.
        */
        Thing agentListContainer = new Thing();
        AgentOrderSensor agentOrderSensor = (AgentOrderSensor) (new AgentOrderSensor());
        agentOrderSensor.setNewBoxedProperty("agent", agent);
        agentOrderSensor.setProperty("agentListContainer", agentListContainer);
        sensors.add(agentOrderSensor);
        
        /*
          -----------------
          Info agent sensor
          -----------------

          Filters out a list of info agents from an agent list.
        */
        Thing infoAgentListContainer = new Thing();

        AgentSensor infoAgentSensor = (AgentSensor) (new AgentSensor());
        infoAgentSensor.setProperty("agentListContainer", agentListContainer);
        infoAgentSensor.setNewBoxedProperty("type", "goodinfocomm");
        infoAgentSensor.setProperty("resultContainer", infoAgentListContainer);
        sensors.add(infoAgentSensor);

        /*
          -----------------------
          Compromise agent sensor
          -----------------------

          Filters out a list of compromise agents from an agent list.
        */
        Thing compromiseAgentListContainer = new Thing();

        AgentSensor compromiseAgentSensor = (AgentSensor) (new AgentSensor());
        compromiseAgentSensor.setProperty("agentListContainer", agentListContainer);
        compromiseAgentSensor.setNewBoxedProperty("type", "compromise");
        compromiseAgentSensor.setProperty("resultContainer", compromiseAgentListContainer);
        sensors.add(compromiseAgentSensor);

        /*
          -----------------
          DDoS agent sensor
          -----------------

          Filters out a list of DDoS agents from an agent list.
        */
        Thing DDoSAgentListContainer = new Thing();

        AgentSensor DDoSAgentSensor = (AgentSensor) (new AgentSensor());
        DDoSAgentSensor.setProperty("agentListContainer", agentListContainer);
        DDoSAgentSensor.setNewBoxedProperty("type", "ddos");
        DDoSAgentSensor.setProperty("resultContainer", DDoSAgentListContainer);
        sensors.add(DDoSAgentSensor);

        /*
          -------------------------------
          DDoS communication agent sensor
          -------------------------------

          Filters out a list of DDoS communication agents from an agent list.
        */
        Thing DDoSCommAgentListContainer = new Thing();

        AgentSensor DDoSCommAgentSensor = (AgentSensor) (new AgentSensor());
        DDoSCommAgentSensor.setProperty("agentListContainer", agentListContainer);
        DDoSCommAgentSensor.setNewBoxedProperty("type", "ddoscomm");
        DDoSCommAgentSensor.setProperty("resultContainer", DDoSCommAgentListContainer);
        sensors.add(DDoSCommAgentSensor);
        
        /*
          ------------------
          SOMAS agent sensor
          ------------------

          Filters out a list of SOMAS agents from an agent list.
        */
        Thing SOMASAgentListContainer = new Thing();

        AgentSensor SOMASAgentSensor = (AgentSensor) (new AgentSensor());
        SOMASAgentSensor.setProperty("agentListContainer", agentListContainer);
        SOMASAgentSensor.setNewBoxedProperty("type", "somas");
        SOMASAgentSensor.setProperty("resultContainer", SOMASAgentListContainer);
        sensors.add(SOMASAgentSensor);
        
        /*
          -------------------
          Sender agent sensor
          -------------------

          Filters out a list of sender agents from an agent list.
        */
        Thing senderAgentListContainer = new Thing();

        AgentSensor senderAgentSensor = (AgentSensor) (new AgentSensor());
        senderAgentSensor.setProperty("agentListContainer", agentListContainer);
        senderAgentSensor.setNewBoxedProperty("type", "sender");
        senderAgentSensor.setProperty("resultContainer", senderAgentListContainer);
        sensors.add(senderAgentSensor);
        
        /*
          ---------------------
          Receiver agent sensor
          ---------------------

          Filters out a list of receiver agents from an agent list.
        */
        Thing receiverAgentListContainer = new Thing();

        AgentSensor receiverAgentSensor = (AgentSensor) (new AgentSensor());
        receiverAgentSensor.setProperty("agentListContainer", agentListContainer);
        receiverAgentSensor.setNewBoxedProperty("type", "receiver");
        receiverAgentSensor.setProperty("resultContainer", receiverAgentListContainer);
        sensors.add(receiverAgentSensor);
        

        /*
          ===============
          Agent actuators
          ===============
        */
        /*
          ------------------------
          Change location actuator
          ------------------------
        */
        ChangeLocationActuator changeLocationActuator = (ChangeLocationActuator) (new ChangeLocationActuator());
        changeLocationActuator.setNewBoxedProperty("agent", agent);
        changeLocationActuator.setProperty("oldLocationContainer", locationContainer);
        changeLocationActuator.setProperty("newLocationContainer", nextHopContainer);
        actuators.add(changeLocationActuator);

        
        MASONSimScheduler scheduler = (MASONSimScheduler) this.getBoxedProperty("scheduler");
        MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");
        
        /*
          -----------------------------
          Delete set of agents actuator
          -----------------------------
        */
        DeleteAgentListActuator deleteAgentListActuator = (DeleteAgentListActuator) (new DeleteAgentListActuator());
        deleteAgentListActuator.setNewBoxedProperty("scheduler", scheduler);
        deleteAgentListActuator.setNewBoxedProperty("descheduler", descheduler);
        deleteAgentListActuator.setProperty("locationContainer", locationContainer);
        deleteAgentListActuator.setProperty("agentListContainer", agentListContainer);
        deleteAgentListActuator.setProperty("deleteAgentListContainer", SOMASAgentListContainer);
        actuators.add(deleteAgentListActuator);
        
        /*
          ---------------------------
          Bad info depositor actuator
          ---------------------------

          This actuator increments the amount of bad info deposited on
          a good info receiver, simulating information corruption.
        */
        DepositBadInfoActuator depositBadInfoActuator = (DepositBadInfoActuator) (new DepositBadInfoActuator());
        depositBadInfoActuator.setProperty("badInfoIncrementerContainer", (new Thing()).setProperty("object", new Double(1.0)));
        depositBadInfoActuator.setProperty("locationContainer", locationContainer);;
        actuators.add(depositBadInfoActuator);
        
        /*
          -------------------
          Compromise actuator
          -------------------

          Places a DDoS agent on given location, if not already
          compromised.
        */
        CompromiseActuator compromiseActuator = (CompromiseActuator) (new CompromiseActuator());
        compromiseActuator.setProperty("locationContainer", locationContainer);;
        actuators.add(compromiseActuator);
        
        /*
          ---------------------
          Create agent actuator
          ---------------------

          General actuator for creating agents.
        */
        Actuator createAgentActuator;
        // For the data extraction scenario the comprise agents should not create new
        // agents. In DataExtractionScenarion this disableCreateAgentActuator is created
        // set. Other scenarios just assume compromise agents create new agents
        if(this.hasBoxedProperty("disableCreateAgentActuator")) 
        	createAgentActuator = new NullActuator();
        else {
        	createAgentActuator = (CreateAgentActuator) (new CreateAgentActuator());
	        createAgentActuator.setProperty("locationContainer", locationContainer);
	        createAgentActuator.setProperty("agentListContainer", agentListContainer);
	        createAgentActuator.setNewBoxedProperty("scheduler", scheduler);
	        createAgentActuator.setNewBoxedProperty("descheduler", descheduler);;
        	actuators.add(createAgentActuator);
        }
        
        ArrayList<Location> infoDepotList = (ArrayList<Location>) this.getBoxedProperty("infoDepotList");

        ArrayList<Location> targetList = this.hasBoxedProperty("targetList") ? (ArrayList<Location>) this.getBoxedProperty("targetList") : new ArrayList<Location>();
        
        /*
          ==========
          Agent rule
          ==========

          This is essentially the brain of the compromise agent.
          Currently, the behavior is preset, it does not contain any
          kind of learning capability.
        */
        CompromiseAgentRule compromiseAgentRule = (CompromiseAgentRule) (new CompromiseAgentRule());
        compromiseAgentRule.setProperty("receiverAgentListContainer", receiverAgentListContainer);
        compromiseAgentRule.setProperty("senderAgentListContainer", senderAgentListContainer);
        compromiseAgentRule.setProperty("DDoSAgentListContainer", DDoSAgentListContainer);
        compromiseAgentRule.setProperty("locationContainer", locationContainer);
        compromiseAgentRule.setProperty("infoAgentListContainer", infoAgentListContainer);
        compromiseAgentRule.setProperty("compromiseCheckContainer", compromiseCheckContainer);
        compromiseAgentRule.setProperty("graphContainer", graphContainer);

        compromiseAgentRule.setNewBoxedProperty("scheduler", scheduler);
        compromiseAgentRule.setNewBoxedProperty("descheduler", descheduler);

        compromiseAgentRule.setNewBoxedProperty("targetList", targetList);
        compromiseAgentRule.setNewBoxedProperty("infoDepotList", infoDepotList);
            
        compromiseAgentRule.setNewBoxedProperty("changeLocationActuator", changeLocationActuator);
        compromiseAgentRule.setNewBoxedProperty("deleteAgentListActuator", deleteAgentListActuator);
        compromiseAgentRule.setNewBoxedProperty("depositBadInfoActuator", depositBadInfoActuator);
        compromiseAgentRule.setNewBoxedProperty("compromiseActuator", compromiseActuator);
        compromiseAgentRule.setNewBoxedProperty("createAgentActuator", createAgentActuator);
        
        rules.add(compromiseAgentRule);

        /*
          --------------------------
          Return newly created agent
          --------------------------
        */
        this.setNewBoxedProperty("result", agent);
    }
}

