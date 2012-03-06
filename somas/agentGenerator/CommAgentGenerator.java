/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package agentGenerator;

import common.*;
import actuators.ChangeLocationActuator;
import actuators.DeleteAgentListActuator;
import agent.*;
import environment.*;
import rules.CommAgentRule;
import rules.Rule;
import sensors.LocationSensor;
import sensors.NextHopSensor;
import sensors.NextNetworkSearchHopSensor;
import sensors.PropertySensor;
import sensors.Sensor;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import util.*;
import java.util.*;

public class CommAgentGenerator extends Machine {
    public CommAgentGenerator() {super();}
    public CommAgentGenerator(Thing t) {super(t);}
    public void run() {
//        ArrayList<String> typeList = this.hasBoxedProperty("typeList") ? (ArrayList<String>) this.getBoxedProperty("typeList") : new ArrayList<String>();
      ArrayList<String> typeList = (ArrayList<String>) this.getBoxedProperty("typeList");
        typeList.add("comm");

        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        ArrayList<Rule> rules = new ArrayList<Rule>();

        Location location = (Location) this.getBoxedProperty("location");
        Thing locationContainer = (new Thing())
            .setProperty("object", location);

        Object message = this.hasProperty("message") ? (Object) this.getBoxedProperty("message"):"";

        Agent agent = (Agent) (new Agent())
	    .setNewBoxedProperty("location", location)
            .setProperty("locationContainer", locationContainer)
            .setNewBoxedProperty("typeList", typeList)
            .setNewBoxedProperty("message", message)
             .setNewBoxedProperty("sensors", sensors)
             .setNewBoxedProperty("rules", rules)
             .setNewBoxedProperty("typeList", typeList);

        Location destination = (Location) this.getBoxedProperty("destination");
        Double lifeCounter = (Double) (this.hasProperty("lifeCounter") ? this.getBoxedProperty("lifeCounter"): new Double(100));
        Thing graphContainer = (Thing) this.getProperty("graphContainer"); // TODO make sure all agents that need this have it, and implement a graph update component
        Thing destinationContainer = (new Thing())
            .setProperty("object", destination);

        LocationSensor locationSensor = (LocationSensor) (new LocationSensor())
            .setNewBoxedProperty("agent", agent)
            .setProperty("resultContainer", locationContainer);
        sensors.add(locationSensor);

        Thing nextHopContainer = new Thing();
        ArrayList<Location> visitedList = new ArrayList<Location>();
        visitedList.add(location);
        ArrayList<Location> hopStack = new ArrayList<Location>();
        NextHopSensor nextHopSensor = (NextHopSensor) (new NextHopSensor())
            .setProperty("locationContainer", locationContainer)
	    .setNewBoxedProperty("destination", destination)
            .setProperty("graphContainer", graphContainer)
            .setProperty("resultContainer", nextHopContainer);
        sensors.add(nextHopSensor);

        ArrayList<Agent> deleteAgentList = new ArrayList<Agent>();
        deleteAgentList.add(agent);

        Thing agentListContainer = (new Thing())
            .setProperty("object", deleteAgentList);

        MASONSimScheduler scheduler = (MASONSimScheduler) this.getBoxedProperty("scheduler");
        MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");
        
        CommAgentRule commAgentRule = (CommAgentRule) (new CommAgentRule())
            .setNewBoxedProperty("lifeCounter", lifeCounter)
            .setProperty("locationContainer", locationContainer)
            .setProperty("destinationContainer", destinationContainer)
            .setNewBoxedProperty("agent", agent)
            .setProperty("nextHopContainer", nextHopContainer)
            .setNewBoxedProperty("deleteAgentListActuator", (new DeleteAgentListActuator())
	 				.setNewBoxedProperty("scheduler", scheduler)
	 				.setNewBoxedProperty("descheduler", descheduler)
            		.setProperty("locationContainer", locationContainer)
            		.setNewBoxedProperty("deleteAgentList", deleteAgentList))
            .setNewBoxedProperty("changeLocationActuator", (new ChangeLocationActuator())
            		.setNewBoxedProperty("agent", agent)
            		.setProperty("oldLocationContainer", locationContainer)
            		.setProperty("newLocationContainer", nextHopContainer)
                                 .setProperty("agentListContainer", agentListContainer))
            ;
        rules.add(commAgentRule);
        
        this.setBoxedProperty("result", agent);
    }
}
