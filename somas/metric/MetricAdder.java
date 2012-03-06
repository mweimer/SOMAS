/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package metric;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class MetricAdder extends Metric {
    public MetricAdder() {super();}
    public MetricAdder(Thing t) {super(t);}
    public void run() {
        ArrayList<Thing> metricContainerList = (ArrayList<Thing>) this.getBoxedProperty("metricContainerList");

        Double result = new Double(0);
        for(Thing metricContainer : metricContainerList)
            result += (Double) metricContainer.getProperty("object");

        this.setNewBoxedProperty("result", result); // HACK
    }
}

