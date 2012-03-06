/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package metric;

import java.util.HashSet;
import java.util.Set;


import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.alg.*;

import common.*;
import environment.*;
import java.util.*;

public class ShortestPathMetric extends Metric {
    public ShortestPathMetric() {super();}
    
    @Override
        public void run() {
        Graph<Location, Thing> graph = (Graph<Location, Thing>) this.getBoxedProperty("graph");

        Set<Location> vertexSet = new HashSet<Location>(graph.vertexSet());

        Set<Thing> remainingEdges = new HashSet<Thing>(graph.edgeSet());
                
        Set<Thing> removedEdges = new HashSet<Thing>();
                
        for(Location start : graph.vertexSet()) 
            if(start.getBoxedProperty("isVitalNode") != null && 
               (Boolean) start.getBoxedProperty("isVitalNode")) {
                List<Location> neighborList = (List<Location>) start.getBoxedProperty("locationList");

                for(Location end : neighborList)
                    removedEdges.add(graph.getEdge(start, end));
            }

        remainingEdges.removeAll(removedEdges);

        Graph<Location, Thing> subGraph = new DefaultDirectedGraph<Location, Thing>(Thing.class);
        for(Location location : graph.vertexSet())
            subGraph.addVertex(location);
        for(Thing edge : remainingEdges) {
            Location start = graph.getEdgeSource(edge);
            Location end = graph.getEdgeTarget(edge);
            subGraph.addEdge(start, end);
        }

        Double graphAllPairsShortestPathSum = new Double(0.0);
        Double subGraphAllPairsShortestPathSum = new Double(0.0);

        vertexSet = new HashSet<Location>(subGraph.vertexSet());

        for(Location start : vertexSet) {
            BellmanFordShortestPath<Location, Thing> subGraphShortestPathCalculator = new BellmanFordShortestPath<Location, Thing>(subGraph, start);
            BellmanFordShortestPath<Location, Thing> graphShortestPathCalculator = new BellmanFordShortestPath<Location, Thing>(graph, start);

            for(Location end : vertexSet)
                if(start != end) {
                    Double subGraphCost = subGraphShortestPathCalculator.getCost(end);
                    Double graphCost = graphShortestPathCalculator.getCost(end); // TODO : for some reason, this is sometimes infinite when the previous is finite

                    if(!subGraphCost.isNaN() && !subGraphCost.isInfinite() & !graphCost.isInfinite()) {
                        subGraphAllPairsShortestPathSum += subGraphCost; 

                        graphAllPairsShortestPathSum += graphCost;

                    }

                }
        }

        Double allPairsShortestPathDifference = 1.0;
                
        if(subGraphAllPairsShortestPathSum != 0)
            allPairsShortestPathDifference = graphAllPairsShortestPathSum / subGraphAllPairsShortestPathSum;
                
        this.setBoxedProperty("objectiveValue", allPairsShortestPathDifference);
    }
}

