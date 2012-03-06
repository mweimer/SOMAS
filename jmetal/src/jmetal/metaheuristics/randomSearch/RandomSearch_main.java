/**
 * RandomSearch_main.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *   A simple algorithm that perform a random search.
 */
package jmetal.metaheuristics.randomSearch;

import jmetal.base.*;
import jmetal.base.operator.crossover.*   ;
import jmetal.base.operator.mutation.*    ;
import jmetal.base.operator.selection.*   ;
import jmetal.problems.*                  ;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.WFG.*;
import jmetal.problems.LZ09.* ;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.qualityIndicator.QualityIndicator;

public class RandomSearch_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments.
   * @throws JMException
   * @throws IOException
   * @throws SecurityException
   * Usage: three options
   *      - jmetal.metaheuristics.randomSearch.RandomSearch_main
   *      - jmetal.metaheuristics.randomSearch.RandomSearch_main problemName
   */
  public static void main(String [] args) throws
                                  JMException, SecurityException, IOException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator

    QualityIndicator indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("RandomSearch_main.log"); 
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
      //problem = new Water("Real");
      //problem = new ZDT1("ArrayReal", 1000);
      //problem = new ZDT4("BinaryReal");
      //problem = new WFG1("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    } // else

    algorithm = new RandomSearch(problem);

    // Algorithm parameters
    algorithm.setInputParameter("maxEvaluations",25000);

    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");      
    
    if (indicators != null) {
      logger_.info("Quality indicators") ;
      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
      logger_.info("GD         : " + indicators.getGD(population)) ;
      logger_.info("IGD        : " + indicators.getIGD(population)) ;
      logger_.info("Spread     : " + indicators.getSpread(population)) ;
      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;
    } // if                   
  } //main
} // Randomsearch_main
