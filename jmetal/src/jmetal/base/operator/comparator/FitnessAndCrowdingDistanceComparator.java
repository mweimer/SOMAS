/**
 * FitnessAndCrowdingComparator.java
 * 
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import java.util.Comparator;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on the fitness and crowding distance.
 */
public class FitnessAndCrowdingDistanceComparator implements Comparator{
  
  /** 
   * stores a comparator for check the fitness value of the solutions
   */
  private static final Comparator fitnessComparator_ =
                              new FitnessComparator();
  /** 
   * stores a comparator for check the crowding distance
   */
  private static final Comparator crowdingDistanceComparator_ = 
                              new CrowdingDistanceComparator();
  
 /**
  * Compares two solutions.
  * @param solution1 Object representing the first <code>Solution</code>.
  * @param solution2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if solution1 is less than, equal, or greater than 
  * solution2, respectively.
  */
  public int compare(Object solution1, Object solution2) {    
    
    int flag = fitnessComparator_.compare(solution1,solution2);
    if (flag != 0) {
      return flag;
    } else {
      return crowdingDistanceComparator_.compare(solution1,solution2);        
    }
  } // compares
} // FitnessAndCrowdingDistanceComparator
