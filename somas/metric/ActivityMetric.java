/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package metric;

import java.util.ArrayList;

import agent.Agent;
import environment.Location;

public class ActivityMetric extends Metric {
    public ActivityMetric() {super();}
    
    @Override
        public void run() {
        Double previousAgentCount = null;
        if(this.getProperty("previousAgentCountContainer") == null) {
            previousAgentCount = new Double (0); // HACK
            this.setNewBoxedProperty("previousAgentCount", previousAgentCount);
        } else
            previousAgentCount = (Double) this.getBoxedProperty("previousAgentCount");


        Location location = (Location) this.getBoxedProperty("location");
        Double currentAgentCount = new Double(((ArrayList<Agent>) location.getBoxedProperty("agentList")).size());
        this.setBoxedProperty("previousAgentCount", currentAgentCount);

        Double objectiveValue = (Double) this.getBoxedProperty("objectiveValue");
        if(objectiveValue == null)
            objectiveValue = new Double(0);

        objectiveValue += Math.abs(previousAgentCount - currentAgentCount);

        this.setBoxedProperty("objectiveValue", objectiveValue);
    }
    
}


