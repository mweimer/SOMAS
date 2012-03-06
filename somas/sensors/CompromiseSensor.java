/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class CompromiseSensor extends Sensor {
    public CompromiseSensor() {super();}
    public CompromiseSensor(Thing t) {super(t);}
    public void run() {
        Location location = (Location) this.getBoxedProperty("location");
        Double compromiseDetectionProbability = (Double) this.getBoxedProperty("compromiseDetectionProbability");
        Boolean compromiseCheck = false;          
        if(Math.random() < compromiseDetectionProbability)
            compromiseCheck = true;
        this.setBoxedProperty("result", compromiseCheck);
    }
}

