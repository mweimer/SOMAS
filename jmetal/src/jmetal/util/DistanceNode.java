/**
 * DistanceNode.java
 * 
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.util;


/**
 * This is an auxiliar class for calculating the SPEA2 environmental selection.
 * Each instance of DistanceNode contains two parameter called
 * <code>reference_</code> and <code>distance_</code>.
 * <code>reference_</code> indicates one <code>Solution</code> in a
 * <code>SolutionSet</code> and <code>distance_</code> represents the distance_
 * to this solution.
 */
public class DistanceNode {
  
  /**
   * Indicates the position of a <code>Solution</code> in a 
   * <code>SolutionSet</code>.
   */
  private int reference_;
  
  /**
   * Indicates the distance to the <code>Solution</code> represented by 
   * <code>reference_</code>.
   */
  private double distance_;
    
  /**
   * Constructor.
   * @param distance The distance to a <code>Solution</code>.
   * @param reference The position of the <code>Solution</code>.
   */
  public DistanceNode(double distance, int reference){
    distance_ = distance;
    reference_ = reference;
  } // DistanceNode
    
  /**
   * Sets the distance to a <code>Solution</code>
   * @param distance The distance
   */
  public void setDistance(double distance){
    distance_ = distance;
  } // setDistance
    
  /**
   * Sets the reference to a <code>Solution</code>
   * @param reference The reference
   */
  public void setReferece(int reference){
    reference_ = reference;
  } // setReference
    
  /** 
   * Gets the distance
   * @return the distance
   */
  public double getDistance(){
    return distance_;        
  } // getDistance
    
  /**
   * Gets the reference
   * @return the reference
   */
  public int getReference(){
    return reference_;
  } // getReference
} // DistanceNode
