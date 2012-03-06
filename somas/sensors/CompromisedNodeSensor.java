/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class CompromisedNodeSensor extends Sensor {
    public CompromisedNodeSensor() {super();}
    public CompromisedNodeSensor(Thing t) {super(t);}
    public void run() {
        Double detectionProbability = (Double) this.getBoxedProperty("detectionProbability");
        Location location  = (Location) this.getBoxedProperty("location"); // TODO requires location sensor
        Boolean compromise = (Boolean) location.getBoxedProperty("compromised");
        Boolean result = false;
        if(Math.random() < detectionProbability)
            result = new Boolean(compromise);
        this.setBoxedProperty("result", result);
    }
}

