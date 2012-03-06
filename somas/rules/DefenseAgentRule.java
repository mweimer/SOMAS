/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package rules;

import actuators.*;
import common.*;
import agent.*;
import agentGenerator.CommAgentGenerator;
import agentGenerator.DDoSAgentGenerator;
import environment.*;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import util.*;
import java.util.*;

public class DefenseAgentRule extends Rule {
	public DefenseAgentRule() {super();}
	public DefenseAgentRule(Thing t) {super(t);}
	public void run() {
		Boolean compromiseCheck = (Boolean) this.getBoxedProperty("compromiseCheck");

		if(!compromiseCheck) {
			Actuator compromiseActuator  = (Actuator) this.getBoxedProperty("compromiseActuator");
			compromiseActuator.execute();
		}

		Location location = (Location) this.getBoxedProperty("location");
		ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");

		compromiseCheck = (Boolean) this.getBoxedProperty("compromiseCheck");
		((Actuator) this.getBoxedProperty("deleteAgentListActuator")).execute();
	}

}



