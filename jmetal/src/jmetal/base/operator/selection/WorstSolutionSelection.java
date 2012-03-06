/**
 * WorstSolutionSelection.java
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.base.operator.selection;

import java.util.Comparator;

import jmetal.base.*;
import jmetal.base.operator.comparator.BinaryTournamentComparator;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.comparator.ObjectiveComparator;
import jmetal.util.PseudoRandom;

/**
 * This class implements a selection operator used for selecting the worst 
 * solution in a SolutionSet according to a given comparator
 */
public class WorstSolutionSelection extends Selection {
  
	// Comparator
  private Comparator comparator_;
    
  /**
   * Constructor
   * @param comparator
   */
  public WorstSolutionSelection(Comparator comparator) {
  	comparator_ = comparator ;
  }
  
  /**
  * Performs the operation
  * @param object Object representing a SolutionSet.
  * @return the best solution found
  */
  public Object execute(Object object) {
    SolutionSet solutionSet = (SolutionSet)object;
    
    if (solutionSet.size() == 0) {
      return null;
    }
    int worstSolution ;
    
    worstSolution = 0 ;
   	
    for (int i = 1; i < solutionSet.size(); i++) {
    	if (comparator_.compare(solutionSet.get(i), solutionSet.get(worstSolution)) > 0)  
    		worstSolution = i ;
    } // for
    
    return worstSolution ;    
  } // Execute     
} // WorstObjectiveSelection
