/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.util.ArrayList;

import environment.Location;

import agent.Agent;


public class LocationNumberNeighborsSensor extends Sensor{
    public LocationNumberNeighborsSensor() {super();}
    
    @Override
	public void run() {
        ArrayList<Location> locationNeighborList = 
            (ArrayList<Location>)
            this.getBoxedProperty("locationList");
                
        this.setBoxedProperty("sensorVariable", new Double(locationNeighborList.size()));
    }

}


