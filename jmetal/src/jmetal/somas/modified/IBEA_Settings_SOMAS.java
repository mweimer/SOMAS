package jmetal.somas.modified;

import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.comparator.FitnessComparator;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.experiments.Settings;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.somas.SOMASProblem;
import jmetal.util.JMException;

public class IBEA_Settings_SOMAS extends Settings
{
	// set values for these variables in the experiment file
	public int populationSize_;
	public int maxEvaluations_;
	public int archiveSize_;
	public boolean useJPPF_;

	public double mutationProbability_;
	public double bitFlipProbability_;
	public double crossoverProbability_;
	public double bitSwapProbability_;

	/**
	 * Constructor
	 */
	public IBEA_Settings_SOMAS(SOMASProblem problem) {
		super(problem) ;
	}

	/**
	 * Configure with user-defined parameter settings
	 * @return AIBEA algorithm object
	 * @throws jmetal.util.JMException
	 */
	public Algorithm configure() throws JMException {
		Algorithm algorithm ;
		Operator  selection ;
		Operator  crossover ;
		Operator  mutation  ;

		QualityIndicator indicators ;

		// Creating the problem
		algorithm = new IBEA_SOMAS((SOMASProblem) problem_) ;

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", populationSize_);
		algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
		algorithm.setInputParameter("archiveSize", archiveSize_);
		algorithm.setInputParameter("useJPPF", useJPPF_);

	    // Crossover operator
	    crossover = new UXCrossover();
	    crossover.setParameter("probability",crossoverProbability_);
	    crossover.setParameter("bitSwapProbability",bitSwapProbability_);
	    
	    //Mutation operator
	    mutation = new BitFlipMutation_SOMAS();
	    mutation.setParameter("probability",mutationProbability_);  
	    mutation.setParameter("bitFlipProbability",bitFlipProbability_);

		// Selection Operator 
		selection = new BinaryTournament(new FitnessComparator());

		// Add the operators to the algorithm
		algorithm.addOperator("crossover",crossover);
		algorithm.addOperator("mutation",mutation);
		algorithm.addOperator("selection",selection);

		// Creating the indicator object
		if (paretoFrontFile_ != null && !paretoFrontFile_.equals("")) {
			indicators = new QualityIndicator(problem_, paretoFrontFile_);
			algorithm.setInputParameter("indicators", indicators) ;  
		}
		return algorithm ;
	} 
} 

