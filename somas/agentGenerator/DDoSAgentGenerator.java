/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package agentGenerator;

import sensors.*;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import rules.*;
import actuators.*;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class DDoSAgentGenerator extends MaliciousAgentGenerator {
	public DDoSAgentGenerator() {super();}
	public DDoSAgentGenerator(Thing t) {super(t);}
	public void run() {
		ArrayList<Sensor> sensors = new ArrayList<Sensor>();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		ArrayList<Actuator> actuators = new ArrayList<Actuator>();

		ArrayList<String> typeList = this.hasProperty("typeList") ? (ArrayList<String>) this.getBoxedProperty("typeList") : new ArrayList<String>();
		typeList.add("ddos");
		typeList.add("malicious");

		Location location = (Location) this.getBoxedProperty("location");

        Sampler sampler = (Sampler) this.getBoxedProperty("sampler");
        Double pheromone = (Double) sampler.execute().getBoxedProperty("result");
        
		Agent agent = (Agent) (new Agent())
		.setNewBoxedProperty("location", location)
		.setNewBoxedProperty("typeList", typeList)
		.setNewBoxedProperty("sensors", sensors)
		.setNewBoxedProperty("rules", rules)
		.setNewBoxedProperty("pheromone", pheromone)
		.setNewBoxedProperty("actuator", actuators);

		ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");

		Thing locationContainer = (new Thing())
		.setProperty("object", location);

		Thing agentListContainer = (new Thing())
		.setProperty("object", agentList);

		LocationSensor locationSensor = (LocationSensor) (new LocationSensor())
		.setNewBoxedProperty("agent", agent)
		.setProperty("resultContainer", locationContainer);
		sensors.add(locationSensor);

		AgentListSensor agentListSensor = (AgentListSensor) (new AgentListSensor())
		.setProperty("locationContainer", locationContainer)
		.setProperty("resultContainer", agentListContainer);
		sensors.add(agentListSensor);

		Thing DDoSCommAgentListContainer = new Thing();
		AgentSensor DDoSCommAgentSensor = (AgentSensor) (new AgentSensor())
		.setProperty("agentListContainer", agentListContainer)
		.setNewBoxedProperty("type", "ddoscomm")
		.setProperty("resultContainer", DDoSCommAgentListContainer);
		sensors.add(DDoSCommAgentSensor);

		ArrayList<Location> targetList = this.hasBoxedProperty("targetList") ? (ArrayList<Location>) this.getBoxedProperty("targetList") : new ArrayList<Location>();
		Thing targetListContainer = (new Thing())
		.setProperty("object", targetList);
		TargetSensor targetSensor = (TargetSensor) (new TargetSensor())
		.setProperty("DDoSCommAgentListContainer", DDoSCommAgentListContainer)
		.setProperty("targetListContainer", targetListContainer)
		.setProperty("resultContainer", targetListContainer);
		sensors.add(targetSensor);

		Thing graphContainer = (Thing) this.getProperty("graphContainer");
		Double lifeCounter = (Double) (this.hasProperty("lifeCounter") ? this.getBoxedProperty("lifeCounter"): new Double(100));

		MASONSimScheduler scheduler = (MASONSimScheduler) this.getBoxedProperty("scheduler");
		MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");

		ArrayList<String> ddosAttackTypeList = new ArrayList<String>();
		ddosAttackTypeList.add("ddosattack");
		ddosAttackTypeList.add("malicious");

		DDoSAgentRule ddosAgentRule = (DDoSAgentRule) (new DDoSAgentRule())
		.setNewBoxedProperty("SendCommAgentListActuator", (new SendCommAgentListActuator())
				.setNewBoxedProperty("scheduler", scheduler)
				.setNewBoxedProperty("descheduler", descheduler)
				.setProperty("locationContainer", locationContainer)
				.setProperty("targetListContainer", targetListContainer)
				.setProperty("graphContainer", graphContainer)
				.setProperty("agentListContainer", agentListContainer)
				.setNewBoxedProperty("CreateAgentActuator", (new CreateAgentActuator())
						.setNewBoxedProperty("scheduler", scheduler)
						.setNewBoxedProperty("descheduler", descheduler)
						.setNewBoxedProperty("agentGenerator", (new CommAgentGenerator())
								.setNewBoxedProperty("typeList", ddosAttackTypeList)
								.setNewBoxedProperty("scheduler", scheduler)
								.setNewBoxedProperty("descheduler", descheduler)
								.setNewBoxedProperty("location", location)
								.setProperty("graphContainer", graphContainer)
								.setNewBoxedProperty("lifeCounter", lifeCounter))
								.setProperty("agentListContainer", agentListContainer)));

		rules.add(ddosAgentRule);

		this.setNewBoxedProperty("result", agent);
	}
}


