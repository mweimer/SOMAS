/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package rules;

import common.*;
import actuators.*;
import java.util.*;

public class ExecuteAllRule extends Rule {
    public ExecuteAllRule() {super();}
    public ExecuteAllRule(Thing t) {
        super(t);
    }

    public void run() {
        ArrayList<Actuator> actuators = (ArrayList<Actuator>) this.getBoxedProperty("actuators");

        for(Actuator actuator : actuators)
            actuator.execute();
    }
}

