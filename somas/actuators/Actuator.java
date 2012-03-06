/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;

public abstract class Actuator extends Machine {
    public Actuator() {super();}
    public Actuator(Thing t) {
        super(t);
    }
}

