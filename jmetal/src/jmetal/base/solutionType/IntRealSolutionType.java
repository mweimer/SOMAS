/**
 * IntRealSolutionType
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * Class representing the solution type of solutions composed of IntReal 
 * variables
 */
package jmetal.base.solutionType;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import jmetal.base.variable.Int;
import jmetal.base.variable.Real;
import jmetal.util.Configuration;

public class IntRealSolutionType extends SolutionType {

	private int intVariables_ ;
	private int realVariables_ ;

	/**
	 * Constructor
	 * @param problem
	 * @param intVariables Number of integer variables
	 * @param realVariables Number of real variables
	 * @throws ClassNotFoundException 
	 */
	public IntRealSolutionType(Problem problem, int intVariables, int realVariables) throws ClassNotFoundException {
		super(problem) ;
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		intVariables_ = intVariables ;
		realVariables_ = realVariables ;

		problem.setSolutionType(this) ;

		// Initializing the types of the variables
		for (int i = 0; i < intVariables_; i++) 
			problem.variableType_[i] = Class.forName("jmetal.base.variable.Int") ; 

		for (int i = intVariables_; i < (intVariables_ + realVariables_); i++) {
			problem.variableType_[i] = Class.forName("jmetal.base.variable.Real") ; 
		} // for    
	} // Constructor

	/**
	 * Creates the variables of the solution
	 * @param decisionVariables
	 * @throws ClassNotFoundException 
	 */
	public Variable[] createVariables() throws ClassNotFoundException {
		Variable [] variables = new Variable[problem_.getNumberOfVariables()];

		for (int var = 0; var < problem_.getNumberOfVariables(); var++)
			if (problem_.variableType_[var] == Class.forName("jmetal.base.variable.Int"))
				variables[var] = new Int((int)problem_.getLowerLimit(var),
						(int)problem_.getUpperLimit(var)); 
			else if (problem_.variableType_[var] == Class.forName("jmetal.base.variable.Real"))
				variables[var] = new Real(problem_.getLowerLimit(var),
						problem_.getUpperLimit(var));  
			else {
				Configuration.logger_.severe("DecisionVariables.DecisionVariables: " +
				"error creating a Solution of type IntReal") ;
			} // else
		return variables ;
	} // createVariables
} // IntRealSolutionType
