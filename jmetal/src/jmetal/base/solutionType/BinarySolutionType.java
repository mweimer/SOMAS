/**
 * BinarySolutionType
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * Class representing the solution type of solutions composed of Binary 
 * variables
 */
package jmetal.base.solutionType;

import java.io.Serializable;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import jmetal.base.variable.Binary;

public class BinarySolutionType extends SolutionType {

	/**
	 * Constructor
	 * @param problem
	 * @throws ClassNotFoundException 
	 */
	public BinarySolutionType(Problem problem) throws ClassNotFoundException {
		super(problem) ;
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this) ;

		// Initializing the types of the variables
		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			problem.variableType_[i] = Class.forName("jmetal.base.variable.Binary") ; 
		} // for    
	} // Constructor
	
	/**
	 * Creates the variables of the solution
	 * @param decisionVariables
	 */
	public Variable[] createVariables() {
		Variable[]  variables = new Variable[problem_.getNumberOfVariables()];
		
    for (int var = 0; var < problem_.getNumberOfVariables(); var++)
    	variables[var] = new Binary(problem_.getLength(var)); 
    
    return variables ;
	} // createVariables
} // BinarySolutionType
