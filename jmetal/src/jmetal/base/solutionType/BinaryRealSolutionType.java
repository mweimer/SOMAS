/**
 * BinaryRealSolutionType
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * Class representing the solution type of solutions composed of BinaryReal 
 * variables
 */
package jmetal.base.solutionType;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import jmetal.base.variable.BinaryReal;

public class BinaryRealSolutionType extends SolutionType {

	/**
	 * Constructor
	 * @param problem
	 * @throws ClassNotFoundException 
	 */
	public BinaryRealSolutionType(Problem problem) throws ClassNotFoundException {
		super(problem) ;
		problem_.variableType_ = new Class[problem.getNumberOfVariables()];
		problem_.setSolutionType(this) ;

		// Initializing the types of the variables
		for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
			problem_.variableType_[i] = Class.forName("jmetal.base.variable.BinaryReal") ; 
		} // for    
	} // Constructor
	
	/**
	 * Creates the variables of the solution
	 * @param decisionVariables
	 */
	public Variable[] createVariables() {
		Variable [] variables = new Variable[problem_.getNumberOfVariables()];
	  
    for (int var = 0; var < problem_.getNumberOfVariables(); var++) {
      if (problem_.getPrecision() == null) {
        int [] precision = new int[problem_.getNumberOfVariables()] ;
        for (int i = 0; i < problem_.getNumberOfVariables(); i++)
          precision[i] = jmetal.base.variable.BinaryReal.DEFAULT_PRECISION ;
        problem_.setPrecision(precision) ;
      } // if
      variables[var] = new BinaryReal(problem_.getPrecision(var),
                                      problem_.getLowerLimit(var),
                                      problem_.getUpperLimit(var));   
    } // for 
    return variables ;    
	} // createVariables
} // BinaryRealSolutionType
