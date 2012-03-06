/**
 * @author Juan J. Durillo
 * @version 1.0
 * 
 * AlgorithmsWareHouse.java
 * 
 * This class is aimed at storing the algorithms included in jMetal.
 */
package jmetal.gui.warehouses;

import java.util.Properties;
import java.util.*;

public class ProblemWareHouse {
  
	private  static  List<String>       problems_;         // Stores the name of the algorithms
	private  static  List<Properties>   problemsSettings_;  // Stores the configuration of the algorithms
	
	/**
	 * Inserts a new problem and its settings into the warehouse
	 * @param name The name of the problem
	 * @param configuration A <code>Properties</code> object containing the settings
	 */
	public static void addProblem(String name, Properties configuration) {
       // Initialize the list of problems and settings
	   if (problems_ == null) 
		   problems_ = new ArrayList<String>();
	   // Initialize the list containing the configurations
	   if (problemsSettings_ == null) 
		   problemsSettings_ = new ArrayList<Properties>();

       // Find the position
       int pos = 0;
       boolean found = false;
       while (pos < problems_.size() && !found) {
         int flag = name.toUpperCase().compareTo(problems_.get(pos).toUpperCase());         

         if (flag < 0) {
           found = true;         
         } else {
           pos++;
         }

       }

       // Store the name and the configuration
       if (!found) {
         problems_.add(name);
         problemsSettings_.add(configuration);
       } else {
	     problems_.add(pos, name);
	     problemsSettings_.add(pos,configuration);
       }

	} // addProblem
	
	/**
	 * Returns the index of a problem contained into the warehouse or -1 if the problem is not contained
	 * @param name The name of the problem
	 * @return The index into the warehouse or -1 if the problem is not contained
	 */
	public static int getProblemIndex(String name) {
		Iterator<String> iterator = problems_.iterator();
		int index = 0;
		
		while (iterator.hasNext()) {
			if (name.equals(iterator.next()))
			   return index;
			index++;
		}
		return -1; // Default (returned if the algorithm is not found)
	} // getProblemIndex
	
	/**
	 * Returns the name of the problem at a given position
	 * @param name The index of the desired problem
	 * @return The name of the problem or null if the problem is not contained
	 */
	public static String getProblemName(int index) {
		if ((index < 0) || (index >= problems_.size())) // Base case: index is out of range
	       return null;
		return problems_.get(index);
	} // getProblemName
	
	
	/**
	 * Returns the Settings of a problem
	 * @param name The name of the problem
	 * @return A <code>Properties</code> object containing the settings or <code>null</code> if the algorithm
	 * is not contained into the settings
	 */
	public static Properties getSettings(String name) {
       int index = getProblemIndex(name); // get the index
       return getSettings(index);      // get the configuration
	} // getSettings

	/**
	 * Returns the Settings of a Problem
	 * @param index The index of the problem in the warehouse
	 * @return A <code>Properties</code> object containing the settings or <code>null</code> if the problem
	 * is not contained into the settings
	 */
	public static Properties getSettings(int index) {
		if ((index < 0) || (index >= problemsSettings_.size())) // Base case: index is out of range
		   return null;
		return problemsSettings_.get(index);
	} // getSettings
	
	
	/**
	 * Modifies the settings of a problem
	 * @param index The index of the problem to modify
	 * @param settings The settings of the problems
	 */
	public static void setSettings(int index, Properties settings) {
		if (!((index < 0) || (index >= problemsSettings_.size()))) // Base case: index is out of range
		   problemsSettings_.set(index,settings);
	} // setSettings

	
	/**
	 * Returns the number of problems stored into the warehouse
	 * @return The number of problems stored into the warehouse
	 */
	public static int size() {
		if (problems_ == null)
			return 0;
		return problems_.size();
	} // size
	
} // ProblemsWareHause
