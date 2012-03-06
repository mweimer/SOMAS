/**
 * CellDE_main.java
 *
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * This algorithm is described in:
 *   J.J. Durillo, A.J. Nebro, F. Luna, E. Alba "Solving Three-Objective 
 *   Optimization Problems Using a new Hybrid Cellular Genetic Algorithm". 
 *   PPSN'08. Dortmund. September 2008. 
 */
package jmetal.metaheuristics.cellde;

import jmetal.base.*;
import jmetal.base.operator.crossover.*   ;
import jmetal.base.operator.selection.*   ;
import jmetal.problems.*                  ;

import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import jmetal.qualityIndicator.QualityIndicator;

public class CellDE_main {
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
  public static void main(String [] args) throws 
                                 JMException, SecurityException, IOException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  selection ;
    Operator  crossover ;
    
    QualityIndicator indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("MOCell_main.log");
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
      //problem = new ZDT4("ArrayReal");
      //problem = new WFG1("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    } // else
    
    algorithm = new CellDE(problem);
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("archiveSize",100);
    algorithm.setInputParameter("maxEvaluations",25000);
    algorithm.setInputParameter("feedBack", 20);
    
    // Crossover operator 
    crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");                   
    crossover.setParameter("CR", 0.5);                   
    crossover.setParameter("F", 0.5);
    
    // Add the operators to the algorithm
    selection = SelectionFactory.getSelectionOperator("BinaryTournament") ; 

    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("selection",selection);
    
    // Execute the Algorithm 
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total execution time: "+estimatedTime);

    // Log messages 
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
    } // if
  }//main
} // CellDE_main