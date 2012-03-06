/**
 * Main.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.experiments;

import jmetal.base.*;
import jmetal.problems.*  ;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.io.IOException;

import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.qualityIndicator.QualityIndicator;

public class Main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments.
   * @throws JMException
   * @throws IOException
   * @throws SecurityException
   * Usage: three options
   *      - jmetal.experiments.Main algorithmName
   *      - jmetal.experiments.Main algorithmName problemName
   *      - jmetal.experiments.Main algorithmName problemName paretoFrontFile
   * @throws ClassNotFoundException 
   */
  public static void main(String [] args) throws
                                  JMException, SecurityException, IOException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use

    QualityIndicator indicators ; // Object to get quality indicators

    Properties properties;
    Settings settings = null;

    String algorithmName   = "" ;
    String problemName     = "Kursawe" ; // Default problem
    String paretoFrontFile = "" ;

    properties   = new Properties() ;
    indicators = null ;
    problem = null ;

    if (args.length == 0) { //
      System.err.println("Sintax error. Usage:") ;
      System.err.println("a) jmetal.experiments.Main algorithmName ") ;
      System.err.println("b) jmetal.experiments.Main algorithmName problemName") ;
      System.err.println("c) jmetal.experiments.Main algorithmName problemName paretoFrontFile") ;
      System.exit(-1) ;
    } // if
    else if (args.length == 1) { // algorithmName
      algorithmName = args[0] ;
      Object [] problemParams = {"Real"};
      problem = (new ProblemFactory()).getProblem(problemName, problemParams);      
      Object [] settingsParams = {problem} ;
      settings = (new SettingsFactory()).getSettingsObject(algorithmName, settingsParams) ;
      } // if
    else if (args.length == 2) { // algorithmName problemName
      algorithmName = args[0] ;
      problemName = args[1] ;
      Object [] problemParams = {"Real"};
      problem = (new ProblemFactory()).getProblem(problemName, problemParams);      
      Object [] settingsParams = {problem} ;
      settings = (new SettingsFactory()).getSettingsObject(algorithmName, settingsParams) ;
      } // if
    else if (args.length == 3) { // algorithmName problemName paretoFrontFile
      algorithmName = args[0] ;
      problemName = args[1] ;
      paretoFrontFile = args[2] ;
      Object [] problemParams = {"Real"};
      problem = (new ProblemFactory()).getProblem(problemName, problemParams);      
      Object [] settingsParams = {problem} ;
      settings = (new SettingsFactory()).getSettingsObject(algorithmName, settingsParams) ;
      
      properties.setProperty("paretoFrontFile_", paretoFrontFile);
      indicators = new QualityIndicator(problem, paretoFrontFile);
    } // if

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler(algorithmName + ".log");
    logger_.addHandler(fileHandler_) ;

    algorithm = settings.configure(properties);

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

      if (algorithm.getOutputParameter("evaluations") != null) {
        Integer evals = (Integer)algorithm.getOutputParameter("evaluations") ;
        int evaluations = (Integer)evals.intValue();
        logger_.info("Speed      : " + evaluations + " evaluations") ;
      } // if
    } // if
  } //main
} // main
