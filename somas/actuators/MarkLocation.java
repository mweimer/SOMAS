/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import environment.*;
import java.util.*;

import common.Thing;

public class MarkLocation extends Actuator {
    public MarkLocation(Thing t) {
        super(t);
    }
    public MarkLocation() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("isVitalNode");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("location");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
	public void run() {
    	Location location = (Location) this.getBoxedProperty("location");
    	
        location.setBoxedProperty("isVitalNode", new Boolean(true)); 
    }
}


