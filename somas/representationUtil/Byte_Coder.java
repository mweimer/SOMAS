/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package representationUtil;

public class Byte_Coder extends Coder {
	public Byte_Coder(){
		geneSize = Byte.SIZE - 1; // Allows negative numbers since the sign bit is also read
	}
	
	
	@Override
	public Object decode(boolean[] gene) {
		String bitPattern = "";
		
		for(int i = 0; i < gene.length; i++)
			bitPattern += gene[i] ? "1":"0";
		
		Byte result = Byte.valueOf(bitPattern, 2); 

		// Handle the sign bit
		//result *= gene[gene.length - 1] ? -1:1;
		
		return result;
	}

	@Override
	public boolean[] encode(Object val) {
		assert(val instanceof Integer);
		boolean[] gene = new boolean[geneSize];
		
		Integer iv = (Integer) val;
		
		for(int i = 0; iv > 0; i++, iv /= 2)
			gene[i] = (iv % 2 == 1);
		
		return gene;
	}
}
