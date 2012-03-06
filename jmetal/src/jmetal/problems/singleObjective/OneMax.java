/**
 * OneMax.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.problems.singleObjective;

import jmetal.base.*;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.BinarySolutionType;
import jmetal.base.variable.Binary;

/**
 * Class representing problem OneMax. The problem consist of maximizing the
 * number of '1's in a binary string.
 */
public class OneMax extends Problem {

  
 /**
  * Creates a new OneMax problem instance
  * @param numberOfBits Length of the problem
  */
  public OneMax(Integer numberOfBits)  throws ClassNotFoundException {
    numberOfVariables_  = 1;
    numberOfObjectives_ = 1;
    numberOfConstraints_= 0;
    problemName_        = "ONEMAX";
             
    solutionType_ = new BinarySolutionType(this) ;
    	    
    variableType_ = new Class[numberOfVariables_] ;
    length_       = new int[numberOfVariables_];
    
    variableType_[0] = Class.forName("java.base.variable.Binary") ;
    length_      [0] = numberOfBits ;
  } // OneMax
    
 /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
  */      
  public void evaluate(Solution solution) {
    Binary variable ;
    int    counter  ;
    
    variable = ((Binary)solution.getDecisionVariables()[0]) ;
    
    counter = 0 ;

    for (int i = 0; i < variable.getNumberOfBits() ; i++) 
      if (variable.bits_.get(i) == true)
        counter ++ ;

    // OneMax is a maximization problem: multiply by -1 to minimize
    solution.setObjective(0, -1.0*counter);            
  } // evaluate
} // OneMax
