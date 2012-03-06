/**
 * CellDE.java
 * @author Juan J. Durillo, Antonio J. Nebro
 * @version 1.0
 */
package jmetal.metaheuristics.cellde;

import jmetal.base.*;
import java.util.Comparator;
import jmetal.base.operator.comparator.*;
import jmetal.util.*;

/**
 * This class represents the original asynchronous MOCell algorithm
 * hybridized with Diferential evolutions (GDE3), called CellDE. It uses an 
 * archive based on spea2 fitness to store non-dominated solutions.
 */
public class CellDE extends Algorithm{

  /**
   * Stores the problem to solve
   */
  private Problem problem_;

  /** 
   * Constructor
   * @param problem Problem to solve
   */
  public CellDE(Problem problem){
    problem_ = problem;
  } // CellDE


  /**   
   * Runs of the CellDE algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution  
   * @throws JMException 
   * @throws ClassNotFoundException 
   */ 
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize, archiveSize, maxEvaluations, evaluations, feedBack;
    Operator crossoverOperator, selectionOperator;
    SolutionSet currentSolutionSet;
    SolutionSet archive;
    SolutionSet [] neighbors;    
    Neighborhood neighborhood;
    Comparator dominance = new DominanceComparator(),
    crowding  = new CrowdingComparator();  
    Distance distance = new Distance();

    //Read the params
    populationSize    = ((Integer)getInputParameter("populationSize")).intValue();
    archiveSize       = ((Integer)getInputParameter("archiveSize")).intValue();
    maxEvaluations    = ((Integer)getInputParameter("maxEvaluations")).intValue();                
    feedBack          = ((Integer)getInputParameter("feedBack")).intValue();

    //Read the operators
    crossoverOperator = operators_.get("crossover");
    selectionOperator = operators_.get("selection");        

    //Initialize the variables    
    currentSolutionSet  = new SolutionSet(populationSize);                       
    archive            = new jmetal.util.archive.StrengthRawFitnessArchive(archiveSize);
    evaluations        = 0;                        
    neighborhood       = new Neighborhood(populationSize);
    neighbors          = new SolutionSet[populationSize];

    //Create the initial population
    for (int i = 0; i < populationSize; i++){
      Solution solution = new Solution(problem_);
      problem_.evaluate(solution);           
      problem_.evaluateConstraints(solution);
      currentSolutionSet.add(solution);
      solution.setLocation(i);
      evaluations++;
    }         

    while (evaluations < maxEvaluations){       
      for (int ind = 0; ind < currentSolutionSet.size(); ind++){
        Solution individual = new Solution(currentSolutionSet.get(ind));

        Solution [] parents = new Solution[3];
        Solution offSpring;

        neighbors[ind] = neighborhood.getEightNeighbors(currentSolutionSet,ind);   
        
        //parents
        parents[0] = (Solution)selectionOperator.execute(neighbors[ind]);
        parents[1] = (Solution)selectionOperator.execute(neighbors[ind]);
        parents[2] = individual ;

        //Create a new solution, using genetic operators mutation and crossover
        offSpring = (Solution)crossoverOperator.execute(new Object[]{individual, parents});               
        
        //->Evaluate offspring and constraints
        problem_.evaluate(offSpring);
        problem_.evaluateConstraints(offSpring);
        evaluations++;

        int flag = dominance.compare(individual,offSpring);

        if (flag == 1){ //The offSpring dominates
          offSpring.setLocation(individual.getLocation());                                      
          //currentSolutionSet.reemplace(offSpring[0].getLocation(),offSpring[0]);
          currentSolutionSet.replace(ind,new Solution(offSpring));
          //newSolutionSet.add(offSpring);
          archive.add(new Solution(offSpring));                   
        } else if (flag == 0) { //Both two are non-dominates
          neighbors[ind].add(offSpring);
          //(new Spea2Fitness(neighbors[ind])).fitnessAssign();                   
          //neighbors[ind].sort(new FitnessAndCrowdingDistanceComparator()); //Create a new comparator;
          Ranking rank = new Ranking(neighbors[ind]);
          for (int j = 0; j < rank.getNumberOfSubfronts(); j++){
            distance.crowdingDistanceAssignment(rank.getSubfront(j),problem_.getNumberOfObjectives());
          }

          boolean deleteMutant = true;          
          int compareResult = crowding.compare(individual,offSpring);
          if (compareResult == 1) {//The offSpring[0] is better
            deleteMutant = false;
          }

          if (!deleteMutant){
            offSpring.setLocation(individual.getLocation());
            //currentSolutionSet.reemplace(offSpring[0].getLocation(),offSpring[0]);
            //newSolutionSet.add(offSpring);
            currentSolutionSet.replace(offSpring.getLocation(), offSpring);
            archive.add(new Solution(offSpring));
          }else{
            //newSolutionSet.add(new Solution(currentSolutionSet.get(ind)));
            archive.add(new Solution(offSpring));    
          }
        }                              
      }             
      
      //Store a portion of the archive into the population
      for (int j = 0; j < feedBack; j++){
        if (archive.size() > j){
          int r = PseudoRandom.randInt(0,currentSolutionSet.size()-1);
          if (r < currentSolutionSet.size()){
            Solution individual = archive.get(j);
            individual.setLocation(r);            
            currentSolutionSet.replace(r,new Solution(individual));
          }
        }
      }           
    }
    return archive;
  } // execute        
} // CellDE