/**
 * ZDT6.java
 *
 * @author Antonio J. Nebro
 * @author Juanjo Durillo
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
 * Class representing problem ZDT6
 */
public class ZDT6 extends Problem {
    
 /**
  * Creates a default instance of problem ZDT6 (10 decision variables)
  * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
  */
  public ZDT6(String solutionType) throws ClassNotFoundException {
    this(solutionType, 10); // 10 variables by default
  } // ZDT6
  
 /**
  * Creates a instance of problem ZDT6
  * @param numberOfVariables Number of variables
  * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
  */
  public ZDT6(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ = 2;
    numberOfConstraints_= 0;
    problemName_        = "ZDT6";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];
        
    for (int var = 0; var < numberOfVariables_; var++) {
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
  } //ZDT6
  
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */    
  public void evaluate(Solution solution) throws JMException {
		XReal x = new XReal(solution) ;
        
    double x1   = x.getValue(0)       ;
    double [] f = new double[numberOfObjectives_]   ;
    f[0]        = 1.0 - Math.exp((-4.0)*x1) * Math.pow(Math.sin(6.0*Math.PI*x1),6.0);
    double g    = this.evalG(x)                   ;
    double h    = this.evalH(f[0],g)                ;
    f[1]        = h * g                             ;
    
    solution.setObjective(0,f[0]);
    solution.setObjective(1,f[1]);    
  } //evaluate
    
  /**
  * Returns the value of the ZDT6 function G.
  * @param decisionVariables The decision variables of the solution to 
  * evaluate.
   * @throws JMException 
  */
  public double evalG(XReal x) throws JMException{
    double g = 0.0;
    for (int var = 1; var < this.numberOfVariables_; var++)
      g += x.getValue(var);
    g = g / (numberOfVariables_ - 1);
    g = java.lang.Math.pow(g,0.25);
    g = 9.0 * g;
    g = 1.0 + g;        
    return g;
  } // evalG
  
  /**
  * Returns the value of the ZDT6 function H.
  * @param f First argument of the function H.
  * @param g Second argument of the function H.
  */
  public double evalH(double f, double g){
    return 1.0 - Math.pow((f/g),2.0);
  } // evalH       
} //ZDT6
