/**
 * ExecutionGUI.java
 * @author Juan J. Durillo
 * @version 1.0
 * 
 * This class is aimed at executing and showing the Pareto Front computed by
 * a multi-objective algorithm
 */

package jmetal.gui;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.gui.plot.Plot2Da;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.operator.selection.BinaryTournament2;
import jmetal.gui.components.algorithms.AddAlgorithm;
import jmetal.gui.operators.AddOperator;
import jmetal.gui.components.problems.AddProblem;

import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;

public class SimpleExecutionSupportGUI extends JFrame {
SingleExecutionPanel sep = new SingleExecutionPanel();
Plot2Da plot2D = new Plot2Da();
JColorChooser jcc = new JColorChooser();
  private JMenuBar menuBar_;

  public SimpleExecutionSupportGUI() {

    // Giving a title to this window
    setTitle("jMetal Simple Execution GUI");

    // Obtaining the container into the jFrame
    Container container = getContentPane();

    // Creating a TabbedPane to store the different configurations
    JTabbedPane tabbed = new JTabbedPane();

    tabbed.addTab("Configuration 1", sep);

    // Creating a JPanel for the tabbed and the plot areas
    JPanel tabAndPlot = new JPanel();
    tabAndPlot.setLayout(new BoxLayout(tabAndPlot,BoxLayout.X_AXIS));
    tabAndPlot.add(tabbed);
    tabAndPlot.add(plot2D);

    jcc.setColor(Color.BLACK);
    AbstractColorChooserPanel [] panels = jcc.getChooserPanels();
    for (int i = 1; i < panels.length; i++) {
      jcc.removeChooserPanel(panels[i]);
    }
    jcc.setPreviewPanel(new JPanel());

    container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
    container.add(tabAndPlot);

    JButton execute = new JButton("Execute");
    execute.addActionListener(new ActionListener() {

      @SuppressWarnings("empty-statement")
      public void actionPerformed(ActionEvent e) {
        Problem problem = null;   // The problem to solve
       

        Properties problemSettings  = sep.pcp.getConfiguration((String)sep.pcp.problemsBox_.getSelectedItem());
        Properties algorithmSettings = sep.acp.getConfiguration((String)sep.acp.algorithmsBox_.getSelectedItem());
        String problemName           = (String) sep.pcp.problemsBox_.getSelectedItem();
        String algorithmName         = (String) sep.acp.algorithmsBox_.getSelectedItem();

        try {
          
          if ((problemSettings == null) || (problemSettings.size()==0)) {
            Object[] params = {}; // Parameters of the problem
            problem = (new ProblemFactory()).getProblem(problemName, params);
          } else {
            problem = (new jmetal.gui.utils.PrintProblemsInfo()).getProblem(problemName, problemSettings);
          }

          if (problem.getNumberOfObjectives() > 2) {
              javax.swing.JOptionPane.showMessageDialog(null, "Sorry, current version of Single Execution Support GUI works only with bi-objective problems");
          } else {
          
            
             try {
               Object [] settingsParams = {problem} ;
               Algorithm algorithm_ = (new jmetal.gui.utils.PrintAlgorithmsInfo()).getAlgorithm(algorithmName, algorithmSettings, problem);
  
               long initTime      ;
               long estimatedTime ;    
               initTime = System.currentTimeMillis();
               
  	             algorithm_.execute().printObjectivesToFile("Success!");
	             estimatedTime = System.currentTimeMillis() - initTime;
	          
            } catch (ClassNotFoundException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
            }

             plot2D.plot_.refresh("Success!",jcc.getColor(),plot2D.check_.isSelected());
             plot2D.plot_.repaint();
             File f = new File("Success!");
             f.delete();
          }
        } catch (IllegalArgumentException ex) {
          Logger.getLogger(SimpleExecutionSupportGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMException ex) {
          Logger.getLogger(SimpleExecutionSupportGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

    });

    JPanel aux = new JPanel();
    aux.setLayout(new BoxLayout(aux,BoxLayout.X_AXIS));
    aux.add(Box.createHorizontalGlue());
    aux.add(execute);

    container.add(Box.createHorizontalStrut(30));
    container.add(aux);

     // Adding a MenuBar
     menuBar_ = new JMenuBar();

     // File menu
     {
       JMenu menu = new JMenu("File");
       menu.setMnemonic('F');

       JMenuItem menuItemExit = new JMenuItem("Exit");
       menuItemExit.addActionListener(new ActionListener() {
        @SuppressWarnings("empty-statement")
        public void actionPerformed(ActionEvent e)
        {
          System.exit(0);
        }
       });
       menu.add(menuItemExit);
       menuBar_.add(menu);
     }

     // Add menu
     {
       JMenu menu = new JMenu("Add ...");
       menu.setMnemonic('A');

       JMenuItem menuItemAlgorithm  = new JMenuItem("Algorithm");
       menuItemAlgorithm.addActionListener(new AddAlgorithm(sep.acp));

       JMenuItem menuItemOperator   = new JMenuItem("Operator");
       menuItemOperator.addActionListener(new AddOperator());
       JMenuItem menuItemIndicator  = new JMenuItem("Quality Indicator");

       JMenuItem menuItemProblem    = new JMenuItem("Problem");
       menuItemProblem.addActionListener(new AddProblem());

       menu.add(menuItemAlgorithm);
       menu.add(menuItemOperator);
       menu.add(menuItemProblem);
       menu.add(menuItemIndicator);
       menuBar_.add(menu);
     }


     // File menu
     {
       JMenu menu = new JMenu("Configure");
       menu.setMnemonic('C');

       JMenuItem menuItemConfigure = new JMenuItem("Change Color");

       menuItemConfigure.addActionListener(new ActionListener() {
        @SuppressWarnings("empty-statement")
        public void actionPerformed(ActionEvent e)
        {
          JFrame auxFrame = new JFrame("Select a new color");
          auxFrame.add(jcc);
          auxFrame.setSize(jcc.getSize());
          auxFrame.pack();
          Toolkit toolkit = Toolkit.getDefaultToolkit();
          Dimension screenSize = toolkit.getScreenSize();
          auxFrame.setLocation(screenSize.width /2 - auxFrame.getWidth()/2,
                        screenSize.height/2 - auxFrame.getHeight()/2);
          auxFrame.setVisible(true);
        }
       });
       menu.add(menuItemConfigure);
       menuBar_.add(menu);
     }

     setJMenuBar(menuBar_);

    // Setting the size of the Window
    pack();

    // Establishing the Default Location of the windows

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    setLocation(screenSize.width /2 - getSize().width/2,
                screenSize.height/2 - getSize().height/2);

    // Making the frame visible
    setResizable(false);
    setVisible(true);
  }

  public static void main(String [] args) {
    new SimpleExecutionSupportGUI();
  }
}
