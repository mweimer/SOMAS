/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package util;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.AsUndirectedGraph;

import common.*;
import environment.*;
import java.util.*;

public class SOMAUtil {
    public static List<Location> getNeighbors(Graph<Location, Thing> graph, Location location) {
        List<Location> neighbors = new ArrayList<Location>();

        for(Thing edge : graph.edgesOf(location)) {
            List<Location> edgeEnds = new ArrayList<Location>();
            edgeEnds.add(graph.getEdgeTarget(edge));
            edgeEnds.add(graph.getEdgeSource(edge));
            edgeEnds.remove(location);
            neighbors.addAll(edgeEnds);
        }
        
        return neighbors;
    }

    public static List<Location> getEdgeNeighbors(Graph<Location, Edge> graph, Location location) {
        List<Location> neighbors = new ArrayList<Location>();

        for(Edge edge : graph.edgesOf(location)) {
            List<Location> edgeEnds = new ArrayList<Location>();
            edgeEnds.add(graph.getEdgeTarget(edge));
            edgeEnds.add(graph.getEdgeSource(edge));
            edgeEnds.remove(location);
            neighbors.addAll(edgeEnds);
        }

        return neighbors;
    }

    static public void combineGraphs(Graph<Location, Edge> graph, Graph<Location, Edge> addition) {
        for(Location vertex : addition.vertexSet())
            graph.addVertex(vertex);
        for(Edge edge : addition.edgeSet()) {
            Location start = addition.getEdgeSource(edge);
            Location end = addition.getEdgeTarget(edge);
            graph.addEdge(start, end, edge);
        }
    }
    
    public static Integer doubleToInteger(Double num) {
        return Math.round(Math.round(num));
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }
    
    public static Location nextHop(Graph<Location, Edge> graph, Location location, Location destination) {
    	if(location == destination)
    		return location;
    	
    	if(!graph.containsVertex(destination) || !graph.containsVertex(location)|| BellmanFordShortestPath.findPathBetween(new AsUndirectedGraph<Location, Edge>((DirectedGraph<Location,Edge>)graph), location, destination) == null)
    		return location;
    	
        ArrayList<Edge> path = new ArrayList<Edge>(BellmanFordShortestPath.findPathBetween(new AsUndirectedGraph<Location, Edge>((DirectedGraph<Location,Edge>)graph), location, destination));
        
        if(path != null)
            return Graphs.getOppositeVertex(graph, path.get(0), location);
        return null;
    }
}

