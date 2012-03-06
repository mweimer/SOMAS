/**
 * IntSolutionType
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * Class representing the solution type of solutions composed of Int variables 
 */
package jmetal.base.solutionType;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import jmetal.base.variable.Int;

public class IntSolutionType extends SolutionType {

	/**
	 * Constructor
	 * @param problem
	 * @throws ClassNotFoundException 
	 */
	public IntSolutionType(Problem problem) throws ClassNotFoundException {
		super(problem) ;
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this) ;

		// Initializing the types of the variables
		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			problem.variableType_[i] = Class.forName("jmetal.base.variable.Int") ;
		} // for    
	} // Constructor

	/**
	 * Creates the variables of the solution
	 * @param decisionVariables
	 */
	public Variable[] createVariables() {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];

		for (int var = 0; var < problem_.getNumberOfVariables(); var++)
			variables[var] = new Int((int)problem_.getLowerLimit(var),
					(int)problem_.getUpperLimit(var));    

		return variables ;
	} // createVariables
} // IntSolutionType
