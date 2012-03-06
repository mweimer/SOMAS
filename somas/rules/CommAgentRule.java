/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package rules ;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class CommAgentRule extends Rule {
    public CommAgentRule() {super();}
    public CommAgentRule(Thing t) {super(t);}
    public void run() {
        Double lifeCounter = (Double) this.getBoxedProperty("lifeCounter");

        lifeCounter -= Math.pow(Math.random(), 1.0);

        Location location = (Location) this.getBoxedProperty("location");
        Location destination = (Location) this.getBoxedProperty("destination");

    	Machine changeLocationActuator = (Machine) ((Thing) this.getBoxedProperty("changeLocationActuator"));
        // Test to see if agent is stuck: covers both arrival and unreachable destinations
        Location oldLocation = (Location) changeLocationActuator.getBoxedProperty("oldLocation");
        Location newLocation = (Location) changeLocationActuator.getBoxedProperty("newLocation");
        
        if(oldLocation == newLocation && lifeCounter > 10.0) { 
        	if(location == destination)
        		lifeCounter = 10.0;
        	else
        		lifeCounter = 1.0;
        }
        if(lifeCounter < 0.0){
            ((Machine) ((Thing) this.getBoxedProperty("deleteAgentListActuator")))
                .execute();
        }
        else {
        	changeLocationActuator
                .execute();
        }

        this.setBoxedProperty("lifeCounter", lifeCounter);
    }
}

