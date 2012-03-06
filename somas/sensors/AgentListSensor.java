/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package sensors;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class AgentListSensor extends Sensor {
	public AgentListSensor() {
		super();
	}

	public AgentListSensor(Thing t) {
		super(t);
	}

	public void run() {
		Location location = (Location) this.getBoxedProperty("location");
		ArrayList<Agent> agentList = (ArrayList<Agent>) location
				.getBoxedProperty("agentList");
		this.setBoxedProperty("result", agentList);
	}
}
