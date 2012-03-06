/**
 * OMOPSO_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * OMOPSO_Settings class of algorithm OMOPSO
 */
package jmetal.experiments.settings;

import jmetal.metaheuristics.omopso.*;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.experiments.Settings;
import jmetal.gui.utils.PropUtils;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class OMOPSO_Settings extends Settings{
  
  // Default settings
  public int    swarmSize_         = 100 ;
  public int    maxIterations_     = 250 ;
  public int    archiveSize_       = 100 ;
  public double perturbationIndex_ = 0.5 ;
  
  /**
   * Constructor
   */
  public OMOPSO_Settings(Problem problem) {
    super(problem) ;
  } // OMOPSO_Settings
  
  /**
   * Configure OMOPSO with user-defined parameter settings
   * @return A OMOPSO algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm ;
    
    QualityIndicator indicators ;
    
    // Creating the problem
    algorithm = new OMOPSO(problem_) ;
    
    // Algorithm parameters
    algorithm.setInputParameter("swarmSize", swarmSize_);
    algorithm.setInputParameter("maxIterations", maxIterations_);
    algorithm.setInputParameter("archiveSize", archiveSize_);
    algorithm.setInputParameter("perturbationIndex", perturbationIndex_);
    
   // Creating the indicator object
   if (! paretoFrontFile_.equals("")) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setInputParameter("indicators", indicators) ;  
   } // if
    return algorithm ;
  } // configure
} // OMOPSO_Settings
