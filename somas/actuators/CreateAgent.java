/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
 package actuators;

import common.*;
import agent.*;
import agentGenerator.SOMASAgentGenerator;
import environment.*;
import simulator.*;
import unknown.*;
import java.util.*;

public class CreateAgent extends Actuator {
    public CreateAgent(Thing t) {
	super(t);
    }
    public CreateAgent() {
        ArrayList<String> providedThings = new ArrayList<String>();
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("localChromosomeList");
        requiredThings.add("parameterList");
        requiredThings.add("decoder");
        requiredThings.add("location");
        requiredThings.add("agentList");
        requiredThings.add("scheduler");
        requiredThings.add("agentGenerator");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
	public void run() {
    	
    	// Generate genes from chromosome
        ArrayList<Chromosome> localChromosomeList = (ArrayList<Chromosome>) this.getBoxedProperty("localChromosomeList");

        Chromosome chromosome = (Chromosome) this.getBoxedProperty("chromosome");

        localChromosomeList.add(chromosome);
        
        Agent oldAgent = (Agent)
        this
        .getBoxedProperty("agent");
        MASONSimScheduler scheduler = (MASONSimScheduler) oldAgent.getBoxedProperty("scheduler");
        MASONSimDeScheduler descheduler= (MASONSimDeScheduler) oldAgent.getBoxedProperty("descheduler");
        
        
        SOMASAgentGenerator somasAgentGenerator = new SOMASAgentGenerator(localChromosomeList, scheduler, descheduler);
        Agent newAgent = somasAgentGenerator.generateAgent();

        // Assumes the generator takes care of the chromosomes
            
            
        ((ArrayList<Agent>)
        ((Location)
        		this
        		.getBoxedProperty("location"))
        		.getBoxedProperty("agentList"))
        		.add(newAgent);

        Location location = (Location) this.getBoxedProperty("location"); 
        // MESSY this shouldn't be necessary
        newAgent.setNewBoxedProperty("scheduler", scheduler);
	    newAgent.setNewBoxedProperty("descheduler", descheduler); 
        newAgent
        		.setBoxedProperty("location", location)
            ;
            
            ((Machine)
            ((MASONSimScheduler) oldAgent.getBoxedProperty("scheduler"))
            .setProperty("machine", newAgent))
            .execute();
    }
}


