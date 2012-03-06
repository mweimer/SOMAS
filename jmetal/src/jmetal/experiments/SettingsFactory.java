/**
 * SettingsFactory.java
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.experiments;

import java.lang.reflect.Constructor;

import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * This class represents a factory for problems
 */
public class SettingsFactory {
  /**
   * Creates a settings object
   * @param name Name of the algorithm
   * @param params Parameters
   * @return The settings object
   * @throws JMException 
   */
  /**
   * Creates a settings object
   * @param name Name of the algorithm
   * @param params Parameters
   * @return The settings object
   * @throws JMException 
   */
  public Settings getSettingsObject(String algorithmName, Object [] params) throws JMException {
    // Params are the arguments
    // The only argument is the problem to solve
    
    String base = "jmetal.experiments.settings." + algorithmName + "_Settings";

    try {
      Class problemClass = Class.forName(base);
      Constructor [] constructors = problemClass.getConstructors();
      int i = 0;
      //find the constructor
      while ((i < constructors.length) && 
             (constructors[i].getParameterTypes().length!=params.length)) {
        i++;
      }
      // constructors[i] is the selected one constructor
      Settings algorithmSettings = (Settings)constructors[i].newInstance(params);
      return algorithmSettings;      
    }// try
    catch(Exception e) {
      Configuration.logger_.severe("SettingsFactory.getSettingsObject: " +
          "Settings '"+ base + "' does not exist. "  +
          "Please, check the algorithm name in jmetal/metaheuristics") ;
      throw new JMException("Exception in " + base + ".getSettingsObject()") ;
    } // catch            
  } // getSttingsObject    
 
} // SettingsFactory
