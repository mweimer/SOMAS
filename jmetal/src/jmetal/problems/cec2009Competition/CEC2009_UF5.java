/**
 * CEC2009_UF5.java
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
 * Class representing problem CEC2009_UF5
 */
public class CEC2009_UF5 extends Problem {
  int    N_       ;
  double epsilon_ ;
 /** 
  * Constructor.
  * Creates a default instance of problem CEC2009_UF5 (30 decision variables)
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF5(String solutionType) throws ClassNotFoundException {
    this(solutionType, 30, 10, 0.1); // 30 variables, N =10, epsilon = 0.1
  } // CEC2009_UF1
  
 /**
  * Creates a new instance of problem CEC2009_UF5.
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF5(String solutionType, Integer numberOfVariables, int N, double epsilon) throws ClassNotFoundException {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  2;
    numberOfConstraints_=  0;
    problemName_        = "CEC2009_UF5";
    
    N_       = N       ;
    epsilon_ = epsilon ;

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];

    // Establishes upper and lower limits for the variables
    for (int var = 0; var < numberOfVariables_; var++)
    {
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
  } // CEC2009_UF5
    
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

  	int count1, count2;
		double sum1, sum2, yj, hj ;
		sum1   = sum2   = 0.0;
		count1 = count2 = 0;
    
    for (int j = 2 ; j <= numberOfVariables_; j++) {
			yj = x[j-1]-Math.sin(6.0*Math.PI*x[0]+j*Math.PI/numberOfVariables_);
			hj = 2.0*yj*yj - Math.cos(4.0*Math.PI*yj) + 1.0;
			if (j % 2 == 0) {
				sum2  += hj;
				count2++;
			} else {
				sum1  += hj;
				count1++;
			}
    }
    hj = (0.5/N_ + epsilon_)*Math.abs(Math.sin(2.0*N_*Math.PI*x[0]));

    solution.setObjective(0, x[0] + hj + 2.0*sum1 / (double)count1);
    solution.setObjective(1, 1.0 - x[0] + hj + 2.0*sum2 / (double)count2);
  } // evaluate
} // CEC2009_UF5
