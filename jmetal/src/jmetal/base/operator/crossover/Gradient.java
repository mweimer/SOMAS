/**
 * Gradiente.java
 * Class representing an initial approximation to the gradient
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.base.operator.crossover;

import java.util.Properties;
import jmetal.base.*;    
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;


public class Gradient extends Crossover {
    
  /**
   * REAL_SOLUTION represents class jmetal.base.solutionType.RealSolutionType
   */
  private static Class REAL_SOLUTION ; 
  
  /** 
   * Constructor
   * Create a new Gradient operator    */
  public Gradient() {
    try {
    	REAL_SOLUTION = Class.forName("jmetal.base.solutionType.RealSolutionType") ;
    } catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    }
  } // Gradient


  /**
   * Constructor
   * Create a new Gradient operator    */
  public Gradient(Properties properties) {
    this();
  } // Gradiente
  
  /**
   * Perform the crossover operation. 
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containing the two offsprings
   */
  public Solution[] doCrossover(Solution parent1, 
                                Solution parent2) throws JMException {
    
	double [][] gradiente = new double[parent1.numberOfObjectives()][parent1.numberOfVariables()];
	
	for (int i = 0; i < parent1.numberOfObjectives(); i++) {
	  double f1, f2, deltaf;
	  f1 = parent1.getObjective(i);
	  f2 = parent2.getObjective(i);
	  deltaf = f1 - f2;
	  for (int j = 0; j < parent1.numberOfVariables(); j++) {
		double x1, x2, deltax;
		x1 = parent1.getDecisionVariables()[j].getValue();
		x2 = parent2.getDecisionVariables()[j].getValue();
		
		deltax = x1 - x2;
		
		if (deltax == 0)
			deltax = Double.MIN_VALUE;
		
		
	    gradiente[i][j] = deltaf / deltax; 	  
	  }
	}
	
	// We use the gradient to update the position of the solutions
	double [] direccion = new double[parent1.numberOfVariables()];
	for (int i = 0; i < direccion.length; i++) 
		direccion[i] = 0.0;
	
	for (int i = 0; i < parent1.numberOfVariables(); i++) {
		for (int j = 0; j < parent1.numberOfObjectives();j++) {
			direccion[i] = gradiente[j][i];
		}
	}
	
    Solution [] offSpring = new Solution[2];   
    offSpring[0] = new Solution(parent1);
    offSpring[1] = new Solution(parent2);
         
    for (int j = 0; j < offSpring.length; j++) {
	    for (int i = 0; i < parent1.numberOfVariables(); i++) {
	       double newValue = offSpring[j].getDecisionVariables()[i].getValue() - 
	                     direccion[i];
	       if (newValue > parent1.getDecisionVariables()[i].getUpperBound())
	    	   newValue = parent1.getDecisionVariables()[i].getUpperBound();
	       if (newValue < parent1.getDecisionVariables()[i].getLowerBound())
	    	   newValue = parent1.getDecisionVariables()[i].getLowerBound();
	       
	       offSpring[j].getDecisionVariables()[i].setValue(newValue);
	    }
    }
    
     return offSpring;                                                                                      
  } // doCrossover
  
  
  /**
  * Executes the operation
  * @param object An object containing an array of two parents
  * @return An object containing the offSprings
  */
  public Object execute(Object object) throws JMException {
    Solution [] parents = (Solution [])object;

    if ((parents[0].getType().getClass() != REAL_SOLUTION) ||
        (parents[1].getType().getClass() != REAL_SOLUTION)) {

      Configuration.logger_.severe("SBXCrossover.execute: the solutions " +
          "are not of the right type. The type should be 'Real', but " +
          parents[0].getType() + " and " + 
          parents[1].getType() + " are obtained");

      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;
    } // if 
    
    Double probability = (Double)getParameter("probability");
    if (parents.length < 2)
    {
      Configuration.logger_.severe("SBXCrossover.execute: operator needs two " +
          "parents");
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;      
    }
   
    
    Solution [] offSpring;
    offSpring = doCrossover(
                            parents[0],
                            parents[1]);
        
        
    for (int i = 0; i < offSpring.length; i++)
    {
      offSpring[i].setCrowdingDistance(0.0);
      offSpring[i].setRank(0);
    } 
    return offSpring;//*/
  } // execute 
} // SBXCrossover
