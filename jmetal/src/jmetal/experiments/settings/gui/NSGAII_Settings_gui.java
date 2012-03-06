/**
 * NSGAII_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * NSGAII_Settings_gui class of algorithm NSGAII
 */
package jmetal.experiments.settings.gui;

import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

/**
 * @author Antonio J. Nebro
 */
public class NSGAII_Settings_gui extends Settings_gui {
  public int populationSize_                   ;
  public int maxEvaluations_                   ;
  public double mutationProbability_           ;
  public double crossoverProbability_          ;
  public Mutation   mutation_                  ;
  public Crossover  crossover_                 ;
  public double mutationDistributionIndex_     ;
  public double crossoverDistributionIndex_    ;

  /**
   * Constructor
   * @throws JMException
   */
  public NSGAII_Settings_gui() throws JMException {    
    populationSize_ = 100   ;
    maxEvaluations_ = 25000 ;
    mutationProbability_  = 0.0; // This value will be ignored    
    crossoverProbability_ = 0.9 ;    
    paretoFrontFile_ = "" ;
    mutation_ = MutationFactory.getMutationOperator("PolynomialMutation");
    crossover_ = CrossoverFactory.getCrossoverOperator("SBXCrossover");
  } // NSGAII_Settings


  public Algorithm configure() throws JMException {
      Algorithm algorithm ;
    Operator  selection;

    QualityIndicator indicators ;

    // Creating the algorithm. There are two choices: NSGAII and its steady-
    // state variant ssNSGAII
    algorithm = new NSGAII(problem_) ;
    //algorithm = new ssNSGAII(problem_) ;

    // Algorithm parameters
    algorithm.setInputParameter("populationSize",populationSize_);
    algorithm.setInputParameter("maxEvaluations",maxEvaluations_);

    // Mutation and Crossover for Real codification






    // Selection Operator
    selection = SelectionFactory.getSelectionOperator("BinaryTournament2") ;

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover_);
    algorithm.addOperator("mutation",mutation_);
    algorithm.addOperator("selection",selection);

   // Creating the indicator object
   if ((paretoFrontFile_!=null) && (!paretoFrontFile_.equals(""))) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setInputParameter("indicators", indicators) ;
   } // if

    return algorithm ;
  } // configure

  

 
} // NSGAII_Settings_gui
