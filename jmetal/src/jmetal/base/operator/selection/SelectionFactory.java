/**
 * SelectionFactory.java
 *
 * @author Juanjo Durillo
 * @version 1.1
 */

package jmetal.base.operator.selection;

import jmetal.base.Operator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * Class implementing a selection operator factory.
 */
public class SelectionFactory {
    
  /**
   * Gets a selection operator through its name.
   * @param name of the operator
   * @return the operator
   * @throws JMException 
   */
  public static Operator getSelectionOperator(String name) throws JMException {
    if (name.equalsIgnoreCase("BinaryTournament"))
      return new BinaryTournament();
    else if (name.equalsIgnoreCase("BinaryTournament2"))
      return new BinaryTournament2();
    else if (name.equalsIgnoreCase("PESA2Selection"))
      return new PESA2Selection();
    else if (name.equalsIgnoreCase("RandomSelection"))
      return new RandomSelection();    
    else if (name.equalsIgnoreCase("RankingAndCrowdingSelection"))
      return new RankingAndCrowdingSelection();
    else if (name.equalsIgnoreCase("DifferentialEvolutionSelection"))
      return new DifferentialEvolutionSelection();
    else {
      Configuration.logger_.severe("Operator '" + name + "' not found ");
      throw new JMException("Exception in " + name + ".getSelectionOperator()") ;
    } // else    
  } // getSelectionOperator
    
} // SelectionFactory
