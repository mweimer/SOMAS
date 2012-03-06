/**
 * RandomSearch.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.randomSearch;

import jmetal.base.*;
import jmetal.util.*;

/**
 * This class implements the NSGA-II algorithm.
 */
public class RandomSearch extends Algorithm {

  /**
   * stores the problem  to solve
   */
  private Problem  problem_;

  /**
  * Constructor
  * @param problem Problem to solve
  */
  public RandomSearch(Problem problem){
    this.problem_ = problem;
  } // RandomSearch

  /**
  * Runs the RandomSearch algorithm.
  * @return a <code>SolutionSet</code> that is a set of solutions
  * as a result of the algorithm execution
   * @throws JMException
  */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int maxEvaluations ;
    int evaluations    ;

    maxEvaluations    = ((Integer)getInputParameter("maxEvaluations")).intValue();

    //Initialize the variables
    evaluations = 0;

    NonDominatedSolutionList ndl = new NonDominatedSolutionList();

    // Create the initial solutionSet
    Solution newSolution;
    for (int i = 0; i < maxEvaluations; i++) {
      newSolution = new Solution(problem_);
      problem_.evaluate(newSolution);
      problem_.evaluateConstraints(newSolution);
      evaluations++;
      ndl.add(newSolution);
    } //for

    return ndl;
  } // execute
} // RandomSearch
