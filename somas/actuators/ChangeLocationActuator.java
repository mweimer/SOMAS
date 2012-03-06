/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
 package actuators;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

/**
 * Non SOMAS agents, change the agent's location based on self
 * descriptive parameters "oldLocation" and "newLocation".
 */

public class ChangeLocationActuator extends Actuator {
    public ChangeLocationActuator() {super();}
    public ChangeLocationActuator(Thing t) {super(t);}
    public void run() {
        Location oldLocation = (Location) this.getBoxedProperty("oldLocation");
        Location newLocation = (Location) this.getBoxedProperty("newLocation");
        Agent agent = (Agent) this.getBoxedProperty("agent");

        ArrayList<Agent> oldAgentList = (ArrayList<Agent>) oldLocation.getBoxedProperty("agentList");
        ArrayList<Agent> newAgentList = (ArrayList<Agent>) newLocation.getBoxedProperty("agentList");

        oldAgentList.remove(agent);
        newAgentList.add(agent);
        agent.setBoxedProperty("location", newLocation);
    }
}

