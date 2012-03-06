/**
 * MOCell_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * MOCell_Settings class of algorithm MOCell
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.mocell.*;
import java.util.Properties;
import jmetal.base.*;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.gui.utils.PropUtils;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;


public class MOCell_Settings extends Settings{

	// Default settings
	public int populationSize_                    = 100   ;
	public int maxEvaluations_                    = 25000 ;
	public int archiveSize_                       = 100   ;
	public int feedback_                          = 20    ;
	public double mutationProbability_            = 1.0/problem_.getNumberOfVariables();
	public double crossoverProbability_           = 0.9   ;
	public double distributionIndexForMutation_   = 20.0  ;
	public double distributionIndexForCrossover_  = 20.0  ;

	/**
	 * Constructor
	 */
	public MOCell_Settings(Problem problem) {
		super(problem) ;
	} // MOCell_Settings

	/**
	 * Configure the MOCell algorithm with default parameter settings
	 * @return an algorithm object
	 * @throws jmetal.util.JMException
	 */
	public Algorithm configure() throws JMException {
		Algorithm algorithm ;
		
		Crossover crossover ;
		Mutation  mutation  ;
		Operator  selection ;

		QualityIndicator indicators ;

		// Selecting the algorithm: there are six MOCell variants
		//algorithm = new sMOCell1(problem_) ;
		//algorithm = new sMOCell2(problem_) ;
		//algorithm = new aMOCell1(problem_) ;
		//algorithm = new aMOCell2(problem_) ;
		//algorithm = new aMOCell3(problem_) ;
		algorithm = new MOCell(problem_) ;

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", populationSize_);
		algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
		algorithm.setInputParameter("archiveSize",archiveSize_ );
		algorithm.setInputParameter("feedBack",feedback_);


		// Mutation and Crossover for Real codification 
		crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover");                   
		crossover.setParameter("probability", crossoverProbability_);                   
		crossover.setParameter("distributionIndex", distributionIndexForCrossover_);

		mutation = MutationFactory.getMutationOperator("PolynomialMutation");                    
		mutation.setParameter("probability",mutationProbability_);
		mutation.setParameter("distributionIndex", distributionIndexForMutation_);

		// Selection Operator 
		selection = SelectionFactory.getSelectionOperator("BinaryTournament") ;  

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Creating the indicator object
		if (! paretoFrontFile_.equals("")) {
			indicators = new QualityIndicator(problem_, paretoFrontFile_);
			algorithm.setInputParameter("indicators", indicators) ;  
		} // if

		return algorithm ;
	} // configure
} // MOCell_Settings
