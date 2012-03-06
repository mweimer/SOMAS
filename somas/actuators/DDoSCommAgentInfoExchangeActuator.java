/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class DDoSCommAgentInfoExchangeActuator extends Actuator {
    public DDoSCommAgentInfoExchangeActuator() {super();}
    public DDoSCommAgentInfoExchangeActuator(Thing t) {super(t);}
    public void run() {
        ArrayList<Agent> DDoSCommAgentList = (ArrayList<Agent>) this.getBoxedProperty("DDoSCommAgentList");
        ArrayList<Location> DDoSAgentLocationList = (ArrayList<Location>) this.getBoxedProperty("DDoSAgentLocationList");

        HashSet<Location> DDoSAgentLocationSet = new HashSet<Location>(DDoSAgentLocationList);
        
        for(Agent DDoSCommAgent : DDoSCommAgentList)
            DDoSAgentLocationSet.add((Location) DDoSCommAgent.getBoxedProperty("DDoSAgentLocation"));

        DDoSAgentLocationList = new ArrayList<Location>(DDoSAgentLocationSet);
        this.setBoxedProperty("DDoSAgentLocationList", DDoSAgentLocationList);
    }
}


