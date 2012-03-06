/**
 * XInt.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * Wrapper for accessing integer-coded solutions
 */
package jmetal.util.wrapper;

import jmetal.base.Solution;
import jmetal.base.SolutionType;
import jmetal.base.variable.ArrayInt;
import jmetal.util.Configuration;
import jmetal.util.JMException;

public class XInt {
	Solution solution_ ;
	SolutionType type_ ;

	private static Class<?> INT_SOLUTION ; 
	private static Class<?> ARRAY_INT_SOLUTION ; 

	/**
	 * Constructor
	 */
	public XInt() {
		try {
			INT_SOLUTION = Class.forName("jmetal.base.solutionType.IntSolutionType") ;
			ARRAY_INT_SOLUTION = Class.forName("jmetal.base.solutionType.ArrayIntSolutionType") ;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // Constructor

	/**
	 * Constructor
	 * @param solution
	 */
	public XInt(Solution solution) {
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
	public int getValue(int index) throws JMException {
		if (type_.getClass() == INT_SOLUTION){
			return (int)solution_.getDecisionVariables()[index].getValue() ;			
		} 
		else if (type_.getClass() == ARRAY_INT_SOLUTION) {
			return ((ArrayInt)(solution_.getDecisionVariables()[0])).array_[index] ;
		}
		else {
			Configuration.logger_.severe("jmetal.util.wrapper.XInt.getValue, solution type " +
					type_ + "+ invalid") ;		
		}
		return 0;
	} // Get value

	/**
	 * Sets the value of a variable
	 * @param index Index of the variable
	 * @param value Value to be assigned
	 * @throws JMException
	 */
	public void setValue(int index, int value) throws JMException {
		if (type_.getClass() == INT_SOLUTION)
			solution_.getDecisionVariables()[index].setValue(value) ;
		else if (type_.getClass() == ARRAY_INT_SOLUTION)
			((ArrayInt)(solution_.getDecisionVariables()[0])).array_[index]=value ;
		else
			Configuration.logger_.severe("jmetal.util.wrapper.XInt.setValue, solution type " +
					type_ + "+ invalid") ;		
	} // setValue	

	/**
	 * Gets the lower bound of a variable
	 * @param index Index of the variable
	 * @return The lower bound of the variable
	 * @throws JMException
	 */
	public int getLowerBound(int index) throws JMException {
		if (type_.getClass() == INT_SOLUTION)
			return (int)solution_.getDecisionVariables()[index].getLowerBound() ;
		else if (type_.getClass() == ARRAY_INT_SOLUTION) 
			return (int)((ArrayInt)(solution_.getDecisionVariables()[0])).getLowerBound(index) ;
		else {
			Configuration.logger_.severe("jmetal.util.wrapper.Xreal.getLowerBound, solution type " +
					type_ + "+ invalid") ;		
		}
		return 0 ;
	} // getLowerBound

	/**
	 * Gets the upper bound of a variable
	 * @param index Index of the variable
	 * @return The upper bound of the variable
	 * @throws JMException
	 */
	public int getUpperBound(int index) throws JMException {
		if (type_.getClass() == INT_SOLUTION)		
			return (int)solution_.getDecisionVariables()[index].getUpperBound() ;
		else if (type_.getClass() == ARRAY_INT_SOLUTION) 
			return (int)((ArrayInt)(solution_.getDecisionVariables()[0])).getUpperBound(index) ;
		else
			Configuration.logger_.severe("jmetal.util.wrapper.Xreal.getUpperBound, solution type " +
					type_ + "+ invalid") ;		

		return 0 ;
	} // getUpperBound

	/**
	 * Returns the number of variables of the solution
	 * @return
	 */
	public int getNumberOfDecisionVariables() {
		if (type_.getClass() == INT_SOLUTION)		
			return solution_.getDecisionVariables().length ;
		else if (type_.getClass() == ARRAY_INT_SOLUTION) 
			return ((ArrayInt)(solution_.getDecisionVariables()[0])).getLength() ;
		else
			Configuration.logger_.severe("jmetal.util.wrapper.XInt.size, solution type " +
					type_ + "+ invalid") ;		
		return 0 ;
	} // size
} // XInt