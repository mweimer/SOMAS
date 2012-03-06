/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package representations;

import representationUtil.Coder;
import representationUtil.Util;

public class GrayBitRep extends Representation{
    public GrayBitRep(Representation r) {
        super(r);
    }

    public GrayBitRep(Coder c) {
        coder = c;
    }
        
    public GrayBitRep(Representation r, Coder c) {
        super(r, c);
    }

    public GrayBitRep(boolean[][] chromo, Coder c) {
        this(c);
        setChromosome(chromo);
    }
        
    public GrayBitRep(Object[] vals, Coder c) {
        super(vals, c);
    }
        
    public GrayBitRep() {
        super();
    }

    @Override
        public void encode(Object[] vals) {
        chromosome = new boolean[vals.length][0];
                
        totalsize = 0;
                
        for(int i = 0; i < vals.length; i++) {
            chromosome[i] = Util.gray(coder.encode(vals[i]));
                        
            totalsize += chromosome[i].length;
        }
    }

    @Override
        public Object[] decode() {
        Object[] vals = new Object[chromosome.length];
                
        for(int i = 0; i < chromosome.length; i++)
            vals[i] = coder.decode(Util.ungray(chromosome[i]));
                                
        return vals;
    }
}
