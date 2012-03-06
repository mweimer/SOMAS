/**
 * pMOEAD_main.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * This class executes a parallel version of the MOEAD algorithm described in:
 *   A.J. Nebro, J.J. Durillo, 
 *   "A Study of the parallelization of the multi-objective metaheuristic 
 *   MOEA/D"
 *   LION 4, Venice, January 2010.
 *   */
package jmetal.metaheuristics.moead;

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

public class pMOEAD_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments. The first (optional) argument specifies 
   *      the problem to solve.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three options
   *      - jmetal.metaheuristics.moead.MOEAD_main
   *      - jmetal.metaheuristics.moead.MOEAD_main problemName
   *      - jmetal.metaheuristics.moead.MOEAD_main problemName ParetoFrontFile
 
   */
  public static void main(String [] args) throws JMException, SecurityException, IOException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
     
    QualityIndicator indicators ; // Object to get quality indicators

    int numberOfThreads = 1 ;
    String dataDirectory = "" ;
    
    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("pMOEAD.log"); 
    logger_.addHandler(fileHandler_) ;
    
    indicators = null ;
    if (args.length == 1) { // args[0] = problem name
      Object [] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0],params);
    } // if
    else if (args.length == 2) { // args[0] = problem name, [1] = pareto front file
        Object [] params = {"Real"};
        problem = (new ProblemFactory()).getProblem(args[0],params);
        indicators = new QualityIndicator(problem, args[1]) ;
      } // if
    else if (args.length == 3) { // args[0] = problem name, [1] = threads, 
    	                         //     [2] = data directory
        Object [] params = {"Real"};
        problem = (new ProblemFactory()).getProblem(args[0],params);
        numberOfThreads = Integer.parseInt(args[1]) ;
        dataDirectory = args[2] ;
      } // if
    else { // Problem + number of threads + data directory
      problem = new Kursawe("Real", 3); 
      //problem = new Kursawe("BinaryReal", 3);
      //problem = new Water("Real");
      //problem = new ZDT1("ArrayReal", 100);
      //problem = new ConstrEx("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    } // else

    algorithm = new pMOEAD(problem);
    
    // Algorithm parameters
    numberOfThreads = 4 ;
    algorithm.setInputParameter("populationSize",300);
    algorithm.setInputParameter("maxEvaluations",150000);
    algorithm.setInputParameter("numberOfThreads", numberOfThreads);
    
    // Directory with the files containing the weight vectors used in 
    // Q. Zhang,  W. Liu,  and H Li, The Performance of a New Version of MOEA/D 
    // on CEC09 Unconstrained MOP Test Instances Working Report CES-491, School 
    // of CS & EE, University of Essex, 02/2009.
    // http://dces.essex.ac.uk/staff/qzhang/MOEAcompetition/CEC09final/code/ZhangMOEADcode/moead0305.rar
    algorithm.setInputParameter("dataDirectory",
    "/Users/antonio/Softw/pruebas/data/MOEAD_parameters/Weight");
    
    // Crossover operator 
    crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");                   
    crossover.setParameter("CR", 1.0);                   
    crossover.setParameter("F", 0.5);
    
    // Mutation operator
    mutation = MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setParameter("probability",1.0/problem.getNumberOfVariables());
    mutation.setParameter("distributionIndex",20.0);  
    
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + " ms");
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
  } //main
} // pMOEAD_main
