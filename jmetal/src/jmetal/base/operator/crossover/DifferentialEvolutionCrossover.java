/**
 * DifferentialEvolutionCrossover.java
 * Class representing the crossover operator used in differential evolution
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.base.operator.crossover;

import java.util.Properties;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.solutionType.RealSolutionType;
import jmetal.base.Solution;
import jmetal.base.SolutionType;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

/**
 * Differential evolution crossover operators
 * Comments:
 * - The operator receives two parameters: the current individual and an array
 *   of three parent individuals
 * - The best and rand variants depends on the third parent, according whether
 *   it represents the current of the "best" individual or a randon one. 
 *   The implementation of both variants are the same, due to that the parent 
 *   selection is external to the crossover operator. 
 * - Implemented variants:
 *   - rand/1/bin (best/1/bin)
 *   - rand/1/exp (best/1/exp)
 *   - current-to-rand/1 (current-to-best/1)
 *   - current-to-rand/1/bin (current-to-best/1/bin)
 *   - current-to-rand/1/exp (current-to-best/1/exp)
 */
public class DifferentialEvolutionCrossover extends Crossover {
	/**
	 * DEFAULT_CR defines a default CR (crossover operation control) value
	 */
	public static final double DEFAULT_CR = 0.5; 

	/**
	 * DEFAULT_F defines the default F (Scaling factor for mutation) value
	 */
	private static final double DEFAULT_F = 0.5;

	/**
	 * DEFAULT_K defines a default K value used in variants current-to-rand/1
	 * and current-to-best/1
	 */
	public static final double DEFAULT_K = 0.5; 

	/**
	 * DEFAULT_VARIANT defines the default DE variant
	 */

	private static final String DEFAULT_DE_VARIANT = "rand/1/bin";

	/**
	 * REAL_SOLUTION represents class jmetal.base.solutionType.RealSolutionType
	 */
	private static Class REAL_SOLUTION ; 
	
  /**
   * ARRAY_REAL_SOLUTION represents class jmetal.base.solutionType.ArrayRealSolutionType
   */
  private static Class ARRAY_REAL_SOLUTION ; 

	public double CR_  ;
	public double F_   ;
	public double K_   ;
	private String DE_Variant_ ; // DE variant (rand/1/bin, rand/1/exp, etc.)

	/**
	 * Constructor
	 */
	public DifferentialEvolutionCrossover() {
		try {
			REAL_SOLUTION = Class.forName("jmetal.base.solutionType.RealSolutionType") ;
    	ARRAY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.ArrayRealSolutionType") ;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CR_ = DEFAULT_CR ;
		F_  = DEFAULT_F  ;
		K_  = DEFAULT_K   ;
		DE_Variant_ = DEFAULT_DE_VARIANT ;
	} // Constructor


	/**
	 * Constructor
	 */
	public DifferentialEvolutionCrossover(Properties properties) {
		this();
		CR_ = (new Double((String)properties.getProperty("CR_")));
		F_  = (new Double((String)properties.getProperty("F_")));
		K_  = (new Double((String)properties.getProperty("K_")));
		DE_Variant_ = properties.getProperty("DE_Variant_") ;
	} // Constructor

	/**
	 * Executes the operation
	 * @param object An object containing an array of three parents
	 * @return An object containing the offSprings
	 */
	public Object execute(Object object) throws JMException {
		Object[] parameters = (Object[])object ;
		Solution current   = (Solution) parameters[0];
		Solution [] parent = (Solution [])parameters[1];

		Solution child ;
		
		if (((parent[0].getType().getClass() != REAL_SOLUTION) &&
				(parent[1].getType().getClass() != REAL_SOLUTION) &&
				(parent[2].getType().getClass() != REAL_SOLUTION)) && 
				((parent[0].getType().getClass() != ARRAY_REAL_SOLUTION) &&
				(parent[1].getType().getClass() != ARRAY_REAL_SOLUTION)&&
				(parent[2].getType().getClass() != ARRAY_REAL_SOLUTION))) {

			Configuration.logger_.severe("DifferentialEvolutionCrossover.execute: " +
					" the solutions " +
					"are not of the right type. The type should be 'Real' or 'ArrayReal', but " +
					parent[0].getType() + " and " + 
					parent[1].getType() + " and " + 
					parent[2].getType() + " are obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;
		}

		Double CR = (Double)getParameter("CR");
		if (CR != null) {
			CR_ = CR ;
		} // if
		Double F = (Double)getParameter("F");
		if (F != null) {
			F_ = F ;
		} // if
		Double K = (Double)getParameter("K");
		if (K != null) {
			K_ = K ;
		} // if

		// STEP 2. Checking the variant
		String DE_Variant = (String)getParameter("DE_VARIANT");
		if (DE_Variant != null) {
			DE_Variant_ = DE_Variant ;
		} // if

		int jrand ;

		child = new Solution(current) ;
		
		XReal xParent0 = new XReal(parent[0]) ;
		XReal xParent1 = new XReal(parent[1]) ;
		XReal xParent2 = new XReal(parent[2]) ;
		XReal xCurrent = new XReal(current) ;
		XReal xChild   = new XReal(child) ;

		int numberOfVariables = xParent0.getNumberOfDecisionVariables() ;
		jrand = (int)(PseudoRandom.randInt(0, numberOfVariables - 1)) ;

		// STEP 4. Checking the DE variant
		if ((DE_Variant_.compareTo("rand/1/bin") == 0) || 
				(DE_Variant_.compareTo("best/1/bin") == 0)) { 
			for (int j=0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
					double value ;
					value = xParent2.getValue(j)  + F_ * (xParent0.getValue(j) -
							                                  xParent1.getValue(j)) ;
					if (value < xChild.getLowerBound(j))
						value =  xChild.getLowerBound(j) ;
					if (value > xChild.getUpperBound(j))
						value = xChild.getUpperBound(j) ;

					xChild.setValue(j, value) ;
				}
				else {
					double value ;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value) ;
				} // else
			} // for
		} // if
		else if ((DE_Variant_.compareTo("rand/1/exp") == 0) || 
				(DE_Variant_.compareTo("best/1/exp") == 0)) {
			CR = CR_ ;
			for (int j=0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR || j == jrand) {
					double value ;
					value = xParent2.getValue(j)  + F_ * (xParent0.getValue(j) -
							xParent1.getValue(j)) ;

					if (value < xChild.getLowerBound(j))
						value =  xChild.getLowerBound(j) ;
					if (value > xChild.getUpperBound(j))
						value = xChild.getUpperBound(j) ;

					xChild.setValue(j, value) ;
				}
				else {
					CR = 0.0 ;
					double value ;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value) ;
			  } // else
			} // for		
		} // if
		else if ((DE_Variant_.compareTo("current-to-rand/1") == 0) || 
				(DE_Variant_.compareTo("current-to-best/1") == 0)) { 
			for (int j=0; j < numberOfVariables; j++) {
				double value ;
				value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
					    xCurrent.getValue(j)) +					
						  F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

				if (value < xChild.getLowerBound(j))
					value =  xChild.getLowerBound(j) ;
				if (value > xChild.getUpperBound(j))
					value = xChild.getUpperBound(j) ;

				xChild.setValue(j, value) ;
			} // for		
		} // if
		else if ((DE_Variant_.compareTo("current-to-rand/1/bin") == 0) ||
				(DE_Variant_.compareTo("current-to-best/1/bin") == 0)) { 
			for (int j=0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
					double value ;
					value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
							xCurrent.getValue(j)) +					
							F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

					if (value < xChild.getLowerBound(j))
						value =  xChild.getLowerBound(j) ;
					if (value > xChild.getUpperBound(j))
						value = xChild.getUpperBound(j) ;

					xChild.setValue(j, value) ;
				}
				else {
					double value ;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value) ;
				} // else
			} // for
		} // if
		else if ((DE_Variant_.compareTo("current-to-rand/1/exp") == 0) || 
				(DE_Variant_.compareTo("current-to-best/1/exp") == 0)) {
			CR = CR_ ;
			for (int j=0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR || j == jrand) {
					double value ;
					value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
							xCurrent.getValue(j)) +					
							F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

					if (value < xChild.getLowerBound(j))
						value =  xChild.getLowerBound(j) ;
					if (value > xChild.getUpperBound(j))
						value = xChild.getUpperBound(j) ;

					xChild.setValue(j, value) ;
				}
				else {
					CR = 0.0 ;
					double value ;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value) ;
				} // else
			} // for		
		} // if		
		else {
			Configuration.logger_.severe("DifferentialEvolutionCrossover.execute: " +
					" unknown DE variant (" + DE_Variant_ + ")");
			Class<String> cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;
		} // else
		return child ;
	}
} // DifferentialEvolutionCrossover
