/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package metric;

import java.util.HashSet;
import java.util.Set;


import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;

import agent.Agent;
import common.*;
import environment.*;
import java.util.*;

public class AgentTypeCounterMetric extends Metric {
    public AgentTypeCounterMetric() {super();}
    
    @Override
        public void run() {
    	MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");
    	Machine machine = (Machine) descheduler.getProperty("machine");

    	if(machine instanceof Agent) {
    		Agent agent = (Agent) machine;
    		
    	String type = (String) this.getBoxedProperty("type");
    	
    	ArrayList<String> typeList = (ArrayList<String>) agent.getBoxedProperty("typeList");

    		if(typeList.contains(type)) {
    		Double objectiveValue = (Double) this.getBoxedProperty("objectiveValue");
    		objectiveValue++;
    		this.setBoxedProperty("objectiveValue", objectiveValue);
    		}
    	}    	
    }
}

