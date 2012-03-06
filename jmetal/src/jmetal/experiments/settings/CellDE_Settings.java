/**
 * CellDE_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * CellDE_Settings class of algorithm CellDE
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.cellde.*;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.gui.utils.PropUtils;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

/**
 *
 * @author Antonio
 */
public class CellDE_Settings extends Settings{
  
  // Default settings
  public double CR_          = 0.5;
  public double F_           = 0.5    ;
  
  public int populationSize_ = 100   ;
  public int archiveSize_    = 100   ;
  public int maxEvaluations_ = 25000 ;
  public int archiveFeedback_= 20    ;
 
  /**
   * Constructor
   */
  public CellDE_Settings(Problem problem) {
    super(problem) ;
  } // CellDE_Settings
  
  /**
   * Configure the algorithm with the specified parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm ;
    Operator  selection ;
    Operator  crossover ;
    Operator  mutation  ;
    
    QualityIndicator indicators ;
    
    // Creating the problem
    algorithm = new CellDE(problem_) ;
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize", populationSize_);
    algorithm.setInputParameter("archiveSize", archiveSize_);
    algorithm.setInputParameter("maxEvaluations",maxEvaluations_);
    algorithm.setInputParameter("feedBack", archiveFeedback_);
    
    // Crossover operator 
    crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");                   
    crossover.setParameter("CR", CR_);                   
    crossover.setParameter("F", F_);
    
    // Add the operators to the algorithm
    selection = SelectionFactory.getSelectionOperator("BinaryTournament") ; 

    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("selection",selection);
   
   // Creating the indicator object
   if (! paretoFrontFile_.equals("")) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setInputParameter("indicators", indicators) ;  
   } // if
    
    return algorithm ;
  } // configure
} // CellDE_Settings
