package evaluators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.bind.v2.TODO;

import agentGenerator.SOMASAgentGenerator;

import scenario.*;

public class Evaluator {
	
    public static double[] eval(String scenarioName, int[] decisionSpace, int numRuns){
    	
    	SOMASAgentGenerator.NV = false;
		SOMASAgentGenerator.NE = false;
		SOMASAgentGenerator.NCDA = true;
		
    	int numObjectives = Scenario.getNumObjectives(scenarioName);
    	double[] objectiveValues = new double[numObjectives];;

		objectiveValues = new double[numObjectives];
		for(int i = 0; i < numObjectives; i++)
			objectiveValues[i] = 0.0;

		Integer[] boxedDecisionSpace = new Integer[decisionSpace.length];
		for(int i = 0; i < decisionSpace.length; i++)
			boxedDecisionSpace[i] = new Integer(decisionSpace[i]);
				
		ArrayList<Double[]> resultList = new ArrayList<Double[]>();
		/*
	  	===============
	  	Run simulations 
	  	===============
	  	*/

		// Serial simulations		
		while(resultList.size() < numRuns) { 
			Scenario scenario = Scenario.getNewScenario(scenarioName);
			scenario.addDecisionSpace(boxedDecisionSpace);
			scenario.run();
			Double[] resultValues = scenario.getObjectiveValues();
			
			// validate resulting objective values
			// only add to result list if all the values are not 0
			// TODO find source of why its occasionally all 0
			double sum = 0;
			for(double value : resultValues)
				sum += value;
			if(sum != 0)
				resultList.add(resultValues);
		}
		
		/*
  	  	=============================
  	  	Extract results and take mean
  	  	=============================
		*/
		for(Double[] result : resultList) {
			for(int i = 0; i < numObjectives; i++)
				objectiveValues[i] += result[i];
		}            

		for(int i = 0; i < numObjectives; i++)
			objectiveValues[i] /= new Double(resultList.size());
			
		/*
  	  	==============
  	  	Return results
  	  	==============
		*/
		return objectiveValues;
    	
    }
}
