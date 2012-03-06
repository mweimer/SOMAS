/**
 * WFG1.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.problems.WFG;

import jmetal.base.Solution;
import jmetal.base.Variable;
import jmetal.util.JMException;

/**
 * This class implements the WFG1 problem
 * Reference: Simon Huband, Luigi Barone, Lyndon While, Phil Hingston
 *            A Scalable Multi-objective Test Problem Toolkit.
 *            Evolutionary Multi-Criterion Optimization: 
 *            Third International Conference, EMO 2005. 
 *            Proceedings, volume 3410 of Lecture Notes in Computer Science
 */
public class WFG1 extends WFG {
 
 /**
  * Constructor
  * Creates a default WFG1 instance with 
  * 2 position-related parameters
  * 4 distance-related parameters 
  * and 2 objectives
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public WFG1(String solutionType) throws ClassNotFoundException {
    this(solutionType, 2, 4, 2) ;
  } // WFG1

 /**
  * Creates a WFG1 problem instance
  * @param k Number of position parameters
  * @param l Number of distance parameters
  * @param M Number of objective functions
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public WFG1(String solutionType, Integer k, Integer l, Integer M) throws ClassNotFoundException {
    super(solutionType, k,l,M);
    problemName_ = "WFG1";
        
    S_ = new int[M_];
    for (int i = 0; i < M_; i++)
      S_[i] = 2 * (i+1);
        
    A_ = new int[M_-1];        
    for (int i = 0; i < M_-1; i++)
      A_[i] = 1;          
        
  } // WFG1
      
  /** 
  * Evaluates a solution 
  * @param z The solution to evaluate
  * @return a double [] with the evaluation results
  */  
  public float [] evaluate(float [] z){                
    float [] y;
        
    y = normalise(z);
    y = t1(y,k_);
    y = t2(y,k_);
    try {
      y = t3(y);
    } catch (JMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    y = t4(y,k_,M_);
    
        
    float [] result = new float[M_];
    float [] x = calculate_x(y);
    for (int m = 1; m <= M_ - 1 ; m++) {
      result [m-1] = D_*x[M_-1] + S_[m-1] * (new Shapes()).convex(x,m);
    }
        
    result[M_-1] = D_*x[M_-1] + S_[M_-1] * (new Shapes()).mixed(x,5,(float)1.0);
        
    return result;
  } // evaluate
    
  /**
   * WFG1 t1 transformation
   */
  public float [] t1(float [] z, int k){
    float [] result = new float[z.length];
        
    for (int i = 0; i < k; i++) {
      result[i] = z[i];
    }
        
    for (int i = k; i < z.length; i++) {
      result[i] = (float)(new Transformations()).s_linear(z[i],(float)0.35);
    }
        
    return result;      
  } // t1

  /**
  * WFG1 t2 transformation
  */
  public float [] t2(float [] z, int k){
    float [] result = new float[z.length];
        
    for (int i = 0; i < k; i++) {
      result[i] = z[i];
    }
        
    for (int i = k; i < z.length; i++) {
      result[i] = (float)(new Transformations()).b_flat(z[i],(float)0.8,(float)0.75,(float)0.85);
    }
        
    return result;
  } // t2
    
  /**
  * WFG1 t3 transformation
   * @throws JMException 
  */
  public float [] t3(float [] z) throws JMException{
    float [] result = new float[z.length];
        
    for (int i = 0; i < z.length; i++) {
      result[i] = (float) (new Transformations()).b_poly(z[i],(float)0.02);
    }
        
    return result;
  } // t3
    
  /**
  * WFG1 t4 transformation
  */
  public float [] t4(float [] z, int k, int M){
    float [] result = new float[M];
    float [] w      = new float[z.length];
                
    for (int i = 0; i < z.length; i++) {
      w[i] = (float)2.0 * (i + 1);
    }
        
    for (int i = 1; i <= M-1; i++){
      int head = (i - 1)*k/(M-1) + 1;
      int tail = i * k / (M - 1);                                   
      float [] subZ = subVector(z,head-1,tail-1);
      float [] subW = subVector(w,head-1,tail-1);
            
      result[i-1] = (float) (new Transformations()).r_sum(subZ,subW);            
    }
        
    int head = k + 1 - 1;
    int tail = z.length - 1;              
    float [] subZ = subVector(z,head,tail);      
    float [] subW = subVector(w,head,tail);        
    result[M-1] = (new Transformations()).r_sum(subZ,subW);
                
    return result;
  } // t4
                
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */  
  public final void evaluate(Solution solution) throws JMException {
    float [] variables = new float[getNumberOfVariables()];
    Variable[] dv = solution.getDecisionVariables();
        
    for (int i = 0; i < getNumberOfVariables(); i++) {
      variables[i] = (float)dv[i].getValue();    
    }
        
    float [] f = evaluate(variables);
        
    for (int i = 0; i < f.length; i++) {
      solution.setObjective(i,f[i]);
    }
  } //evaluate
} // WFG1
