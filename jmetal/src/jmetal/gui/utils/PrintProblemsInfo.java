package jmetal.gui.utils;

/**
 * PrintClassInfo.java
 *
 * @author Juan J. Durillo
 * @version 1.0
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
import jmetal.base.Problem;


public class PrintProblemsInfo {

  /**
   * Constructor
   * This constructor does nothing by default
   */
  public PrintProblemsInfo()
  {
     // Do nothing
  }


  /**
   * Print class information
   */
  public void printProblemInfo(String problemName, String packageName)
    throws ClassNotFoundException,
           FileNotFoundException,
           IOException,
           InstantiationException,
           IllegalAccessException,
           NoSuchFieldException
  {
     Class auxClass = Class.forName(packageName+"."+problemName+"_gui");
     Properties properties = Configuration.getSettings();
     properties.setProperty("PROBLEM"+"."+problemName,"");
     properties.setProperty(problemName+".PACKAGE", packageName);



     Class superClass = auxClass.getSuperclass();
     Constructor []  constructors = superClass.getConstructors();
     int selectedConstructor = -1;
     int selectedConstructorLength = 0;
     for (int i = 0; i < constructors.length; i++) {
        if (constructors[i].getParameterTypes().length > selectedConstructorLength) {
          if ((constructors[i].getParameterTypes().length > 0) &&
               (constructors[i].getParameterTypes()[0]!=Properties.class)) {
            selectedConstructorLength = constructors[i].getParameterTypes().length;
            selectedConstructor = i;
          }
        }
     }

     Field fields = auxClass.getDeclaredField("parameterList");
     Object objeto = auxClass.newInstance();
     String [] parameterList = (String []) fields.get(objeto);



     Class [] parameters = constructors[selectedConstructor].getParameterTypes();
     for (int i = 0; i < parameterList.length; i++) {
       try {
           
        if (parameters[i].equals(double.class) || parameters[i].equals(Double.class)) {
          String key = problemName + ".PARAMETER." + parameterList[i];
          properties.setProperty(key,"double");
          properties.setProperty(problemName+".DEFAULT."+parameterList[i],(String) auxClass.getDeclaredField(parameterList[i]).get(objeto));
          properties.setProperty(problemName+".ORDER."+parameterList[i],i+"");

        } else if ((parameters[i].equals(int.class)) || (parameters[i].equals(Integer.class))){
          String key = problemName + ".PARAMETER."+ parameterList[i];
          properties.setProperty(key,"int");
          properties.setProperty(problemName+".DEFAULT."+parameterList[i], auxClass.getDeclaredField(parameterList[i]).get(objeto).toString());
          properties.setProperty(problemName+".ORDER."+parameterList[i],i+"");

        } else if ((parameters[i].equals(String.class) && (parameterList[i].equals("SolutionType")))) {
          String key = problemName  + ".PARAMETER."+ parameterList[i];
          properties.setProperty(key, "SolutionType");      
          properties.setProperty(problemName+".DEFAULT."+parameterList[i],(String) auxClass.getDeclaredField(parameterList[i]).get(objeto));
          properties.setProperty(problemName+".ORDER."+parameterList[i],i+"");
     
          //  properties.setProperty(algorithmName+fields[i].getName(),".Mutation");
        }
       } catch (IllegalArgumentException ex) {
         Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
       }
     }


     Configuration.save(properties);
  }


  public Problem getProblem(String name, Properties properties) {
    Problem problem = null;
    Properties aProblem = properties;
    List parameters = new ArrayList();


    Properties problemParameters = PropUtils.getPropertiesWithPrefix(aProblem, ".PARAMETER");        

    int i = 0;
    Iterator iterator = problemParameters.keySet().iterator();
    while (iterator.hasNext()) {
       i++;
       String next = (String) iterator.next();
       String type = problemParameters.getProperty(next);

       int index = new Integer((String)aProblem.getProperty(".ORDER"+ next));
       if (index > parameters.size())
           index = parameters.size();


       if (type.equals("int")) {
           parameters.add(index , new Integer(aProblem.getProperty(".VALUE"+ next)));
       } else if (type.equals("double")) {
           parameters.add(index , new Double(aProblem.getProperty(".VALUE"+ next)));
       } else if (type.equals("SolutionType")) {
           parameters.add(index , (String) (aProblem.getProperty(".VALUE"+ next)));
       }
       
    }
        try {
            Constructor[] c = Class.forName(aProblem.getProperty(".PACKAGE")+"."+name).getConstructors();
            int index = 0;
            Constructor selected = c[index];

            while (selected.getParameterTypes().length != i) {
                selected = c[++index];
            }
            try {
                Object a [] = parameters.toArray();
                
                problem = (Problem) selected.newInstance(parameters.toArray());
                
            } catch (InstantiationException ex) {
                Logger.getLogger(PrintProblemsInfo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(PrintProblemsInfo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(PrintProblemsInfo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(PrintProblemsInfo.class.getName()).log(Level.SEVERE, null, ex);
            }



        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PrintProblemsInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

    return problem;
  }



  /*
   * Writes the information of problem classes contained in the jMetal default package
   */
  public void printProblemsInfo() {
    try {
        printProblemInfo("ZDT1", "jmetal.gui.problems.ZDT");
        printProblemInfo("ZDT2", "jmetal.gui.problems.ZDT");
        printProblemInfo("ZDT3", "jmetal.gui.problems.ZDT");
        printProblemInfo("ZDT4", "jmetal.gui.problems.ZDT");
 //       printProblemInfo("ZDT5", "jmetal.gui.problems.ZDT");
        printProblemInfo("ZDT6", "jmetal.gui.problems.ZDT");
        printProblemInfo("DTLZ1", "jmetal.gui.problems.DTLZ");
        printProblemInfo("DTLZ2", "jmetal.gui.problems.DTLZ");
        printProblemInfo("DTLZ3", "jmetal.gui.problems.DTLZ");
        printProblemInfo("DTLZ4", "jmetal.gui.problems.DTLZ");
        printProblemInfo("DTLZ5", "jmetal.gui.problems.DTLZ");
        printProblemInfo("DTLZ6", "jmetal.gui.problems.DTLZ");
        printProblemInfo("DTLZ7", "jmetal.gui.problems.DTLZ");
        printProblemInfo("WFG1", "jmetal.gui.problems.WFG");
        printProblemInfo("WFG2", "jmetal.gui.problems.WFG");
        printProblemInfo("WFG3", "jmetal.gui.problems.WFG");
        printProblemInfo("WFG4", "jmetal.gui.problems.WFG");
        printProblemInfo("WFG5", "jmetal.gui.problems.WFG");
        printProblemInfo("WFG6", "jmetal.gui.problems.WFG");
        printProblemInfo("WFG7", "jmetal.gui.problems.WFG");
        printProblemInfo("WFG8", "jmetal.gui.problems.WFG");
        printProblemInfo("WFG9", "jmetal.gui.problems.WFG");
        printProblemInfo("Schaffer", "jmetal.gui.problems");
        printProblemInfo("ConstrEx", "jmetal.gui.problems");
        printProblemInfo("Fonseca", "jmetal.gui.problems");
        printProblemInfo("Golinski", "jmetal.gui.problems");
        printProblemInfo("Kursawe", "jmetal.gui.problems");
//        printProblemInfo("OKA1", "jmetal.gui.problems");
//        printProblemInfo("OKA2", "jmetal.gui.problems");
        printProblemInfo("Osyczka2", "jmetal.gui.problems");
        printProblemInfo("Srinivas", "jmetal.gui.problems");
        printProblemInfo("Tanaka", "jmetal.gui.problems");
//        printProblemInfo("Viennet2", "jmetal.gui.problems");
//        printProblemInfo("Viennet3", "jmetal.gui.problems");
//        printProblemInfo("Viennet4", "jmetal.gui.problems");
//        printProblemInfo("Water", "jmetal.gui.problems");
    } catch (Exception ex) {
        Logger.getLogger(PrintProblemsInfo.class.getName()).log(Level.SEVERE, null, ex); 
    }
  }
  
  
  /**
   * Example main
   */
  public static void main(String [] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {
    (new PrintProblemsInfo()).printProblemsInfo();

  }
}
