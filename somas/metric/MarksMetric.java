/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package metric;

import org.jgrapht.Graph;
import common.*;
import environment.*;
import java.util.*;

public class MarksMetric extends Metric {
    public MarksMetric() {super();}
    
    @Override
	public void run() {
        Graph<Location, Thing> graph = (Graph<Location, Thing>) this.getBoxedProperty("graph");

        Double markSum = new Double(0);
                
        ArrayList<Location> unmarkedLocations = new ArrayList<Location>();
        for(Location location : graph.vertexSet()) 
            if(location.getBoxedProperty("isVitalNode") != null && 
               (Boolean) location.getBoxedProperty("isVitalNode"))
                markSum += 1;

        this.setBoxedProperty("objectiveValue", markSum);
    }

}
