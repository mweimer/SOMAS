/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package unknown;

import common.*;

public class Chromosome extends Machine { // HACK shouldn't really be a
											// Machine - so it can be used in
											// the pheromoneordersensor
	public Chromosome() {
		super();
	}

	public Chromosome(Thing t) {
		super(t);
	}

	public void run() {
	}
}
