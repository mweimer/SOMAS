/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package sensors;

import agent.*;
import environment.*;
import java.util.*;

import common.Machine;

public class AgentOrderSensor extends Sensor {
	public AgentOrderSensor() {
		ArrayList<String> providedThings = new ArrayList<String>();
		providedThings.add("agentList");
		this.setProperty("providedThings", providedThings);

		ArrayList<String> requiredThings = new ArrayList<String>();
		requiredThings.add("agent");
		requiredThings.add("agentList");
		this.setProperty("requiredThings", requiredThings);
	}

	@Override
	public void run() {
		ArrayList<Agent> agentList = new ArrayList<Agent>(
				(ArrayList<Agent>) ((Location) ((Agent) this
						.getBoxedProperty("agent"))
						.getBoxedProperty("location"))
						.getBoxedProperty("agentList"));

		// This agent is already a part of the agentList
		agentList = (ArrayList<Agent>) ((Machine) (new ThingPheromoneOrderSensor())
				.setNewBoxedProperty("thingList", agentList)).execute()
				.getBoxedProperty("result");
		this.setBoxedProperty("agentList", agentList);
	}
}
