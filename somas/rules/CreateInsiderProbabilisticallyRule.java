/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package rules;

import common.*;
import actuators.*;
import agent.Agent;
import agentGenerator.CompromiseAgentGenerator;

import java.util.*;

import environment.Location;

import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;

public class CreateInsiderProbabilisticallyRule extends Rule {
    public CreateInsiderProbabilisticallyRule() {super();}
    public CreateInsiderProbabilisticallyRule(Thing t) {
        super(t);
    }

    public void run() {
    	Machine tempMachine = null;
    	Machine tempMachine2 = null;
    	Machine tempMachine3 = null;

    	Double creationProbability = (Double) this.getBoxedProperty("creationProbability");
    	
        MASONSimScheduler scheduler = (MASONSimScheduler) this.getBoxedProperty("scheduler");
        MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");
        Location location = (Location) this.getBoxedProperty("location");
        ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");

        if(Math.random() < creationProbability && location.hasBoxedProperty("insiderPresent")) {
        	ArrayList<Location> infoDepotList = (ArrayList<Location>) this.getBoxedProperty("infoDepotList");
        	Thing graphContainer = (Thing) this.getProperty("graphContainer");
        	
        tempMachine = ((Machine) (new CompromiseAgentGenerator()));
        tempMachine.setNewBoxedProperty("scheduler", scheduler);
        tempMachine.setNewBoxedProperty("descheduler", descheduler);
        tempMachine.setNewBoxedProperty("location", location);
        tempMachine.setNewBoxedProperty("infoDepotList", infoDepotList);
        tempMachine.setProperty("graphContainer", graphContainer);
    tempMachine2 = ((Machine) (new CreateAgentActuator()));
    tempMachine2.setNewBoxedProperty("agentList", agentList);
    tempMachine2.setNewBoxedProperty("scheduler", scheduler);
    tempMachine2.setProperty("resultContainer", new Thing());
    tempMachine2.setNewBoxedProperty("descheduler", descheduler);
    tempMachine2.setNewBoxedProperty("agentGenerator", tempMachine);
    tempMachine2.execute();
        }
    }
}

