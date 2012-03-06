package jmetal.somas;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.experiments.Experiment;
import jmetal.experiments.Settings;
import jmetal.somas.modified.IBEA_Settings_SOMAS;
import jmetal.somas.modified.runExperiment_SOMAS;
import jmetal.util.JMException;

public class SOMASExperiment extends Experiment
{
	// For these variables if no value is set by the command line arguments then
	// the default values listed here will be used.
	
	// general jMetal and SOMAS settings
	public static int numSimRunsPerEval = 6;//30; // must be at least 1
	public static int numIndependentRuns = 1;//5; // must be at least 1
	public static String scenarioName = "CompetitionScenario"; // must be in nameScenMap in the Evaluator class
	public static String experimentName = scenarioName + "Exp";
	public static String outputPath = "./experiment_results/" + experimentName; // will overwrite if directory already exist
	public static boolean visualizeResults = false; // display pareto front graph and quality indicator info when experiment finishes
	
	// IBEA algorithm settings
	public static boolean parallelizeEvaluations = false; // parallelization done with JPPF
	public static int numGenerations = 4;//20; // must be at least 2
	public static int populationSize = 20;//100; // must be at least 1
	public static double mutationProbability = 1.0; // must be between 0.0 and 1.0
	public static double crossoverProbability = 1.0; // must be between 0.0 and 1.0
	public static double bitFlipProbability = 0.1; // must be between 0.0 and 1.0
	public static double bitSwapProbability = 0.1; // must be between 0.0 and 1.0
	
	// resulting chromosomes and their objective values are put into this set
	public static ArrayList<SolutionSet> iRunResults = new ArrayList<SolutionSet>();
	
	@Override
	public void algorithmSettings(Problem problem, int problemId, Algorithm[] algorithm) throws ClassNotFoundException 
	{
		try 
		{
			 // Assume: using SOMAS version of IBEA for all algorithms
		      int numberOfAlgorithms = algorithmNameList_.length;

		      for (int i = 0; i < numberOfAlgorithms; i++)
		      {
		    	IBEA_Settings_SOMAS settings = new IBEA_Settings_SOMAS((SOMASProblem) problem);
		    	settings.maxEvaluations_ = numGenerations*populationSize;
		    	settings.populationSize_ = populationSize;
		    	settings.archiveSize_ = populationSize;
		    	settings.useJPPF_ = parallelizeEvaluations;
		    	settings.mutationProbability_ = mutationProbability;
		    	settings.crossoverProbability_ = crossoverProbability;
		    	settings.bitFlipProbability_ = bitFlipProbability;
		    	settings.bitSwapProbability_ = bitSwapProbability;
		    	if (paretoFrontFile_ != null && !paretoFrontFile_[problemId].equals(""))
			       settings.paretoFrontFile_ = paretoFrontFile_[problemId];
			      
		        algorithm[i] = settings.configure();
		      }	      
		} 
		catch(JMException e)
		{
			Logger.getLogger(SOMASExperiment.class.getName()).log(Level.SEVERE, null, e); 
		}

	}
	
	public static void main(String[] args) throws JMException, IOException 
	{
		processArgs(args);
		
		SOMASExperiment exp = new SOMASExperiment();
		
		exp.experimentName_  = experimentName;
	    exp.algorithmNameList_   = new String[] {"IBEA"};
	    exp.problemList_     = new String[] {"SOMASProblem"};
	    
	    SOMASProblem.scenarioName = scenarioName;
	    SOMASProblem.numRuns = numSimRunsPerEval;
		   
	    int numberOfAlgorithms = exp.algorithmNameList_.length;
	    exp.experimentBaseDirectory_ = outputPath;    
	    exp.algorithmSettings_ = new Settings[numberOfAlgorithms];  
	    exp.independentRuns_ = numIndependentRuns;
	    
	    System.out.println("Starting " + experimentName + " with " + scenarioName);
	    System.out.println("Number of independent runs: " + numIndependentRuns + " Number of sim runs per evaluation: " + numSimRunsPerEval);
	    System.out.println("Number of generations: " + numGenerations + " Populations size: " + populationSize);
	    System.out.println("Crossover probability: " + crossoverProbability + " Bitswap probability: " + bitSwapProbability);
	    System.out.println("Mutation probability: " + mutationProbability + " Bitflip probability: " + bitFlipProbability);
	    		
	    // Run the experiments
	    long initTime = System.currentTimeMillis();
	    exp.runExperiment() ;
	    long estimatedTime = System.currentTimeMillis() - initTime;
   
	    double estimatedHours =  ((double) estimatedTime) / (1000.0 * 60.0 * 60.0);
	    System.out.println(experimentName + " Complete!");
	    DecimalFormat df = new DecimalFormat("####.##");
	    System.out.println("Total execution time: "+ df.format(estimatedHours) + " hours");
	    System.out.println("File output directory: " + outputPath);
	    System.out.println("Variables values (chromosomes) have been writen to VAR files");
	    System.out.println("Objectives values have been writen to FUN files");	
	    
	    JPPFManager.closeClient();
	    
	    if(visualizeResults)
	    	ResultsVisualizer.visualizeResults(scenarioName, iRunResults);
	}
	
	private static void processArgs(String[] args)
	{
		try
		{
			for(int i= 0; i < args.length; i++)
			{
				if(args[i].equalsIgnoreCase("-sr")) // sim runs
					numSimRunsPerEval = Integer.parseInt(args[i+1]);
				else if(args[i].equalsIgnoreCase("-ir")) // independent runs
					numIndependentRuns = Integer.parseInt(args[i+1]);
				else if(args[i].equalsIgnoreCase("-en")) // experiment name
				{
					experimentName = args[i+1];
					outputPath = "./experiment_results/" + experimentName;
				}
				else if(args[i].equalsIgnoreCase("-op")) // output path
				{
					if(args[i+1].charAt(args[i+1].length()-1) == '/')
						outputPath = args[i+1] + experimentName;
					else
						outputPath = args[i+1] + "/" + experimentName;
				}
				else if(args[i].equalsIgnoreCase("-sn")) // scenario name
				{
					scenarioName = args[i+1];
					experimentName = scenarioName + "Exp";
					outputPath = "./experiment_results/" + experimentName;
				}	
				else if(args[i].equalsIgnoreCase("-ng")) // number of generations
					numGenerations = Integer.parseInt(args[i+1]);
				else if(args[i].equalsIgnoreCase("-ps")) // population size
					populationSize = Integer.parseInt(args[i+1]);
				else if(args[i].equalsIgnoreCase("-mp")) // mutation probability
					mutationProbability = Double.parseDouble(args[i+1]);
				else if(args[i].equalsIgnoreCase("-cp")) // crossover probability
					crossoverProbability = Double.parseDouble(args[i+1]);
				else if(args[i].equalsIgnoreCase("-sp")) // bitswap probability
					bitSwapProbability = Double.parseDouble(args[i+1]);	
				else if(args[i].equalsIgnoreCase("-fp")) // bitflip probability
					bitFlipProbability = Double.parseDouble(args[i+1]);	
				else if(args[i].equalsIgnoreCase("-pe")) // parallelize evaluations
					parallelizeEvaluations = true;
				else if(args[i].equalsIgnoreCase("-se")) // serialize evaluations
					parallelizeEvaluations = false;	
				else if(args[i].equalsIgnoreCase("-dv")) // display front
					visualizeResults = true;	
				else if(args[i].equalsIgnoreCase("-hv")) // hide front
					visualizeResults = false;
			}
		}
		catch (Exception e)
		{
			System.err.println("Error parsing command line arguments.");
			System.exit(-1);
		}
	}
	
	/**
	* Runs the experiment
	*/
	@Override
	public void runExperiment() throws JMException, IOException {
	    // Step 1: check experiment base directory
		checkExperimentDirectory();
		
		outputParetoFrontFile_ = scenarioName + ".FUN";
	    outputParetoSetFile_ = scenarioName + ".VAR";
		    
		map_.put("name", experimentName_);
		map_.put("experimentDirectory", experimentBaseDirectory_);
		map_.put("algorithmNameList", algorithmNameList_);
		map_.put("problemList", problemList_);
		map_.put("independentRuns", independentRuns_);
		map_.put("outputParetoFrontFile", outputParetoFrontFile_);
		map_.put("outputParetoSetFile", outputParetoSetFile_);
		map_.put("problemsSettings", problemsSettings_);
		
		for (int i = 0; i < problemList_.length; i++) { 
			new runExperiment_SOMAS(this, map_, i, 1, problemList_.length).run();
		}
	} // runExperiment
	
	@Override
	public void checkExperimentDirectory() {
		File experimentDirectory;

	    experimentDirectory = new File(experimentBaseDirectory_);
	    if (experimentDirectory.exists()) {
	      System.out.println("Experiment directory exists");
	      if (experimentDirectory.isDirectory()) {
	        System.out.println("Experiment directory is a directory. Deleting directory and creating new directory.");
	        deleteDirectory(experimentDirectory);
	      } else {
	        System.out.println("Experiment directory is not a directory. Deleting file and creating directory.");
	        experimentDirectory.delete();
	      }
	      new File(experimentBaseDirectory_).mkdirs();
	    } // if
	    else {
	      System.out.println("Experiment directory does NOT exist. Creating");
	      new File(experimentBaseDirectory_).mkdirs();
	    } // else
	} // checkDirectories
	
	private boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	} //deleteDirectory
}
