/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package unknown;

import representationUtil.*;
import representations.GrayBitRep;
import representations.IntBitRep;
import common.*;

public class IntBitToDelimitedDoubleChromosomeDecoder extends Decoder {
    public IntBitToDelimitedDoubleChromosomeDecoder() {super();}
    public IntBitToDelimitedDoubleChromosomeDecoder(Thing t) {
        super(t);
    }
    @Override
	public void run() {
        Object[] byteGenes = (new GrayBitRep((IntBitRep) ((Chromosome) this.getBoxedProperty("chromosome")).getBoxedProperty("representation"), new Byte_Coder())).decode(); // Can change this to lengthen chromosome

        Double[] delimitedGenes = new Double[byteGenes.length];
        int numGenes = delimitedGenes.length;
        for(int i = 0; i < numGenes; i++) 
            delimitedGenes[i] = new Double(new Double((Byte) byteGenes[i])/new Double(Byte.MAX_VALUE));

        this.setBoxedProperty("result", delimitedGenes);
    }
}


