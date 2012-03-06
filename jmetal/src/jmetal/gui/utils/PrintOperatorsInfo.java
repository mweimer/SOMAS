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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.Operator;


public class PrintOperatorsInfo {

  public static final String OPERATOR_LABEL = "OPERATOR";
  public static final String OPERATOR_PACKAGE_LABEL = ".PACKAGE";
  public static final String OPERATOR_PARAM_LABEL = ".PARAMETER.";
  public static final String OPERATOR_PARAM_VALUE =".VALUE.";
  public static final String OPERATOR_OPERATOR =".OPERATOR.";

  /**
   * Constructor
   * This constructor does nothing by default
   */
  public PrintOperatorsInfo()
  {
     // Do nothing
  }

  /**
   * Print class information
   */
  public void printOperatorInfo(String operatorName, String packageName)
    throws ClassNotFoundException,
           FileNotFoundException,
           IOException,
           InstantiationException,
           IllegalAccessException
  {

  
     Class auxClass = Class.forName(packageName+"."+operatorName);
     Properties properties = Configuration.getSettings();

     Field [] fields   = auxClass.getDeclaredFields();
     Object objeto =  auxClass.newInstance();

     // Which kind of operator is this?
     if (objeto.getClass().getSuperclass().equals(jmetal.base.operator.crossover.Crossover.class)) {
        properties.setProperty("Crossover."+operatorName,"");
     } else if (objeto.getClass().getSuperclass().equals(jmetal.base.operator.mutation.Mutation.class)) {
        properties.setProperty("Mutation."+operatorName,"");
     } else if (objeto.getClass().getSuperclass().equals(jmetal.base.operator.selection.Selection.class)) {
        properties.setProperty("Selection."+operatorName,"");
     }
         
     properties.setProperty(operatorName+OPERATOR_PACKAGE_LABEL, packageName);


     for (int i = 0; i < fields.length; i++) {
       try {
         if (fields[i].getModifiers()==Modifier.PUBLIC) {

            if (fields[i].getType().equals(double.class)) {
              String key = operatorName + OPERATOR_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"double");
              key = operatorName + ".DEFAULT." + fields[i].getName();
              properties.setProperty(key, (new Double(fields[i].getDouble(objeto))).toString());

            } else if (fields[i].getType().equals(int.class)) {
              String key = operatorName + OPERATOR_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"int");
              key = operatorName  + ".DEFAULT."+ fields[i].getName();
              properties.setProperty(key, (new Integer(fields[i].getInt(objeto))).toString());

            }
         }
       } catch (IllegalArgumentException ex) {
         Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IllegalAccessException ex) {
         Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
       }

     }
     this.getOperator("PolynomialMutation",properties);
     Configuration.save(properties);
  }



  public Operator getOperator(String name, Properties properties) {
     
 
    Operator operator = null;
    
    Properties anOperator = PropUtils.getPropertiesWithPrefix(properties,name);
    

    // Reading the parameters of the operator

    Properties problemParameters = PropUtils.getPropertiesWithPrefix(anOperator, ".PARAMETER.");
    try {
        try {
            String packageName = (Configuration.getSettings()).getProperty(name+".PACKAGE");
            operator = (Operator) Class.forName(packageName+"."+name).newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(PrintOperatorsInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrintOperatorsInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(PrintOperatorsInfo.class.getName()).log(Level.SEVERE, null, ex);
    }


    int i = 0;
    Iterator iterator = problemParameters.keySet().iterator();
    while (iterator.hasNext()) {
       String next = (String) iterator.next();
       String type = problemParameters.getProperty(next);

       if (type.equals("int")) {
           operator.setParameter(next, new Integer(anOperator.getProperty(".VALUE."+ next)));
  
       } else if (type.equals("double")) {
           operator.setParameter(next, new Double(anOperator.getProperty(".VALUE."+ next)));
  
       }

    }
    return operator;
  }




  /*
   * Writes the information of operator classes contained in the jMetal default package
   */  
  public void printOperatorInfo() {
    try {
      //printOperatorInfo("BitFlipMutation", "jmetal.base.operator.mutation");
      //printOperatorInfo("NonUniformMutation", "jmetal.base.operator.mutation");
      printOperatorInfo("PolynomialMutation", "jmetal.base.operator.mutation");
      //printOperatorInfo("SwapMutation", "jmetal.base.operator.mutation");
      //printOperatorInfo("UniformMutation", "jmetal.base.operator.mutation");
      //printOperatorInfo("DifferentialEvolutionCrossover", "jmetal.base.operator.crossover");
      //printOperatorInfo("HUXCrossover", "jmetal.base.operator.crossover");
      //printOperatorInfo("PMXCrossover", "jmetal.base.operator.crossover");
      printOperatorInfo("SBXCrossover", "jmetal.base.operator.crossover");
      //printOperatorInfo("SinglePointCrossover", "jmetal.base.operator.crossover");
      //printOperatorInfo("TwoPointsCrossover", "jmetal.base.operator.crossover");
    } catch (Exception ex) {
      Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
    }  
  }

  /**
   * Example main
   */
  public static void main(String [] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {
     (new PrintOperatorsInfo()).printOperatorInfo();	  
  }
}
