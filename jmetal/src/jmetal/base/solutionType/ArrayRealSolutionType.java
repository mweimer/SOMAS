/**
 * ArrayRealSolutionType
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * Class representing the solution type of solutions composed of an ArrayReal 
 * variable 
 */
package jmetal.base.solutionType;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import jmetal.base.variable.ArrayReal;
import jmetal.base.variable.Real;

public class ArrayRealSolutionType extends SolutionType {

	/**
	 * Constructor
	 * @param problem
	 * @throws ClassNotFoundException 
	 */
	public ArrayRealSolutionType(Problem problem) throws ClassNotFoundException {
		super(problem) ;
		problem_.variableType_ = new Class[1];
		problem_.setSolutionType(this) ;

		// Initializing the types of the variables
	  problem_.variableType_[0] = Class.forName("jmetal.base.variable.ArrayReal") ; 
	}
	
	/**
	 * Creates the variables of the solution
	 * @param decisionVariables
	 */
	public Variable[] createVariables() {
		Variable [] variables = new Variable[1];
		
    variables[0] = new ArrayReal(problem_.getNumberOfVariables(), problem_); 
    return variables ;
	} // createVariables
	
	/**
	 * Copy the variables
	 * @param decisionVariables
	 * @return An array of variables
	 */
	public Variable[] copyVariables(Variable[] vars) {
		Variable[] variables ;
		
		variables = new Variable[1];
	  variables[0] = vars[0].deepCopy();
		
		return variables ;
	} // copyVariables
} // ArrayRealSolutionType
