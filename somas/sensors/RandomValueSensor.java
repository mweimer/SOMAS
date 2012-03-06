/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import java.util.*;

public class RandomValueSensor extends Sensor {
    public RandomValueSensor() {
        ArrayList<String> providedThings = new ArrayList<String>();
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        providedThings.add("randomValueVariableContainers");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
        public void run() {
        for(Thing randomValueVariable : (ArrayList<Thing>) this.getProperty("randomVariableContainers"))
            randomValueVariable.setProperty("object", new Double(Math.random()));
    }
}


