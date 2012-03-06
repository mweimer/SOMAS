/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.util.ArrayList;

import common.Thing;

import agent.Agent;

public class ContextPheromoneSensor extends Sensor {
    public ContextPheromoneSensor() {super();}
    
    @Override
	public void run() {
        Double pheromone = (Double)
            ((Thing) this
             .getBoxedProperty("context"))
            .getBoxedProperty("pheromone");

        this.setBoxedProperty("sensorVariable", new Double(pheromone));
    }

}


