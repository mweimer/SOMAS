/**
 * Schaffer.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.problems;

import jmetal.base.*;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.util.JMException;

/**
 * Class representing problem Schaffer
 */
public class Schaffer extends Problem {    

 /**
  * Constructor.
  * Creates a default instance of problem Schaffer
  * @param solutionType The solution type must "Real" or "BinaryReal".s 
  */
  public Schaffer(String solutionType) throws ClassNotFoundException {
    numberOfVariables_  = 1;
    numberOfObjectives_ = 2;
    numberOfConstraints_ =0;
    problemName_         = "Schaffer";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    lowerLimit_[0] = -100000;
    upperLimit_[0] =  100000;
    
    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
    	solutionType_ = new RealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }  
  } //Schaffer

    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution solution) throws JMException {
    Variable[] variable  = solution.getDecisionVariables();
    
    double [] f = new double[numberOfObjectives_];
    f[0] = variable[0].getValue() * 
    variable[0].getValue();
    
    f[1] = (variable[0].getValue() - 2.0) * 
           (variable[0].getValue() - 2.0);
        
    solution.setObjective(0,f[0]);
    solution.setObjective(1,f[1]);
  } //evaluate    
} //Schaffer
