package classification;

import java.util.*;

import agent.Agent;

import common.Machine;
import environment.Location;
import environment.Network;

public class ClassificationMetrics extends Machine {
	
	// track which phase of the simulation we are in, to know when to record observations
	public static final int BEGINNING = 1;
	public static final int MIDDLE = 2;
	public static final int END = 3;
	
//	public int phase = 0;
	
	public boolean phasestart = true;

	public Network network;
	
	public HashMap<Integer, NodeCounter> NodeCounters = new HashMap<Integer, NodeCounter>();
	
	public int t = 1;
	
	public ClassificationMetrics(Network network) {
		this.network = network;
	}
	
	@Override
	public void run() {
		for(Location location : network.getGlobalGraph().vertexSet()) {
			if(location.hasProperty("observerID")) {
				ArrayList<Agent> agentList = null;
				if(location.hasBoxedProperty("agentList")) {
					agentList = (ArrayList<Agent>)location.getBoxedProperty("agentList");
				}
				Integer id = (Integer)location.getProperty("observerID");
				NodeCounter nodectr = null;
				if(NodeCounters.containsKey(id))
					 nodectr = NodeCounters.get(id);
				else
					nodectr = new NodeCounter();						
				
				if((Boolean) location.getBoxedProperty("isVitalNode") == false)
					nodectr.uptime++;
				
				
				
				for(int i = 0; i < NodeCounter.NUM_COUNTS; i++) {
					nodectr.counts[i][t-1] = 0;
				}

				if(agentList != null) {
					for(Agent agent : agentList)
					{
						String type = agent.hasBoxedProperty("typeList") ? ((ArrayList<String>) agent.getBoxedProperty("typeList")).get(0): "default";

						if(type == "compromise")
							nodectr.counts[NodeCounter.C_AGENT][t-1]++;
						else if(type =="ddosattack")
							nodectr.counts[NodeCounter.D_ATTACK][t-1]++;
						else if (type =="ddos")
							nodectr.counts[NodeCounter.D_AGENT][t-1]++;
						else if (type == "ddoscomm")
							nodectr.counts[NodeCounter.D_COMM][t-1]++;
					}					
				}
/*
				for(int i = 0; i < NodeCounter.NUM_COUNTS; i++) {
					nodectr.counts_avg[i] = ((double) nodectr.n * nodectr.counts_avg[i] + (double) nodectr.counts[i]) / (double)(nodectr.n + 1);
				}
				nodectr.n++;
*/
/*
				if(t > (int) nodectr.t[nodectr.next_t]) { // start recording block
					if(nodectr.next_t == 0 || nodectr.next_t == 2)
						nodectr.updateTimeHacks();
					nodectr.next_t++;
				}
				else if(t == (int) nodectr.t[nodectr.next_t]) { // end recording block
					if(nodectr.next_t == 1 || nodectr.next_t == 3)
						nodectr.updateTimeHacks();
					nodectr.updateStats();
					//nodectr.n = 0;
					//for(int i = 0; i < NodeCounter.NUM_COUNTS; i++) {
					//	nodectr.counts_avg[i] = 0.0;
					//}
				}
*/				
				NodeCounters.put(id, nodectr);
			}			
		}
		t++;
	}

}
