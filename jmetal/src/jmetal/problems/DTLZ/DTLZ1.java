/**
 * DTLZ1.java
 *
 * @author Antonio J. Nebro
 * @author Juanjo Durillo
 * @version 1.0
 * Created on 16 de octubre de 2006, 17:30
 */

package jmetal.problems.DTLZ;

import jmetal.base.*;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

/** 
 * Class representing problem DTLZ1 
 */
public class DTLZ1 extends Problem {   
 /** 
  * Creates a default DTLZ1 problem (7 variables and 3 objectives)
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public DTLZ1(String solutionType) throws ClassNotFoundException {
    this(solutionType, 7, 3);
  } // DTLZ1   
    
  /** 
  * Creates a DTLZ1 problem instance
  * @param numberOfVariables Number of variables
  * @param numberOfObjectives Number of objective functions
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public DTLZ1(String solutionType, 
               Integer numberOfVariables, 
  		         Integer numberOfObjectives) throws ClassNotFoundException {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ = numberOfObjectives.intValue();
    numberOfConstraints_= 0;
    problemName_        = "DTLZ1";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    for (int var = 0; var < numberOfVariables; var++){
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } //for
        
    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
    	solutionType_ = new RealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }            
  }            
 
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */    
  public void evaluate(Solution solution) throws JMException {
    Variable[] gen  = solution.getDecisionVariables();
                
    double [] x = new double[numberOfVariables_];
    double [] f = new double[numberOfObjectives_];
    int k = numberOfVariables_ - numberOfObjectives_ + 1;
        
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = gen[i].getValue();
        
    double g = 0.0;
    for (int i = numberOfVariables_ - k; i < numberOfVariables_; i++)
      g += (x[i] - 0.5)*(x[i] - 0.5) - Math.cos(20.0 * Math.PI * ( x[i] - 0.5));
        
    g = 100 * (k + g);        
    for (int i = 0; i < numberOfObjectives_; i++)
      f[i] = (1.0 + g) * 0.5;
        
    for (int i = 0; i < numberOfObjectives_; i++){
      for (int j = 0; j < numberOfObjectives_ - (i + 1); j++)            
        f[i] *= x[j];                
        if (i != 0){
          int aux = numberOfObjectives_ - (i + 1);
          f[i] *= 1 - x[aux];
        } //if
    }//for
        
    for (int i = 0; i < numberOfObjectives_; i++)
      solution.setObjective(i,f[i]);        
  } // evaluate   
  
}

