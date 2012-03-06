/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package agentGenerator;

import sensors.*;
import rules.*;
import actuators.*;
import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class ReceiverAgentGenerator extends Machine {
    public ReceiverAgentGenerator() {super();}
    public ReceiverAgentGenerator(Thing t) {super(t);}
    public void run() {
        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        ArrayList<Rule> rules = new ArrayList<Rule>();
        ArrayList<Actuator> actuators = new ArrayList<Actuator>();

        ArrayList<String> typeList = this.hasProperty("typeList") ? (ArrayList<String>) this.getBoxedProperty("typeList") : new ArrayList<String>();
        typeList.add("receiver");

	Location location = (Location) this.getBoxedProperty("location");

        Agent agent = (Agent) (new Agent())
	    .setNewBoxedProperty("location", location)
            .setNewBoxedProperty("typeList", typeList) 
            .setNewBoxedProperty("sensors", sensors)
            .setNewBoxedProperty("rules", rules)
            .setNewBoxedProperty("actuators", actuators);

        ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");

        Thing locationContainer = (new Thing())
            .setProperty("object", location);

        Thing agentListContainer = (new Thing())
            .setProperty("object", agentList);
          
        LocationSensor locationSensor = (LocationSensor) (new LocationSensor())
            .setNewBoxedProperty("agent", agent)
            .setProperty("resultContainer", locationContainer);
        sensors.add(locationSensor);

        AgentListSensor agentListSensor = (AgentListSensor) (new AgentListSensor())
            .setProperty("locationContainer", locationContainer)
            .setProperty("resultContainer", agentListContainer);
        sensors.add(agentListSensor);

        Thing commAgentListContainer = new Thing();
        // For the insider attack scenario the receiver agents should look for packets
        // with of bad info type. In InsiderAttackScenarion this infoType is created
        // set. Other scenarios just assume the info type is "goodinfocomm".
        String infoType;
        if(this.hasBoxedProperty("infoType")) 
        	infoType = (String) this.getBoxedProperty("infoType");
        else infoType = "goodinfocomm";
        AgentSensor commAgentSensor = (AgentSensor) (new AgentSensor())
            .setProperty("agentListContainer", agentListContainer)
            .setNewBoxedProperty("type", infoType)
            .setProperty("resultContainer", commAgentListContainer);
        sensors.add(commAgentSensor);
        
        Thing infoCounterContainer = (Thing) this.getProperty("infoCounterContainer");
        ReceiverAgentRule receiverAgentRule = (ReceiverAgentRule) (new ReceiverAgentRule())
            					.setNewBoxedProperty("incrementInfoActuator", (new IncrementInfoActuator())
                                .setProperty("commAgentListContainer", commAgentListContainer)
                                .setProperty("infoCounterContainer", infoCounterContainer));
        rules.add(receiverAgentRule);
        
        this.setNewBoxedProperty("result", agent);
    }
}

