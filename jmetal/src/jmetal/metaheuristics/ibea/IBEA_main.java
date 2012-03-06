/**
 * IBEA_main.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.ibea;

import java.io.IOException;
import jmetal.base.*;
import jmetal.base.operator.crossover.*   ;
import jmetal.base.operator.mutation.*    ;
import jmetal.base.operator.selection.*   ;
import jmetal.problems.*                  ;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.WFG.*;
import jmetal.problems.LZ09.* ;
import jmetal.qualityIndicator.QualityIndicator;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import jmetal.base.operator.comparator.FitnessComparator;

public class IBEA_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three choices
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName paretoFrontFile
   */
  public static void main(String [] args) throws JMException, IOException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator

    QualityIndicator indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("IBEA.log"); 
    logger_.addHandler(fileHandler_) ;
    
    indicators = null ;
    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0],params);
    } // if
    else if (args.length == 2) {
      Object [] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0],params);
      indicators = new QualityIndicator(problem, args[1]) ;
    } // if
    else { // Default problem
      problem = new Kursawe("Real", 3); 
      //problem = new Kursawe("BinaryReal", 3);
      //problem = new Water("Real");
      //problem = new ZDT1("ArrayReal", 100);
      //problem = new ConstrEx("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    } // else

    algorithm = new IBEA(problem);

    // Algorithm parameters
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("archiveSize",100);
    algorithm.setInputParameter("maxEvaluations",25000);

    // Mutation and Crossover for Real codification
    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover");
    crossover.setParameter("probability",1.0);
    crossover.setParameter("distribuitionIndex",20.0);
    mutation = MutationFactory.getMutationOperator("PolynomialMutation");
    mutation.setParameter("probability",1.0/problem.getNumberOfVariables());
    mutation.setParameter("distributionIndex",20.0);

    /* Mutation and Crossover Binary codification */
    /*
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");
    crossover.setParameter("probability",0.9);
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");
    mutation.setParameter("probability",1.0/80);
    */

    /* Selection Operator */
    selection = new BinaryTournament(new FitnessComparator());
    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);

    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

    // Print the results
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");    
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
  
    if (indicators != null) {
      logger_.info("Quality indicators") ;
      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
      logger_.info("GD         : " + indicators.getGD(population)) ;
      logger_.info("IGD        : " + indicators.getIGD(population)) ;
      logger_.info("Spread     : " + indicators.getSpread(population)) ;
      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;  
    } // if
  } //main
} // IBEA_main.java
