/**
 * DE_main.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.metaheuristics.singleObjective.differentialEvolution;

import jmetal.base.*;
import jmetal.base.operator.crossover.*   ;
import jmetal.base.operator.mutation.*    ; 
import jmetal.base.operator.selection.*   ;
import jmetal.problems.singleObjective.*  ; 
import jmetal.util.JMException;

/**
 * This class runs a single-objective genetic algorithm (GA). The GA can be 
 * a steady-state GA (class SSGA) or a generational GA (class GGA). The OneMax
 * problem is used to test the algorithms.
 */
public class DE_main {

  public static void main(String [] args) throws JMException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator
            
    //int bits ; // Length of bit string in the OneMax problem
  
    //bits = 512 ;
    //problem = new OneMax(bits);
 
    problem = new Sphere("Real", 20) ;
    //problem = new Easom("Real") ;
    //problem = new Griewank("Real", 10) ;
    
    algorithm = new DE(problem) ;   // Asynchronous cGA
    
    /* Algorithm parameters*/
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("maxEvaluations", 1000000);
    
    // Crossover operator 
    crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");                   
    crossover.setParameter("CR", 0.1);                   
    crossover.setParameter("F", 0.5);
    crossover.setParameter("DE_VARIANT", "rand/1/bin") ;
    
    // Add the operators to the algorithm
    selection = SelectionFactory.getSelectionOperator("DifferentialEvolutionSelection") ;

    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("selection",selection);
 
    /* Execute the Algorithm */
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total execution time: " + estimatedTime);

    /* Log messages */
    System.out.println("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    System.out.println("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");          
  } //main
} // DE_main
