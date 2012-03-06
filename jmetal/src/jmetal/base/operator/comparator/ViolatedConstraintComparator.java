/**
 * ViolatedConstraintComparator.java
 * 
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import jmetal.base.Solution;
import java.util.Comparator;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on the number of violated constraints.
 */
public class ViolatedConstraintComparator implements Comparator{
    
 /**
  * Compares two solutions.
  * @param o1 Object representing the first <code>Solution</code>.
  * @param o2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
  * respectively.
  */
  public int compare(Object o1, Object o2) {
    Solution solution1 = (Solution) o1;
    Solution solution2 = (Solution) o2;
    
    if (solution1.getNumberOfViolatedConstraint() < 
        solution2.getNumberOfViolatedConstraint()) {
      return -1;
    } else if (solution2.getNumberOfViolatedConstraint() < 
               solution1.getNumberOfViolatedConstraint()) {
      return 1;
    }
    
    return 0;                         
  } // compare 
} // ViolatedConstraintComparator
