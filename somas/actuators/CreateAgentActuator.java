/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package actuators;

import common.*;
import agent.*;
import environment.*;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import util.*;
import java.util.*;

public class CreateAgentActuator extends Actuator {
	public CreateAgentActuator() {
		super();
	}

	public CreateAgentActuator(Thing t) {
		super(t);
	}

	public void run() {
		MASONSimScheduler scheduler = (MASONSimScheduler) this
				.getBoxedProperty("scheduler");
		MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this
				.getBoxedProperty("descheduler");

		ArrayList<Agent> agentList = (ArrayList<Agent>) this
				.getBoxedProperty("agentList");
		Agent agent = (Agent) ((Agent) ((Machine) ((Thing) this
				.getBoxedProperty("agentGenerator")).setNewBox("result"))
				.execute().getBoxedProperty("result")).setNewBoxedProperty(
				"scheduler", scheduler).setNewBoxedProperty("descheduler",
				descheduler);
		agentList.add(agent);

		((Machine) scheduler.setProperty("machine", agent)).execute();

		this.setBoxedProperty("agentList", agentList);
		this.setBoxedProperty("result", agent);
	}
}
