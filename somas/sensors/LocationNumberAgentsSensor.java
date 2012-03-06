/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.util.ArrayList;

import environment.Location;

import agent.Agent;


public class LocationNumberAgentsSensor extends Sensor{
    public LocationNumberAgentsSensor() {super();}
    
    @Override
	public void run() {
        ArrayList<Agent> locationAgentList = (ArrayList<Agent>) 
            this.getBoxedProperty("agentList");
                
        this.setBoxedProperty("sensorVariable", new Double(locationAgentList.size()));
    }

}


