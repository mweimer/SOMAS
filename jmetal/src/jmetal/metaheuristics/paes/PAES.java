/**
 * Paes.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.paes;

import jmetal.base.*;
import jmetal.util.archive.AdaptiveGridArchive;
import jmetal.base.operator.comparator.*;
import jmetal.util.JMException;

import java.util.Comparator;

/**
 * This class implements the NSGA-II algorithm. 
 */
public class PAES extends Algorithm {        
    
  /**
   * Stores the problem to solve
   */
  private Problem problem_;  
   
  /** 
  * Create a new PAES instance for resolve a problem
  * @param problem Problem to solve
  */                 
  public PAES(Problem problem) {                
    problem_ = problem;        
  } // Paes
    
  /**
   * Tests two solutions to determine which one becomes be the guide of PAES
   * algorithm
   * @param solution The actual guide of PAES
   * @param mutatedSolution A candidate guide
   */
  public Solution test(Solution solution, 
                       Solution mutatedSolution, 
                       AdaptiveGridArchive archive){  
    
    int originalLocation = archive.getGrid().location(solution);
    int mutatedLocation  = archive.getGrid().location(mutatedSolution); 

    if (originalLocation == -1) {
      return new Solution(mutatedSolution);
    }
    
    if (mutatedLocation == -1) {
      return new Solution(solution);
    }
        
    if (archive.getGrid().getLocationDensity(mutatedLocation) < 
        archive.getGrid().getLocationDensity(originalLocation)) {
      return new Solution(mutatedSolution);
    }
    
    return new Solution(solution);          
  } // test
    
  /**   
  * Runs of the Paes algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
   * @throws JMException 
  */    
  public SolutionSet execute() throws JMException, ClassNotFoundException {     
    int bisections, archiveSize, maxEvaluations, evaluations;
    AdaptiveGridArchive archive;
    Operator mutationOperator;
    Comparator dominance;
    
    //Read the params
    bisections     = ((Integer)this.getInputParameter("biSections")).intValue();
    archiveSize    = ((Integer)this.getInputParameter("archiveSize")).intValue();
    maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();

    //Read the operators        
    mutationOperator = this.operators_.get("mutation");        

    //Initialize the variables                
    evaluations = 0;
    archive     = new AdaptiveGridArchive(archiveSize,bisections,problem_.getNumberOfObjectives());        
    dominance = new DominanceComparator();           
            
    //-> Create the initial solution and evaluate it and his constraints
    Solution solution = new Solution(problem_);
    problem_.evaluate(solution);        
    problem_.evaluateConstraints(solution);
    evaluations++;
        
    // Add it to the archive
    archive.add(new Solution(solution));            
   
    //Iterations....
    do {
      // Create the mutate one
      Solution mutatedIndividual = new Solution(solution);  
      mutationOperator.execute(mutatedIndividual);
            
      problem_.evaluate(mutatedIndividual);                     
      problem_.evaluateConstraints(mutatedIndividual);
      evaluations++;
      //<-
            
      // Check dominance
      int flag = dominance.compare(solution,mutatedIndividual);            
            
      if (flag == 1) { //If mutate solution dominate                  
        solution = new Solution(mutatedIndividual);                
        archive.add(mutatedIndividual);                
      } else if (flag == 0) { //If none dominate the other                               
        if (archive.add(mutatedIndividual)) {                    
          solution = test(solution,mutatedIndividual,archive);
        }                                
      }                                              
    } while (evaluations < maxEvaluations);                                    
        
    //Return the  population of non-dominated solution
    return archive;                
  }  // execute  
} // PAES
