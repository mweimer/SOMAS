/**
 * HUXCrossover.java
 * @author Juan J. Durillo
 * @version 1.0
 * Class representing a HUX crossover operator
 */
package jmetal.base.operator.crossover;

import java.util.Properties;
import jmetal.base.variable.*;
import jmetal.base.*;    
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class allows to apply a HUX crossover operator using two parent
 * solutions.
 * NOTE: the operator is applied to the first variable of the solutions, and 
 * the type of the solutions must be binary 
 * (e.g., <code>SolutionType_.Binary</code> or 
 * <code>SolutionType_.BinaryReal</code>.
 */
public class HUXCrossover extends Crossover{

  /**
   * BINARY_SOLUTION represents class jmetal.base.solutionType.RealSolutionType
   */
  private static Class BINARY_SOLUTION ; 
  /**
   * BINARY_REAL_SOLUTION represents class jmetal.base.solutionType.BinaryRealSolutionType
   */
  private static Class BINARY_REAL_SOLUTION ; 

  /**
   * Constructor
   * Create a new instance of the HUX crossover operator.
   */
  public HUXCrossover() {
    try {
    	BINARY_SOLUTION = Class.forName("jmetal.base.solutionType.BinarySolutionType") ;
    	BINARY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.BinaryRealSolutionType") ;
    } catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } // catch
  } // HUXCrossover


   /**
   * Constructor
   * Create a new intance of the HUX crossover operator.
   */
   public HUXCrossover(Properties properties) {
     this();
   } // HUXCrossover



  /**
   * Perform the crossover operation
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containing the two offsprings
   * @throws JMException 
   */
  public Solution[] doCrossover(double   probability, 
                                Solution parent1, 
                                Solution parent2) throws JMException {
    Solution [] offSpring = new Solution[2];
    offSpring[0] = new Solution(parent1);
    offSpring[1] = new Solution(parent2);
    try {         
      if (PseudoRandom.randDouble() < probability)
      {
        for (int var = 0; var < parent1.getDecisionVariables().length; var++) {
          Binary p1 = (Binary)parent1.getDecisionVariables()[var];
          Binary p2 = (Binary)parent2.getDecisionVariables()[var];

          for (int bit = 0; bit < p1.getNumberOfBits(); bit++) {
            if (p1.bits_.get(bit) != p2.bits_.get(bit)) {
              if (PseudoRandom.randDouble() < 0.5) {
                ((Binary)offSpring[0].getDecisionVariables()[var])
                .bits_.set(bit,p2.bits_.get(bit));
                ((Binary)offSpring[1].getDecisionVariables()[var])
                .bits_.set(bit,p1.bits_.get(bit));
              }
            }
          }
        }  
        //7. Decode the results
        for (int i = 0; i < offSpring[0].getDecisionVariables().length; i++)
        {
          ((Binary)offSpring[0].getDecisionVariables()[i]).decode();
          ((Binary)offSpring[1].getDecisionVariables()[i]).decode();
        }
      }          
    }catch (ClassCastException e1) {
      
      Configuration.logger_.severe("HUXCrossover.doCrossover: Cannot perfom " +
          "SinglePointCrossover ") ;
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".doCrossover()") ;
    }        
    return offSpring;                                                                                      
  } // doCrossover

  
  /**
  * Executes the operation
  * @param object An object containing an array of two solutions 
  * @return An object containing the offSprings
  */
  public Object execute(Object object) throws JMException {
    Solution [] parents = (Solution [])object;
    
    if ( ((parents[0].getType().getClass() != BINARY_SOLUTION) ||
          (parents[1].getType().getClass() != BINARY_SOLUTION)) && 
         ((parents[0].getType().getClass() != BINARY_REAL_SOLUTION) ||
          (parents[1].getType().getClass() != BINARY_REAL_SOLUTION))) {
      
      Configuration.logger_.severe("HUXCrossover.execute: the solutions " +
          "are not of the right type. The type should be 'Binary' of " +
          "'BinaryReal', but " +
          parents[0].getType() + " and " + 
          parents[1].getType() + " are obtained");

      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;

    } // if 
    
    Double probability = (Double)getParameter("probability");
    if (parents.length < 2)
    {
      Configuration.logger_.severe("HUXCrossover.execute: operator needs two " +
          "parents");
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;      
    }
    else if (probability == null)
    {
      Configuration.logger_.severe("HUXCrossover.execute: probability not " +
      "specified");
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;  
    }         
    
    Solution [] offSpring = doCrossover(probability.doubleValue(),
                                                       parents[0],
                                                       parents[1]);
    
    for (int i = 0; i < offSpring.length; i++)
    {
      offSpring[i].setCrowdingDistance(0.0);
      offSpring[i].setRank(0);
    } 
    return offSpring;
    
  } // execute
} // HUXCrossover
