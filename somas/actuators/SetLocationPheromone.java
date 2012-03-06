/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import agent.*;
import java.util.*;

import util.Sampler;

import common.Thing;

public class SetLocationPheromone extends Actuator {
    public SetLocationPheromone() {super();}
    public SetLocationPheromone(Thing t) {
        super(t);
    }
    @Override
	public void run() {
    	Sampler sampler = (Sampler) this.getBoxedProperty("sampler");
    	
        Double pheromone = (Double) sampler.execute().getBoxedProperty("result");
        
        // System.out.println("pre pheromone at location : " + pheromone); // DEBUG
        
        ArrayList<Agent> agentList = (ArrayList<Agent>) this.getBoxedProperty("agentList");

        for(Agent agent : agentList)
            pheromone += agent.hasBoxedProperty("pheromone") ? (Double) agent.getBoxedProperty("pheromone") : new Double(0);

        // System.out.println("post pheromone at location : " + pheromone); // DEBUG
            
        this.setBoxedProperty("pheromone", pheromone);
    }
}

