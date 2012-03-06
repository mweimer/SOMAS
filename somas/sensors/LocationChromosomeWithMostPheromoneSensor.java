/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import java.util.*;

public class LocationChromosomeWithMostPheromoneSensor extends Sensor {
    public LocationChromosomeWithMostPheromoneSensor(Thing t) {
        super(t);
    }
    public LocationChromosomeWithMostPheromoneSensor() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("sensorVariableContainer");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("locationChromosomeListContainer");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
	public void run() {
        ((Machine)
         (new ThingWithMostPheromoneSensor())
         .setProperty("thingListContainer",
                      (this
                       .getProperty("locationChromosomeListContainer")))
         .setProperty("sensorVariableContainer",
                      (this
                       .getProperty("sensorVariableContainer"))))
            .execute()
            ;
    }
}


