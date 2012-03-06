/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import java.util.*;

public class DecreasePheromone extends Actuator {
    public DecreasePheromone() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("pheromone");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("pheromone");
        requiredThings.add("parameterList");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
	public void run() {
        this
            .setBoxedProperty
            ("pheromone",

             (Double)
             this
             .getBoxedProperty
             ("pheromone")

             -

             (Double)
             ((ArrayList<Thing>)
              this
              .getProperty("parameterList"))
             .get(0)
             .getProperty("object"));
    }
}


