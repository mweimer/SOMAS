/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package sensors;    

import org.jgrapht.Graph;

import util.SOMAUtil;

import common.Thing;
import environment.*;

public class NextHopSensor extends Sensor {
    public NextHopSensor() {super();}
    public NextHopSensor(Thing t) {super(t);}
    public void run() {
	Location location = (Location) this.getBoxedProperty("location");
	Location destination = (Location) this.getBoxedProperty("destination");
	Graph<Location, Edge> graph = (Graph<Location, Edge>) this.getBoxedProperty("graph");

	this.setBoxedProperty("result", SOMAUtil.nextHop(graph, location, destination));
    }
}
