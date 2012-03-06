/**
 * XReal.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * Wrapper for accessing real-coded solutions
 */
package jmetal.util.wrapper;

import jmetal.base.Solution;
import jmetal.base.SolutionType;
import jmetal.base.variable.ArrayReal;
import jmetal.util.Configuration;
import jmetal.util.JMException;

public class XReal {
	Solution solution_ ;
	SolutionType type_ ;

	private static Class<?> REAL_SOLUTION ; 
	private static Class<?> BINARY_REAL_SOLUTION ; 
	private static Class<?> ARRAY_REAL_SOLUTION ; 

	/**
	 * Constructor
	 */
	public XReal() {
		try {
			REAL_SOLUTION = Class.forName("jmetal.base.solutionType.RealSolutionType") ;
			BINARY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.BinaryRealSolutionType") ;
			ARRAY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.ArrayRealSolutionType") ;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // Constructor

	/**
	 * Constructor
	 * @param solution
	 */
	public XReal(Solution solution) {
		this() ;
		type_ = solution.getType() ;
		solution_ = solution ;
	}

	/**
	 * Gets value of a variable
	 * @param index Index of the variable
	 * @return The value of the variable
	 * @throws JMException
	 */
	public double getValue(int index) throws JMException {
		if ((type_.getClass() == REAL_SOLUTION) ||
				(type_.getClass() == BINARY_REAL_SOLUTION)){
			return solution_.getDecisionVariables()[index].getValue() ;			
		} 
		else if (type_.getClass() == ARRAY_REAL_SOLUTION) {
			return ((ArrayReal)(solution_.getDecisionVariables()[0])).array_[index] ;
		}
		else {
			Configuration.logger_.severe("jmetal.util.wrapper.XReal.getValue, solution type " +
					type_ + "+ invalid") ;		
		}
		return 0.0 ;
	}

	/**
	 * Sets the value of a variable
	 * @param index Index of the variable
	 * @param value Value to be assigned
	 * @throws JMException
	 */
	public void setValue(int index, double value) throws JMException {
		if (type_.getClass() == REAL_SOLUTION)
			solution_.getDecisionVariables()[index].setValue(value) ;
		else if (type_.getClass() == ARRAY_REAL_SOLUTION)
			((ArrayReal)(solution_.getDecisionVariables()[0])).array_[index]=value ;
		else
			Configuration.logger_.severe("jmetal.util.wrapper.XReal.setValue, solution type " +
					type_ + "+ invalid") ;		
	} // setValue	

	/**
	 * Gets the lower bound of a variable
	 * @param index Index of the variable
	 * @return The lower bound of the variable
	 * @throws JMException
	 */
	public double getLowerBound(int index) throws JMException {
		if ((type_.getClass() == REAL_SOLUTION) ||
				(type_.getClass() == BINARY_REAL_SOLUTION))
			return solution_.getDecisionVariables()[index].getLowerBound() ;
		else if (type_.getClass() == ARRAY_REAL_SOLUTION) 
			return ((ArrayReal)(solution_.getDecisionVariables()[0])).getLowerBound(index) ;
		else {
			Configuration.logger_.severe("jmetal.util.wrapper.XReal.getLowerBound, solution type " +
					type_ + "+ invalid") ;		

		}
		return 0.0 ;
	} // getLowerBound

	/**
	 * Gets the upper bound of a variable
	 * @param index Index of the variable
	 * @return The upper bound of the variable
	 * @throws JMException
	 */
	public double getUpperBound(int index) throws JMException {
		if ((type_.getClass() == REAL_SOLUTION) ||
				(type_.getClass() == BINARY_REAL_SOLUTION))			
			return solution_.getDecisionVariables()[index].getUpperBound() ;
		else if (type_.getClass() == ARRAY_REAL_SOLUTION) 
			return ((ArrayReal)(solution_.getDecisionVariables()[0])).getUpperBound(index) ;
		else
			Configuration.logger_.severe("jmetal.util.wrapper.XReal.getUpperBound, solution type " +
					type_ + "+ invalid") ;		

		return 0.0 ;
	} // getUpperBound

	/**
	 * Returns the number of variables of the solution
	 * @return
	 */
	public int getNumberOfDecisionVariables() {
		if ((type_.getClass() == REAL_SOLUTION) ||
				(type_.getClass() == BINARY_REAL_SOLUTION))		
			return solution_.getDecisionVariables().length ;
		else if (type_.getClass() == ARRAY_REAL_SOLUTION) 
			return ((ArrayReal)(solution_.getDecisionVariables()[0])).getLength() ;
		else
			Configuration.logger_.severe("jmetal.util.wrapper.XReal.size, solution type " +
					type_ + "+ invalid") ;		
		return 0 ;
	} // size
} // XReal