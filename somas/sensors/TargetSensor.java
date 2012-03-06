/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class TargetSensor extends Sensor {
    public TargetSensor() {super();}
    public TargetSensor(Thing t) {super(t);}
    public void run() {
        ArrayList<Agent> DDoSCommAgentList= (ArrayList<Agent>) this.getBoxedProperty("DDoSCommAgentList");
        ArrayList<Location> targetList= (ArrayList<Location>) this.getBoxedProperty("targetList");

        HashSet<Location> uniqueTargetList = new HashSet<Location>(targetList);
        for(Agent agent : DDoSCommAgentList) {
            Location newTarget = (Location) agent.getBoxedProperty("message");
            uniqueTargetList.add(newTarget);
        }

        this.setBoxedProperty("result", new ArrayList<Location>(uniqueTargetList));
    }
}

