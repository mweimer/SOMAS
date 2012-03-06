/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import unknown.*;
import java.util.*;

import common.Thing;

public class SetPheromoneOnSelfChromosome extends Actuator {
    public SetPheromoneOnSelfChromosome(Thing t) {
        super(t);
    }
    public SetPheromoneOnSelfChromosome() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("pheromone");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("chromosome");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
	public void run() {
        ((Chromosome)
         this
         .getBoxedProperty("chromosome"))

            .setBoxedProperty
            ("pheromone",
             
             (this
              .getBoxedProperty("pheromone")))
            ;
    }
}


