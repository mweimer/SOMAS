/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package agentGenerator;

import common.*;
import actuators.CreateAgentActuator;
import actuators.SendCommAgentListActuator;
import agent.*;
import environment.*;
import rules.Rule;
import rules.SenderAgentRule;
import sensors.Sensor;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import util.*;
import java.util.*;

public class SenderAgentGenerator extends Machine {
	public SenderAgentGenerator() {super();}
	public SenderAgentGenerator(Thing t) {super(t);}
	public void run() {
		ArrayList<Sensor> sensors = new ArrayList<Sensor>();
		ArrayList<Rule> rules = new ArrayList<Rule>();

		ArrayList<String> typeList = this.hasProperty("typeList") ? (ArrayList<String>) this.getBoxedProperty("typeList") : new ArrayList<String>();
		typeList.add("sender");

		Location location = (Location) this.getBoxedProperty("location");

		Agent agent = (Agent) (new Agent())
		.setNewBoxedProperty("location", location)
		.setNewBoxedProperty("typeList", typeList)
		.setNewBoxedProperty("sensors", sensors)
		.setNewBoxedProperty("rules", rules);

		Thing agentListContainer = (Thing) location.getProperty("agentListContainer");
		Thing targetListContainer = (Thing) this.getProperty("targetListContainer");
		Thing lifeCounterContainer = (Thing) this.getProperty("lifeCounterContainer");

		Thing graphContainer = (Thing) this.getProperty("graphContainer");

		Thing locationContainer = (new Thing())
		.setProperty("object", location);

		ArrayList<String> commAgentTypeList = new ArrayList<String>();
		commAgentTypeList.add("goodinfocomm");

		MASONSimScheduler scheduler = (MASONSimScheduler) this.getBoxedProperty("scheduler");
		MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");

		Double sendprob;
		if(this.hasBoxedProperty("sendProbability"))
			sendprob = (Double) this.getBoxedProperty("sendProbability");
		else sendprob = new Double(0.1);
		SenderAgentRule senderAgentRule = (SenderAgentRule) (new SenderAgentRule())
		.setNewBoxedProperty("sendProbability", sendprob)
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
								.setNewBoxedProperty("scheduler", scheduler)
								.setNewBoxedProperty("descheduler", descheduler)
								.setNewBoxedProperty("typeList", commAgentTypeList)
								.setNewBoxedProperty("location", location)
								.setProperty("graphContainer", graphContainer)
								.setProperty("lifeCounterContainer", lifeCounterContainer))
								.setProperty("agentListContainer", agentListContainer)));
		rules.add(senderAgentRule);
		this.setNewBoxedProperty("result", agent);
	}
}

