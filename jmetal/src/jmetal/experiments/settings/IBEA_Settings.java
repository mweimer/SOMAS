/**
 * IBEA_Settings.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 * IBEA_Settings class of algorithm IBEA
 */
package jmetal.experiments.settings;

import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.comparator.FitnessComparator;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.ibea.IBEA;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

public class IBEA_Settings extends Settings{

	// Default settings
	public int populationSize_ = 100   ;
	public int maxEvaluations_ = 25000 ;
	public int archiveSize_    = 100 ;

	public double mutationProbability_  = 1.0/problem_.getNumberOfVariables() ;
	public double crossoverProbability_ = 0.9 ;

	public double  distributionIndexForMutation_ = 20    ;
	public double  distributionIndexForCrossover_ = 20    ;

	/**
	 * Constructor
	 */
	public IBEA_Settings(Problem problem) {
		super(problem) ;
	} // IBEA_Settings

	/**
	 * Configure NSGAII with user-defined parameter settings
	 * @return A NSGAII algorithm object
	 * @throws jmetal.util.JMException
	 */
	public Algorithm configure() throws JMException {
		Algorithm algorithm ;
		Operator  selection ;
		Operator  crossover ;
		Operator  mutation  ;

		QualityIndicator indicators ;

		// Creating the problem
		algorithm = new IBEA(problem_) ;

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", populationSize_);
		algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
		algorithm.setInputParameter("archiveSize", archiveSize_);

		// Mutation and Crossover for Real codification 
		crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover");                   
		crossover.setParameter("probability", crossoverProbability_);                   
		crossover.setParameter("distributionIndex",distributionIndexForCrossover_);

		mutation = MutationFactory.getMutationOperator("PolynomialMutation");                    
		mutation.setParameter("probability", mutationProbability_);
		mutation.setParameter("distributionIndex",distributionIndexForMutation_);    

		// Selection Operator 
		selection = new BinaryTournament(new FitnessComparator());

		// Add the operators to the algorithm
		algorithm.addOperator("crossover",crossover);
		algorithm.addOperator("mutation",mutation);
		algorithm.addOperator("selection",selection);

		// Creating the indicator object
		if (! paretoFrontFile_.equals("")) {
			indicators = new QualityIndicator(problem_, paretoFrontFile_);
			algorithm.setInputParameter("indicators", indicators) ;  
		} // if
		return algorithm ;
	} // configure
} // IBEA_Settings
