/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package agent;

import common.Machine;
import common.Thing;

import java.util.*;

import sensors.*;

import rules.*;

public class Agent extends Machine {
    public Agent() {super();
    	this.setNewBoxedProperty("pheromone", new Double(0));
    }
    
    public Agent(Thing t) {
        super(t);
    }
        
    @Override
        @SuppressWarnings("unchecked")
        public void run() {

    	ArrayList<Sensor> sensors = (ArrayList<Sensor>) this.getBoxedProperty("sensors");
        for(Sensor s : sensors) {
            s.execute();
        }
        ArrayList<Rule> rules = (ArrayList<Rule>) this.getBoxedProperty("rules");
        for(Rule r : rules)
            r.execute();
    }

}