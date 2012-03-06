/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class CompromiseLocation extends Actuator {
    public CompromiseLocation() {super();}
    public CompromiseLocation(Thing t) {super(t);}
    public void run() {
        Location location = (Location) this.getBoxedProperty("location");
        Double compromiseProbability= (Double) location.getBoxedProperty("compromiseProbability");
        if(Math.random() < compromiseProbability)
            location.setBoxedProperty("compromised", new Boolean(true));
    }
}

