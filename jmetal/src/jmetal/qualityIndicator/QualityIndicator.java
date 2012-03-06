/**
 * QualityIndicator.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * This class provides methods for calculating the values of quality indicators
 * from a solution set. After creating an instance of this class, which requires
 * the file containing the true Pareto of the problem as a parementer, methods
 * such as getHypervolume(), getSpread(), etc. are available
 */

package jmetal.qualityIndicator;

import jmetal.base.Problem;
import jmetal.base.SolutionSet;

/**
 * QualityIndicator class
 */
public class QualityIndicator {
  SolutionSet trueParetoFront_ ;
  double      trueParetoFrontHypervolume_ ;
  Problem     problem_ ; 
  jmetal.qualityIndicator.util.MetricsUtil utilities_  ;
  
  /**
   * Constructor
   * @param paretoFrontFile
   */
  public QualityIndicator(Problem problem, String paretoFrontFile) {
    problem_ = problem ;
    utilities_ = new jmetal.qualityIndicator.util.MetricsUtil() ;
    trueParetoFront_ = utilities_.readNonDominatedSolutionSet(paretoFrontFile);
    trueParetoFrontHypervolume_ = new Hypervolume().hypervolume(
                 trueParetoFront_.writeObjectivesToMatrix(),   
                 trueParetoFront_.writeObjectivesToMatrix(),
                 problem_.getNumberOfObjectives());
  } // Constructor 
  
  /**
   * Returns the hypervolume of solution set
   * @param solutionSet
   * @return The value of the hypervolume indicator
   */
  public double getHypervolume(SolutionSet solutionSet) {
    return new Hypervolume().hypervolume(solutionSet.writeObjectivesToMatrix(),
                                         trueParetoFront_.writeObjectivesToMatrix(),
                                         problem_.getNumberOfObjectives());
  } // getHypervolume

    
  /**
   * Returns the hypervolume of the true Pareto front
   * @return The hypervolume of the true Pareto front
   */
  public double getTrueParetoFrontHypervolume() {
    return trueParetoFrontHypervolume_ ;
  }
  
  /**
   * Returns the inverted generational distance of solution set
   * @param solutionSet
   * @return The value of the hypervolume indicator
   */
  public double getIGD(SolutionSet solutionSet) {
    return new InvertedGenerationalDistance().invertedGenerationalDistance(
                    solutionSet.writeObjectivesToMatrix(),
                    trueParetoFront_.writeObjectivesToMatrix(),
                    problem_.getNumberOfObjectives());
  } // getIGD
  
 /**
   * Returns the generational distance of solution set
   * @param solutionSet
   * @return The value of the hypervolume indicator
   */
  public double getGD(SolutionSet solutionSet) {
    return new GenerationalDistance().generationalDistance(
                    solutionSet.writeObjectivesToMatrix(),
                    trueParetoFront_.writeObjectivesToMatrix(),
                    problem_.getNumberOfObjectives());
  } // getGD
  
  /**
   * Returns the spread of solution set
   * @param solutionSet
   * @return The value of the hypervolume indicator
   */
  public double getSpread(SolutionSet solutionSet) {
    return new Spread().spread(solutionSet.writeObjectivesToMatrix(),
                               trueParetoFront_.writeObjectivesToMatrix(),
                               problem_.getNumberOfObjectives());
  } // getGD
  
    /**
   * Returns the epsilon indicator of solution set
   * @param solutionSet
   * @return The value of the hypervolume indicator
   */
  public double getEpsilon(SolutionSet solutionSet) {
    return new Epsilon().epsilon(solutionSet.writeObjectivesToMatrix(),
                                 trueParetoFront_.writeObjectivesToMatrix(),
                                 problem_.getNumberOfObjectives());
  } // getEpsilon
} // QualityIndicator
