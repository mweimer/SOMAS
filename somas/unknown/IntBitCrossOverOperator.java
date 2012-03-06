/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package unknown;

import representations.IntBitRep;
import representations.Representation;
import common.*;

public class IntBitCrossOverOperator extends Operator {
    public IntBitCrossOverOperator() {super();}
    public IntBitCrossOverOperator(Thing t) {
        super(t);
    }
    @Override
	public void run() {
        Representation rep1 = (Representation) ((Chromosome) this.getBoxedProperty("chromosome1")).getBoxedProperty("representation");
        Representation rep2 = (Representation) ((Chromosome) this.getBoxedProperty("chromosome2")).getBoxedProperty("representation");
        Representation rep3 = new IntBitRep(rep1.crossover(rep2));
        Chromosome result = new Chromosome();
        result.setNewBoxedProperty("representation", rep3)
            .setNewBoxedProperty("pheromone", new Double(0));
        
        this
            .setNewBoxedProperty
            ("result",
             result);
    }
}


