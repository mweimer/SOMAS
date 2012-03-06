package jmetal.somas;

import java.util.List;

import org.jppf.client.JPPFJob;
import org.jppf.server.protocol.JPPFTask;

import scenario.Scenario;

import evaluators.Evaluator;
import jmetal.base.*;
import jmetal.base.solutionType.*;
import jmetal.base.variable.*;
import jmetal.util.JMException;

public class SOMASProblem extends Problem
{
	public static String scenarioName = null;
	public static int numRuns;
	
	private static final int decisionSpaceSize = 5000;
	
	public SOMASProblem() throws ClassNotFoundException 
	{	
		problemName_         = "SOMASProblem";
		numberOfVariables_ = 1;
	    numberOfObjectives_  = Scenario.getNumObjectives(scenarioName);
	    numberOfConstraints_ = 0 ;
	   
	    length_       = new int[numberOfVariables_];   
	    for(int i = 0; i < numberOfVariables_; i++)
	    	length_[i] = decisionSpaceSize;
	    
	    solutionType_ = new BinarySolutionType(this);      
	}

	@Override
	public void evaluate(Solution solution) throws JMException 
	{	
		int[] decisionSpace = convertToIntArray(solution.getDecisionVariables());
		double[] objectiveValues = Evaluator.eval(scenarioName, decisionSpace, numRuns);
		for(int i = 0; i < objectiveValues.length; i++)
			solution.setObjective(i, objectiveValues[i]);	
	}
	
	public void parallelEvaluate(List<Solution> solutions)
	{
		if(JPPFManager.jppfClient == null)
			JPPFManager.initClient();
		
		try
		{
			JPPFJob job = new JPPFJob();
	        for(int i = 0; i < solutions.size(); i++)
	        {
	        	int[] decisionSpace = convertToIntArray(solutions.get(i).getDecisionVariables());
	        	job.addTask(new EvalSolutionTask(scenarioName, decisionSpace, numRuns));
	        }
	              
	        List<JPPFTask> taskResults = JPPFManager.jppfClient.submit(job);
	        
	        if(taskResults != null) 
	        {
	            for(int i = 0; i < solutions.size(); i++) 
	            {
		            	JPPFTask task = taskResults.get(i);
		            	Solution solution = solutions.get(i);
		                if(task.getResult() != null)
		                {
		                	double[] objectiveValues = (double[]) task.getResult();
		                	
		                	// if results come back from node with all 0, something screwed up
		                	// do evaluation locally instead
		                	// TODO find source of why its occasionally all 0
		                	double sum = 0;
		        			for(double value : objectiveValues)
		        				sum += value;
		        			if(sum == 0) 
		        			{
		        				int[] decisionSpace = convertToIntArray(solution.getDecisionVariables());
		        				objectiveValues = Evaluator.eval(scenarioName, decisionSpace, numRuns);
		        			}
		        			
		                	for(int j = 0; j < objectiveValues.length; j++)
		                		solution.setObjective(j, objectiveValues[j]);
		                }
		                if(task.getException() != null) 
		                	task.getException().printStackTrace();
    	        }                   
            }  
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private int[] convertToIntArray(Variable[] decisionVariables)
	{
		int[] decisionSpace = new int[decisionSpaceSize]; 
		Binary dv = (Binary) decisionVariables[0];
		for(int i= 0; i < decisionVariables.length; i++)
		{
			if(dv.getIth(i))
				decisionSpace[i] = 1;		
			else
				decisionSpace[i] = 0;			
		}
		
		return decisionSpace;
	}

}
