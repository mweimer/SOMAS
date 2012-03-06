/**
 * Operator.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.base;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import jmetal.util.JMException;

/**
 * Class representing an operator
 */
public abstract class Operator implements Serializable {
   
  /**
   * Stores the current operator parameters. 
   * It is defined as a Map of pairs <<code>String</code>, <code>Object</code>>, 
   * and it allow objects to be accessed by their names, which  are specified 
   * by the string.
   */
  protected Map<String , Object> parameters_;
  
  /**
   * Constructor.
   */
  public Operator() {
    parameters_ = new HashMap<String , Object>(); 
  } // Operator
        
  /**
   * Abstract method that must be defined by all the operators. When invoked, 
   * this method executes the operator represented by the current object.
   * @param object  This param inherits from Object to allow different kinds 
   *                of parameters for each operator. For example, a selection 
   *                operator typically receives a <code>SolutionSet</code> as 
   *                a parameter, while a mutation operator receives a 
   *                <code>Solution</code>.
   * @return An object reference. The returned value depends on the operator. 
   */
  abstract public Object execute(Object object) throws JMException ;

  
  /**
   * Sets a new <code>Object</code> parameter to the operator.
   * @param name The parameter name.
   * @param value Object representing the parameter.
   */
  public void setParameter(String name, Object value) {
    parameters_.put(name.toUpperCase(), value);
  } // setParameter
  
  /**
   * Returns an object representing a parameter of the <code>Operator</code>
   * @param name The parameter name.
   * @return the parameter.
   */
  public Object getParameter(String name) {
    return parameters_.get(name.toUpperCase());
  } //getParameter  

} // Operator
