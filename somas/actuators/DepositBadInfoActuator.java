/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class DepositBadInfoActuator extends Actuator {
    public DepositBadInfoActuator() {super();}
    public DepositBadInfoActuator(Thing t) {super(t);}
    public void run() {
        Location location = (Location) this.getBoxedProperty("location");
        Double badInfoCounter = location.hasBoxedProperty("badInfoCounter") ? (Double) location.getBoxedProperty("badInfoCounter") : new Double(0);  // HACK
        Double badInfoIncrementer = (Double) this.getBoxedProperty("badInfoIncrementer");
        badInfoCounter += badInfoIncrementer;
        Thing temp = ((Boolean) location.hasBoxedProperty("badInfoCounter")) ? location.setBoxedProperty("badInfoCounter", badInfoCounter) : location.setNewBoxedProperty("badInfoCounter", badInfoCounter); // HACK
    }
}

