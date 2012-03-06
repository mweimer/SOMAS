/**
 * UniformMutation.java
 * Class representing a uniform mutation operator
 * @author Antonio J.Nebro
 * @version 1.0
 */
package jmetal.base.operator.mutation;

import java.util.Properties;
import jmetal.base.Solution;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;
import jmetal.base.operator.mutation.Mutation;

/**
 * This class implements a uniform mutation operator.
 * NOTE: the type of the solutions must be <code>SolutionType_.Real</code>
 */
public class UniformMutation extends Mutation{
  /**
   * Stores the value used in a uniform mutation operator
   */
  private Double perturbation_;

  /**
   * REAL_SOLUTION represents class jmetal.base.solutionType.RealSolutionType
   */
  private static Class REAL_SOLUTION ; 

  /**
   * REAL_SOLUTION represents class jmetal.base.solutionType.ArrayRealSolutionType
   */
  private static Class ARRAY_REAL_SOLUTION ; 

  
  /** 
   * Constructor
   * Creates a new uniform mutation operator instance
   */
  public UniformMutation() {
    try {
    	REAL_SOLUTION = Class.forName("jmetal.base.solutionType.RealSolutionType") ;
    	ARRAY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.ArrayRealSolutionType") ;
    } catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } // catch
  } // UniformMutation


  /**
   * Constructor
   * Creates a new uniform mutation operator instance
   */
  public UniformMutation(Properties properties) {
    this();
  } // UniformMutation


  /**
  * Performs the operation
  * @param probability Mutation probability
  * @param solution The solution to mutate
   * @throws JMException 
  */
  public void doMutation(double probability, Solution solution) throws JMException {  
  	XReal x = new XReal(solution) ; 

    for (int var = 0; var < solution.getDecisionVariables().length; var++) {
      if (PseudoRandom.randDouble() < probability) {
        double rand = PseudoRandom.randDouble();
        double tmp = (rand - 0.5)*perturbation_.doubleValue();
                                
        tmp += x.getValue(var);
                
        if (tmp < x.getLowerBound(var))
          tmp = x.getLowerBound(var);
        else if (tmp > x.getUpperBound(var))
          tmp = x.getUpperBound(var);
                
        x.setValue(var, tmp) ;
      } // if
    } // for
  } // doMutation
  
  /**
  * Executes the operation
  * @param object An object containing the solution to mutate
   * @throws JMException 
  */
  public Object execute(Object object) throws JMException {
    Solution solution = (Solution )object;
    
    if ((solution.getType().getClass() != REAL_SOLUTION) &&
    		(solution.getType().getClass() != ARRAY_REAL_SOLUTION)) {
      Configuration.logger_.severe("UniformMutation.execute: the solution " +
          "is not of the right type. The type should be 'Real', but " +
          solution.getType() + " is obtained");

      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;
    } // if 
    
    Double probability;
        
    if (perturbation_ == null)
      perturbation_ = (Double)getParameter("perturbationIndex");
        
    probability = (Double)getParameter("probability");
    if (probability == null)
    {
      Configuration.logger_.severe("UniformMutation.execute: probability " +
      "not specified");
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;  
    }
    
    doMutation(probability.doubleValue(),solution);
        
    return solution;
  } // execute                  
} // UniformMutation
