/**
 * RandomSelection.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.selection;

import jmetal.base.*;
import jmetal.util.PseudoRandom;

/**
 * This class implements a random selection operator used for selecting two
 * random parents
 */
public class RandomSelection extends Selection {
  
  /**
  * Performs the operation
  * @param object Object representing a SolutionSet.
  * @return an object representing an array with the selected parents
  */
  public Object execute(Object object) {
    SolutionSet population = (SolutionSet)object;
    int pos1, pos2;
    pos1 = PseudoRandom.randInt(0,population.size()-1);
    pos2 = PseudoRandom.randInt(0,population.size()-1);
    while ((pos1 == pos2) && (population.size()>1)) {
      pos2 = PseudoRandom.randInt(0,population.size()-1);
    }
    
    Solution [] parents = new Solution[2];
    parents[0] = population.get(pos1);
    parents[1] = population.get(pos2);
    
    return parents;
  } // Execute     
} // RandomSelection
