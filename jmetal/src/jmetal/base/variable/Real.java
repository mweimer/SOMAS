/**
 * Real.java
 *
 * @author juanjo durillo 
 * @version 1.0
 */
package jmetal.base.variable;

import jmetal.base.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Configuration.*;


/**
 * This class implements a Real value decision variable
 */
public class Real extends Variable{

  /**
   * Stores the value of the real variable
   */
  private double value_;
  
  /**
   * Stores the lower bound of the real variable
   */
  private double lowerBound_;
  
  /**
   * Stores the upper bound of the real variable
   */
  private double upperBound_;
    
  /**
   * Constructor
   */
  public Real() {
  } // Real
    
  
  /**
   * Constructor
   * @param lowerBound Lower limit for the variable
   * @param upperBound Upper limit for the variable
   */
  public Real(double lowerBound, double upperBound){
    lowerBound_ = lowerBound;
    upperBound_ = upperBound;
    value_ = PseudoRandom.randDouble()*(upperBound-lowerBound)+lowerBound;        
  } //Real
    
  
  /** 
   * Copy constructor.
   * @param variable The variable to copy.
   * @throws JMException 
   */
  public Real(Variable variable) throws JMException{
    lowerBound_ = variable.getLowerBound();
    upperBound_ = variable.getUpperBound();
    value_ = variable.getValue();        
  } //Real

  /**
   * Gets the value of the <code>Real</code> variable.
   * @return the value.
   */
  public double getValue() {
    return value_;
  } // getValue
  
  /**
   * Sets the value of the variable.
   * @param value The value.
   */
  public void setValue(double value) {
    value_ = value;
  } // setValue
    
  /** 
   * Returns a exact copy of the <code>Real</code> variable
   * @return the copy
   */
  public Variable deepCopy(){
    try {
      return new Real(this);
    } catch (JMException e) {
      Configuration.logger_.severe("Real.deepCopy.execute: JMException");
      return null ;
    }
  } // deepCopy

  
  /**
   * Gets the lower bound of the variable.
   * @return the lower bound.
   */
  public double getLowerBound() {
    return lowerBound_;
  } //getLowerBound

  /**
   * Gets the upper bound of the variable.
   * @return the upper bound.
   */
  public double getUpperBound() {
    return upperBound_;
  } // getUpperBound
    
  
  /**
   * Sets the lower bound of the variable.
   * @param lowerBound The lower bound.
   */
  public void setLowerBound(double lowerBound)  {
    lowerBound_ = lowerBound;
  } // setLowerBound
    
  /**
   * Sets the upper bound of the variable.
   * @param upperBound The upper bound.
   */
  public void setUpperBound(double upperBound) {
    upperBound_ = upperBound;
  } // setUpperBound
  
  
  /**
   * Returns a string representing the object
   * @return the string
   */
  public String toString(){
    return value_+"";
  } //toString
} // Real
