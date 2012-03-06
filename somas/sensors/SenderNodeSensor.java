/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.util.ArrayList;

import agent.Agent;

import common.Thing;

public class SenderNodeSensor extends Sensor {
    public SenderNodeSensor() {super();}
    public SenderNodeSensor(Thing t) {super(t);}
    public void run() {
        ArrayList<Agent> agentList= (ArrayList<Agent>) this.getBoxedProperty("agentList");
        this.setBoxedProperty("result", (Boolean) agentList.contains("sender"));  // A node is important if it is sending info
    }
}

