/**
 * GDE3_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * CellDE_Settings class of algorithm GDE3
 */
package jmetal.experiments.settings;

import jmetal.experiments.*;
import jmetal.metaheuristics.gde3.*;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.gui.utils.PropUtils;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class GDE3_Settings extends Settings {
  // Default settings
  public double CR_ = 0.1;
  public double F_ = 0.5;
  public int populationSize_ = 100;
  public int maxIterations_ = 250;
  
  /**
   * Constructor
   */
  public GDE3_Settings(Problem problem) {
    super(problem);
  } // CellDE_Settings

  /**
   * Configure the algorithm with the specified parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm;
    Operator selection;
    Operator crossover;
    Operator mutation;

    QualityIndicator indicators;

    // Creating the problem
    algorithm = new GDE3(problem_);

    // Algorithm parameters
    algorithm.setInputParameter("populationSize", populationSize_);
    algorithm.setInputParameter("maxIterations", maxIterations_);

    // Crossover operator 
    crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");
    crossover.setParameter("CR", CR_);
    crossover.setParameter("F", F_);

    // Add the operators to the algorithm
    selection = SelectionFactory.getSelectionOperator("DifferentialEvolutionSelection");

    algorithm.addOperator("crossover", crossover);
    algorithm.addOperator("selection", selection);

    // Creating the indicator object
    if (!paretoFrontFile_.equals("")) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setInputParameter("indicators", indicators);
    } // if

    return algorithm;
  } // configure
} // GDE3_Settings
