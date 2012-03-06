/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import environment.*;
import java.util.*;

import common.Thing;

public class UnMarkLocation extends Actuator {
    public UnMarkLocation(Thing t) {
        super(t);
    }
    public UnMarkLocation() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("isVitalNode");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("location");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
	public void run() {
        ((Location) this.getBoxedProperty("location"))
            .setBoxedProperty("isVitalNode", new Boolean(false)); 
    }
}


