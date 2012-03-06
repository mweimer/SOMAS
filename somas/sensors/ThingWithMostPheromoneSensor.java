/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import java.util.*;

public class ThingWithMostPheromoneSensor extends Sensor {
    public ThingWithMostPheromoneSensor(Thing t) {
        super(t);
    }
    public ThingWithMostPheromoneSensor() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("sensorVariable");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("thingList");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
        public void run() {
        ArrayList<Thing> thingList = (ArrayList<Thing>) this.getBoxedProperty("thingList");
    
        int numThings = thingList.size();

        Double maxPheromone = Double.NEGATIVE_INFINITY;
        Double thingID = new Double(0);
        
        for(int i = 0; i < numThings; i++) {
            Double pheromoneValue = (Double) thingList.get(i).getBoxedProperty("pheromone");
            
            if(pheromoneValue > maxPheromone) {
                maxPheromone = pheromoneValue;
                thingID = new Double(i);
            }
        }

        thingID /= numThings;

        this.setBoxedProperty("sensorVariable", thingID);
    }
}


