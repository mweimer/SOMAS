/**
 * SMPSO_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * SMPSO_Settings class of algorithm SMPSO
 */
package jmetal.experiments.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import jmetal.metaheuristics.smpso.*;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.experiments.Settings;
import jmetal.gui.utils.PropUtils;
import jmetal.problems.ZDT.ZDT1;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

/**
 *
 * @author Antonio
 */
public class SMPSO_Settings extends Settings{
  
  // Default settings
  public int    swarmSize_                 = 100 ;
  public int    maxIterations_             = 250 ;
  public int    archiveSize_               = 100 ;
  public double mutationDistributionIndex_ = 20.0 ;
  public double mutationProbability_       = 1.0/problem_.getNumberOfVariables() ;

  /**
   * Constructor
   */
  public SMPSO_Settings(Problem problem) {
    super(problem) ;
  } // SMPSO_Settings
  
  /**
   * Configure NSGAII with user-defined parameter settings
   * @return A NSGAII algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm ;
    Mutation  mutation ;
    
    QualityIndicator indicators ;
    
    // Creating the problem
    algorithm = new SMPSO(problem_) ;
    
    // Algorithm parameters
    algorithm.setInputParameter("swarmSize", swarmSize_);
    algorithm.setInputParameter("maxIterations", maxIterations_);
    algorithm.setInputParameter("archiveSize", archiveSize_);
    
		mutation = MutationFactory.getMutationOperator("PolynomialMutation");                    
		mutation.setParameter("probability", mutationProbability_);
		mutation.setParameter("distributionIndex", mutationDistributionIndex_);

    algorithm.addOperator("mutation",mutation);

		// Creating the indicator object
		if (! paretoFrontFile_.equals("")) {
			indicators = new QualityIndicator(problem_, paretoFrontFile_);
			algorithm.setInputParameter("indicators", indicators) ;  
		} // if
		
		return algorithm ;
  } // Configure
} // SMPSO_Settings
