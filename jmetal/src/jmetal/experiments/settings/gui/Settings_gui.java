/**
 * Settings.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 *
 * Abstract Settings class
 */

package jmetal.experiments.settings.gui;

import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.experiments.Settings;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

public abstract class Settings_gui extends Settings{


  /**
   * Constructor
   */
  public Settings_gui()
  {
  } // Constructor


  /**
   * Default configure method
   * @return A problem with the default configuration
   * @throws jmetal.util.JMException
   */
  public Algorithm configure(Properties settings, Problem problem) throws JMException,
                                                                          IllegalArgumentException,
                                                                          IllegalAccessException,
                                                                          ClassNotFoundException {
      problem_ = problem;
      return configure(settings);

  } // configure




} // Settings_gui