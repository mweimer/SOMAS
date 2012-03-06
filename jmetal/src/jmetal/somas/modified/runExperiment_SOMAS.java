/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jmetal.somas.modified;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.experiments.Experiment;
import jmetal.experiments.Settings;
import jmetal.somas.SOMASExperiment;
import jmetal.somas.SOMASProblem;
import jmetal.util.JMException;
import jmetal.util.Ranking;
/**
 *
 * @author antonio
 */
public class runExperiment_SOMAS extends Thread {

  public SOMASExperiment experiment_ ;
  public int id_ ;
  public HashMap<String, Object> map_ ;
  public int numberOfThreads_ ;
  public int numberOfProblems_ ;

  int first_;
  int last_;

  String experimentName_;
  String[] algorithmNameList_; // List of the names of the algorithms to be executed
  String[] problemList_; // List of problems to be solved
  String[] paretoFrontFile_; // List of the files containing the pareto fronts
  // corresponding to the problems in problemList_
  String experimentBaseDirectory_; // Directory to store the results
  String paretoFrontDirectory_; // Directory containing the Pareto front files
  String outputParetoFrontFile_; // Name of the file containing the output
  // Pareto front
  String outputParetoSetFile_; // Name of the file containing the output
  // Pareto set
  int independentRuns_; // Number of independent runs per algorithm
  Settings[] algorithmSettings_; // Paremeter settings of each algorithm

  public runExperiment_SOMAS(SOMASExperiment experiment, 
                       HashMap<String, Object> map,
                       int id,
                       int numberOfThreads,
                       int numberOfProblems) {
    experiment_ = experiment ;
    id_ = id ;
    map_ = map ;
    numberOfThreads_ = numberOfThreads  ;
    numberOfProblems_ = numberOfProblems;

    int partitions = numberOfProblems / numberOfThreads;

    first_ = partitions * id;
    if (id == (numberOfThreads - 1)) {
      last_ = numberOfProblems - 1;
    } else {
      last_ = first_ + partitions - 1;
    }

    System.out.println("Id: " + id + "  Partitions: " + partitions +
      " First: " + first_ + " Last: " + last_);
  }

  public void run() {
    Algorithm[] algorithm; // jMetal algorithms to be executed

    String experimentName = (String) map_.get("name");
    experimentBaseDirectory_ = (String) map_.get("experimentDirectory");
    algorithmNameList_ = (String[]) map_.get("algorithmNameList");
    problemList_ = (String[]) map_.get("problemList");
    independentRuns_ = (Integer) map_.get("independentRuns");
    outputParetoFrontFile_ = (String) map_.get("outputParetoFrontFile");
    outputParetoSetFile_ = (String) map_.get("outputParetoSetFile");

    int numberOfAlgorithms = algorithmNameList_.length;
    System.out.println("Experiment: Number of algorithms: " + numberOfAlgorithms) ;
    System.out.println("Experiment: runs: " + independentRuns_) ;
    algorithm = new Algorithm[numberOfAlgorithms] ;

    System.out.println("Experiment Name: " + experimentName);
    System.out.println("experimentDirectory: " + experimentBaseDirectory_);
    System.out.println("numberOfThreads_: " + numberOfThreads_);
    System.out.println("numberOfProblems_: " + numberOfProblems_);
    System.out.println("first: " + first_);
    System.out.println("last: " + last_);

    SolutionSet resultFront = null;  
    
    for (int problemId = first_; problemId <= last_; problemId++) {
      Problem problem = null;   // The problem to solve

      try 
      {
		problem = new SOMASProblem();
      } 
      catch (ClassNotFoundException ex) 
      {
    	Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
      }

      //  configure the algorithms
      try {
	      experiment_.algorithmSettings(problem, problemId, algorithm);
      } catch (ClassNotFoundException e1) {
	      // TODO Auto-generated catch block
	      e1.printStackTrace();
      }
      for (int runs = 0; runs < independentRuns_; runs++) {
        System.out.println("Iruns: " + runs) ;

        // run all the algorithms
        for (int i = 0; i < numberOfAlgorithms; i++) {
          System.out.println(algorithm[i].getClass()) ;
          // create output directories
          File experimentDirectory;
          String directory;

          directory = experimentBaseDirectory_ + "/data/" + algorithmNameList_[i] + "/" +
            problemList_[problemId];

          experimentDirectory = new File(directory);
          if (!experimentDirectory.exists()) {
            boolean result = new File(directory).mkdirs();
            System.out.println("Creating " + directory);
          }

          // run the algorithm
          System.out.println("Running algorithm: " + algorithmNameList_[i] +
            ", problem: " + problemList_[problemId] +
            ", run: " + runs);
          try {
            try {
	            resultFront = algorithm[i].execute();
            } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
          } catch (JMException ex) {
            Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
          }

          SOMASExperiment.iRunResults.add(resultFront);
          
          // put the results in the output directory
          resultFront.printObjectivesToFile(directory + "/" + outputParetoFrontFile_ + "." + runs);
          resultFront.printVariablesToFile(directory + "/" + outputParetoSetFile_ + "." + runs);

        } // for
      } // for
      
      // calculate and output aggregate front
      String directory = experimentBaseDirectory_ + "/data/" + algorithmNameList_[0] + "/" + problemList_[problemId];
      SolutionSet aggregateResults = new SolutionSet();
      for(SolutionSet solutionSet : SOMASExperiment.iRunResults)
    	  aggregateResults = aggregateResults.union(solutionSet);
      Ranking ranking = new Ranking(aggregateResults);
      SolutionSet nonDominatedResults = ranking.getSubfront(0);
      nonDominatedResults.printObjectivesToFile(directory + "/" + outputParetoFrontFile_ + ".aggregate");
      nonDominatedResults.printVariablesToFile(directory + "/" + outputParetoSetFile_ + ".aggregate");
    } //for
  }
}
