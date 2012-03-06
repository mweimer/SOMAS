/**
 * AdaptiveGridArchive.java
 *
 * @author Juan J. Durillo
 * @version 1.0 
 */
package jmetal.util.archive;

import jmetal.base.*;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.util.AdaptiveGrid;
import java.util.*;

/**
 * This class implements an archive based on an adaptative grid used in PAES
 */
public class AdaptiveGridArchive extends Archive {
  
  /** 
   * Stores the adaptive grid
   */
  private AdaptiveGrid grid_;
  
  /** 
   * Stores the maximum size of the archive
   */
  private int maxSize_;
  
  /**
   * Stores a <code>Comparator</code> for dominance checking
   */
  private Comparator dominance_;
    
  /**
   * Constructor.
   * 
   * @param maxSize The maximum size of the archive
   * @param bisections The maximum number of bi-divisions for the adaptive
   * grid.
   * @param objectives The number of objectives.
   */
  public AdaptiveGridArchive(int maxSize,int bisections, int objectives) {
    super(maxSize);
    maxSize_   = maxSize;
    dominance_ = new DominanceComparator();
    grid_      = new AdaptiveGrid(bisections,objectives);
  } // AdaptiveGridArchive
   
  /**
   * Adds a <code>Solution</code> to the archive. If the <code>Solution</code>
   * is dominated by any member of the archive then it is discarded. If the 
   * <code>Solution</code> dominates some members of the archive, these are
   * removed. If the archive is full and the <code>Solution</code> has to be
   * inserted, one <code>Solution</code> of the most populated hypercube of the
   * adaptative grid is removed.
   * @param solution The <code>Solution</code>
   * @return true if the <code>Solution</code> has been inserted, false
   * otherwise.
   */
  public boolean add(Solution solution) {
    //Iterator of individuals over the list
    Iterator<Solution> iterator = solutionsList_.iterator();
        
    while (iterator.hasNext()){
      Solution element = iterator.next();
      int flag = dominance_.compare(solution,element);
      if (flag == -1) { // The Individual to insert dominates other 
    	                // individuals in  the archive
        iterator.remove(); //Delete it from the archive
        int location = grid_.location(element);
        if (grid_.getLocationDensity(location) > 1) {//The hypercube contains 
          grid_.removeSolution(location);            //more than one individual
        } else {
          grid_.updateGrid(this);
        } // else
      } // if 
      else if (flag == 1) { // An Individual into the file dominates the 
    	                      // solution to insert
        return false; // The solution will not be inserted
      } // else if           
    } // while
        
    // At this point, the solution may be inserted
    if (size() == 0){ //The archive is empty
      solutionsList_.add(solution);
      grid_.updateGrid(this);        
      return true;
    } //
        
    if (size() < maxSize_){ //The archive is not full              
      grid_.updateGrid(solution,this); // Update the grid if applicable
      int location ;
      location= grid_.location(solution); // Get the location of the solution
      grid_.addSolution(location); // Increment the density of the hypercube
      solutionsList_.add(solution); // Add the solution to the list
      return true;
    } // if
        
    // At this point, the solution has to be inserted and the archive is full
    grid_.updateGrid(solution,this);
    int location = grid_.location(solution);
    if (location == grid_.getMostPopulated()) { // The solution is in the 
    	                                        // most populated hypercube
      return false; // Not inserted
    } else {
      // Remove an solution from most poblated area
      iterator = solutionsList_.iterator();
      boolean removed = false;
      while (iterator.hasNext()) {
        if (!removed) {
          Solution element = iterator.next();
          int location2 = grid_.location(element);
          if (location2 == grid_.getMostPopulated()) {
            iterator.remove();
            grid_.removeSolution(location2);
          } // if
        } // if
      } // while
      
      // A solution from most populated hypercube has been removed, 
      // insert now the solution
      grid_.addSolution(location);
      solutionsList_.add(solution);            
    }
    return true;
  } // add
    
  /**
   * Returns the AdaptativeGrid used
   * @return the AdaptativeGrid
   */
  public AdaptiveGrid getGrid() {
    return grid_;
  } // AdaptativeGrid  
} // AdaptativeGridArchive
