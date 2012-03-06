/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import agent.*;
import unknown.*;
import java.util.*;

import common.Machine;
import common.Thing;

public class LocalChromosomeOrderSensor extends Sensor {
    public LocalChromosomeOrderSensor(Thing t) {
        super(t);
    }
    public LocalChromosomeOrderSensor() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("localChromosomeList");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("agent");
        this.setProperty("requiredThings", requiredThings);
    }
    
    @Override
	public void run() {
        ArrayList<Chromosome> localChromosomeList =
            new ArrayList<Chromosome>(
                                      (ArrayList<Chromosome>)
                                      ((Agent)
                                       this
                                       .getBoxedProperty("agent"))
                                      .getBoxedProperty("localChromosomeList"))
                                                                    
            ;

        localChromosomeList = (ArrayList<Chromosome>) ((Machine) 
                                                       (new ThingPheromoneOrderSensor())
                                                       .setNewBoxedProperty("thingList", localChromosomeList))
            .execute()
            .getBoxedProperty("result");
        this.setBoxedProperty("localChromosomeList", localChromosomeList);
    }
}

