/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package representations;

import representationUtil.Coder;
import representationUtil.IntBit_Coder;

public class IntBitRep extends Representation {
    public IntBitRep() 
    {
        coder = new IntBit_Coder();  // MESSY ?
    }
        
    public IntBitRep(int[] v) { // MESSY
        coder = new IntBit_Coder(); // MESSY ?
        Integer[] vals = new Integer[v.length];
        for(int i = 0; i < vals.length; i++)
            vals[i] = v[i];

        encode(vals);
    }

    public IntBitRep(boolean[][] c) {
        chromosome = c;
        coder = new IntBit_Coder(); // MESSY ?
    }
                
    public IntBitRep(Representation r) {
        super(r);
    }

    public IntBitRep(Representation gbr, Coder c) {
        super(gbr, c);
    }
        
    @Override
        public void encode(Object[] vals) {
        chromosome = new boolean[vals.length][0];
                
        totalsize = 0;
                
        for(int i = 0; i < vals.length; i++) {
            chromosome[i] = coder.encode(vals[i]);
                        
            totalsize += chromosome[i].length;
        }
    }

    @Override
        public Object[] decode() {
        Object[] vals = new Object[chromosome.length];
                
        for(int i = 0; i < chromosome.length; i++)
            vals[i] = coder.decode(chromosome[i]);
                                
        return vals;
    }
}
