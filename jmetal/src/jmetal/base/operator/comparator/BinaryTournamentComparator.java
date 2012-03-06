/** 
 * BinaryTournamentComparator.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import java.util.*;
import jmetal.base.Solution;

/**
 * This class implements a <code>Comparator</code> for <code>Solution</code>
 */
public class BinaryTournamentComparator implements Comparator{
  
  /**
   * stores a dominance comparator
   */
  private static final Comparator dominance_ = new DominanceComparator();
  
  /**
   * Compares two solutions.
   * A <code>Solution</code> a is less than b for this <code>Comparator</code>.
   * if the crowding distance of a if greater than the crowding distance of b.
   * @param o1 Object representing a <code>Solution</code>.
   * @param o2 Object representing a <code>Solution</code>.
   * @return -1, or 0, or 1 if o1 is less than, equals, or greater than o2,
   * respectively.
   */
  public int compare(Object o1, Object o2) {
    int flag = dominance_.compare(o1,o2);
    if (flag!=0) {
      return flag;
    }
    
    double crowding1, crowding2;
    crowding1 = ((Solution)o1).getCrowdingDistance();
    crowding2 = ((Solution)o2).getCrowdingDistance();
    
    if (crowding1 > crowding2) {
      return -1;
    } else if (crowding2 > crowding1) {
      return 1;
    } else {
      return 0;
    }
  } // compare
} // BinaryTournamentComparator.
