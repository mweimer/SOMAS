/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package representationUtil;

import java.io.Serializable;

// MESSY this whole construct can probably be simplified by making it generic

public abstract class Coder implements Serializable {
	public Coder() {
		super();
	}

	protected int geneSize = 0;

	abstract public boolean[] encode(Object val);

	abstract public Object decode(boolean[] gene);

	public boolean[] adjust(boolean[] gene) {
		boolean[] adjGene = new boolean[geneSize];

		for (int i = geneSize - gene.length; i < geneSize; i++)
			adjGene[i] = gene[i];

		return adjGene;
	}

	public int getGeneSize() {
		return geneSize;
	}

	public void setGeneSize(int gs) {
		geneSize = gs;
	}
}
