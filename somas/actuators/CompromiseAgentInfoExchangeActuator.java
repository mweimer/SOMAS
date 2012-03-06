/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class CompromiseAgentInfoExchangeActuator extends Actuator {
    public CompromiseAgentInfoExchangeActuator() {super();}
    public CompromiseAgentInfoExchangeActuator(Thing t) {super(t);}
    public void run() {
        ArrayList<Location> DDoSAgentLocationList = (ArrayList<Location>) this.getBoxedProperty("DDoSAgentLocationList");
        ArrayList<Agent> compromiseAgentList= (ArrayList<Agent>) this.getBoxedProperty("compromiseAgentList");

        for(Agent compromiseAgent : compromiseAgentList) {
            ArrayList<Location> otherDDoSAgentLocationList  = (ArrayList<Location>) compromiseAgent.getBoxedProperty("DDoSAgentLocationList");

            DDoSAgentLocationList.addAll(otherDDoSAgentLocationList);
        }
        this.setBoxedProperty("DDoSAgentLocationList", DDoSAgentLocationList);
    }
}

