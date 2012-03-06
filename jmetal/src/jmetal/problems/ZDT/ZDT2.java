/**
 * ZDT2.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.problems.ZDT;

import jmetal.base.*;
import jmetal.base.solutionType.ArrayRealSolutionType;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

/**
 *  Class representing problem ZDT2
 */
public class ZDT2 extends Problem{
      
 /** 
  * Constructor.
  * Creates a default instance of  problem ZDT2 (30 decision variables)
  * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
  */
  public ZDT2(String solutionType) throws ClassNotFoundException {
    this(solutionType, 30); // 30 variables by default
  } // ZDT2

  
 /** 
  * Constructor.
  * Creates a new ZDT2 problem instance.
  * @param numberOfVariables Number of variables
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public ZDT2(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  2;
    numberOfConstraints_=  0;
    problemName_        = "ZDT2";

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];

    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } //for

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
  } //ZDT2
  
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */    
  public void evaluate(Solution solution) throws JMException {
		XReal x = new XReal(solution) ;
        
    double [] f = new double[numberOfObjectives_] ;
    f[0]        = x.getValue(0)     ;
    double g    = this.evalG(x)                 ;
    double h    = this.evalH(f[0],g)              ;
    f[1]        = h * g                           ;
        
    solution.setObjective(0,f[0]);
    solution.setObjective(1,f[1]);
  } //evaluate
  
  /**
   * Returns the value of the ZDT2 function G.
   * @param decisionVariables The decision variables of the solution to 
   * evaluate.
   * @throws JMException 
   */  
  private double evalG(XReal x) throws JMException {
    double g = 0.0;        
    for (int i = 1; i < x.getNumberOfDecisionVariables();i++)
      g += x.getValue(i);
    double constante = (9.0 / (numberOfVariables_-1));
    g = constante * g;
    g = g + 1.0;
    return g;        
  } //evalG
    
  /**
   * Returns the value of the ZDT2 function H.
   * @param f First argument of the function H.
   * @param g Second argument of the function H.
   */
  public double evalH(double f, double g) {
    double h = 0.0;
    h = 1.0 - java.lang.Math.pow(f/g,2.0);
    return h;        
  } // evalH
} //ZDT2
