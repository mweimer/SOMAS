/**
 * DistanceToPopulationComparator.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import java.util.Comparator;
import jmetal.base.Solution;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on the euclidean distance to a
 * solution set. This distances are obtained through the method 
 * <code>getDistanceToPopulation<code>.
 */
public class DistanceToPopulationComparator implements Comparator{
    
 /**
  * Compares two solutions.
  * @param o1 Object representing the first <code>Solution</code>.
  * @param o2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
  * respectively.
  */
  public int compare(Object o1, Object o2) {
    if (o1==null)
      return 1;
    else if (o2 == null)
      return -1;
    
    double distance1 = ((Solution)o1).getDistanceToSolutionSet();
    double distance2 = ((Solution)o2).getDistanceToSolutionSet();
    if (distance1 < distance2) {
      return -1;
    } else if (distance1 > distance2) {
      return 1;
    }
    
    return 0;
  } // compare
} // DistanceToPopulationComparator
