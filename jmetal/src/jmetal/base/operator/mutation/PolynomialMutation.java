/**
 * PolynomialMutation.java
 * @author Juan J. Durillo
 * @version 1.0
 * 
 */
package jmetal.base.operator.mutation;

import java.util.Properties;
import jmetal.base.Solution;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.variable.ArrayReal;

/**
 * This class implements a polynomial mutation operator. 
 * NOTE: the operator is applied to Real solutions, so the type of the solutions
 * must be </code>SolutionType_.Real</code>.
 * NOTE: if you use the default constructor, the value of the etc_m parameter is
 * ETA_M_DEFAULT_. You can change it using the parameter
 * "distributionIndex" before invoking the execute() method -- see lines 116-119
 */
public class PolynomialMutation extends Mutation {
	/**
	 * ETA_M_DEFAULT_ defines a default index for mutation
	 */
	public static final double ETA_M_DEFAULT_ = 20.0;

	/**
	 * eta_c stores the index for mutation to use
	 */
	public double eta_m_=ETA_M_DEFAULT_;

  /**
   * REAL_SOLUTION represents class jmetal.base.solutionType.RealSolutionType
   */
  private static Class REAL_SOLUTION ; 

  /**
   * REAL_SOLUTION represents class jmetal.base.solutionType.ArrayRealSolutionType
   */
  private static Class ARRAY_REAL_SOLUTION ; 

	/**
	 * Constructor
	 * Creates a new instance of the polynomial mutation operator
	 */
	public PolynomialMutation() {
    try {
    	REAL_SOLUTION = Class.forName("jmetal.base.solutionType.RealSolutionType") ;
    	ARRAY_REAL_SOLUTION = Class.forName("jmetal.base.solutionType.ArrayRealSolutionType") ;
    } catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } // catch
	} // PolynomialMutation

	/**
	 * Constructor.
	 * Create a new PolynomialMutation operator with an specific index
	 */
	public PolynomialMutation(double eta_m) {
		this() ;
		eta_m_ = eta_m;
	} // PolynomialMutation


	/**
	 * Constructor.
	 * Create a new PolynomialMutation operator with an specific index
	 */
	public PolynomialMutation(Properties properties) {
		this();
		eta_m_ = (new Double((String)properties.getProperty("eta_m_")));
	} // PolynomialMutation


	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException 
	 */
	public void doMutation(double probability, Solution solution) throws JMException {        
		double rnd, delta1, delta2, mut_pow, deltaq;
		double y, yl, yu, val, xy;
		XReal x = new XReal(solution) ;		
		for (int var=0; var < solution.numberOfVariables(); var++) {
			if (PseudoRandom.randDouble() <= probability)
			{
				y      = x.getValue(var);
				yl     = x.getLowerBound(var);                
				yu     = x.getUpperBound(var);
				delta1 = (y-yl)/(yu-yl);
				delta2 = (yu-y)/(yu-yl);
				rnd = PseudoRandom.randDouble();
				mut_pow = 1.0/(eta_m_+1.0);
				if (rnd <= 0.5)
				{
					xy     = 1.0-delta1;
					val    = 2.0*rnd+(1.0-2.0*rnd)*(Math.pow(xy,(eta_m_+1.0)));
					deltaq =  java.lang.Math.pow(val,mut_pow) - 1.0;
				}
				else
				{
					xy = 1.0-delta2;
					val = 2.0*(1.0-rnd)+2.0*(rnd-0.5)*(java.lang.Math.pow(xy,(eta_m_+1.0)));
					deltaq = 1.0 - (java.lang.Math.pow(val,mut_pow));
				}
				y = y + deltaq*(yu-yl);
				if (y<yl)
					y = yl;
				if (y>yu)
					y = yu;
				x.setValue(var, y);                           
			}
		} // for

	} // doMutation

	/**
	 * Executes the operation
	 * @param object An object containing a solution
	 * @return An object containing the mutated solution
	 * @throws JMException 
	 */  
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;

		if ((solution.getType().getClass() != REAL_SOLUTION)  &&
				(solution.getType().getClass() != ARRAY_REAL_SOLUTION)) {
			Configuration.logger_.severe("PolynomialMutation.execute: the solution " +
					"type " + solution.getType() + " is not allowed with this operator");

			Class cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;
		} // if 

		Double probability = (Double)getParameter("probability");       
		if (probability == null)
		{
			Configuration.logger_.severe("PolynomialMutation.execute: probability " +
			"not specified");
			Class cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;  
		}

		Double distributionIndex = (Double)getParameter("distributionIndex");
		if (distributionIndex != null) {
			eta_m_ = distributionIndex ;
		} // if

		doMutation(probability.doubleValue(),solution);
		return solution;      
	} // execute

} // PolynomialMutation
