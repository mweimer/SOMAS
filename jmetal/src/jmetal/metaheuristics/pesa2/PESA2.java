/**
 * PESA2.java
 * @author Juan J. Durillo
 * @version 1.0
 * 
 */
package jmetal.metaheuristics.pesa2;

import jmetal.base.*;
import jmetal.util.archive.AdaptiveGridArchive;
import jmetal.base.operator.selection.PESA2Selection;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements the PESA2 algorithm. 
 */
public class PESA2 extends Algorithm{
  
  /**
   * Stores the problem to solve
   */
  private Problem problem_;
  
  /**
  * Constructor
  * Creates a new instance of PESA2
  */
  public PESA2(Problem problem) {
    problem_ = problem;
  } // PESA2
    
  /**   
  * Runs of the PESA2 algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
   * @throws JMException 
  */  
  public SolutionSet execute() throws JMException, ClassNotFoundException {        
    int archiveSize, bisections, maxEvaluations, evaluations, populationSize;        
    AdaptiveGridArchive archive;
    SolutionSet solutionSet;
    Operator crossover,mutation, selection;
        
    // Read parameters
    populationSize = ((Integer)(inputParameters_.get("populationSize"))).intValue();
    archiveSize    = ((Integer)(inputParameters_.get("archiveSize"))).intValue()   ;
    bisections     = ((Integer)(inputParameters_.get("bisections"))).intValue()    ;
    maxEvaluations = ((Integer)(inputParameters_.get("maxEvaluations"))).intValue();
    
    // Get the operators
    crossover = operators_.get("crossover");
    mutation  = operators_.get("mutation");
            
    // Initialize the variables
    evaluations = 0;    
    archive = new AdaptiveGridArchive(archiveSize,bisections,
                                        problem_.getNumberOfObjectives());
    solutionSet  = new SolutionSet(populationSize);
    selection    = new PESA2Selection();

    //-> Create the initial individual and evaluate it and his constraints
    for (int i = 0; i < populationSize; i++){
      Solution solution = new Solution(problem_);
      problem_.evaluate(solution);        
      problem_.evaluateConstraints(solution);
      evaluations++;    
      solutionSet.add(solution);      
    }
    //<-                
        
    // Incorporate non-dominated solution to the archive
    for (int i = 0; i < solutionSet.size();i++){
      archive.add(solutionSet.get(i)); // Only non dominated are accepted by 
                                      // the archive
    }
    
    // Clear the init solutionSet
    solutionSet.clear();
    
    //Iterations....
    Solution [] parents = new Solution[2];
    do {
      //-> Create the offSpring solutionSet                    
      while (solutionSet.size() < populationSize){                        
        parents[0] = (Solution) selection.execute(archive);
        parents[1] = (Solution) selection.execute(archive);
        
        Solution [] offSpring = (Solution []) crossover.execute(parents);        
        mutation.execute(offSpring[0]);
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        evaluations++;
        solutionSet.add(offSpring[0]);                
      }
            
      for (int i = 0; i < solutionSet.size(); i++)
        archive.add(solutionSet.get(i));
      
      // Clear the solutionSet
      solutionSet.clear();
                                                                        
    }while (evaluations < maxEvaluations);                                            
    //Return the  solutionSet of non-dominated individual
    return archive;                
  } // execute      
} // PESA2
