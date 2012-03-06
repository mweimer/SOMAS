/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

    import org.jgrapht.Graph;
import common.*;
import agent.*;
import environment.*;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import util.*;
import java.util.*;

public class SendCommAgentListActuator extends Actuator {
    public SendCommAgentListActuator() {super();}
    public SendCommAgentListActuator(Thing t) {super(t);}
    public void run() {
        Location location = (Location) this.getBoxedProperty("location");

        Graph<Location, Edge> graph = (Graph<Location,Edge>) this.getBoxedProperty("graph");
        
        ArrayList<Location> targetList = (ArrayList<Location>) this.getBoxedProperty("targetList");

        Thing graphContainer = (Thing) this.getProperty("graphContainer");

        Thing lifeCounterContainer = (new Thing())
            .setProperty("object", new Integer(100)); // TODO make sure this number is large enough

        MASONSimScheduler scheduler = (MASONSimScheduler) this.getBoxedProperty("scheduler");
        MASONSimDeScheduler descheduler= (MASONSimDeScheduler) this.getBoxedProperty("descheduler");

        for(Location target : targetList) {
            if(SOMAUtil.nextHop(graph, location, target) != location) // HACK sends info as long as the target is reachable
            {
            Machine createAgentActuator = (Machine) this.getBoxedProperty("CreateAgentActuator");

            ((Machine) createAgentActuator
            .getBoxedProperty("agentGenerator"))
            .setNewBoxedProperty("location", location)
                .setNewBoxedProperty("message", "info")
                .setNewBoxedProperty("destination", target);

            ((Machine) createAgentActuator
                    .setNewBoxedProperty("location", location)
            .setNewBoxedProperty("scheduler", scheduler)
 			.setNewBoxedProperty("descheduler", descheduler)
 			.setNewBox("result"))
            .execute();

                 	break; // HACK
            }
            else // DEBUG
            {
            }
    	}
    }
}

