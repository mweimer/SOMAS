/**
 * NSGAIIBinary_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * Example of using a binary representation in NSGAII
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.nsgaII.*;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

/**
 *
 * @author Antonio J. Nebro
 */
public class NSGAIIBinary_Settings extends Settings {
  
  // Default settings
  int populationSize_ = 100   ;
  int maxEvaluations_ = 25000 ;

  double mutationProbability_  = 1.0/problem_.getNumberOfBits();
  double crossoverProbability_ = 0.9 ;  
  
  /**
   * Constructor
   */
  public NSGAIIBinary_Settings(Problem problem) {
    super(problem) ;
  } // NSGAIIBinary_Settings
  
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
    algorithm = new NSGAII(problem_) ;
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize", populationSize_);
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

    
    // Mutation and Crossover Binary codification
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");                   
    crossover.setParameter("probability",0.9);                   
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");     
    mutation.setParameter("probability", mutationProbability_) ;
    
    // Selection Operator 
    selection = SelectionFactory.getSelectionOperator("BinaryTournament2") ;   
    
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
} // NSGAIIBinary_Settings
