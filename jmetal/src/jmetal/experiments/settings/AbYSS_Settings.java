/**
 * AbYSS_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * MOCell_Settings class of algorithm AbYSS
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.abyss.*;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.localSearch.MutationLocalSearch;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.experiments.Settings;


import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

/**
 * Constructor
 */
public class AbYSS_Settings extends Settings {
  // Default settings
  public int populationSize_ = 20;
  public int maxEvaluations_ = 25000;
  public int archiveSize_ = 100;
  public int refSet1Size_ = 10;
  public int refSet2Size_ = 10;
  public double mutationProbability_ = 1.0 / problem_.getNumberOfVariables();
  public double crossoverProbability_ = 1.0;
  public double distributionIndexForMutation_ = 20;
  public double distributionIndexForCrossover_ = 20;
  public int improvementRounds_ = 1;
  
  /**
   * Constructor
   * @param problem Problem to solve
   */
  public AbYSS_Settings(Problem problem) {
    super(problem);
  } // AbYSS_Settings

  /**
   * Configure the MOCell algorithm with default parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm;
    Operator crossover;
    Operator mutation;
    Operator improvement; // Operator for improvement

    QualityIndicator indicators;

    // Creating the problem
    algorithm = new AbYSS(problem_);

    // Algorithm parameters
    algorithm.setInputParameter("populationSize", populationSize_);
    algorithm.setInputParameter("refSet1Size", refSet1Size_);
    algorithm.setInputParameter("refSet2Size", refSet2Size_);
    algorithm.setInputParameter("archiveSize", archiveSize_);
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

    // Mutation and Crossover for Real codification 
    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover");
    crossover.setParameter("probability", crossoverProbability_);
    crossover.setParameter("distributionIndex", distributionIndexForCrossover_);

    mutation = MutationFactory.getMutationOperator("PolynomialMutation");
    mutation.setParameter("probability", mutationProbability_);
    mutation.setParameter("distributionIndex", distributionIndexForMutation_);

    improvement = new MutationLocalSearch(problem_, mutation);
    improvement.setParameter("improvementRounds", improvementRounds_);

    // STEP 6. Add the operators to the algorithm
    algorithm.addOperator("crossover", crossover);
    algorithm.addOperator("improvement", improvement);

    // Creating the indicator object
    if (!paretoFrontFile_.equals("")) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setInputParameter("indicators", indicators);
    } // if
    return algorithm;
  } // Constructor
} // AbYSS_Settings
