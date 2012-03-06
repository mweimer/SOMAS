/**
 * Viennet2.java
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
 * Class representing problem Viennet2
 */
public class Viennet2 extends Problem{           
  
 /** 
  * Constructor.
  * Creates a default instance of the Viennet2 problem
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public Viennet2(String solutionType) throws ClassNotFoundException {
    numberOfVariables_   = 2 ;
    numberOfObjectives_  = 3 ;
    numberOfConstraints_ = 0;
    problemName_         = "Viennet4";
        
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] =  -4.0;
      upperLimit_[var] =   4.0;
    } // for
        
    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
      solutionType_ = new RealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }  
  } //Viennet2
      
    
  /**
   * Evaluates a solution
   * @param solution The solution to evaluate
   * @throws JMException 
   */
  public void evaluate(Solution solution) throws JMException {                    
    double [] x = new double[numberOfVariables_];
    double [] f = new double[numberOfObjectives_];
        
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = solution.getDecisionVariables()[i].getValue();
        
    // First function
    f[0] = (x[0]-2)*(x[0]-2)/2.0 + (x[1]+1)*(x[1]+1)/13.0 + 3.0 ;

    // Second function
    f[1] = (x[0]+x[1]-3.0)*(x[0]+x[1]-3.0)/36.0 +
           (-x[0]+x[1]+2.0)*(-x[0]+x[1]+2.0)/8.0 - 17.0;

    // Third function
    f[2] = (x[0]+2*x[1]-1)*(x[0]+2*x[1]-1)/175.0 +
                          (2*x[1]-x[0])*(2*x[1]-x[0])/17.0 - 13.0 ;        
        
    for (int i = 0; i < numberOfObjectives_; i++)
      solution.setObjective(i,f[i]);        
  } // evaluate 
} // Viennet2


