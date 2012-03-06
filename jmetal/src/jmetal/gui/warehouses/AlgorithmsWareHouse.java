/**
 * @author Juan J. Durillo
 * @version 1.0
 * 
 * AlgorithmsWareHouse.java
 * 
 * This class is aimed at storing the algorithms included in jMetal.
 */
package jmetal.gui.warehouses;

import java.util.*;

public class AlgorithmsWareHouse {
  
	private  static  List<String>       algorithms_;         // Stores the name of the algorithms
	private  static  List<Properties>   algorithmsSettings_;  // Stores the configuration of the algorithms
	
	/**
	 * Inserts a new algorithm and its settings into the warehouse
	 * @param name The name of the algorithm
	 * @param configuration A <code>Properties</code> object containing the settings
	 */
	public static void addAlgorithm(String name, Properties configuration) {
       // Initialize algorithms and algorithms settings
	   if (algorithms_ == null) 
		   algorithms_ = new ArrayList<String>();
	   // Initialize the list containing the configurations
	   if (algorithmsSettings_ == null) 
		   algorithmsSettings_ = new ArrayList<Properties>();


       // Find the position
       int pos = 0;
       boolean found = false;
       while (pos < algorithms_.size() && !found) {
         int flag = name.toUpperCase().compareTo(algorithms_.get(pos).toUpperCase());

         if (flag < 0) {
           found = true;
         } else {
           pos++;
         }

       }

       // Store the name and the configuration
       if (!found) {
         algorithms_.add(name);
         algorithmsSettings_.add(configuration);
       } else {
	     algorithms_.add(pos, name);
	     algorithmsSettings_.add(pos,configuration);
       }
	} // addAlgorithm
	
	/**
	 * Returns the index of an algorithm contained into the warehouse or -1 if the algorithms is not contained
	 * @param name The name of the algorithm
	 * @return The index into the warehouse or -1 if the algorithm is not contained
	 */
	public static int getAlgorithmIndex(String name) {
		Iterator<String> iterator = algorithms_.iterator();
		int index = 0;
		
		while (iterator.hasNext()) {
			if (name.equals(iterator.next()))
			   return index;
			index++;
		}
		return -1; // Default (returned if the algorithm is not found)
	} // getAlgoritmIndex
	
	/**
	 * Returns the name of the algorithm at a given position
	 * @param name The index of the desired algorithm
	 * @return The name of the algorithm or null if the algorithm is not contained
	 */
	public static String getAlgorithmName(int index) {
		if ((index < 0) || (index >= algorithms_.size())) // Base case: index is out of range
	       return null;
		return algorithms_.get(index);
	} // getAlgoritmName
	
	
	/**
	 * Returns the Settings of an algorithm
	 * @param name The name of the algorithm
	 * @return A <code>Properties</code> object containing the settings or <code>null</code> if the algorithm
	 * is not contained into the settings
	 */
	public static Properties getSettings(String name) {
       int index = getAlgorithmIndex(name); // get the index
       return getSettings(index);      // get the configuration
	} // getSettings

	/**
	 * Returns the Settings of an algorithm
	 * @param index The index of the algorithm in the warehouse
	 * @return A <code>Properties</code> object containing the settings or <code>null</code> if the algorithm
	 * is not contained into the settings
	 */
	public static Properties getSettings(int index) {
		if ((index < 0) || (index >= algorithmsSettings_.size())) // Base case: index is out of range
		   return null;
		return algorithmsSettings_.get(index);
	} // getSettings
	
	
	/**
	 * Modifies the settings of one algorithm
	 * @param index The index of the algorithm to modify
	 * @param settings The settings of the algorithms
	 */
	public static void setSettings(int index, Properties settings) {
		if (!((index < 0) || (index >= algorithmsSettings_.size()))) // Base case: index is out of range
		   algorithmsSettings_.set(index,settings);
	} // setSettings

	
	/**
	 * Returns the number of algorithms stored into the warehouse
	 * @return The number of contained algorithms
	 */
	public static int size() {
		if (algorithms_ == null)
			return 0;
		return algorithms_.size();
	} // size
	
} // AlgorithmsWareHause
