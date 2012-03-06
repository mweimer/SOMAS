package jmetal.somas;

import org.jppf.server.protocol.JPPFTask;
import evaluators.Evaluator;

public class EvalSolutionTask extends JPPFTask
{
	private String scenarioName = null; 
	private int[] decisionSpace = null;
	private int numRuns;
	
	public EvalSolutionTask(String scenarioName, int[] decisionSpace, int numRuns) {
		this.scenarioName = scenarioName;
		this.decisionSpace = decisionSpace; 
		this.numRuns = numRuns;
	}
	
	@Override
	public void run() {
		setResult(Evaluator.eval(scenarioName, decisionSpace, numRuns));
		decisionSpace = null;
		scenarioName = null;
	}
}
