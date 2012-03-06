/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.util.ArrayList;

import environment.Location;

import agent.Agent;

public class LocationSensor extends Sensor {
    public LocationSensor() {super();}
    
    @Override
	public void run() {
    	Agent agent = (Agent)
        this
        .getBoxedProperty("agent"); 
    	
        Location location = (Location) 
            agent
            .getBoxedProperty("location");

        this.setBoxedProperty("result", location);
    }

}    

