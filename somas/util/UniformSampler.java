/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package util;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import common.*;
import java.util.*;

public class UniformSampler extends Sampler {
    public UniformSampler() {super();}
    public UniformSampler(Thing t) {
        super(t);
    }
    public void run() {
    	Double start = (Double) this.getBoxedProperty("start");
    	Double end= (Double) this.getBoxedProperty("end");
    	Uniform pdf = new Uniform(start, end, RandomEngine.makeDefault());
    	this.setNewBoxedProperty("result", pdf.nextDouble());
    }
}

