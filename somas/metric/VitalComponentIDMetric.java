/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package metric;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import representationUtil.Long_Coder;

import representations.GrayBitRep;
import representations.IntBitRep;
import sim.field.continuous.*;
import sim.field.network.*;
import sim.engine.*;
import sim.util.*;

import sim.portrayal.network.*;
import sim.portrayal.continuous.*;
import sim.engine.*;
import sim.display.*;
import javax.swing.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.Subgraph;
import org.jgrapht.alg.*;

import java.awt.Color;

import common.*;
import actuators.*;
import agent.*;
import environment.*;
import metric.*;
import rules.*;
import scenario.*;
import sensors.*;
import simulator.*;
import unknown.*;
import util.*;
import java.util.*;

public class VitalComponentIDMetric extends Metric {

	public void run() {
		Graph<Location, Thing> graph = (Graph<Location, Thing>) this.getBoxedProperty("graph");

		ArrayList<Location> unmarkedLocations = new ArrayList<Location>();
		for(Location location : graph.vertexSet()) 
			if(location.getBoxedProperty("isVitalNode") != null && 
					!(Boolean) location.getBoxedProperty("isVitalNode"))
				unmarkedLocations.add(location);

		Graph<Location, Thing> subGraph = new Subgraph<Location, Thing, Graph<Location, Thing>>(graph, new HashSet<Location>(unmarkedLocations));

		Double allPairsShortestPathSum = new Double(0.0);

		ArrayList<Location> vertexSet = new ArrayList<Location>(subGraph.vertexSet());

		for(Location start : vertexSet) {
			BellmanFordShortestPath<Location, Thing> shortestPathCalculator = new BellmanFordShortestPath<Location, Thing>(subGraph, start);

			for(Location end : vertexSet) 
				allPairsShortestPathSum += shortestPathCalculator.getCost(end);
		}

		this.setBoxedProperty("objectiveValue", allPairsShortestPathSum);
	}
}

