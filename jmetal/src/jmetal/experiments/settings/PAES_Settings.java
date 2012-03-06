/**
 * PAES_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * PAES_Settings class of algorithm PAES
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.paes.*;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.Problem;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.experiments.Settings;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

public class PAES_Settings extends Settings{

	// Default settings
	public int maxEvaluations_ = 25000 ;
	public int archiveSize_    = 100   ;
	public int biSections_     = 5     ;
	public double mutationProbability_ = 1.0/problem_.getNumberOfVariables() ;
	public double distributionIndex_   = 20.0 ;

	/**
	 * Constructor
	 */
	public PAES_Settings(Problem problem) {
		super(problem) ;
	} // PAES_Settings

	/**
	 * Configure the MOCell algorithm with default parameter settings
	 * @return an algorithm object
	 * @throws jmetal.util.JMException
	 */
	public Algorithm configure() throws JMException {
		Algorithm algorithm ;
		Mutation  mutation   ;

		QualityIndicator indicators ;

		// Creating the problem
		algorithm = new PAES(problem_) ;

		// Algorithm parameters
		algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
		algorithm.setInputParameter("biSections", biSections_);
		algorithm.setInputParameter("archiveSize",archiveSize_ );

		// Add the operators to the algorithm
    mutation = MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setParameter("probability", mutationProbability_);
    mutation.setParameter("distributionIndex", distributionIndex_);

		algorithm.addOperator("mutation",mutation);

		// Creating the indicator object
		if (! paretoFrontFile_.equals("")) {
			indicators = new QualityIndicator(problem_, paretoFrontFile_);
			algorithm.setInputParameter("indicators", indicators) ;  
		} // if
		return algorithm ;
	} // configure
} // PAES_Settings
