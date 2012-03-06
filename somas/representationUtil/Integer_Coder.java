/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package representationUtil;

public class Integer_Coder extends Coder {
    public Integer_Coder(){
        geneSize = Integer.SIZE;
    }
        
        
    @Override
        public Object decode(boolean[] gene) {
        Integer result = new Integer(0);
                
        for(int i = 0; i < gene.length; i++)
            result += gene[i] ?(int) Math.pow(2, i):0;
                
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
