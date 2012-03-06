/**
 * DENSEA_Main.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.densea;

import java.io.IOException;
import jmetal.base.*;
import jmetal.base.operator.crossover.*   ;
import jmetal.base.operator.mutation.*    ; 
import jmetal.base.operator.selection.*   ;
import jmetal.base.variable.*             ;
import jmetal.metaheuristics.densea.DENSEA;
import jmetal.problems.*                  ;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.WFG.*;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class DENSEA_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object
  
  public static void main(String [] args) throws JMException, IOException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator
      
    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("Densea.log"); 
    logger_.addHandler(fileHandler_) ;
    
    problem = new RadioNetworkDesign(149);
    
    algorithm = new DENSEA(problem);
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("maxEvaluations",25000);
    
    // Mutation and Crossover Binary codification 
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");                   
    crossover.setParameter("probability",0.9);                   
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");                    
    mutation.setParameter("probability",1.0/149);
    
    // Selection Operator 
    selection = new BinaryTournament();                            
    
    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total time of execution: "+estimatedTime);

    // Log messages 
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");           
  }//main
}

