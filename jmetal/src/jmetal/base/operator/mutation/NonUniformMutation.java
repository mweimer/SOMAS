/**
 * NonUniformMutation.java
 * @author Juan J. Durillo
 * @version 1.0
 * 
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
  * This class implements a non-uniform mutation operator.
  * NOTE: the type of the solutions must be <code>SolutionType_.Real</code>
  */
public class NonUniformMutation extends Mutation{
  /**
   * perturbation_ stores the perturbation value used in the Non Uniform 
   * mutation operator
   */
  private Double perturbation_ = null;
  
  /**
   * maxIterations_ stores the maximun number of iterations. 
   */
  private Integer maxIterations_ = null;    
  
  /**
   * actualIteration_ stores the iteration in which the operator is going to be
   * applied
   */
  private Integer actualIteration_ = null;
         
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
  * Creates a new instance of the non uniform mutation
  */
  public NonUniformMutation() {
    try {
    	REAL_SOLUTION = Class.forName("jmetal.base.solutionType.RealSolutionType") ;
    	ARRAY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.ArrayRealSolutionType") ;
    } catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } // catch
  } // NonUniformMutation
            

  /**
  * Constructor
  * Creates a new instance of the non uniform mutation
  */
  public NonUniformMutation(Properties properties){
     this();
  } // NonUniformMutation


  /**
  * Perform the mutation operation
  * @param probability Mutation probability
  * @param solution The solution to mutate
   * @throws JMException 
  */
  public void doMutation(double probability, Solution solution) throws JMException {                

  	XReal x = new XReal(solution) ; 
    for (int var = 0; var < solution.getDecisionVariables().length; var++) {         
      if (PseudoRandom.randDouble() < probability) {
        double rand = PseudoRandom.randDouble();
        double tmp;
                
        if (rand <= 0.5) {
          tmp = delta(x.getUpperBound(var) - x.getValue(var),
                      perturbation_.doubleValue());
          tmp += x.getValue(var);
        }
        else {
          tmp = delta(x.getLowerBound(var) - x.getValue(var),
                      perturbation_.doubleValue());
          tmp += x.getValue(var);
        }
                
        if (tmp < x.getLowerBound(var))
          tmp = x.getLowerBound(var);
        else if (tmp > x.getUpperBound(var))
          tmp = x.getUpperBound(var);
                
        x.setValue(var, tmp) ;
      }
    }
  } // doMutation
    

  /**
   * Calculates the delta value used in NonUniform mutation operator
   */
  private double delta(double y, double bMutationParameter) {
    double rand = PseudoRandom.randDouble();
    int it,maxIt;
    it    = actualIteration_.intValue();
    maxIt = maxIterations_.intValue();
        
    return (y * (1.0 - 
                Math.pow(rand,
                         Math.pow((1.0 - it /(double) maxIt),bMutationParameter)
                         )));
  } // delta

  /**
  * Executes the operation
  * @param object An object containing a solution
  * @return An object containing the mutated solution
   * @throws JMException 
  */
  public Object execute(Object object) throws JMException {
    Solution solution = (Solution)object;
    
    if ((solution.getType().getClass() != REAL_SOLUTION) &&
    		(solution.getType().getClass() != ARRAY_REAL_SOLUTION)){
      Configuration.logger_.severe("NonUniformMutation.execute: the solution " +
      		solution.getType() + "is not of the right type");

      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;
    } // if 
    
    Double probability;
      
    if (perturbation_ == null)
      perturbation_ = (Double) getParameter("perturbationIndex");
        
    if (maxIterations_ == null)
      maxIterations_ = (Integer) getParameter("maxIterations");
        
    actualIteration_ = (Integer) getParameter("currentIteration");
    probability =(Double)getParameter("probability");
    
    if (probability == null)
    {
      Configuration.logger_.severe("NonUniformMutation.execute: probability " +
          "not specified");
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;  
    }         
    
    doMutation(probability.doubleValue(),solution);
        
    return solution;    
  } // execute
} // NonUniformMutation
