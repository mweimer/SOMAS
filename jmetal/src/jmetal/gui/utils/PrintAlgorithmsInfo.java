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
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.crossover.DifferentialEvolutionCrossover;
import jmetal.base.operator.localSearch.MutationLocalSearch;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.base.operator.selection.BinaryTournament2;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.problems.ZDT.ZDT1;
import jmetal.util.JMException;


public class PrintAlgorithmsInfo {

  public static final String ALGORITHM_LABEL = "Algorithm";
  public static final String ALGORITHM_PACKAGE_LABEL = ".PACKAGE";
  public static final String ALGORITHM_PARAM_LABEL = ".PARAMETER.";
  public static final String ALGORITHM_PARAM_VALUE =".VALUE.";
  public static final String ALGORITHM_OPERATOR_LABEL =".OPERATOR.";

  /**
   * Constructor
   * This constructor does nothing by default
   */
  public PrintAlgorithmsInfo()
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
     Properties properties = Configuration.getSettings();
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
              key = algorithmName + ".DEFAULT." + fields[i].getName();
              properties.setProperty(key, (new Double(fields[i].getDouble(objeto))).toString());
  
            } else if (fields[i].getType().equals(int.class)) {
              
              String key = algorithmName + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"int");
              key = algorithmName  + ".DEFAULT."+ fields[i].getName();
              properties.setProperty(key, (new Integer(fields[i].getInt(objeto))).toString());
            
            } else if (fields[i].getType().equals(jmetal.base.operator.mutation.Mutation.class)) {
              String key = algorithmName  + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key, "Mutation");
              key = algorithmName + ".DEFAULT." +fields[i].getName();

              String aux = fields[i].get(objeto).getClass().getName();
              int lastIndex = aux.lastIndexOf('.');
              properties.setProperty(key, aux.substring(lastIndex+1));
              

            } else if (fields[i].getType().equals(jmetal.base.operator.crossover.Crossover.class)) {
              String key = algorithmName  + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"Crossover");key = algorithmName + fields[i].getName();
              key = algorithmName + ".DEFAULT." + fields[i].getName();

              String aux = fields[i].get(objeto).getClass().getName();
              int lastIndex = aux.lastIndexOf('.');
              properties.setProperty(key, aux.substring(lastIndex+1));

            } else if (fields[i].getType().equals(jmetal.base.operator.selection.Selection.class)) {
              String key = algorithmName  + ALGORITHM_PARAM_LABEL+ fields[i].getName();
              properties.setProperty(key,"Mutation");key = algorithmName + fields[i].getName();
              key = algorithmName + ".DEFAULT." + fields[i].getName();
              String aux = fields[i].get(objeto).getClass().getName();
              int lastIndex = aux.lastIndexOf('.');
              properties.setProperty(key, aux.substring(lastIndex+1));
            }
          }
       } catch (IllegalArgumentException ex) {
         Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IllegalAccessException ex) {
         Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
       }
     }
     Configuration.save(properties);
  }



//  public Algorithm getAlgorithm(String name, Properties properties, Problem problem) {
//
//    Algorithm algorithm = null;
//    List parameters = new ArrayList();
//
//
//
//    try {
//
//        Constructor c = null;
//        try {
//            String classe = Configuration.getSettings().getProperty(".PACKAGE") + "." + name;
//            c = Class.forName(classe).getConstructor(new Class[]{jmetal.base.Problem.class});
//        } catch (NoSuchMethodException ex) {
//            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SecurityException ex) {
//            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Object [] params = {problem};
//
//        try {
//            algorithm = (Algorithm) c.newInstance(params);
//        } catch (InstantiationException ex) {
//            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvocationTargetException ex) {
//            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    } catch (ClassNotFoundException ex) {
//        Logger.getLogger(PrintOperatorsInfo.class.getName()).log(Level.SEVERE, null, ex);
//    }
//
//
//
//    Iterator iterator = algorithmParameter.keySet().iterator();
//    List<String> parameterList = new ArrayList<String>();
//    while (iterator.hasNext())
//        parameterList.add((String)iterator.next());
//
//
//    Properties algorithmOperator = PropUtils.getPropertiesWithPrefix(anAlgorithm, ".OPERATOR.");
//    iterator = algorithmOperator.keySet().iterator();
//    System.out.println(algorithmOperator);
//    while (iterator.hasNext()) {
//       String next = (String) iterator.next();
//       Operator op = ((new PrintOperatorsInfo().getOperator(anAlgorithm.getProperty(".DEFAULT."+ next), properties)));
//
//       Iterator iterator2 = parameterList.iterator();
//
//       while (iterator2.hasNext()) {
//           String parameterName = (String)iterator2.next();
//           System.out.println("ParameterName: " + parameterName);
//
//
//           if (parameterName.startsWith(next)) {
//
//              String type = algorithmParameter.getProperty(parameterName);
//              System.out.println("El tipo es: "+type);
//
//              System.out.println("El nombre del paramtero es: "+parameterName.substring(next.length()).toLowerCase());
//
//              if (type.equals("int")) {
//                op.setParameter(parameterName.substring(next.length()).toLowerCase(), new Integer(anAlgorithm.getProperty(".DEFAULT."+ parameterName)));
//                //System.out.println(next + " " + new Integer(anAlgorithm.getProperty(".DEFAULT."+ next)));
//              } else if (type.equals("double")) {
//                op.setParameter(parameterName.substring(next.length()).toLowerCase(), new Double(anAlgorithm.getProperty(".DEFAULT."+ parameterName)));
//                //System.out.println(next + " " + new Double(anAlgorithm.getProperty(".DEFAULT."+ next)));
//              }
//              iterator.remove();
//           }
//
//       }
//       algorithm.addOperator(next, op);
//    }
//
//    iterator = parameterList.iterator();
//
//    while (iterator.hasNext()) {
//       String next = (String) iterator.next();
//       String type = algorithmParameter.getProperty(next);
//
//       if (type.equals("int")) {
//           algorithm.setInputParameter(next, new Integer(anAlgorithm.getProperty(".DEFAULT."+ next)));
//           System.out.println(next + " " + new Integer(anAlgorithm.getProperty(".DEFAULT."+ next)));
//       } else if (type.equals("double")) {
//           algorithm.setInputParameter(next, new Double(anAlgorithm.getProperty(".DEFAULT."+ next)));
//           System.out.println(next + " " + new Double(anAlgorithm.getProperty(".DEFAULT."+ next)));
//       } else if (type.equals("Crossover")) {
//
//           System.out.println(".DEFAULT."+ next);
//           System.out.println(anAlgorithm.getProperty(".DEFAULT."+ next));
//
//           Operator op = ((new PrintOperatorsInfo().getOperator(anAlgorithm.getProperty(".DEFAULT."+ next), properties)));
//           algorithm.addOperator(next, op);
//       } else if (type.equals("Mutation")) {
//
//           System.out.println(".DEFAULT."+ next);
//           System.out.println(anAlgorithm.getProperty(".DEFAULT."+ next));
//
//           Operator op = ((new PrintOperatorsInfo().getOperator(anAlgorithm.getProperty(".DEFAULT."+ next), properties)));
//           algorithm.addOperator(next, op);
//       }
//
//    }
//
//
//      return Algorithm;
//  }


  public Algorithm getAlgorithm(String name, Properties properties, Problem p) {
    Algorithm algorithm = null;
    List parameters = new ArrayList();
    Properties anAlgorithm = properties;


    // Reading the parameters of the operator

    Properties algorithmParameter = PropUtils.getPropertiesWithPrefix(anAlgorithm, ".PARAMETER.");
    try {
        
        Constructor c = null;
        try {
            c = Class.forName(anAlgorithm.getProperty(".PACKAGE") + "." + name).getConstructor(new Class[]{jmetal.base.Problem.class});
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object [] params = {p};
        
        try {
            algorithm = (Algorithm) c.newInstance(params);
        } catch (InstantiationException ex) {
            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

    } catch (ClassNotFoundException ex) {
        Logger.getLogger(PrintOperatorsInfo.class.getName()).log(Level.SEVERE, null, ex);
    }



    Iterator iterator = algorithmParameter.keySet().iterator();
    List<String> parameterList = new ArrayList<String>();
    while (iterator.hasNext())
        parameterList.add((String)iterator.next());
    Properties algorithmOperator = PropUtils.getPropertiesWithPrefix(anAlgorithm, ".OPERATOR.");
    

    // AbYSS has a SBX and a Polynomial Mutation Operators
    if (name.equals("AbYSS")) {
       algorithmOperator.setProperty("crossover", "Crossover");
       algorithmOperator.setProperty("mutation","Mutation");
       anAlgorithm.setProperty(".VALUE.crossover","SBXCrossover");
       anAlgorithm.setProperty(".VALUE.mutation","PolynomialMutation");
    }

    
    iterator = algorithmOperator.keySet().iterator();
    // Configuring and adding the operators
    while (iterator.hasNext()) {
       String next = (String) iterator.next();
       Operator op = ((new PrintOperatorsInfo().getOperator(anAlgorithm.getProperty(".VALUE."+ next), properties)));
       Iterator iterator2 = parameterList.iterator();
       while (iterator2.hasNext()) {
           String parameterName = (String)iterator2.next();
           if (parameterName.startsWith(next)) {         
              String type = algorithmParameter.getProperty(parameterName);
              if (type.equals("int")) {
                op.setParameter(parameterName.substring(next.length()).toLowerCase(), new Integer(anAlgorithm.getProperty(".VALUE."+ parameterName)));
  
              } else if (type.equals("double")) {
                Double doubleValue = new Double(new Double(anAlgorithm.getProperty(".VALUE."+ parameterName)));
                if (doubleValue.isNaN()) {
                    doubleValue = 1.0 / p.getNumberOfVariables();
                }
                op.setParameter(parameterName.substring(next.length()).toLowerCase(), doubleValue);
  
              }
              iterator2.remove();
           }
       }

       if (name.equals("AbYSS") && next.equals("mutation")) {
    
         // SPECIAL CASE: AbYSS
         Operator improvement = new MutationLocalSearch(p,op);
    
         int rounds = new Integer(anAlgorithm.getProperty(".VALUE.improvementRounds"));
         improvement.setParameter("improvementRounds", rounds);
         algorithm.addOperator("improvement", improvement);
       } else {
         // Base case
         algorithm.addOperator(next, op);
       }
    }


    if (name.equals("GDE3") || name.equals("CellDE")) {
        Operator df = new DifferentialEvolutionCrossover();
        double F = new Double(anAlgorithm.getProperty(".VALUE.F"));
        double CR = new Double(anAlgorithm.getProperty(".VALUE.CR"));
        algorithm.addOperator("crossover",df);

        if (name.equals("GDE3")) {
            Operator selection = null;
            try {
              // Add the operators to the algorithm
              selection = SelectionFactory.getSelectionOperator("DifferentialEvolutionSelection");
            } catch (JMException ex) {
              Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
            algorithm.addOperator("selection",selection);
        } else {
            algorithm.addOperator("selection",new BinaryTournament());
        }
    } else if (name.equals("NSGAII")) {
        algorithm.addOperator("selection", new BinaryTournament2());
    }    else if (name.equals("SMPSO")) {
        Operator mutation;
            try {
                mutation = MutationFactory.getMutationOperator("PolynomialMutation");
                mutation.setParameter("probability",1.0/p.getNumberOfVariables());
                mutation.setParameter("distributionIndex",20.0);
                algorithm.addOperator("mutation",mutation);
            } catch (JMException ex) {
                Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
            }


    } else {
        algorithm.addOperator("selection", new BinaryTournament());
    }



    iterator = parameterList.iterator();
    // Adding operators and parameters
    while (iterator.hasNext()) {
       String next = (String) iterator.next();
       String type = algorithmParameter.getProperty(next);

       if (type.equals("int")) {
           algorithm.setInputParameter(next, new Integer(anAlgorithm.getProperty(".VALUE."+ next)));
       } else if (type.equals("double")) {
           algorithm.setInputParameter(next, new Double(anAlgorithm.getProperty(".VALUE."+ next)));
       }

    /*   else if (type.equals("Crossover")) {
           Operator op = ((new PrintOperatorsInfo().getOperator(anAlgorithm.getProperty(".VALUE."+ next), properties)));
           algorithm.addOperator(next, op);
       } else if (type.equals("Mutation")) {
           Operator op = ((new PrintOperatorsInfo().getOperator(anAlgorithm.getProperty(".VALUE."+ next), properties)));
           algorithm.addOperator(next, op);
       }*/
    }
    return algorithm;
  }






  /*
   * Writes the information of problem classes contained in the jMetal default package
   */
  public void printAlgorithmInfo() {
    try {
      //printAlgorithmInfo("AbYSS", "jmetal.metaheuristics.abyss");
      //printAlgorithmInfo("CellDE", "jmetal.metaheuristics.cellde");
      //printAlgorithmInfo("GDE3", "jmetal.metaheuristics.gde3");
      //printAlgorithmInfo("IBEA", "jmetal.metaheuristics.ibea");
      //printAlgorithmInfo("MOCell", "jmetal.metaheuristics.mocell");
      //printAlgorithmInfo("MOEAD", "jmetal.metaheuristics.moead");
      //printAlgorithmInfo("NSGAII", "jmetal.metaheuristics.nsgaII");
      //printAlgorithmInfo("OMOPSO", "jmetal.metaheuristics.omopso");
      //printAlgorithmInfo("PAES", "jmetal.metaheuristics.paes");
      //printAlgorithmInfo("SMPSO", "jmetal.metaheuristics.smpso");
      //printAlgorithmInfo("SPEA2", "jmetal.metaheuristics.spea2");
      //printAlgorithmInfo("SMPSO", "jmetal.metaheuristics.smpso");
    } catch (Exception ex) {
      Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
    }	  
  }
  
  /**
   * Example main
   */
  public static void main(String [] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {
//        try {
//            //(new PrintAlgorithmsInfo()).printAlgorithmInfo();
//            Algorithm alg = new PrintAlgorithmsInfo().getAlgorithm("NSGAII", Configuration.getSettings(), new ZDT1("Real"));
//            Operator selection = new jmetal.base.operator.selection.BinaryTournament2();
//            alg.addOperator("selection", selection);
//            alg.execute().printObjectivesToFile("FUNsiona");
//        } catch (JMException ex) {
//            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(PrintAlgorithmsInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
       (new PrintAlgorithmsInfo()).printAlgorithmInfo();

  }
} // PrintAlgorithmInfo
