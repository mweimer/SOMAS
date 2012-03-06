/**
 * @author Juan J. Durillo
 * @version 1.0
 * 
 * Configuration.java
 * 
 * This class is aimed to provide a java Properties with all the information of the jMetal elements
 */
package jmetal.gui.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Configuration {

	private static Properties jMetalProperties_;
	
  public static String guiDataFile_ = "gui.data" ;	
	
	/**
	 * Returns a <code>Properties</code> object containing all the information related to jMetal configurable
	 * elements
	 * @return A <code>Properties</code> object with the information, null in other case
	 */
	public static Properties getSettings (){
		if (jMetalProperties_ == null) {
		  try {
	        jMetalProperties_ = PropUtils.load(guiDataFile_);
		  } catch (FileNotFoundException fnfe) {
			try {
              return createConfiguration();			   
			} catch (Exception e) {
		      Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, e);
			}
	      } catch (IOException ex) {
	        Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
	      }
		}
	    return jMetalProperties_;
	} // getSettings

	
	/**
	 * Creates a new configuration file with the default information contained in jMetal.
	 */
	public static Properties createConfiguration() throws FileNotFoundException, IOException {
	  try {
		 FileOutputStream out = new FileOutputStream(guiDataFile_);
		 out.close();
	  } catch (Exception e) {
		 Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE,null,e); 
	  }
	  (new PrintAlgorithmsInfo()).printAlgorithmInfo();
      (new PrintProblemsInfo()).printProblemsInfo();
      (new PrintOperatorsInfo()).printOperatorInfo();
	  return PropUtils.load(guiDataFile_);
	} // createConfiguration
	
	
	/**
	 * Refresh the information related to configurable jMetal elements 
	 * elements
	 */	
	public static void reload() {
		if (jMetalProperties_ == null) {
			  try {
		        jMetalProperties_ = PropUtils.load(guiDataFile_);
		      } catch (IOException ex) {
		        Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
		      }
			}
	} // reload


        public static void save(Properties properties) {
        FileOutputStream os = null;
        try {
            jMetalProperties_.putAll(properties);
            os = new FileOutputStream(Configuration.guiDataFile_, false);
            try {
                properties.store(os, "--No comments--");
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }

} // Configuration
