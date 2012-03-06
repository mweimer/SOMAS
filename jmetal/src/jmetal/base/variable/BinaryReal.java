/**
 * BinaryReal.java
 *
 * @author Juan J. durillo
 * @version 1.0
 */
package jmetal.base.variable;

import java.util.BitSet;

import jmetal.base.Variable;
import jmetal.util.PseudoRandom;
import jmetal.util.Configuration.*;

/** This class extends the Binary class to represent a Real variable encoded by
 * a binary string
 */
public class BinaryReal extends Binary {
	
	/**
   * Defines the default number of bits used for binary coded variables.
   */	
	public static final int DEFAULT_PRECISION = 30; 
	
  /**
   * Stores the real value of the variable
   */
  private double value_;
  
  /**
   * Stores the lower limit for the variable
   */
  private double lowerBound_;
  
  /**
   * Stores the upper limit for the variable
   */
  private double upperBound_;  
     
  /**
   * Constructor.
   */
  public BinaryReal() {    
    super();
  } //BinaryReal

  /**
   * Constructor
   * @param numberOfBits Length of the binary string.
   * @param lowerBound The lower limit for the variable
   * @param upperBound The upper limit for the variable.
   */
  public BinaryReal(int numberOfBits, double lowerBound, double upperBound){
    super(numberOfBits);
    lowerBound_     = lowerBound;
    upperBound_     = upperBound;
    
    decode();                              
  } //BinaryReal
    
  /**
   * Copy constructor
   * @param variable The variable to copy
   */
  public BinaryReal(BinaryReal variable){
    super(variable);

    lowerBound_   = variable.lowerBound_;
    upperBound_   = variable.upperBound_;
    /*
    numberOfBits_ = variable.numberOfBits_;
     
    bits_ = new BitSet(numberOfBits_);
    for (int i = 0; i < numberOfBits_; i++)
      bits_.set(i,variable.bits_.get(i));
    */  
    value_ = variable.value_;
  } //BinaryReal
    
  
  /**
   * Decodes the real value encoded in the binary string represented
   * by the <code>BinaryReal</code> object. The decoded value is stores in the 
   * <code>value_</code> field and can be accessed by the method
   * <code>getValue</code>.
   */
  public void decode(){
    double value = 0.0;        
    for (int i = 0; i < numberOfBits_; i++) {
      if (bits_.get(i)) {
        value += Math.pow(2.0,i);
      }
    }
        
    value_ = value * (upperBound_ - lowerBound_) /
                                              (Math.pow(2.0,numberOfBits_)-1.0);        
    value_ += lowerBound_;    
  } //decode
 
  /**
   * Returns the double value of the variable.
   * @return the double value.
   */
  public double getValue() {
    return value_;
  } //getValue

  /**
   * Creates an exact copy of a <code>BinaryReal</code> object.
   * @return The copy of the object
   */
  public Variable deepCopy() {
    return new BinaryReal(this);
  } //deepCopy

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
   * @param lowerBound the lower bound.
   */
  public void setLowerBound(double lowerBound) {
    lowerBound_ = lowerBound;
  } // setLowerBound

  /**
   * Sets the upper bound of the variable.
   * @param upperBound the upper bound.
   */
  public void setUpperBound(double upperBound) {
    upperBound_ = upperBound;
  } // setUpperBound
  
  /**
   * Returns a string representing the object.
   * @return the string.
   */
  public String toString() {
    return value_+"";
  } // toString
}
