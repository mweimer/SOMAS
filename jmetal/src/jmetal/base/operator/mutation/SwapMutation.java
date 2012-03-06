/**
 * SwapMutation.java
 * Class representing a swap mutation operator
 * @author Antonio J.Nebro
 * @version 1.0
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
 * This class implements a swap mutation.
 * NOTE: the operator is applied to the first variable of the solutions, and 
 * the type of those variables must be <code>VariableType_.Permutation</code>.
 */
public class SwapMutation extends Mutation{
  /** 
   * Constructor
   */
  public SwapMutation() {    
  } // Constructor


  /**
   * Constructor
   */
  public SwapMutation(Properties properties) {
    this();
  } // Constructor

  /**
   * Performs the operation
   * @param probability Mutation probability
   * @param solution The solution to mutate
   * @throws JMException 
   */
  public void doMutation(double probability, Solution solution) throws JMException {   
    int permutation[] ;
    int permutationLength ;
    try {
	    if (solution.getDecisionVariables()[0].getVariableType() ==
	    	Class.forName("jmetal.base.variable.Permutation")) {

	      permutationLength = ((Permutation)solution.getDecisionVariables()[0]).getLength() ;
	      permutation = ((Permutation)solution.getDecisionVariables()[0]).vector_ ;

	      if (PseudoRandom.randDouble() < probability) {
	        int pos1 ;
	        int pos2 ;

	        pos1 = PseudoRandom.randInt(0,permutationLength-1) ;
	        pos2 = PseudoRandom.randInt(0,permutationLength-1) ;

	        while (pos1 == pos2) {
	          if (pos1 == (permutationLength - 1)) 
	            pos2 = PseudoRandom.randInt(0, permutationLength- 2);
	          else 
	            pos2 = PseudoRandom.randInt(pos1, permutationLength- 1);
	        } // while
	        // swap
	        int temp = permutation[pos1];
	        permutation[pos1] = permutation[pos2];
	        permutation[pos2] = temp;    
	      } // if
	    } // if
	    else  {
	      Configuration.logger_.severe("SwapMutation.doMutation: invalid type. " +
	          ""+ solution.getDecisionVariables()[0].getVariableType());

	      Class cls = java.lang.String.class;
	      String name = cls.getName(); 
	      throw new JMException("Exception in " + name + ".doMutation()") ;
	    }
    } catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } // catch               
  } // doMutation

  /**
   * Executes the operation
   * @param object An object containing the solution to mutate
   * @return an object containing the mutated solution
   * @throws JMException 
   */
  public Object execute(Object object) throws JMException {
    Solution solution = (Solution)object;

    Double probability = (Double)getParameter("probability");       
    if (probability == null)
    {
      Configuration.logger_.severe("SwapMutation.execute: probability " +
      "not specified");
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;  
    }
    
    this.doMutation(probability.doubleValue(), solution);
    return solution;
  } // execute  
} // SwapMutation
