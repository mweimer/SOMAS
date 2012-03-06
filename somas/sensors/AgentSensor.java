/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package sensors;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class AgentSensor extends Sensor {
	public AgentSensor() {super();}
	public AgentSensor(Thing t) {super(t);}
	public void run() {
		ArrayList<Agent> agentList = (ArrayList<Agent>) this.getBoxedProperty("agentList");
		ArrayList<String> filteredTypeList = this.hasBoxedProperty("filteredTypeList") ? (ArrayList<String>) this.getBoxedProperty("filteredTypeList"): null; // HACK
		String filterType = this.hasBoxedProperty("type") ? (String) this.getBoxedProperty("type") : null; // HACK names not congruent for simplicity of modification

		Double senseProbability = this.hasBoxedProperty("senseProbability") ? (Double) this.getBoxedProperty("senseProbability") : 1.0; // HACK 

		ArrayList<Agent> filteredAgentList = new ArrayList<Agent>();
		for(Agent agent : agentList) {
			if(Math.random() < senseProbability) {
				ArrayList<String> typeList = (ArrayList<String>) agent.getBoxedProperty("typeList"); 
				if(filteredTypeList != null) {
					for(String type : filteredTypeList)
						if(typeList.contains(type))
							filteredAgentList.add(agent);
				}
				else if(filterType != null)
					if(typeList.contains(filterType)) {
//						if((filteredTypeList != null && filteredTypeList.contains("somas")) ||
//								(filterType != null && filterType.equals("somas"))){ // DEBUG
//							System.out.println("somas sensor"); // DEBUG
//						} // DEBUG
						
						filteredAgentList.add(agent);
					}
			}
		}
		this.setBoxedProperty("result", filteredAgentList);
	}
}

