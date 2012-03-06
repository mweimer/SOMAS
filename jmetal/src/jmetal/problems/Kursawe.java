/**
 * Kursawe.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 2.0
 */
package jmetal.problems;

import jmetal.base.*;
import jmetal.base.solutionType.ArrayRealSolutionType;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;
import jmetal.util.wrapper.XReal;

/**
 * Class representing problem Kursawe
 */
public class Kursawe extends Problem {  
    
  /** 
   * Constructor.
   * Creates a default instance of the Kursawe problem.
   * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
   */
  public Kursawe(String solutionType) throws ClassNotFoundException {
    this(solutionType, 3);
  } // Kursawe
  
  /** 
   * Constructor.
   * Creates a new instance of the Kursawe problem.
   * @param numberOfVariables Number of variables of the problem 
   * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
   */
  public Kursawe(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
    numberOfVariables_   = numberOfVariables.intValue() ;
    numberOfObjectives_  = 2                            ;
    numberOfConstraints_ = 0                            ;
    problemName_         = "Kursawe"                    ;
        
    upperLimit_ = new double[numberOfVariables_] ;
    lowerLimit_ = new double[numberOfVariables_] ;
       
    for (int i = 0; i < numberOfVariables_; i++) {
      lowerLimit_[i] = -5.0 ;
      upperLimit_[i] = 5.0  ;
    } // for
        
    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
    	solutionType_ = new RealSolutionType(this) ;
    else if (solutionType.compareTo("ArrayReal") == 0)
    	solutionType_ = new ArrayRealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }
  } // Kursawe
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution solution) throws JMException {
		XReal x = new XReal(solution) ;
        
    double aux, xi, xj           ; // auxiliar variables
    double [] fx = new double[2] ; // function values     
   
    fx[0] = 0.0 ; 
    for (int var = 0; var < numberOfVariables_ - 1; var++) {        
      xi = x.getValue(var)   * 
           x.getValue(var);
      xj = x.getValue(var+1) * 
           x.getValue(var+1) ;
      aux = (-0.2) * Math.sqrt(xi + xj);
      fx[0] += (-10.0) * Math.exp(aux);
    } // for
        
    fx[1] = 0.0;
        
    for (int var = 0; var < numberOfVariables_ ; var++) {
      fx[1] += Math.pow(Math.abs(x.getValue(var)), 0.8) + 
           5.0 * Math.sin(Math.pow(x.getValue(var), 3.0));
    } // for
        
    solution.setObjective(0, fx[0]);
    solution.setObjective(1, fx[1]);
  } // evaluate
} // Kursawe
