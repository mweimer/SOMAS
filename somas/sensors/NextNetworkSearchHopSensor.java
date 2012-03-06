/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package sensors;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

import org.jgrapht.Graph;

public class NextNetworkSearchHopSensor extends Sensor {
	public NextNetworkSearchHopSensor() {
		super();
	}

	public NextNetworkSearchHopSensor(Thing t) {
		super(t);
	}

	public void run() {
		Location location = (Location) this.getBoxedProperty("location");
		ArrayList<Location> neighborList = new ArrayList<Location>(
				(ArrayList<Location>) location.getBoxedProperty("locationList"));
		neighborList.remove(location);

		Graph<Location, Edge> graph = (Graph<Location, Edge>) this
				.getBoxedProperty("graph");

		int locationID = new Double(Math.random() * (neighborList.size()))
				.intValue();
		Location nextHop = neighborList.size() > 0 ? neighborList.get(locationID) : location;

		this.setBoxedProperty("result", nextHop);
	}
}
