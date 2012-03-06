/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package sensors;

import java.util.ArrayList;

import environment.Location;

import agent.Agent;


public class LocationPheromoneSensor extends Sensor{
    public LocationPheromoneSensor() {super();}
    
    @Override
	public void run() {
	Double pheromone = (Double)
	    ((Location)
	     ((Agent) this.getBoxedProperty("agent"))
	     .getBoxedProperty("location"))
	    .getBoxedProperty("pheromone");

	this.setBoxedProperty("sensorVariable", new Double(pheromone));
    }

}


