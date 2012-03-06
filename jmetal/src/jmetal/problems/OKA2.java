/**
 * OKA2.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.problems;

import jmetal.base.*;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

/**
 * Class representing problem Kursawe
 */
public class OKA2 extends Problem {  
   
  
  /** 
   * Constructor.
   * Creates a new instance of the OKA2 problem.
   * @param solutionType The solution type must "Real" or "BinaryReal".
   */
  public OKA2(String solutionType) throws ClassNotFoundException {
    numberOfVariables_   = 3  ;
    numberOfObjectives_  = 2  ;
    numberOfConstraints_ = 0  ;
    problemName_         = "OKA2"                    ;
        
    upperLimit_ = new double[numberOfVariables_] ;
    lowerLimit_ = new double[numberOfVariables_] ;
       
    lowerLimit_[0] = -Math.PI ;
    upperLimit_[0] = Math.PI  ;    
    for (int i = 1; i < numberOfVariables_; i++) {
      lowerLimit_[i] = -5.0 ;
      upperLimit_[i] = 5.0  ;
    } // for
        
    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
    	solutionType_ = new RealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }  
  } // OKA2
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution solution) throws JMException {
    Variable[] decisionVariables  = solution.getDecisionVariables();
        
    double [] fx   = new double[numberOfObjectives_] ; // 2 functions
    double [] x    = new double[numberOfVariables_]  ; // 3 variables
   
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = decisionVariables[i].getValue() ;
    
    fx[0] = x[0] ; 
    
    fx[1] = 1 -Math.pow((x[0]+Math.PI),2)/(4*Math.pow(Math.PI,2)) + 
               Math.pow(Math.abs(x[1] - 5*Math.cos(x[0])),1.0/3.0)+ 
               Math.pow(Math.abs(x[2] - 5*Math.sin(x[0])),1.0/3.0);
        
    solution.setObjective(0, fx[0]);
    solution.setObjective(1, fx[1]);
  } // evaluate
} // OKA2
