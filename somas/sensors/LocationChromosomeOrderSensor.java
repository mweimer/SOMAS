/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import agent.*;
import environment.*;
import unknown.*;
import java.util.*;

public class LocationChromosomeOrderSensor extends Sensor {
    public LocationChromosomeOrderSensor() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("locationChromosomeList");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("agent");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
	public void run() {
        ArrayList<Thing> agentList = new ArrayList<Thing>((ArrayList<Agent>)
                                                          ((Location)
                                                           ((Agent)
                                                            this
                                                            .getBoxedProperty("agent"))
                                                           .getBoxedProperty("location"))
                                                          .getBoxedProperty("agentList"))
            ;
        // This agent is already in the agentList

        ArrayList<Chromosome> locationChromosomeList = new ArrayList<Chromosome>();

        for(Thing a : agentList) {
        	ArrayList<Chromosome> localChromosomeList = a.hasBoxedProperty("localChromosomeList") ? (ArrayList<Chromosome>) a.getBoxedProperty("localChromosomeList") : new ArrayList<Chromosome>();
            locationChromosomeList.addAll(localChromosomeList);
        }
            
        locationChromosomeList = (ArrayList<Chromosome>) ((Machine) 
                                                          (new ThingPheromoneOrderSensor())
                                                          .setNewBoxedProperty("thingList", locationChromosomeList))
            .execute()
            .getBoxedProperty("result");
        this.setBoxedProperty("locationChromosomeList", locationChromosomeList);
    }
}


