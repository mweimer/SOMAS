/**
 * Elitist.java
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.metaheuristics.singleObjective.evolutionStrategy;

import jmetal.base.*;
import jmetal.base.operator.comparator.* ;
import jmetal.base.Algorithm;
import java.util.Comparator;
import jmetal.util.*;

/** 
 * Class implementing a (mu + lambda) ES. Lambda must be divisible by mu
 */
public class ElitistES extends Algorithm {
  private Problem problem_; 
  private int     mu_     ;
  private int     lambda_ ;
  
 /**
  * Constructor
  * Create a new ElitistES instance.
  * @param problem Problem to solve.
  * @mu Mu
  * @lambda Lambda
  */
  public ElitistES(Problem problem, int mu, int lambda){
    problem_ = problem;  
    mu_      = mu     ;
    lambda_  = lambda ;
  } // ElitistES
  
 /**
  * Execute the ElitistES algorithm
 * @throws JMException 
  */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int maxEvaluations ;
    int evaluations    ;

    SolutionSet population          ;
    SolutionSet offspringPopulation ;  

    Operator   mutationOperator ;
    Comparator comparator       ;
    
    comparator = new ObjectiveComparator(0) ; // Single objective comparator
    
    // Read the params
    maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();                
   
    // Initialize the variables
    population          = new SolutionSet(mu_) ;   
    offspringPopulation = new SolutionSet(mu_ + lambda_) ;
    
    evaluations  = 0;                

    // Read the operators
    mutationOperator  = this.operators_.get("mutation");

    System.out.println("(" + mu_ + " + " + lambda_+")ES") ;
     
    // Create the parent population of mu solutions
    Solution newIndividual;
    for (int i = 0; i < mu_; i++) {
      newIndividual = new Solution(problem_);                    
      problem_.evaluate(newIndividual);            
      evaluations++;
      population.add(newIndividual);
    } //for       
     
    // Main loop
    int offsprings ;
    offsprings = lambda_ / mu_ ; 
    while (evaluations < maxEvaluations) {
      // STEP 1. Generate the mu+lambda population
      for (int i = 0; i < mu_; i++) {
        for (int j = 0; j < offsprings; j++) {
          Solution offspring = new Solution(population.get(i)) ;
          mutationOperator.execute(offspring);
          problem_.evaluate(offspring) ;
          offspringPopulation.add(offspring) ;
          evaluations ++ ;
        } // for
      } // for
      
      // STEP 2. Add the mu individuals to the offspring population
      for (int i = 0 ; i < mu_; i++) {
        offspringPopulation.add(population.get(i)) ;
      } // for
      population.clear() ;

      // STEP 3. Sort the mu+lambda population
      offspringPopulation.sort(comparator) ;
            
      // STEP 4. Create the new mu population
      for (int i = 0; i < mu_; i++)
        population.add(offspringPopulation.get(i)) ;

      System.out.println("Evaluation: " + evaluations + " Fitness: " + 
          population.get(0).getObjective(0)) ; 

      // STEP 6. Delete the mu+lambda population
      offspringPopulation.clear() ;
    } // while
    
    // Return a population with the best individual
    SolutionSet resultPopulation = new SolutionSet(1) ;
    resultPopulation.add(population.get(0)) ;
    
    return resultPopulation ;
  } // execute
} // SSGA