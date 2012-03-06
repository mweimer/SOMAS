/**
 * Settings.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 *
 * Abstract Settings class
 */

package jmetal.experiments;

import java.lang.reflect.Field;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

public abstract class Settings {
	protected Problem problem_ ;
	public String paretoFrontFile_ ;

	/**
	 * Constructor
	 */
	public Settings() {
	} // Constructor

	/**
	 * Constructor
	 */
	public Settings(Problem problem) {
		problem_ = problem ;
	} // Constructor

	/**
	 * Default configure method
	 * @return A problem with the default configuration
	 * @throws jmetal.util.JMException
	 */
	abstract public Algorithm configure() throws JMException ;

	/**
	 * Configure method. Change the default configuration
	 * @param settings
	 * @return A problem with the settings indicated as argument
	 * @throws jmetal.util.JMException
	 * @throws ClassNotFoundException 
	 */
	public final Algorithm configure(Properties settings) throws JMException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {

		if (settings != null) {

			Field [] fields = this.getClass().getFields();

			for (int i=0; i < fields.length; i++) {
				if (fields[i].getName().endsWith("_")) { // it is a configuration field             	
					// The configuration field is an integer
					if (fields[i].getType().equals(int.class) ||
							fields[i].getType().equals(Integer.class)) {

						int value = Integer.parseInt(settings.getProperty(fields[i].getName(),""+fields[i].getInt(this)));

						fields[i].setInt(this, value);
					}  
					else if (fields[i].getType().equals(double.class) ||
							fields[i].getType().equals(Double.class)) {
						// The configuration field is a double
						double value = Double.parseDouble(settings.getProperty(fields[i].getName(),""+fields[i].getDouble(this)));

						if (fields[i].getName().equals("mutationProbability_") &&
								value == 0) {
							if ((problem_.getSolutionType().getClass() == Class.forName("jmetal.base.solutionType.RealSolutionType")) ||
									(problem_.getSolutionType().getClass() == Class.forName("jmetal.base.solutionType.ArrayRealSolutionType"))) {
								value = 1.0 / problem_.getNumberOfVariables();
							} else if (problem_.getSolutionType().getClass() == Class.forName("jmetal.base.solutionType.BinarySolutionType") ||
									problem_.getSolutionType().getClass() == Class.forName("jmetal.base.solutionType.BinaryRealSolutionType")) {
								int length = problem_.getNumberOfBits();

								value = 1.0 / length;
								System.out.println("La probabilidad es : " + value);
							} else {
								int length = 0;
								for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
									length+= problem_.getLength(j);
								}
								value = 1.0 / length;
							}
							fields[i].setDouble(this, value);
						} // if
						else {
							fields[i].setDouble(this, value);
						}
					} else {
						Object value = settings.getProperty(fields[i].getName(), null) ;

						if (value!=null) {
							if (fields[i].getType().equals(jmetal.base.operator.crossover.Crossover.class)) {
								Object value2 = CrossoverFactory.getCrossoverOperator((String)value,settings);
								value = value2;
							}

							if (fields[i].getType().equals(jmetal.base.operator.mutation.Mutation.class)) {
								Object value2 = MutationFactory.getMutationOperator((String)value, settings);
								value = value2;
							}

							fields[i].set(this, value);
						}
					}
				}
			} // for

			// At this point all the fields have been read from the properties
			// parameter. Those fields representing crossover and mutations should also
			// be initialized. However, there is still mandatory to configure them
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getType().equals(jmetal.base.operator.crossover.Crossover.class) ||
						fields[i].getType().equals(jmetal.base.operator.mutation.Mutation.class)) {
					Operator operator = (Operator) fields[i].get(this);
					// This field stores a crossover operator
					String tmp = fields[i].getName();
					String aux = fields[i].getName().substring(0, tmp.length()-1);

					for (int j = 0; j < fields.length; j++) {
						if (i != j) {
							if (fields[j].getName().startsWith(aux)) {
								// The field is a configuration parameter of the crossover
								tmp = fields[j].getName().substring(aux.length(), fields[j].getName().length()-1);

								if (
										(fields[j].get(this)!=null)) {
									System.out.println(fields[j].getName());
									if (fields[j].getType().equals(int.class) ||
											fields[j].getType().equals(Integer.class)) {
										operator.setParameter(tmp, fields[j].getInt(this));
									} else if (fields[j].getType().equals(double.class) ||
											fields[j].getType().equals(Double.class)) {
										operator.setParameter(tmp, fields[j].getDouble(this));
									}
								}
							}
						}
					}
				}
			}
			//At this point, we should compare if the pareto front have been added
			paretoFrontFile_ = settings.getProperty("paretoFrontFile_","");
		}

		return configure();
	} // configure

	/**
	 * Change the problem to solve
	 * @param problem
	 */
	void setProblem(Problem problem) {
		problem_ = problem ;
	} // setProblem

} // Settings