/**
 * CEC2009_UF9.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.problems.cec2009Competition;

import jmetal.base.*;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.util.JMException;

/**
 * Class representing problem CEC2009_UF9
 */
public class CEC2009_UF9 extends Problem {
  double epsilon_ ;
  
 /** 
  * Constructor.
  * Creates a default instance of problem CEC2009_UF9 (30 decision variables)
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF9(String solutionType) throws ClassNotFoundException{
    this(solutionType, 30, 0.1); // 30 variables by default, epsilon = 0.1
  } // CEC2009_UF9
  
 /**
  * Creates a new instance of problem CEC2009_UF9.
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF9(String solutionType, Integer numberOfVariables, double epsilon) throws ClassNotFoundException {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  3;
    numberOfConstraints_=  0;
    problemName_        = "CEC2009_UF9";

    epsilon_ = epsilon ;
    
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];

    // Establishes upper and lower limits for the variables
    for (int var = 0; var < numberOfVariables_; var++) {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } // for

    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
    	solutionType_ = new RealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }  
  } // CEC2009_UF9
    
  /** 
   * Evaluates a solution.
   * @param solution The solution to evaluate.
   * @throws JMException 
   */
  public void evaluate(Solution solution) throws JMException {
    Variable[] decisionVariables  = solution.getDecisionVariables();
    
    double [] x = new double[numberOfVariables_] ;
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = decisionVariables[i].getValue() ;

  	int count1, count2, count3;
		double sum1, sum2, sum3, yj;
		sum1   = sum2 = sum3 = 0.0;
		count1 = count2 = count3 = 0;
    
    for (int j = 3 ; j <= numberOfVariables_; j++) {
			yj = x[j-1] - 2.0*x[1]*Math.sin(2.0*Math.PI*x[0]+j*Math.PI/numberOfVariables_);
			if(j % 3 == 1) {
				sum1  += yj*yj;
				count1++;
			} else if(j % 3 == 2) {
				sum2  += yj*yj;
				count2++;
			} else {
				sum3  += yj*yj;
				count3++;
			}
    }
    
    yj = (1.0+epsilon_)*(1.0-4.0*(2.0*x[0]-1.0)*(2.0*x[0]-1.0));
		if (yj < 0.0) 
      yj = 0.0;
        
    solution.setObjective(0, 0.5*(yj + 2*x[0])*x[1]		+ 2.0*sum1 / (double)count1);
    solution.setObjective(1, 0.5*(yj - 2*x[0] + 2.0)*x[1] + 2.0*sum2 / (double)count2);
    solution.setObjective(2, 1.0 - x[1]                   + 2.0*sum3 / (double)count3) ;
  } // evaluate
} // CEC2009_UF9
