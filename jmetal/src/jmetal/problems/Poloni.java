/**
 * Poloni.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.problems;

import jmetal.base.*;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.util.JMException;

/**
 * Class representing problem Poloni. This problem has two objectives to be
 * MAXIMIZED. As jMetal always minimizes, the rule Max(f(x)) = -Min(f(-x)) must
 * be applied.
 */
public class Poloni extends Problem{    
    
 /**
  * Constructor.
  * Creates a default instance of the Poloni problem
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public Poloni(String solutionType)  throws ClassNotFoundException  {
    numberOfVariables_  = 2;
    numberOfObjectives_ = 2;
    numberOfConstraints_= 0;
    problemName_        = "Poloni";

    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = -1* Math.PI;
      upperLimit_[var] =  Math.PI;
    } //for

    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
    	solutionType_ = new RealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }  
 } //Poloni
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution solution) throws JMException {
    final double A1 = 0.5 * Math.sin(1.0) - 2 * Math.cos(1.0) + 
                      Math.sin(2.0) - 1.5 * Math.cos(2.0) ; //!< Constant A1
    final double A2 = 1.5 * Math.sin(1.0) - Math.cos(1.0) + 
                      2 * Math.sin(2.0) - 0.5 * Math.cos(2.0) ; //!< Constant A2
    
    Variable[] decisionVariables  = solution.getDecisionVariables();
    
    double [] x = new double[numberOfVariables_] ;
    double [] f = new double[numberOfObjectives_];
    
    x[0] = decisionVariables[0].getValue();
    x[1] = decisionVariables[1].getValue();        
    
    double B1 = 0.5 * Math.sin(x[0]) - 2 * Math.cos(x[0]) + Math.sin(x[1]) - 
                1.5 * Math.cos(x[1]) ;
    double B2 = 1.5 * Math.sin(x[0]) - Math.cos(x[0]) + 2 * Math.sin(x[1]) - 
                0.5 * Math.cos(x[1]) ;
    
    f[0] = - (1 + Math.pow(A1 - B1, 2) + Math.pow(A2 - B2, 2)) ;
    f[1] = -(Math.pow(x[0]+3,2) + Math.pow(x[1]+1,2)) ;
      
    // The two objectives to be minimized. According to Max(f(x)) = -Min(f(-x)), 
    // they must be multiplied by -1. Consequently, the obtained solutions must
    // be also multiplied by -1 
    
    solution.setObjective(0,-1 * f[0]);
    solution.setObjective(1,-1 * f[1]);
  } // evaluate
} // Poloni

