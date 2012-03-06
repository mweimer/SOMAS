/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.util.ArrayList;

import environment.Location;

import agent.Agent;


public class LocationAgentsCumulativePheromoneSensor extends Sensor{
    public LocationAgentsCumulativePheromoneSensor() {super();}
    
    @Override
	public void run() {
        ArrayList<Agent> locationAgentList = (ArrayList<Agent>)
            this
            .getBoxedProperty("agentList");
                
        Double pheromone = new Double(0);
        for(Agent a : locationAgentList) {
            Double agentPheromone = a.hasProperty("pheromone") ? (Double) a.getBoxedProperty("pheromone") : new Double(0);
            pheromone += agentPheromone;
        }

        this.setBoxedProperty("sensorVariable", new Double(pheromone));
    }

}


