/**
 * ProblemsPanel.java
 * @author Juan J. Durillo
 * @version 1.0
 **/
package jmetal.gui.components.problems;

// This class is amied at providing a Panel containing all the problems

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import javax.swing.*;
import jmetal.gui.ConfigurationsContainer;
import jmetal.gui.components.OkJFrame;
import jmetal.gui.components.OkJPanel;
import jmetal.gui.events.OkEvent;
import jmetal.gui.listeners.OkEventListener;
import jmetal.gui.utils.Configuration;
import jmetal.gui.utils.PropUtils;
import jmetal.gui.warehouses.ProblemWareHouse;

public class ProblemsPanel extends OkJPanel implements ConfigurationsContainer, OkEventListener{


  private static final int WIDTH_  = 200;
  private static final int HEIGHT_ = 400;

  private Properties jMetalProperties_;
  private List<JCheckBox> problemsBox_;
  
  // ProblemsPanel
  public ProblemsPanel()
  {    
    jMetalProperties_ = Configuration.getSettings();

    
    Properties aux = PropUtils.getPropertiesWithPrefix(jMetalProperties_, "PROBLEM.");
    Iterator iterator = aux.keySet().iterator();
    problemsBox_ = new ArrayList<JCheckBox>(aux.keySet().size());

 
    while (iterator.hasNext()) {
      String problemName = (String)iterator.next();
      ProblemWareHouse.addProblem(problemName, PropUtils.setDefaultParameters2(jMetalProperties_,problemName));
// MOD-1      problemsBox_.add(new JCheckBox(problemName));
    }
  
  } // ProblemsPanel


  // Return a Panel containing all the problems
  public Container getPanel() {
     // The base container is a ScrollPane() container
     JScrollPane basePanel = new JScrollPane();
  
     Dimension d = new Dimension(WIDTH_,HEIGHT_);
     basePanel.setMinimumSize(d);
     basePanel.setMaximumSize(d);
     basePanel.setSize(d);
    

     // Panel containing the information
     JPanel panelContainer = new JPanel();
     panelContainer.setMaximumSize(d);
     panelContainer.setMinimumSize(d);
     panelContainer.setSize(d);



     panelContainer.setLayout(new GridBagLayout());
     GridBagConstraints c = new GridBagConstraints();
     c.insets = new Insets(2,2,2,2);

     c.weightx = 1.0;
     c.weighty = 1.0;

     JLabel label = new JLabel("Problems");
     c.gridx = 0;
     c.gridy = 0;
     c.gridheight = 1;
     c.gridwidth  = 2;
     c.anchor = GridBagConstraints.CENTER;
     panelContainer.add(label,c);



     for (int i = 0; i < ProblemWareHouse.size(); i++) {
           JCheckBox tmpCheck = new JCheckBox(ProblemWareHouse.getProblemName(i));
           tmpCheck.addItemListener(new ItemListener() {
              public void itemStateChanged(ItemEvent ev) {
                JCheckBox jcb = (JCheckBox)ev.getSource();
//               if (jcb.isSelected())
//                  selectedCount_++;
//               else if (selectedCount_  > 0)
//                   selectedCount_--;
              }
           });
           problemsBox_.add(tmpCheck);
           c.gridx = 0;
           c.gridy = i+1;
           c.gridwidth = 1;
           c.gridheight = 1;
           c.anchor = GridBagConstraints.WEST;
           panelContainer.add(problemsBox_.get(i),c);

           JButton jbutton = new JButton("...");
           OkJFrame sa = new OkJFrame(ProblemWareHouse.getProblemName(i));
           ConfigureProblemAction cpa = new ConfigureProblemAction(ProblemWareHouse.getProblemName(i));
           sa.setOkJPanel(cpa);
         
           cpa.addOkEvenListener(sa);
           sa.addOkEvenListener(this);
           jbutton.addActionListener(sa);
           c.gridx = 1;
           c.gridy = i+1;
           c.gridwidth = 1;
           c.gridheight = 1;
           c.anchor = GridBagConstraints.EAST;
           panelContainer.add(jbutton,c);
     }
     basePanel.setViewportView(panelContainer);
     return basePanel;
  } // getPanel


  // getSelectedProblems
  public String [] getSelectedProblems() {
    
     // Calculates the number of selected problems
     int selectedCount = 0;
     Iterator<JCheckBox> iterator = problemsBox_.iterator();
     while (iterator.hasNext())
       if (iterator.next().isSelected())
         selectedCount++;

     String [] result = new String[selectedCount];
     int index = 0;
     for (int i = 0; i < ProblemWareHouse.size();i++) {
         if (problemsBox_.get(i).isSelected()) {
             result[index] = ProblemWareHouse.getProblemName(i);
             index++;
         }
     }
     return result;
  }

    // getSelectedAlgorithms
  public Properties [] getParameters() {

     // Calculates the number of selected problems
     int selectedCount = 0;
     Iterator<JCheckBox> iterator = problemsBox_.iterator();
     while (iterator.hasNext())
       if (iterator.next().isSelected())
         selectedCount++;

     Properties [] result = new Properties[selectedCount];     
     int index = 0;
     for (int i = 0; i < ProblemWareHouse.size();i++) {
         if (problemsBox_.get(i).isSelected()) {
             result[index] = ProblemWareHouse.getSettings(i);
             index++;
         }
     }
     return result;
  }



    // getSelectedProblems
  public String [] getSelectedProblemsFronts() {
      // Calculates the number of selected problems
     int selectedCount = 0;
     Iterator<JCheckBox> iterator = problemsBox_.iterator();
     while (iterator.hasNext())
       if (iterator.next().isSelected())
         selectedCount++;


     String [] result = new String[selectedCount];
     int index = 0;
     for (int i = 0; i < ProblemWareHouse.size();i++) {
         if (problemsBox_.get(i).isSelected()) {
             result[index] = ProblemWareHouse.getProblemName(i)+".pf";
             index++;
         }
     }
     return result;
  }

  public void addConfiguration(Properties configuration, String name) {
    int i = 0;
    while ((i < ProblemWareHouse.size()) &&
           (!name.equals(ProblemWareHouse.getProblemName(i)))) {
      i++;
    }
    if (i < ProblemWareHouse.size()) {
      if (ProblemWareHouse.getSettings(i) == null)
        ProblemWareHouse.setSettings(i, new Properties());
        
      Iterator iterator = configuration.keySet().iterator();
      while (iterator.hasNext()) {
        String next = (String) iterator.next();
        ProblemWareHouse.getSettings(i).setProperty(next, configuration.getProperty(next));
      }
    }
  }

  public Properties getConfiguration(String name) {
    int i = 0;
    while ((i < ProblemWareHouse.size()) &&
           (!name.equals(ProblemWareHouse.getProblemName(i)))) {
      i++;
    }
    if (i < ProblemWareHouse.size())
      return ProblemWareHouse.getSettings(i);
    else
      return null;
  }

  public void catchOkEvent(OkEvent evt) {
    getFrameContainer().setVisible(true);
  }

  @Override
  public void draw() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

} // ProblemsPanel