 /**
 * StandardStudy.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.experiments;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Problem;

import jmetal.util.JMException;

/**
 * @author Antonio J. Nebro
 */
public class GUIBasedStudy extends Experiment {

  /**
   * Configures the algorithms in each independent run
   * @param problem The problem to solve
   * @param problemIndex
   */
  public Properties[] parameters = null;


  public void algorithmSettings(Problem problem, int problemIndex, Algorithm[] algorithm) throws ClassNotFoundException {
    try {
      int numberOfAlgorithms = algorithmNameList_.length;

      if (!paretoFrontFile_[problemIndex].equals("")) {
        for (int i = 0; i < numberOfAlgorithms; i++) {
          parameters[i].setProperty("PARETO_FRONT_FILE", paretoFrontFile_[problemIndex]);
          Object [] settingsParams = {problem} ;
          algorithm[i] = (new SettingsFactory()).getSettingsObject(algorithmNameList_[i], settingsParams).configure(parameters[i]);
        }
      } // if
    } catch (IllegalArgumentException ex) {
      Logger.getLogger(GUIBasedStudy.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(GUIBasedStudy.class.getName()).log(Level.SEVERE, null, ex);
    } catch  (JMException ex) {
      ex.printStackTrace();
      Logger.getLogger(StandardStudy.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

} // StandardStudy


