/**
 * acGA.java
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.metaheuristics.singleObjective.geneticAlgorithm;

import jmetal.base.*;

import java.util.Comparator;
import jmetal.base.operator.comparator.*;
import jmetal.base.operator.selection.BestSolutionSelection;
import jmetal.util.*;

/**
 * Class implementing an asynchronous cellular genetic algorithm
 */
public class acGA extends Algorithm {

  /**
   * Stores the problem to solve
   */
  private Problem problem_;

  /** 
   * Constructor
   * @param problem Problem to solve
   */
  public acGA(Problem problem){
    problem_ = problem;
  } // sMOCell1


  /**   
   * Runs of the acGA algorithm.
   * @return a <code>SolutionSet</code> that contains the best found solution  
   * @throws JMException 
   */ 
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize, maxEvaluations, evaluations ;
    Operator mutationOperator  = null ;
    Operator crossoverOperator = null ;
    Operator selectionOperator = null ;

    SolutionSet [] neighbors;    
    SolutionSet population ;
    Neighborhood neighborhood;

    Comparator  comparator      ;
    comparator = new ObjectiveComparator(0) ; // Single objective comparator
    
    Operator findBestSolution ;
    findBestSolution = new BestSolutionSelection(comparator) ;

    //Read the params
    populationSize    = ((Integer)getInputParameter("populationSize")).intValue();
    maxEvaluations    = ((Integer)getInputParameter("maxEvaluations")).intValue();                

    //Read the operators
    mutationOperator  = operators_.get("mutation");
    crossoverOperator = operators_.get("crossover");
    selectionOperator = operators_.get("selection");        

    //Initialize the variables    
    evaluations        = 0;                        
    neighborhood       = new Neighborhood(populationSize);
    neighbors          = new SolutionSet[populationSize];

    population = new SolutionSet(populationSize) ;
    //Create the initial population
    for (int i = 0; i < populationSize; i++){
      Solution solution = new Solution(problem_);
      problem_.evaluate(solution);  
      population.add(solution);
      solution.setLocation(i);
      evaluations++;
    }         
  	
    boolean solutionFound = false ;
    while ((evaluations < maxEvaluations) && !solutionFound) {              
      for (int ind = 0; ind < population.size(); ind++){      	
        Solution individual = new Solution(population.get(ind));

        Solution [] parents = new Solution[2];
        Solution [] offSpring = null ;

        neighbors[ind] = neighborhood.getEightNeighbors(population,ind);                                                           
        neighbors[ind].add(individual);

        //parents
        parents[0] = (Solution)selectionOperator.execute(neighbors[ind]);
        parents[1] = (Solution)selectionOperator.execute(neighbors[ind]);

        //Create a new solution, using genetic operators mutation and crossover
        if (crossoverOperator != null)
          offSpring = (Solution [])crossoverOperator.execute(parents);        
        else {
        	offSpring = new Solution[1] ;
        	offSpring[0] = new Solution(parents[0]) ;
        }
        mutationOperator.execute(offSpring[0]);

        //->Evaluate offspring and constraints
        problem_.evaluate(offSpring[0]);
        //problem_.evaluateConstraints(offSpring[0]);
        evaluations++;

        if (comparator.compare(individual, offSpring[0]) > 0)
        	population.replace(ind, offSpring[0]) ;          
        
        if ((evaluations % 1000) == 0) {
          int bestSolution = (Integer)findBestSolution.execute(population) ;
         	System.out.println ("Evals: " + evaluations + "\t Fitness: " + 
          	  population.get(bestSolution).getObjective(0)) ;
        } // if
      } // for                     
    } // while
    
    population.sort(comparator) ;
    SolutionSet resultPopulation = new SolutionSet(1) ;
    resultPopulation.add(population.get(0)) ;
    
    System.out.println("Evaluations: " + evaluations ) ;
    return resultPopulation ;
  } // execute        
} // acGA

