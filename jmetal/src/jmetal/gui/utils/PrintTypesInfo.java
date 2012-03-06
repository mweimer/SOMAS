package jmetal.gui.utils;

/**
 * PrintClassInfo.java
 *
 * @author Juan J. Durillo
 * @version 0.1
 *
 * This class provides some utilities for writting classes information
 *
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PrintTypesInfo {

  /**
   * Constructor
   * This constructor does nothing by default
   */
  public PrintTypesInfo()
  {
     // Do nothing
  }


  /**
   * Print class information
   */
  public void printTypesInfo(String typeName, String packageName)
    throws ClassNotFoundException,
           FileNotFoundException,
           IOException,
           InstantiationException,
           IllegalAccessException,
           NoSuchFieldException
  {
     Class auxClass = Class.forName(packageName+"."+typeName);
     Properties properties = Configuration.getSettings();
     properties.setProperty("SOLUTION_TYPE"+"."+typeName,"");
     properties.setProperty(typeName+"package", packageName);


     FileOutputStream os = new FileOutputStream(Configuration.guiDataFile_,true);
     properties.store(os,"--No comments--");
     os.close();
  }


  /*
   * Writes the information of problem classes contained in the jMetal default package
   */
  public void printTypesInfo() {
    try {
        printTypesInfo("ArrayRealSolutionType","jmetal.base.solutionType");
        printTypesInfo("BinaryRealSolutionType","jmetal.base.solutionType");
        printTypesInfo("BinarySolutionType","jmetal.base.solutionType");
        printTypesInfo("IntRealSolutionType","jmetal.base.solutionType");
        printTypesInfo("IntSolutionType","jmetal.base.solutionType");
        printTypesInfo("PermutationSolutionType","jmetal.base.solutionType");
        printTypesInfo("RealSolutionType","jmetal.base.solutionType");
    } catch (Exception ex) {
        Logger.getLogger(PrintProblemsInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
  }


  /**
   * Example main
   */
  public static void main(String [] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {
    (new PrintTypesInfo()).printTypesInfo();
  }
}
