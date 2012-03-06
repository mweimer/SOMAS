/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.util.ArrayList;

import common.Thing;

import agent.Agent;

public class ReceiverNodeSensor extends Sensor {
    public ReceiverNodeSensor() {super();}
    public ReceiverNodeSensor(Thing t) {super(t);}
    public void run() {
        ArrayList<Agent> agentList = (ArrayList<Agent>) this.getBoxedProperty("agentList");
        this.setBoxedProperty("result", (Boolean) agentList.contains("receiver"));  // A node is an info node if it is receiving info
    }
}

