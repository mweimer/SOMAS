/**
 * EpsilonDominanceComparator.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import jmetal.base.Solution;
import java.util.Comparator;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on epsilon dominance.
 */
public class EpsilonDominanceComparator implements Comparator{
   
  /**
   * Stores the value of eta, needed for epsilon-dominance.
   */
  private double eta_;
  
  /** 
   * stores a comparator for check the OverallConstraintComparator
   */
  private static final Comparator overallConstraintViolationComparator_ =
                              new OverallConstraintViolationComparator();
  
  /**
   * Constructor.
  *  @param eta Value for epsilon-dominance.
  */
  public EpsilonDominanceComparator(double eta) {
    eta_ = eta;
  }
  
 /** 
  * Compares two solutions.
  * @param solution1 Object representing the first <code>Solution</code>.
  * @param solution2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if solution1 dominates solution2, both are 
  * non-dominated, or solution1 is dominated by solution2, respectively.
  */
  public int compare(Object object1, Object object2) {
    if (object1==null)
      return 1;
    else if (object2 == null)
      return -1;    
        
    int dominate1 ; // dominate1 indicates if some objective of solution1 
                    // dominates the same objective in solution2. dominate2
    int dominate2 ; // is the complementary of dominate1.

    dominate1 = 0 ; 
    dominate2 = 0 ;   
    
    Solution solution1 = (Solution)object1;
    Solution solution2 = (Solution)object2;
    
    int flag; 
    Comparator constraint = new jmetal.base.operator.comparator.OverallConstraintViolationComparator();
    flag = constraint.compare(solution1,solution2);
    
    if (flag != 0) {      
      return flag;
    }

    double value1, value2;
    // Idem number of violated constraint. Apply a dominance Test
    for (int i = 0; i < ((Solution)solution1).numberOfObjectives(); i++) {
      value1 = solution1.getObjective(i);
      value2 = solution2.getObjective(i);

      //Objetive implements comparable!!! 
      if (value1/(1 + eta_) < value2) {
        flag = -1;
      } else if (value1/(1 + eta_) > value2) {
        flag = 1;
      } else {
        flag =  0;
      }
      
      if (flag == -1) {
        dominate1 = 1;
      }
      
      if (flag == 1) {
        dominate2 = 1;
      }
    }
            
    if (dominate1 == dominate2) {
      return 0; // No one dominates the other
    }
    
    if (dominate1 == 1) {
      return -1; // solution1 dominates
    }
    
    return 1;    // solution2 dominates
  } // compare
} // EpsilonDominanceComparator
