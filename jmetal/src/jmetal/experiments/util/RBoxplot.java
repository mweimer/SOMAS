/**
 * RBoxplot.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 *
 * @version 1.1
 */

package jmetal.experiments.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import jmetal.experiments.Experiment;

public class RBoxplot {
  /**
   * This script produces R scripts for generating eps files containing boxplots
   * of the results previosly obtained. The boxplots will be arranged in a grid
   * of rows x cols. As the number of problems in the experiment can be too high,
   * the @param problems includes a list of the problems to be plotted.
   * @param rows
   * @param cols
   * @param problems List of problem to plot
   * @param prefix Prefix to be added to the names of the R scripts
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   */
  public static  void generateScripts(int rows,
                                      int cols,
                                      String[] problems,
                                      String prefix,
                                      boolean notch,
                                      Experiment experiment)
                                            throws FileNotFoundException, IOException {
    // STEP 1. Creating R output directory

    String rDirectory = "R";
    rDirectory = experiment.experimentBaseDirectory_ + "/" +  rDirectory;
    System.out.println("R    : " + rDirectory);
    File rOutput;
    rOutput = new File(rDirectory);
    if (!rOutput.exists()) {
      new File( rDirectory).mkdirs();
      System.out.println("Creating " +  rDirectory + " directory");
    }

    for (int indicator = 0; indicator <  experiment.indicatorList_.length; indicator++) {
      System.out.println("Indicator: " +  experiment.indicatorList_[indicator]);
      String rFile =  rDirectory + "/" + prefix + "." +  experiment.indicatorList_[indicator] + ".Boxplot.R";

      FileWriter os = new FileWriter(rFile, false);
      os.write("postscript(\"" + prefix + "." +
               experiment.indicatorList_[indicator] +
              ".Boxplot.eps\", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)" +
              "\n");
      //os.write("resultDirectory<-\"../data/" + experimentName_ +"\"" + "\n");
      os.write("resultDirectory<-\"../data/" + "\"" + "\n");
      os.write("qIndicator <- function(indicator, problem)" + "\n");
      os.write("{" + "\n");

      for (int i = 0; i <  experiment.algorithmNameList_.length; i++) {
        os.write("file" +  experiment.algorithmNameList_[i] +
                "<-paste(resultDirectory, \"" +
                 experiment.algorithmNameList_[i] + "\", sep=\"/\")" + "\n");
        os.write("file" +  experiment.algorithmNameList_[i] +
                "<-paste(file" +  experiment.algorithmNameList_[i] + ", " +
                "problem, sep=\"/\")" + "\n");
        os.write("file" +  experiment.algorithmNameList_[i] +
                "<-paste(file" +  experiment.algorithmNameList_[i] + ", " +
                "indicator, sep=\"/\")" + "\n");
        os.write( experiment.algorithmNameList_[i] + "<-scan(" + "file" +  experiment.algorithmNameList_[i] + ")" + "\n");
        os.write("\n");
      } // for

      os.write("algs<-c(");
      for (int i = 0; i <  experiment.algorithmNameList_.length - 1; i++) {
        os.write("\"" +  experiment.algorithmNameList_[i] + "\",");
      } // for
      os.write("\"" +  experiment.algorithmNameList_[ experiment.algorithmNameList_.length - 1] + "\")" + "\n");

      os.write("boxplot(");
      for (int i = 0; i <  experiment.algorithmNameList_.length; i++) {
        os.write( experiment.algorithmNameList_[i] + ",");
      } // for
      if (notch) {
        os.write("names=algs, notch = TRUE)" + "\n");
      } else {
        os.write("names=algs, notch = FALSE)" + "\n");
      }
      os.write("titulo <-paste(indicator, problem, sep=\":\")" + "\n");
      os.write("title(main=titulo)" + "\n");

      os.write("}" + "\n");

      os.write("par(mfrow=c(" + rows + "," + cols + "))" + "\n");

      os.write("indicator<-\"" +  experiment.indicatorList_[indicator] + "\"" + "\n");

      for (int i = 0; i < problems.length; i++) {
        os.write("qIndicator(indicator, \"" + problems[i] + "\")" + "\n");
      }

      os.close();
    } // for
    } // generateRBoxplotScripts

}