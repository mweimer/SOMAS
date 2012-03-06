/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package metric;

import java.util.HashSet;
import java.util.Set;


import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import common.*;
import environment.*;
import java.util.*;

public class DisconnectMetric extends Metric {
    public DisconnectMetric() {super();}
    
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
                
        Double graphDisconnects = new Double(0);
        Double subGraphDisconnects = new Double(0);
                
        for(Location start : vertexSet)
            for(Location end : vertexSet)
                if(start != end) { 
                    if(graph.getAllEdges(start, end).size() == 0)
                        graphDisconnects++;
                                        
                    if(subGraph.getAllEdges(start, end).size() == 0) 
                        subGraphDisconnects++;
                }
                
        Double disconnectsDifference = 1.0; 
                
        if(subGraphDisconnects != 0)
            disconnectsDifference = graphDisconnects/subGraphDisconnects;
                
        this.setBoxedProperty("objectiveValue", disconnectsDifference);
    }
}

