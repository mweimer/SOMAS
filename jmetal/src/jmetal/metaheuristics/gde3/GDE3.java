/**
a * GDE3.java
 * @author Antonio J. Nebro
 * @version 1.0  
 */
package jmetal.metaheuristics.gde3;

import java.util.Comparator;

import jmetal.base.*;
import jmetal.util.*;

/**
 * This class implements the GDE3 algorithm. 
 */
public class GDE3 extends Algorithm {
  
  /**
   * stores the problem  to solve
   */
  private Problem  problem_;        
  
  /**
  * Constructor
  * @param problem Problem to solve
  */
  public GDE3(Problem problem){
    this.problem_ = problem;                        
  } // GDE3
  
  /**   
  * Runs of the GDE3 algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
   * @throws JMException 
  */  
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize ;
    int maxIterations  ;
    int evaluations    ;
    int iterations     ;
    
    SolutionSet population          ;
    SolutionSet offspringPopulation ;
    SolutionSet union               ;
    
    Distance   distance  ;
    Comparator dominance ;
    
    Operator selectionOperator ;
    Operator crossoverOperator ;
    
    distance  = new Distance()  ;               
    dominance = new jmetal.base.operator.comparator.DominanceComparator(); 
    
    // Differential evolution parameters
    int r1    ;
    int r2    ;
    int r3    ;
    int jrand ;

    Solution parent[] ;
    
    //Read the parameters
    populationSize = ((Integer)this.getInputParameter("populationSize")).intValue();
    maxIterations  = ((Integer)this.getInputParameter("maxIterations")).intValue();                             
   
    selectionOperator = operators_.get("selection");   
    crossoverOperator = operators_.get("crossover") ;
    
    //Initialize the variables
    population  = new SolutionSet(populationSize);        
    evaluations = 0;                
    iterations  = 0 ;

    // Create the initial solutionSet
    Solution newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution(problem_);                    
      problem_.evaluate(newSolution);            
      problem_.evaluateConstraints(newSolution);
      evaluations++;
      population.add(newSolution);
    } //for       
  
    // Generations ...
    while (iterations < maxIterations) {
      // Create the offSpring solutionSet      
      offspringPopulation  = new SolutionSet(populationSize * 2);        

      for (int i = 0; i < (populationSize); i++){   
        // Obtain parents. Two parameters are required: the population and the 
        //                 index of the current individual
        parent = (Solution [])selectionOperator.execute(new Object[]{population, i});

        Solution child ;
        // Crossover. Two parameters are required: the current individual and the 
        //            array of parents
        child = (Solution)crossoverOperator.execute(new Object[]{population.get(i), parent}) ;

        problem_.evaluate(child) ;
        problem_.evaluateConstraints(child);
        evaluations++ ;
        
        // Dominance test
        int result  ;
        result = dominance.compare(population.get(i), child) ;
        if (result == -1) { // Solution i dominates child
          offspringPopulation.add(population.get(i)) ;
        } // if
        else if (result == 1) { // child dominates
          offspringPopulation.add(child) ;
        } // else if
        else { // the two solutions are non-dominated
          offspringPopulation.add(child) ;
          offspringPopulation.add(population.get(i)) ;
        } // else
      } // for           

      // Ranking the offspring population
      Ranking ranking = new Ranking(offspringPopulation);                        

      int remain = populationSize;
      int index  = 0;
      SolutionSet front = null;
      population.clear();

      // Obtain the next front
      front = ranking.getSubfront(index);

      while ((remain > 0) && (remain >= front.size())){                
        //Assign crowding distance to individuals
        distance.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());                
        //Add the individuals of this front
        for (int k = 0; k < front.size(); k++ ) {
          population.add(front.get(k));
        } // for

        //Decrement remain
        remain = remain - front.size();

        //Obtain the next front
        index++;
        if (remain > 0) {
          front = ranking.getSubfront(index);
        } // if        
      } // while
      
      // remain is less than front(index).size, insert only the best one
      if (remain > 0) {  // front contains individuals to insert                        
        while (front.size() > remain) {
           distance.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());
           front.sort(new jmetal.base.operator.comparator.CrowdingComparator());
           front.remove(front.size()-1);
        }
        for (int k = 0; k < front.size(); k++) {
          population.add(front.get(k));
        }
        
        remain = 0; 
      } // if                   
      
      iterations ++ ;
    } // while
    
    // Return the first non-dominated front
    Ranking ranking = new Ranking(population);        
    return ranking.getSubfront(0);
  } // execute
} // GDE3
