/**
 * RankingAndCrowdingSelection.java
 * @author Juan J. Durillo
 */

package jmetal.base.operator.selection;

import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import java.util.Comparator;
import jmetal.base.operator.comparator.*;

/** 
 * This class implements a selection for selecting a number of solutions from
 * a solutionSet. The solutions are taken by mean of its ranking and 
 * crowding ditance values.
 * NOTE: if you use the default constructor, the problem has to be passed as
 * a parameter before invoking the execute() method -- see lines 67 - 74
 */
public class RankingAndCrowdingSelection extends Selection {

  /**
   * stores the problem to solve 
   */
  private Problem problem_;
  
  /**
   * stores a <code>Comparator</code> for crowding comparator checking.
   */
  private static final Comparator crowdingComparator_ = 
                                  new CrowdingComparator();

  
  /**
   * stores a <code>Distance</code> object for distance utilities.
   */
  private static final Distance distance_ = new Distance();
  
  /**
   * Constructor
   */
  public RankingAndCrowdingSelection() {
    problem_ = null ;
  } // RankingAndCrowdingSelection
  
  /**
   * Constructor
   * @param problem Problem to be solved
   */
  public RankingAndCrowdingSelection(Problem problem) {
    problem_ = problem;
  } // RankingAndCrowdingSelection

  /**
  * Performs the operation
  * @param object Object representing a SolutionSet.
  * @return an object representing a <code>SolutionSet<code> with the selected parents
   * @throws JMException 
  */
  public Object execute (Object object) throws JMException {
    SolutionSet population = (SolutionSet)object;
    int populationSize     = (Integer)parameters_.get("populationSize");
    SolutionSet result     = new SolutionSet(populationSize);

    if (problem_ == null) {
      problem_ = (Problem)getParameter("problem");
      if (problem_ == null) {
        
        Configuration.logger_.severe("RankingAndCrowdingSelection.execute: " +
            "problem not specified") ;
        Class cls = java.lang.String.class;
        String name = cls.getName(); 
        throw new JMException("Exception in " + name + ".execute()") ;  
      } // if
    } // if
    
    //->Ranking the union
    Ranking ranking = new Ranking(population);                        

    int remain = populationSize;
    int index  = 0;
    SolutionSet front = null;
    population.clear();

    //-> Obtain the next front
    front = ranking.getSubfront(index);

    while ((remain > 0) && (remain >= front.size())){                
      //Asign crowding distance to individuals
      distance_.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());                
      //Add the individuals of this front
      for (int k = 0; k < front.size(); k++ ) {
        result.add(front.get(k));
      } // for

      //Decrement remaint
      remain = remain - front.size();

      //Obtain the next front
      index++;
      if (remain > 0) {
        front = ranking.getSubfront(index);
      } // if        
    } // while

    //-> remain is less than front(index).size, insert only the best one
    if (remain > 0) {  // front containt individuals to insert                        
      distance_.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());
      front.sort(crowdingComparator_);
      for (int k = 0; k < remain; k++) {
        result.add(front.get(k));
      } // for

      remain = 0; 
    } // if

    return result;
  } // execute    
} // RankingAndCrowding
