/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package scenario;

import java.util.HashMap;
import java.util.Map;

import agentGenerator.SOMASAgentGenerator;

public class ScenarioVisualizer {

	public static void main(String args[]) {
		Scenario scenario = Scenario.getNewScenario(args[0]);

		if(args[2].equals("NE")){
			SOMASAgentGenerator.NE = true;
		} else {
			SOMASAgentGenerator.NE = false;
		}

		if(args[2].equals("NS")){
			SOMASAgentGenerator.NS = true;
		} else {
			SOMASAgentGenerator.NS = false;
		}
		
		if(args[1].equals("NV")){
			SOMASAgentGenerator.NV = true;
		} else {
			SOMASAgentGenerator.NV = false;
		}			

		scenario.visualize(args[3], true);
	}
}
