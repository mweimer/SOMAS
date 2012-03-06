/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package representationUtil;

public class Util {
    public Util() {super();}
    static public boolean[] gray(boolean[] gene) {
        boolean[] normGene = new boolean[gene.length];
                
        normGene[0] = gene[0];

        for(int i = 1; i < gene.length; i++)
            normGene[i] = gene[i] ^ normGene[i-1];    
        
        return normGene;
    }

    static public boolean[] ungray(boolean[] gene) {
        boolean[] normGene = new boolean[gene.length];
                
        normGene[0] = gene[0];

        for(int i = gene.length - 1; i > 0; i--)
            normGene[i] = gene[i] ^ gene[i-1];    
        
        return normGene;
    }
}
