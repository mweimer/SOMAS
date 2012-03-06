/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package actuators;

import common.*;
import unknown.*;
import util.*;
import java.util.*;

public class CrossoverLocalChromosomeWithLocationChromosome extends Actuator {
	public CrossoverLocalChromosomeWithLocationChromosome(Thing t) {
		super(t);
	}

	public CrossoverLocalChromosomeWithLocationChromosome() {
		ArrayList<String> providedThings = new ArrayList<String>();
		this.setProperty("providedThings", providedThings);

		ArrayList<String> requiredThings = new ArrayList<String>();
		requiredThings.add("crossoverOperator");
		requiredThings.add("localChromosomeList");
		requiredThings.add("locationChromosomeList");
		requiredThings.add("parameterList");
		this.setProperty("requiredThings", requiredThings);
	}

	@Override
	public void run() {
		ArrayList<Chromosome> localChromosomeList = (ArrayList<Chromosome>) this
				.getBoxedProperty("localChromosomeList");
		int selectedLocalChromosome = SOMAUtil
				.doubleToInteger((Double) ((ArrayList<Thing>) this
						.getProperty("parameterList")).get(0).getProperty(
						"object")

						*

						(localChromosomeList.size() - 1));

		ArrayList<Chromosome> locationChromosomeList = (ArrayList<Chromosome>) this
				.getBoxedProperty("locationChromosomeList");

		localChromosomeList.set(selectedLocalChromosome,
				(Chromosome) ((Machine) ((Operator) this
						.getBoxedProperty("crossoverOperator"))

				.setNewBoxedProperty("chromosome1",

				localChromosomeList.get(selectedLocalChromosome))

				.setNewBoxedProperty(
						"chromosome2",

						locationChromosomeList.get(SOMAUtil.doubleToInteger

						((Double) ((ArrayList<Thing>) this
								.getProperty("parameterList")).get(1)
								.getProperty("object")

								*

								(locationChromosomeList.size() - 1)))))

				.execute().getBoxedProperty("result"));
	}
}
