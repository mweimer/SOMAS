/**
 * LocalSearch.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.localSearch;

import jmetal.base.Operator;


/**
 * Abstract class representing a generic local search operator
 */
public abstract class LocalSearch extends Operator{ 
  /**
   * Returns the number of evaluations made by the local search operator
   */
  public abstract int getEvaluations();
} // LocalSearch
