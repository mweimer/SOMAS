/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package unknown;

import representations.IntBitRep;
import representations.Representation;
import actuators.*;
import common.*;

public class IntBitMutationOperator extends MutationOperator {
    public IntBitMutationOperator() {super();}
    public IntBitMutationOperator(Thing t) {
        super(t);
    }
    @Override
	public void run() {
        Chromosome chromosome = (Chromosome)
            this
            .getBoxedProperty("chromosome"); 
        
        Representation representation = new IntBitRep((new IntBitRep((Representation) chromosome.getBoxedProperty("representation"))).mutate());

        chromosome.setBoxedProperty("representation", representation);
    }
}


