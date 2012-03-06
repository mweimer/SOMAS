/**
 * FPGAFitness.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.util;

import jmetal.base.*                    ;
import jmetal.base.operator.comparator.*;
import java.util.*                      ;

/**
 * This class implements facilities for calculating the fitness for the
 * FPGA algorithm
 */
public class FPGAFitness {
  /**
  * Need the population to assign the fitness, this population may contain
  * solutions in the population and the archive
  */
  private SolutionSet solutionSet_ = null;
  
  /**
   * problem to solve
   */
  private Problem     problem_     = null;
        
  /**
   * stores a <code>Comparator</code> for dominance checking
   */
  private static final Comparator dominance_ = new DominanceComparator();
  
  /**
   * Constructor.
   * Create a new instance of Spea2Fitness
   * @param solutionSet The solutionSet to assign the fitness
   * @param problem The problem to solve
   */
  public FPGAFitness(SolutionSet solutionSet, Problem problem) {   
    solutionSet_ = solutionSet;
    problem_    = problem;
    for (int i = 0; i < solutionSet_.size(); i++) {
      solutionSet_.get(i).setLocation(i);
    } // for
  } // FPGAFitness
    
    
  /** 
   * Assign FPGA fitness to the solutions. Similar to the SPEA2 fitness.
   */
  public void fitnessAssign() {
    double [] strength    = new double[solutionSet_.size()];
    double [] rawFitness  = new double[solutionSet_.size()];  
    
    //Ranking  ranking  = new Ranking(solutionSet_);
    //Distance distance = new Distance();
    //distance.crowdingDistanceAsignament(ranking.getSubfront(0),
    //                                    problem_.getNumberOfObjectives());  
    
    for (int i = 0; i < solutionSet_.size(); i++) {
      if (solutionSet_.get(i).getRank()==0) {
        solutionSet_.get(i).setFitness(solutionSet_.get(i).getCrowdingDistance());
        //System.out.println(solutionSet_.get(i).getCrowdingDistance());
      }
    }
           
    //Calculate the strength value
    // strength(i) = |{j | j <- SolutionSet and i dominate j}|
    for (int i = 0; i < solutionSet_.size(); i++) {
      for (int j = 0; j < solutionSet_.size();j++) {
        if (dominance_.compare(solutionSet_.get(i),solutionSet_.get(j))==-1) {
          strength[i] += 1.0;
        } // if        
      } // for
    } // for
       
        
    //Calculate the fitness
    //F(i) = sum(strength(j) | i dominate j) - sum(strenth(j) | j dominate i)
    for (int i = 0;i < solutionSet_.size(); i++) {
      double fitness = 0.0;
      for (int j = 0; j < solutionSet_.size();j++) {
        int flag = dominance_.compare(solutionSet_.get(i),solutionSet_.get(j));
        if (flag == -1) { //i domiante j
          fitness += strength[j];
        }else if (flag==1) {
          fitness -= strength[j];
        } // if
      } // for
    } // for
            
  } // fitnessAsign    
} // FPGAFitness
