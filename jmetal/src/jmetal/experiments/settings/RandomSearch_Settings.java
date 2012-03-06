/**
 * RandomSearch_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * Settings class of algorithm RandomSearch
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.randomSearch.RandomSearch;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.experiments.Settings;


import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

/**
 * Constructor
 */
public class RandomSearch_Settings extends Settings {
  // Default settings
  public int maxEvaluations_ = 25000;
  
  /**
   * Constructor
   * @param problem Problem to solve
   */
  public RandomSearch_Settings(Problem problem) {
    super(problem);
  } // AbYSS_Settings

  /**
   * Configure the MOCell algorithm with default parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm;

    QualityIndicator indicators;

    // Creating the problem
    algorithm = new RandomSearch(problem_);

    // Algorithm parameters
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

    // Creating the indicator object
    if (!paretoFrontFile_.equals("")) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setInputParameter("indicators", indicators);
    } // if
    return algorithm;
  } // Constructor
} // RandomSearch_Settings
