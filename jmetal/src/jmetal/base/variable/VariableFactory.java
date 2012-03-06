/**
 * VariableFactory.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.base.variable;

import jmetal.base.Variable;
import jmetal.base.variable.*;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * This class is intended to be used as a static Factory to obtains variables. 
 */
public class VariableFactory {
    
  /** 
   * Obtains an instance of a <code>Variable</code> given its name.
   * @param name The name of the class from which we want to obtain an instance
   * object
   * @throws JMException 
   */
  public static Variable getVariable(String name) throws JMException{
    Variable variable   = null;
    String baseLocation = "jmetal.base.variable.";
    try {
      Class c = Class.forName(baseLocation + name);
      variable = (Variable) c.newInstance();
      return variable;
    } catch (ClassNotFoundException e1) {
      Configuration.logger_.severe("VariableFactory.getVariable: " +
      "ClassNotFoundException ");
      Class cls = java.lang.String.class;
      String name2 = cls.getName() ;    
      throw new JMException("Exception in " + name2 + ".getVariable()") ;
    } catch (InstantiationException e2) {
      Configuration.logger_.severe("VariableFactory.getVariable: " +
      "InstantiationException ");
      Class cls = java.lang.String.class;
      String name2 = cls.getName() ;    
      throw new JMException("Exception in " + name2 + ".getVariable()") ;
    } catch (IllegalAccessException e3) {
      Configuration.logger_.severe("VariableFactory.getVariable: " +
      "IllegalAccessException ");
      Class cls = java.lang.String.class;
      String name2 = cls.getName() ;    
      throw new JMException("Exception in " + name2 + ".getVariable()") ;
    }
  } // getVariable      
} //VariabeFactory
