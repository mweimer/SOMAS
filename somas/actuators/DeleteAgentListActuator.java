/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import agent.*;
import environment.*;
import simulator.MASONSimDeScheduler;
import util.*;
import java.util.*;

public class DeleteAgentListActuator extends Actuator {
    public DeleteAgentListActuator() {super();}
    public DeleteAgentListActuator(Thing t) {super(t);}
    public void run() {
        Location location= (Location) this.getBoxedProperty("location");
        ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");
        ArrayList<Agent> agentToDeleteList = (ArrayList<Agent>) this.getBoxedProperty("deleteAgentList");
        agentList.removeAll(agentToDeleteList);
        
        MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");
        for(Machine agent : agentToDeleteList) {
            ArrayList<String> typeList = (ArrayList<String>) agent.getBoxedProperty("typeList");
            ((Machine) descheduler.setProperty("machine", agent)).execute();
        }
        location.setBoxedProperty("agentList", agentList);
    }
}


