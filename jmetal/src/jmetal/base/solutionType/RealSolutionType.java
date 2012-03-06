/**
 * RealSolutionType
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * Class representing the solution type of solutions composed of Binary 
 * variables
 */
package jmetal.base.solutionType;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import jmetal.base.variable.Real;

public class RealSolutionType extends SolutionType {

	/**
	 * Constructor
	 * @param problem
	 * @throws ClassNotFoundException 
	 */
	public RealSolutionType(Problem problem) throws ClassNotFoundException {
		super(problem) ;
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this) ;

		// Initializing the types of the variables
		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			problem.variableType_[i] = Class.forName("jmetal.base.variable.Real") ;
		} // for    
	} // Constructor

	/**
	 * Creates the variables of the solution
	 * @param decisionVariables
	 */
	public Variable[] createVariables() {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];

		for (int var = 0; var < problem_.getNumberOfVariables(); var++)
			variables[var] = new Real(problem_.getLowerLimit(var),
					problem_.getUpperLimit(var)); 

		return variables ;
	} // createVariables
} // RealSolutionType
