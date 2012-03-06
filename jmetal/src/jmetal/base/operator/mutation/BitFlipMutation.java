/**
 * BitFlipMutation.java
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.1
 */
package jmetal.base.operator.mutation;

import java.util.Properties;
import jmetal.base.Solution;
import jmetal.base.variable.*;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.base.operator.mutation.Mutation;

/**
 * This class implements a bit flip mutation operator.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single variable.
 */
public class BitFlipMutation extends Mutation {
	/**
	 * BINARY_SOLUTION represents class jmetal.base.solutionType.RealSolutionType
	 */
	private static Class BINARY_SOLUTION ; 
	/**
	 * BINARY_REAL_SOLUTION represents class jmetal.base.solutionType.BinaryRealSolutionType
	 */
	private static Class BINARY_REAL_SOLUTION ; 
	/**
	 * INT_SOLUTION represents class jmetal.base.solutionType.IntSolutionType
	 */
	private static Class INT_SOLUTION ; 

	/**
	 * Constructor
	 * Creates a new instance of the Bit Flip mutation operator
	 */
	public BitFlipMutation() {
		try {
			BINARY_SOLUTION = Class.forName("jmetal.base.solutionType.BinarySolutionType") ;
			BINARY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.BinaryRealSolutionType") ;
			INT_SOLUTION = Class.forName("jmetal.base.solutionType.IntSolutionType") ;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // catch
	} // BitFlipMutation


	/**
	 * Constructor
	 * Creates a new instance of the Bit Flip mutation operator
	 */
	public BitFlipMutation(Properties properties) {
		this();
	} // BitFlipMutation

	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability, Solution solution) throws JMException {
		try {
			if ((solution.getType().getClass() == BINARY_SOLUTION) ||
					(solution.getType().getClass() == BINARY_REAL_SOLUTION)) {
				for (int i = 0; i < solution.getDecisionVariables().length; i++) {
					for (int j = 0; j < ((Binary) solution.getDecisionVariables()[i]).getNumberOfBits(); j++) {
						if (PseudoRandom.randDouble() < probability) {
							((Binary) solution.getDecisionVariables()[i]).bits_.flip(j);
						}
					}
				}

				for (int i = 0; i < solution.getDecisionVariables().length; i++) {
					((Binary) solution.getDecisionVariables()[i]).decode();
				}
			} // if
			else { // Integer representation
				for (int i = 0; i < solution.getDecisionVariables().length; i++)
					if (PseudoRandom.randDouble() < probability) {
						int value = (int) (PseudoRandom.randInt(
								(int)solution.getDecisionVariables()[i].getUpperBound(),
								(int)solution.getDecisionVariables()[i].getLowerBound()));
						solution.getDecisionVariables()[i].setValue(value);
					} // if
			} // else
		} catch (ClassCastException e1) {
			Configuration.logger_.severe("BitFlipMutation.doMutation: " +
					"ClassCastException error" + e1.getMessage());
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".doMutation()");
		}
	} // doMutation

	/**
	 * Executes the operation
	 * @param object An object containing a solution to mutate
	 * @return An object containing the mutated solution
	 * @throws JMException 
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;

		if ((solution.getType().getClass() != BINARY_SOLUTION) &&
				(solution.getType().getClass() != BINARY_REAL_SOLUTION) &&
				(solution.getType().getClass() != INT_SOLUTION)) {
			Configuration.logger_.severe("BitFlipMutation.execute: the solution " +
					"is not of the right type. The type should be 'Binary', " +
					"'BinaryReal' or 'Int', but " + solution.getType() + " is obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if 

		Double probability = (Double) getParameter("probability");
		if (probability == null) {
			Configuration.logger_.severe("BitFlipMutation.execute: probability not " +
			"specified");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		}

		doMutation(probability.doubleValue(), solution);
		return solution;
	} // execute
} // BitFlipMutation
