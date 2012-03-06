/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package util;

import cern.jet.random.Normal;
import cern.jet.random.engine.RandomEngine;
import common.*;
import java.util.*;

public class NormalSampler extends Sampler {
    public NormalSampler() {super();}
    public NormalSampler(Thing t) {
        super(t);
    }
    public void run() {
    	Double mean = (Double) this.getBoxedProperty("mean");
    	Double variance = (Double) this.getBoxedProperty("variance");
    	Normal pdf = new Normal(mean, variance, RandomEngine.makeDefault());
    	this.setNewBoxedProperty("result", pdf.nextDouble());
    }
}