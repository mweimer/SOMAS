/**
 * TSPGA_main.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.metaheuristics.singleObjective.geneticAlgorithm;

import java.io.FileNotFoundException;
import java.io.IOException;

import jmetal.base.*;
import jmetal.base.operator.crossover.*   ;
import jmetal.base.operator.mutation.*    ; 
import jmetal.base.operator.selection.*   ;
import jmetal.problems.singleObjective.*  ; 
import jmetal.util.JMException;

/**
 * This class runs a single-objective genetic algorithm (GA). The GA can be 
 * a steady-state GA (class SSGA) or a generational GA (class GGA). The TSP
 * is used to test the algorithms. The data files accepted as in input are from
 * TSPLIB.
 */
public class TSPGA_main {

  public static void main(String [] args)  throws FileNotFoundException, 
                                                  IOException, JMException, 
                                                  ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator
            
    String problemName = "eil101.tsp" ;
    
    problem = new TSP(problemName);
    
    //algorithm = new SSGA(problem);
    algorithm = new gGA(problem) ;
    
    // Algorithm params
    algorithm.setInputParameter("populationSize",512);
    algorithm.setInputParameter("maxEvaluations",200000);
    
    // Mutation and Crossover for Real codification */
    crossover = CrossoverFactory.getCrossoverOperator("TwoPointsCrossover");
    //crossover = CrossoverFactory.getCrossoverOperator("PMXCrossover");
    crossover.setParameter("probability",0.95);                   
    mutation = MutationFactory.getMutationOperator("SwapMutation");                    
    mutation.setParameter("probability",0.2); 
  
    /* Selection Operator */
    selection = SelectionFactory.getSelectionOperator("BinaryTournament") ;                            
    
    /* Add the operators to the algorithm*/
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);

    /* Execute the Algorithm */
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total time of execution: "+estimatedTime);

    /* Log messages */
    System.out.println("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    System.out.println("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");          
  }//main
} // TSPGA_main
