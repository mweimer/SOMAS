/**
 * Int.java
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.base.variable;

import jmetal.base.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Configuration.*;

/**
 * This class implements an integer decision variable 
 */
public class Int extends Variable {
	private int value_;       //Stores the value of the variable
	private int lowerBound_;  //Stores the lower limit of the variable
	private int upperBound_;  //Stores the upper limit of the variable

	/**
	 * Constructor
	 */
	public Int() {
		lowerBound_ = java.lang.Integer.MIN_VALUE ;
		upperBound_ = java.lang.Integer.MAX_VALUE ;
		value_      = 0                           ;
	} // Int

	/**
	 * Constructor
	 * @param lowerBound Variable lower bound
	 * @param upperBound Variable upper bound
	 */
	public Int(int lowerBound, int upperBound){
		lowerBound_ = lowerBound;
		upperBound_ = upperBound;
		value_ = PseudoRandom.randInt(lowerBound, upperBound) ;
	} // Int

	/**
	 * Constructor
	 * @param value Value of the variable
	 * @param lowerBound Variable lower bound
	 * @param upperBound Variable upper bound
	 */
	public Int(int value, int lowerBound, int upperBound) {
		super();

		value_      = value      ;
		lowerBound_ = lowerBound ;
		upperBound_ = upperBound ;
	} // Int

	/**
	 * Copy constructor.
	 * @param variable Variable to be copied.
	 * @throws JMException 
	 */
	public Int(Variable variable) throws JMException{
		lowerBound_ = (int)variable.getLowerBound();
		upperBound_ = (int)variable.getUpperBound();
		value_ = (int)variable.getValue();        
	} // Int

	/**
	 * Returns the value of the variable.
	 * @return the value.
	 */
	public double getValue() {
		return value_;
	} // getValue

	/**
	 * Assigns a value to the variable.
	 * @param value The value.
	 */ 
	public void setValue(double value) {
		value_ = (int)value;
	} // setValue

	/**
	 * Creates an exact copy of the <code>Int</code> object.
	 * @return the copy.
	 */ 
	public Variable deepCopy(){
		try {
			return new Int(this);
		} catch (JMException e) {
			Configuration.logger_.severe("Int.deepCopy.execute: JMException");
			return null ;
		}
	} // deepCopy

	/**
	 * Returns the lower bound of the variable.
	 * @return the lower bound.
	 */ 
	public double getLowerBound() {
		return lowerBound_;
	} // getLowerBound

	/**
	 * Returns the upper bound of the variable.
	 * @return the upper bound.
	 */ 
	public double getUpperBound() {
		return upperBound_;
	} // getUpperBound

	/**
	 * Sets the lower bound of the variable.
	 * @param lowerBound The lower bound value.
	 */	    
	public void setLowerBound(double lowerBound)  {
		lowerBound_ = (int)lowerBound;
	} // setLowerBound

	/**
	 * Sets the upper bound of the variable.
	 * @param upperBound The new upper bound value.
	 */          
	public void setUpperBound(double upperBound) {
		upperBound_ = (int)upperBound;
	} // setUpperBound

	/**
	 * Returns a string representing the object
	 * @return The string
	 */ 
	public String toString(){
		return value_+"";
	} // toString

} // Int
