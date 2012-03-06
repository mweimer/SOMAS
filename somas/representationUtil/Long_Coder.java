/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package representationUtil;

public class Long_Coder extends Coder {
	public Long_Coder() {
		geneSize = Long.SIZE - 1; // The last bit is the sign bit
	}

	@Override
	public Object decode(boolean[] gene) {
		Long result = new Long(0);

		for (int i = 0; i < gene.length; i++) {
			result += gene[i] ? (int) Math.pow(2, i) : 0;
		}

		return result;
	}

	@Override
	public boolean[] encode(Object val) {
		assert (val instanceof Long);
		boolean[] gene = new boolean[geneSize];

		Long iv = (Long) val;

		for (int i = 0; iv > 0; i++, iv /= 2)
			gene[i] = (iv % 2 == 1);

		return gene;
	}
}
