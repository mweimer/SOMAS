/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package environment;

import common.*;
import actuators.*;
import java.util.*;

import rules.Rule;
import sensors.Sensor;

public class Location extends Machine {
    public Location() {super();}
    public Location(Thing t) {
        super(t);
    }
        
    @Override
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

