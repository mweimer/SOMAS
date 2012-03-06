/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package representationUtil;

public class IntBit_Coder extends Coder {
    public IntBit_Coder(){
        geneSize = 0; 
    }
        
    @Override
        public Object decode(boolean[] gene) {
        Integer result = new Integer(0);
                
        result = gene[0] ? 1:0;
                
        return result;
    }

    @Override
        public boolean[] encode(Object val) {
        assert(val instanceof Integer);
        boolean[] gene = new boolean[1];
                
        gene[0] = (Integer) val == 1;
                
        return gene;
    }
}
