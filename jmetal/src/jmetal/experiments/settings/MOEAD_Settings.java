/**
 * MOEAD_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * MOEAD_Settings class of algorithm MOEAD and pMOEAD
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.moead.*;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class MOEAD_Settings extends Settings {
  // Default settings
  public double CR_ = 1.0 ;
  public double F_  = 0.5 ;
  public int populationSize_ = 600;
  public int maxEvaluations_ = 150000;
 
  public double mutationProbability_ = 1.0/problem_.getNumberOfVariables() ;
  public double distributionIndexForMutation_ = 20;
  
  // Directory with the files containing the weight vectors used in 
  // Q. Zhang,  W. Liu,  and H Li, The Performance of a New Version of MOEA/D 
  // on CEC09 Unconstrained MOP Test Instances Working Report CES-491, School 
  // of CS & EE, University of Essex, 02/2009.
  // http://dces.essex.ac.uk/staff/qzhang/MOEAcompetition/CEC09final/code/ZhangMOEADcode/moead0305.rar
  public String dataDirectory_ =  "/Users/antonio/Softw/pruebas/data/MOEAD_parameters/Weight" ;

  public int numberOfThreads = 2 ; // Parameter of pMOEAD
  public String moeadVersion = "pMOEAD" ; // or "pMOEAD"
  
  /**
   * Constructor
   */
  public MOEAD_Settings(Problem problem) {
    super(problem);
  } // MOEAD_Settings

  /**
   * Configure the algorithm with the specified parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm;
    Operator crossover;
    Operator mutation;

    QualityIndicator indicators ;

    // Creating the problem
    if (moeadVersion.compareTo("MOEAD") == 0 )
      algorithm = new MOEAD(problem_);
    else { // pMOEAD
      algorithm = new pMOEAD(problem_); 
      algorithm.setInputParameter("numberOfThreads", numberOfThreads);
    } // else
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize", populationSize_);
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
    algorithm.setInputParameter("dataDirectory", dataDirectory_) ;
    
    // Crossover operator 
    crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");
    crossover.setParameter("CR", CR_);
    crossover.setParameter("F", F_);   
    
    // Mutation operator
    mutation = MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setParameter("probability", mutationProbability_);
    mutation.setParameter("distributionIndex", distributionIndexForMutation_); 
    
    algorithm.addOperator("crossover", crossover);
    algorithm.addOperator("mutation", mutation);
    
    // Creating the indicator object
    if (!paretoFrontFile_.equals("")) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setInputParameter("indicators", indicators);
    } // if

    return algorithm;
  } // configure
} // MOEAD_Settings
