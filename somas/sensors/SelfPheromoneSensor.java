/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package sensors;

import java.util.ArrayList;

import common.Thing;

import agent.Agent;

public class SelfPheromoneSensor extends Sensor {
    public SelfPheromoneSensor() {super();}
    
	@Override
	public void run() {
	    Double pheromone = (Double)
		((Agent) this
		 .getBoxedProperty("agent"))
		.getBoxedProperty("pheromone");

	    this.setBoxedProperty("sensorVariable", new Double(pheromone));
	}

}


