/**
 * SinglePointCrossover.java
 * Class representing a single point crossover operator
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.1
 */
package jmetal.base.operator.crossover;

import java.util.Properties;
import jmetal.base.*;
import jmetal.base.variable.*;
import jmetal.base.*;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Configuration.*;

/**
 * This class allows to apply a Single Point crossover operator using two parent
 * solutions.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single variable.
 */
public class SinglePointCrossover extends Crossover {
  /**
   * BINARY_SOLUTION represents class jmetal.base.solutionType.RealSolutionType
   */
  private static Class BINARY_SOLUTION ; 
  /**
   * BINARY_REAL_SOLUTION represents class jmetal.base.solutionType.BinaryRealSolutionType
   */
  private static Class BINARY_REAL_SOLUTION ; 
  /**
   * INT_SOLUTION represents class jmetal.base.solutionType.IntSolutionType
   */
  private static Class INT_SOLUTION ; 

  /**
   * Constructor
   * Creates a new instance of the single point crossover operator
   */
  public SinglePointCrossover() {
    try {
    	BINARY_SOLUTION = Class.forName("jmetal.base.solutionType.BinarySolutionType") ;
    	BINARY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.BinaryRealSolutionType") ;
    	INT_SOLUTION = Class.forName("jmetal.base.solutionType.IntSolutionType") ;
    } catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } // catch
  } // SinglePointCrossover


  /**
   * Constructor
   * Creates a new instance of the single point crossover operator
   */
  public SinglePointCrossover(Properties properties) {
      this();
  } // SinglePointCrossover

  /**
   * Perform the crossover operation.
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containig the two offsprings
   * @throws JMException
   */
  public Solution[] doCrossover(double probability,
          Solution parent1,
          Solution parent2) throws JMException {
    Solution[] offSpring = new Solution[2];
    offSpring[0] = new Solution(parent1);
    offSpring[1] = new Solution(parent2);
    try {
      if (PseudoRandom.randDouble() < probability) {
        if ((parent1.getType().getClass() == BINARY_SOLUTION) ||
            (parent1.getType().getClass() == BINARY_REAL_SOLUTION)) {
          //1. Compute the total number of bits
          int totalNumberOfBits = 0;
          for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
            totalNumberOfBits +=
                    ((Binary) parent1.getDecisionVariables()[i]).getNumberOfBits();
          }

          //2. Calcule the point to make the crossover
          int crossoverPoint = PseudoRandom.randInt(0, totalNumberOfBits - 1);

          //3. Compute the variable that containt the crossoverPoint bit
          int variable = 0;
          int acountBits =
                  ((Binary) parent1.getDecisionVariables()[variable]).getNumberOfBits();

          while (acountBits < (crossoverPoint + 1)) {
            variable++;
            acountBits +=
                    ((Binary) parent1.getDecisionVariables()[variable]).getNumberOfBits();
          }

          //4. Compute the bit into the variable selected
          int diff = acountBits - crossoverPoint;
          int intoVariableCrossoverPoint =
                  ((Binary) parent1.getDecisionVariables()[variable]).getNumberOfBits() - diff;

          //5. Make the crossover into the the gene;
          Binary offSpring1, offSpring2;
          offSpring1 =
                  (Binary) parent1.getDecisionVariables()[variable].deepCopy();
          offSpring2 =
                  (Binary) parent2.getDecisionVariables()[variable].deepCopy();

          for (int i = intoVariableCrossoverPoint;
                  i < offSpring1.getNumberOfBits();
                  i++) {
            boolean swap = offSpring1.bits_.get(i);
            offSpring1.bits_.set(i, offSpring2.bits_.get(i));
            offSpring2.bits_.set(i, swap);
          }

          offSpring[0].getDecisionVariables()[variable] = offSpring1;
          offSpring[1].getDecisionVariables()[variable] = offSpring2;

          //6. Apply the crossover to the other variables
          for (int i = 0; i < variable; i++) {
            offSpring[0].getDecisionVariables()[i] =
                    parent2.getDecisionVariables()[i].deepCopy();

            offSpring[1].getDecisionVariables()[i] =
                    parent1.getDecisionVariables()[i].deepCopy();

          }

          //7. Decode the results
          for (int i = 0; i < offSpring[0].getDecisionVariables().length; i++) {
            ((Binary) offSpring[0].getDecisionVariables()[i]).decode();
            ((Binary) offSpring[1].getDecisionVariables()[i]).decode();
          }
        } // Binary or BinaryReal
        else { // Integer representation
          int crossoverPoint = PseudoRandom.randInt(0, parent1.numberOfVariables() - 1);
          int valueX1;
          int valueX2;
          for (int i = crossoverPoint; i < parent1.numberOfVariables(); i++) {
            valueX1 = (int) parent1.getDecisionVariables()[i].getValue();
            valueX2 = (int) parent2.getDecisionVariables()[i].getValue();
            offSpring[0].getDecisionVariables()[i].setValue(valueX2);
            offSpring[1].getDecisionVariables()[i].setValue(valueX1);
          } // for
        } // Int representation
      }
    } catch (ClassCastException e1) {
      Configuration.logger_.severe("SinglePointCrossover.doCrossover: Cannot perfom " +
              "SinglePointCrossover");
      Class cls = java.lang.String.class;
      String name = cls.getName();
      throw new JMException("Exception in " + name + ".doCrossover()");
    }
    return offSpring;
  } // doCrossover

  /**
   * Executes the operation
   * @param object An object containing an array of two solutions
   * @return An object containing an array with the offSprings
   * @throws JMException
   */
  public Object execute(Object object) throws JMException {
    Solution[] parents = (Solution[]) object;

    if (((parents[0].getType().getClass() != BINARY_SOLUTION) ||
         (parents[1].getType().getClass() != BINARY_SOLUTION)) &&
         ((parents[0].getType().getClass() != BINARY_REAL_SOLUTION) ||
         (parents[1].getType().getClass() != BINARY_REAL_SOLUTION)) &&
         ((parents[0].getType().getClass() != INT_SOLUTION) ||
         (parents[1].getType().getClass() != INT_SOLUTION))) {

      Configuration.logger_.severe("SinglePointCrossover.execute: the solutions " +
              "are not of the right type. The type should be 'Binary' or 'Int', but " +
              parents[0].getType() + " and " +
              parents[1].getType() + " are obtained");

      Class cls = java.lang.String.class;
      String name = cls.getName();
      throw new JMException("Exception in " + name + ".execute()");
    } // if

    Double probability = (Double) getParameter("probability");
    if (parents.length < 2) {
      Configuration.logger_.severe("SinglePointCrossover.execute: operator " +
              "needs two parents");
      Class cls = java.lang.String.class;
      String name = cls.getName();
      throw new JMException("Exception in " + name + ".execute()");
    } else if (probability == null) {
      Configuration.logger_.severe("SinglePointCrossover.execute: probability " +
              "not specified");
      Class cls = java.lang.String.class;
      String name = cls.getName();
      throw new JMException("Exception in " + name + ".execute()");
    }

    Solution[] offSpring;
    offSpring = doCrossover(probability.doubleValue(),
            parents[0],
            parents[1]);

    //-> Update the offSpring solutions
    for (int i = 0; i < offSpring.length; i++) {
      offSpring[i].setCrowdingDistance(0.0);
      offSpring[i].setRank(0);
    }
    return offSpring;//*/
  } // execute
} // SinglePointCrossover
