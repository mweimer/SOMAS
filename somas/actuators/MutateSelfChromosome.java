/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
 package actuators;

import common.*;
import unknown.*;
import util.*;
import java.util.*;

public class MutateSelfChromosome extends Actuator {
	public MutateSelfChromosome(Thing t) {
		super(t);
	}

	public MutateSelfChromosome() {
		ArrayList<String> providedThings = new ArrayList<String>();
		providedThings.add("chromosomeList");
		this.setProperty("providedThings", providedThings);

		ArrayList<String> requiredThings = new ArrayList<String>();
		requiredThings.add("localChromosomeList");
		requiredThings.add("mutateOperator");
		requiredThings.add("parameterList");
		this.setProperty("requiredThings", requiredThings);
	}

	@Override
	public void run() {
		// HACK lots of hacks throughout
		ArrayList<Chromosome> localChromosomeList = (ArrayList<Chromosome>) this
				.getBoxedProperty("localChromosomeList");
		int chromosomeLocation = SOMAUtil
				.doubleToInteger((Double) ((ArrayList<Thing>) this
						.getProperty("parameterList")).get(0).getProperty(
						"object")

						*

						(localChromosomeList.size() - 1));
		Chromosome chromosome = localChromosomeList.get(chromosomeLocation);

		Thing chromosomeContainer = new Thing();
		chromosomeContainer.setProperty("object", chromosome);

		((Machine) ((Machine) getBoxedProperty("mutateOperator")).setProperty(
				"chromosomeContainer", chromosomeContainer).setProperty(
				"result", chromosomeContainer)).execute();

		chromosome = (Chromosome) chromosomeContainer.getProperty("object");
		localChromosomeList.set(chromosomeLocation, chromosome);
	}
}
