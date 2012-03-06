/**
 * LZ09_F4.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * Created on 17 de junio de 2006, 17:30
 */

package jmetal.problems.LZ09;

import java.util.Vector;

import jmetal.base.*;
import jmetal.base.solutionType.BinaryRealSolutionType;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

/** 
 * Class representing problem LZ09_F4 
 */
public class LZ09_F4 extends Problem {   
	LZ09 LZ09_ ; 
 /** 
  * Creates a default LZ09_F4 problem (30 variables and 2 objectives)
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public LZ09_F4(String solutionType) throws ClassNotFoundException {
    this(solutionType, 21, 1, 24);
  } // LZ09_F4
  
  /** 
   * Creates a LZ09_F4 problem instance
   * @param solutionType The solution type must "Real" or "BinaryReal". 
   */
   public LZ09_F4(String solutionType,
                  Integer ptype, 
                  Integer dtype,
                  Integer ltype) throws ClassNotFoundException {
     numberOfVariables_  = 30;
     numberOfObjectives_ = 2;
     numberOfConstraints_= 0;
     problemName_        = "LZ09_F4";
         
   	 LZ09_  = new LZ09(numberOfVariables_, 
   			               numberOfObjectives_, 
   			               ptype, 
   			               dtype, 
   			               ltype) ;

     lowerLimit_ = new double[numberOfVariables_];
     upperLimit_ = new double[numberOfVariables_];      
     lowerLimit_[0] = 0.0 ;
     upperLimit_[0] = 1.0 ;
     for (int var = 1; var < numberOfVariables_; var++){
       lowerLimit_[var] = -1.0;
       upperLimit_[var] = 1.0;
     } //for
         
     if (solutionType.compareTo("BinaryReal") == 0)
    	 solutionType_ = new BinaryRealSolutionType(this) ;
     else if (solutionType.compareTo("Real") == 0)
    	 solutionType_ = new RealSolutionType(this) ;
     else {
     	System.out.println("Error: solution type " + solutionType + " invalid") ;
     	System.exit(-1) ;
     }           
   } // LZ09_F4
   
   /** 
    * Evaluates a solution 
    * @param solution The solution to evaluate
     * @throws JMException 
    */    
    public void evaluate(Solution solution) throws JMException {
      Variable[] gen  = solution.getDecisionVariables();
      
      Vector<Double> x = new Vector<Double>(numberOfVariables_) ;
      Vector<Double> y = new Vector<Double>(numberOfObjectives_);
          
      for (int i = 0; i < numberOfVariables_; i++) {
      	x.addElement(gen[i].getValue());
      	y.addElement(0.0) ;
      } // for
        
      LZ09_.objective(x, y) ;
      
      for (int i = 0; i < numberOfObjectives_; i++)
        solution.setObjective(i, y.get(i)); 
    } // evaluate
} // LZ09_F4


