package jmetal.somas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.variable.Binary;
import jmetal.util.Ranking;

public class ResultsVisualizer 
{
	public static void visualizeResults(String scenarioName, ArrayList<SolutionSet> iRunResults)
	{
		ArrayList<SolutionSet> paretoFronts = new ArrayList<SolutionSet>();
		ArrayList<String> paretoFrontNames = new ArrayList<String>();
	       
	    for(int i = 0; i < iRunResults.size(); i++)
	    {
	    	paretoFronts.add(iRunResults.get(i));
	    	paretoFrontNames.add("iRun " + Integer.toString(i));
	    }
	    
	    paretoFronts.add(getNonDominatedAggregation(iRunResults));
	    paretoFrontNames.add("Agg");
	    
	    new PFFrame(scenarioName, paretoFronts, paretoFrontNames);
	}
		
	public static void main(String[] args)
	{
		ArrayList<SolutionSet> paretoFronts = new ArrayList<SolutionSet>();
		ArrayList<String> paretoFrontNames = new ArrayList<String>();
		String scenarioName = "Unknown Scenario";
		String suffix;
		File directory;
		ArrayList<File> funFiles;
		
	    JFileChooser dirChooser = new JFileChooser(); 
	    dirChooser.setCurrentDirectory(new java.io.File("."));
	    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    dirChooser.setAcceptAllFileFilterUsed(false);
	    dirChooser.setDialogTitle("Select Folder With Pareto Front Files");
	    if (dirChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
	        directory = dirChooser.getSelectedFile();
	    else
	    	return;
		
		funFiles = getFunFiles(directory);
		
		for(File file : funFiles)
		{
			int firstDot = file.getName().indexOf('.');
			int secondDot = file.getName().lastIndexOf('.');
			
			if(firstDot > 0 || secondDot > firstDot + 1)
			{
				scenarioName = file.getName().substring(0, firstDot);
				suffix = file.getName().substring(secondDot+1);
			}
			else
			{
				System.err.println("Error: Input file " + file.getName() + "has invalid filename.");
				return;
			}

			paretoFronts.add(readFUNFile(file));
			paretoFrontNames.add("iRun " + suffix);
			String varFileName = scenarioName+"."+"VAR"+"."+suffix;
			boolean foundVarFile = readVARFile(directory.getAbsolutePath()+"\\"+varFileName, paretoFronts.get(paretoFronts.size()-1));
			if(!foundVarFile)
			{
				System.err.println("Error: Could not find corresponding VAR file for " + file.getName());
				System.err.println("Please make sure file " + varFileName + " is in the same directory");
				return;
			}
		}

		if(paretoFronts.size() > 1)
		{
			SolutionSet nonDominatedAggregation = getNonDominatedAggregation(paretoFronts);
			paretoFronts.add(nonDominatedAggregation);
			paretoFrontNames.add("Agg");
		}
		if(paretoFronts.size() > 0)
			new PFFrame(scenarioName, paretoFronts, paretoFrontNames);
	}
	
	private static SolutionSet getNonDominatedAggregation(ArrayList<SolutionSet> sets)
	{
	    SolutionSet aggregateResults = new SolutionSet();
	    for(SolutionSet solutionSet : sets)
	    	aggregateResults = aggregateResults.union(solutionSet);
	    Ranking ranking = new Ranking(aggregateResults);
	    return ranking.getSubfront(0);
	}
	
	private static boolean readVARFile(String fileName, SolutionSet solutionSet)
	{
		File f = new File(fileName);
		if(!f.exists())
			return false;
		String line = null;
		int count = 0;
		try 
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			
            while ((line = bufferedReader.readLine()) != null)
            {
            	Variable[] variables = new Variable[1];
            	Binary chromosome = new Binary();
            	chromosome.bits_ = new BitSet(line.length()-1);
            	chromosome.numberOfBits_ = line.length()-1;
                for(int i = 0; i < line.length()-1; i++)
                {
                	if(line.charAt(i) == '1')
                		chromosome.setIth(i, true);
                	else
                		chromosome.setIth(i, false);
                }
                variables[0] = chromosome;
                solutionSet.get(count).setDecisionVariables(variables);
                count++;
            }
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	private static ArrayList<File> getFunFiles(File dir)
	{
		File[] files = dir.listFiles();
		ArrayList<File> funFiles = new ArrayList<File>();
		for(File file : files)
		{
			String name = file.getName().toLowerCase();
			if(name.contains("fun") && !name.contains("agg"))
				funFiles.add(file);
		}
		
		return funFiles;
	}
	
	private static SolutionSet readFUNFile(File file) 
	{
		try 
		{
			FileInputStream fis   = new FileInputStream(file)     ;
			InputStreamReader isr = new InputStreamReader(fis)    ;
			BufferedReader br      = new BufferedReader(isr)      ;
	      
			SolutionSet solutionSet = new SolutionSet(Integer.MAX_VALUE);
	      
			String aux = br.readLine();
			while (aux!= null) 
			{
				StringTokenizer st = new StringTokenizer(aux);
		        int i = 0;
		        Solution solution = new Solution(st.countTokens());
		        while (st.hasMoreTokens()) {
			          double value = (new Double(st.nextToken())).doubleValue();
			          solution.setObjective(i,value);
			          i++;
	        }
	        solutionSet.add(solution);
	        aux = br.readLine();
	      }
	      br.close();
	      return solutionSet;
	    } 
		catch (Exception e) 
		{
		      e.printStackTrace();
	    }
	    return null;
	} 
}
	

