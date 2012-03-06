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


public class PrintClassInfo {

  public static final String ALGORITHM_LABEL = "Algorithm";
  public static final String ALGORITHM_PACKAGE_LABEL = ".package";
  public static final String ALGORITHM_PARAM_LABEL = ".param";
  public static final String ALGORITHM_PARAM_VALUE =".value";
  public static final String ALGORITHM_OPERATOR =".operator";

  /**
   * Constructor
   * This constructor does nothing by default
   */
  public PrintClassInfo()
  {
     // Do nothing
  }

  /**
   * Print class information
   */
  public void printAlgorithmInfo(String algorithmName, String packageName)
    throws ClassNotFoundException,
           FileNotFoundException,
           IOException,
           InstantiationException,
           IllegalAccessException
  {
     Class auxClass = Class.forName("jmetal.experiments.settings."+algorithmName+"_Settings");
     Properties properties = PropUtils.load(Configuration.guiDataFile_);
     properties.setProperty(ALGORITHM_LABEL+"."+algorithmName,"");
     properties.setProperty(algorithmName+ALGORITHM_PACKAGE_LABEL, packageName);



     Field [] fields   = auxClass.getDeclaredFields();
     Object objeto =  auxClass.newInstance();

     for (int i = 0; i < fields.length; i++) {       
       try {
         if (fields[i].getModifiers() == Modifier.PUBLIC) {
            if (fields[i].getType().equals(double.class)) {
              
              String key = algorithmName + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"double");
              key = algorithmName + ".DEFAULT" + fields[i].getName();
              properties.setProperty(key, (new Double(fields[i].getDouble(objeto))).toString());
  
            } else if (fields[i].getType().equals(int.class)) {
              
              String key = algorithmName + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"int");
              key = algorithmName  + ".DEFAULT"+ fields[i].getName();
              properties.setProperty(key, (new Integer(fields[i].getInt(objeto))).toString());
            
            } else if (fields[i].getType().equals(jmetal.base.operator.mutation.Mutation.class)) {
              String key = algorithmName  + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key, "Mutation");
              key = algorithmName + ".DEFAULT" +fields[i].getName();

              String aux = fields[i].get(objeto).getClass().getName();
              int lastIndex = aux.lastIndexOf('.');
              properties.setProperty(key, aux.substring(lastIndex+1));
              

            } else if (fields[i].getType().equals(jmetal.base.operator.crossover.Crossover.class)) {
              String key = algorithmName  + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"Crossover");key = algorithmName + fields[i].getName();
              key = algorithmName + ".DEFAULT" + fields[i].getName();

              String aux = fields[i].get(objeto).getClass().getName();
              int lastIndex = aux.lastIndexOf('.');
              properties.setProperty(key, aux.substring(lastIndex+1));

            } else if (fields[i].getType().equals(jmetal.base.operator.selection.Selection.class)) {
              String key = algorithmName  + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"Mutation");key = algorithmName + fields[i].getName();
              key = algorithmName + ".DEFAULT" + fields[i].getName();
              String aux = fields[i].get(objeto).getClass().getName();
              int lastIndex = aux.lastIndexOf('.');
              properties.setProperty(key, aux.substring(lastIndex+1));
            }
          }
       } catch (IllegalArgumentException ex) {
         Logger.getLogger(PrintClassInfo.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IllegalAccessException ex) {
         Logger.getLogger(PrintClassInfo.class.getName()).log(Level.SEVERE, null, ex);
       }
     }
     FileOutputStream os = new FileOutputStream(Configuration.guiDataFile_,true);
     properties.store(os,"--No comments--");
     os.close();
  }


  /**
   * Example main
   */
  public static void main(String [] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {
        try {
            PrintClassInfo p = new PrintClassInfo();

            p.printAlgorithmInfo("AbYSS", "jmetal.metaheuristics.abyss");
            p.printAlgorithmInfo("CellDE", "jmetal.metaheuristics.cellde");
            p.printAlgorithmInfo("GDE3", "jmetal.metaheuristics.gde3");
            p.printAlgorithmInfo("IBEA", "jmetal.metaheuristics.ibea");
            //p.printAlgorithmInfo("MOCell", "jmetal.metaheuristics.mocell");
            p.printAlgorithmInfo("MOEAD", "jmetal.metaheuristics.moead");
            p.printAlgorithmInfo("NSGAII", "jmetal.metaheuristics.nsgaII");
            p.printAlgorithmInfo("OMOPSO", "jmetal.metaheuristics.omposo");
            p.printAlgorithmInfo("PAES", "jmetal.metaheuristics.paes");
            p.printAlgorithmInfo("SMPSO", "jmetal.metaheuristics.smpso");
            p.printAlgorithmInfo("SPEA2", "jmetal.metaheuristics.spea2");
            //p.printAlgorithmInfo("SMPSO", "jmetal.metaheuristics.smpso");
          //  p.printProblemInfo("jmetal.problems.ZDT.ZDT1");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PrintClassInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

  }
}
