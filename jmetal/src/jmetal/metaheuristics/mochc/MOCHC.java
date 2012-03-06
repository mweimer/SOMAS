/**
 * MOCHC.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.mochc;

import jmetal.base.*;
import jmetal.util.archive.*;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.base.variable.Binary;
import jmetal.util.JMException;

import java.util.*;

/**
 *
 * Class implementing the CHC algorithm.
 */
public class MOCHC extends Algorithm {

  /**
  * Stores the problem to solve
  */
  private Problem problem_;

  /**
  * Constructor
  * Creates a new instance of MOCHC 
  */
  public MOCHC(Problem problem) {
    problem_ = problem;
  }
  
  /** 
  * Compares two solutionSets to determine if both are equals
  * @param solutionSet A <code>SolutionSet</code>
  * @param newSolutionSet A <code>SolutionSet</code>
  * @return true if both are cotains the same solutions, false in other case
  */
  public boolean equals(SolutionSet solutionSet, SolutionSet newSolutionSet) {
    boolean found;
    for (int i = 0; i < solutionSet.size(); i++){

      int j = 0;
      found = false;
      while (j < newSolutionSet.size()) {

        if (solutionSet.get(i).equals(newSolutionSet.get(j))) {
          found = true;
        }
        j++;
      }
      if (!found) {
        return false;
      }
    }
    return true;
  } // equals

  /**
   * Calculate the hamming distance between two solutions
   * @param solutionOne A <code>Solution</code>
   * @param solutionTwo A <code>Solution</code>
   * @return the hamming distance between solutions
   */
  public int hammingDistance(Solution solutionOne, Solution solutionTwo) {
    int distance = 0;
    for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
      distance += 
        ((Binary)solutionOne.getDecisionVariables()[i]).
        hammingDistance((Binary)solutionTwo.getDecisionVariables()[i]);
    }
    
    return distance;
  } // hammingDistance 

  /**   
  * Runs of the MOCHC algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
  */  
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int iterations       ;
    int populationSize   ;
    int convergenceValue ;
    int maxEvaluations   ;
    int minimumDistance  ;
    int evaluations      ;
    
    Comparator crowdingComparator = new CrowdingComparator();
    
    Operator crossover              ;
    Operator parentSelection        ;
    Operator newGenerationSelection ;
    Operator cataclysmicMutation    ;
    
    double preservedPopulation     ;
    double initialConvergenceCount ;
    boolean condition = false;
    SolutionSet solutionSet, offspringPopulation,newPopulation;

    // Read parameters
    initialConvergenceCount = 
           ((Double)getInputParameter("initialConvergenceCount")).doubleValue();
    preservedPopulation     = 
           ((Double)getInputParameter("preservedPopulation")).doubleValue();
    convergenceValue        = 
           ((Integer)getInputParameter("convergenceValue")).intValue();
    populationSize          = 
           ((Integer)getInputParameter("populationSize")).intValue();
    maxEvaluations          = 
           ((Integer)getInputParameter("maxEvaluations")).intValue();


    // Read operators
    crossover = (Operator)getOperator("crossover");
    cataclysmicMutation = (Operator)getOperator("cataclysmicMutation");
    parentSelection = (Operator)getOperator("parentSelection");
    newGenerationSelection = (Operator)getOperator("newGenerationSelection");

    iterations  = 0 ;
    evaluations = 0 ;
    
    //Calculate the maximum problem sizes
    Solution aux = new Solution(problem_);
    int size = 0;
    for (int var = 0; var < problem_.getNumberOfVariables(); var++) {
      size += ((Binary)aux.getDecisionVariables()[var]).getNumberOfBits();
    }
    minimumDistance = (int) Math.floor(initialConvergenceCount * size);

    solutionSet = new SolutionSet(populationSize);
    for (int i = 0; i < populationSize; i++) {
      Solution solution = new Solution(problem_);
      problem_.evaluate(solution);
      problem_.evaluateConstraints(solution);
      evaluations++;        
      solutionSet.add(solution);
    }      

    while (!condition) {
      offspringPopulation = new SolutionSet(populationSize);
      for (int i = 0; i < solutionSet.size()/2; i++) {
        Solution [] parents   = (Solution [])parentSelection.execute(solutionSet);         

        //Equality condition between solutions
        if (hammingDistance(parents[0],parents[1]) >= (minimumDistance)) {
          Solution [] offspring = (Solution [])crossover.execute(parents);
          problem_.evaluate(offspring[0]);
          problem_.evaluateConstraints(offspring[0]);
          problem_.evaluate(offspring[1]);
          problem_.evaluateConstraints(offspring[1]);
          evaluations += 2;
          offspringPopulation.add(offspring[0]);
          offspringPopulation.add(offspring[1]);
        }
      }
      SolutionSet union = solutionSet.union(offspringPopulation);
      newGenerationSelection.setParameter("populationSize",populationSize);
      newPopulation = (SolutionSet)newGenerationSelection.execute(union);

      if (equals(solutionSet,newPopulation)) {
        minimumDistance--;
      }
      if (minimumDistance <= -convergenceValue) {

        minimumDistance = (int) (1.0/size * (1 - 1.0/size) * size);
        //minimumDistance = (int) (0.35 * (1 - 0.35) * size);

        int preserve = (int)Math.floor(preservedPopulation*populationSize);
        newPopulation = new SolutionSet(populationSize);
        solutionSet.sort(crowdingComparator);
        for (int i = 0; i < preserve; i++) {
          newPopulation.add(new Solution(solutionSet.get(i)));
        }
        for (int i = preserve;i < populationSize; i++) {
          Solution solution = new Solution(solutionSet.get(i));
          cataclysmicMutation.execute(solution);
          problem_.evaluate(solution);
          problem_.evaluateConstraints(solution);
          newPopulation.add(solution);
        }                        
      }
      iterations++;

      solutionSet = newPopulation;
      if (evaluations >= maxEvaluations) {
        condition = true;
      }
    }
    
    
    CrowdingArchive archive;
    archive = new CrowdingArchive(populationSize,problem_.getNumberOfObjectives()) ;
    for (int i = 0; i < solutionSet.size(); i++) {
      archive.add(solutionSet.get(i)) ;
    }

    return archive;
  } // execute
}  // MOCHC
