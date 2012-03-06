/**
 * SPEA2_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * NSGAII_Settings class of algorithm NSGAII
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.spea2.*;
import jmetal.base.*;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.mutation.Mutation;

import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class SPEA2_Settings extends Settings {
  
  public int populationSize_           = 100   ;
  public int archiveSize_              = 100   ;
  public int maxEvaluations_           = 25000 ;
  public double mutationProbability_   = 1.0/problem_.getNumberOfVariables() ;
  public double crossoverProbability_  = 0.9   ;
  public double distributionIndexForCrossover_ = 20.0 ;
  public double distributionIndexForMutation_  = 20.0 ;

  /**
   * Constructor
   */
  public SPEA2_Settings(Problem problem) {
    super(problem) ;
  } // SPEA2_Settings
  
  /**
   * Configure SPEA2 with default parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm ;
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator
    
    QualityIndicator indicators ;
    
    // Creating the problem
    algorithm = new SPEA2(problem_) ;
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize", populationSize_);
    algorithm.setInputParameter("archiveSize", archiveSize_);
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
      
    // Mutation and crossover for real codification
    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover");                   
    crossover.setParameter("probability", crossoverProbability_) ;                   
    crossover.setParameter("distributionIndex", distributionIndexForCrossover_);
    
    mutation = MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setParameter("probability", mutationProbability_);
    mutation.setParameter("distributionIndex", distributionIndexForMutation_);
        
    // Selection operator 
    selection = SelectionFactory.getSelectionOperator("BinaryTournament") ;                           
    
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
} // SPEA2_Settings
