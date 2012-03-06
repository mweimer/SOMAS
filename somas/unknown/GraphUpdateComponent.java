/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package unknown;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

public class GraphUpdateComponent extends Machine {
	public GraphUpdateComponent() {super();}
	public GraphUpdateComponent(Thing t) {super(t);}
	public void run() {
		Graph<Location, Edge> originalGraph = (Graph<Location, Edge>) this.getBoxedProperty("originalGraph");

		Graph<Location, Edge> currentGraph = new DefaultDirectedGraph<Location, Edge>(Edge.class);

		for(Location vertex : originalGraph.vertexSet())
			if((!vertex.hasBoxedProperty("isVitalNode") || (vertex.hasBoxedProperty("isVitalNode") && !(Boolean) vertex.getBoxedProperty("isVitalNode"))) ||
					!(!this.hasBoxedProperty("vitalNotEqualsDown") || !(Boolean)this.getBoxedProperty("vitalNotEqualsDown")))
				currentGraph.addVertex(vertex);
		for(Edge edge : originalGraph.edgeSet())
			if((!edge.hasBoxedProperty("isVitalNode") || (edge.hasBoxedProperty("isVitalNode") && !(Boolean) edge.getBoxedProperty("isVitalNode")) ||
					!(!this.hasBoxedProperty("vitalNotEqualsDown") || !(Boolean)this.getBoxedProperty("vitalNotEqualsDown")))) {
				Location start = originalGraph.getEdgeSource(edge);
				Location end = originalGraph.getEdgeTarget(edge);
				if(((!start.hasBoxedProperty("isVitalNode") || (start.hasBoxedProperty("isVitalNode") && !(Boolean) start.getBoxedProperty("isVitalNode"))) &&
						(!end.hasBoxedProperty("isVitalNode") || (end.hasBoxedProperty("isVitalNode") && !(Boolean) end.getBoxedProperty("isVitalNode")))) ||
						!(!this.hasBoxedProperty("vitalNotEqualsDown") || !(Boolean)this.getBoxedProperty("vitalNotEqualsDown")))
					currentGraph.addEdge(start, end, edge);
			}


		// Reset neighbor lists
		for(Location location : originalGraph.vertexSet()) {
			// If node has been DDoS'd, then take it down
			ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");
			if(agentList.size() > 50) {
				location.setBoxedProperty("isVitalNode", new Boolean(true));
			}            
			if(!location.hasBoxedProperty("isVitalNode") || (location.hasBoxedProperty("isVitalNode") && !(Boolean) location.getBoxedProperty("isVitalNode")))
				location.setBoxedProperty("locationList", SOMAUtil.getEdgeNeighbors(currentGraph, location));
			else if (!this.hasBoxedProperty("vitalNotEqualsDown") || !(Boolean)this.getBoxedProperty("vitalNotEqualsDown")){ // If node is down, deschedule all the agents
				for(Agent agent : agentList) {
					((Machine)
							((Thing) this.getBoxedProperty("descheduler"))
							.setProperty("machine",
									agent))
									.execute();
				}

				location.setBoxedProperty("compromised", new Boolean(false)); // this way the nodes can be reset
				location.removeBoxedProperty("insiderPresent"); // Removes insider threat from node

				agentList.clear();
			}
		}                

		this.setBoxedProperty("result", currentGraph);
	}
}

