/*
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class provides a single GUI for configuring algorithms
 */

package jmetal.gui.components.problems;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Properties;
import java.util.Iterator;
import javax.swing.*;
import jmetal.gui.ConfigurationsContainer;
import jmetal.gui.components.OkJPanel;
import jmetal.gui.utils.Configuration;
import jmetal.gui.utils.PropUtils;
import jmetal.gui.warehouses.ProblemWareHouse;



public class ConfigureProblemAction extends OkJPanel implements ConfigurationsContainer {

    private Properties problemParameters_;
    private Properties jMetalProperties_;
    private Properties defaultProperties_;
    private String problemName_;
    //protected ConfigurationsContainer problemConfigurations_;

    private Object [][] windowsContent_;

    // String name, AlgorithmConfigurations algorithmConfigurations
    public ConfigureProblemAction(String name) {      
      // save the algorithmName
      problemName_       = name;
      jMetalProperties_  = Configuration.getSettings();
      problemParameters_ = PropUtils.getPropertiesWithPrefix(jMetalProperties_,problemName_+".PARAMETER.");
      defaultProperties_ = PropUtils.getPropertiesWithPrefix(ProblemWareHouse.getSettings(problemName_),".DEFAULT.");
    
    }

    public void draw() {

      if (problemParameters_.keySet().size() > 0) {
      Iterator iterator = problemParameters_.keySet().iterator();


      windowsContent_ = new Object[4][problemParameters_.keySet().size()+1];
      windowsContent_[0][0] = null;
      windowsContent_[1][0] = new JLabel("Parameter");
      windowsContent_[2][0] = new JLabel("Type");
      windowsContent_[3][0] = new JLabel("Value");
      int i = 0;
      // Fill the windowsContent

      while (iterator.hasNext()) {

          windowsContent_[0][++i] = iterator.next();
          String type = problemParameters_.getProperty((String)windowsContent_[0][i]);

          if (type.equals("SolutionType")){
      
            windowsContent_[1][i]   = new JLabel((String)windowsContent_[0][i]);
            windowsContent_[2][i]   = new JLabel(type);
            windowsContent_[3][i]   = new JComboBox();

            
            Properties solutionTypes = PropUtils.getPropertiesWithPrefix(jMetalProperties_,
                                                                          "SOLUTION_TYPE.");


            String defaultType = defaultProperties_.getProperty((String)windowsContent_[0][i]);
            
            Iterator iterator2 = solutionTypes.keySet().iterator();
            ((JComboBox)windowsContent_[3][i]).addItem("");
            while (iterator2.hasNext()) {
                String aux = (String)iterator2.next();
                ((JComboBox)windowsContent_[3][i]).addItem(aux);
                if (aux.equalsIgnoreCase(defaultType))
                    ((JComboBox)windowsContent_[3][i]).setSelectedItem(aux);
            }

          } else {


            windowsContent_[1][i]   = new JLabel((String)windowsContent_[0][i]);
            windowsContent_[2][i]   = new JLabel(type);
            windowsContent_[3][i]   = new JTextField();
            ((JTextField)windowsContent_[3][i]).setText(defaultProperties_.getProperty((String)windowsContent_[0][i]));
            ((JTextField)windowsContent_[3][i]).setColumns(4);
          }

      }



      // Place the windowsContent in the panel
      GridBagConstraints c = new GridBagConstraints();
      setLayout(new GridBagLayout());
      c.insets = new Insets(4,4,4,4);
      c.weightx = 1.0;
      c.weighty = 1.0;
      int k = 1;
      for (int j = 0; j < windowsContent_[0].length; j++) {
        k = 1;
        while (k < windowsContent_.length) {
          if (!windowsContent_[k][j].getClass().equals(JComboBox.class)) {
              c.gridx      = k - 1;
              c.gridy      = j;
              c.gridheight = 1;
              c.gridwidth  = 1;
              c.anchor     = c.WEST;
              if (k == windowsContent_.length-1)
                 c.anchor = c.EAST;
              add((Component)windowsContent_[k][j],c);
              k++;
          } else {
              c.gridx      = k - 1;
              c.gridy      = j;
              c.gridheight = 1;
              c.gridwidth  = 1;
              c.anchor     = c.WEST;
              if (k == windowsContent_.length-1)
                 c.anchor = c.EAST;
              add((Component)windowsContent_[k][j],c);
              k = windowsContent_.length;
          }
        }
      }



      c.gridx = 2;
      c.gridy = windowsContent_[0].length;
//      JButton okButton = new JButton("OK");
//      okButton.addActionListener(new ActionListener() {
//
//        public void actionPerformed(ActionEvent e) {
//
//          //problemPanel_.problemSettings[index_] = new Properties();
//          Properties aux = new Properties();
//          if (windowsContent_.length > 0) {
//            for (int index = 1; index < windowsContent_[0].length;index++) {
//
//              if (windowsContent_[3][index].getClass().equals(javax.swing.JComboBox.class)) {
//
//                if (!((JComboBox)windowsContent_[3][index]).getSelectedItem().equals("")){
//                  String typeName = ((JComboBox)windowsContent_[3][index]).getSelectedItem().toString();
//                  aux.setProperty((String)windowsContent_[0][index],typeName);
//                }
//
//              } else {
//                  // If the textField contains anything, set the default value
//                  if (!(((JTextField)windowsContent_[3][index]).getText() == null) &&
//                      !(((JTextField)windowsContent_[3][index]).getText().equals(""))) {
//                     aux.setProperty((String)windowsContent_[0][index],
//                                    ((JTextField)windowsContent_[3][index]).getText());
//                  }
//              }
//            }
//          }
//
//          //problemConfigurations_.addConfiguration(aux, problemName_);
//          ProblemWareHouse.addProblem(problemName_, aux);
//          newOkEvent(new OkEvent(this));
//        }
//
//     } );
//      add(okButton,c);
      
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      Dimension screenSize = toolkit.getScreenSize();
     // problemPanel_.mainContainer_.setVisible(false);
      //frame.
      }
    }

  public void addConfiguration(Properties configuration, String name) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Properties getConfiguration(String name) {
      //problemPanel_.problemSettings[index_] = new Properties();
      Properties aux = ProblemWareHouse.getSettings(problemName_);
      
      // If the users wants to use the 2D gui
    if (windowsContent_.length > 0) {
         for (int index = 1; index < windowsContent_[0].length;index++) {
          if (windowsContent_[3][index].getClass().equals(javax.swing.JComboBox.class)) {

            if (!((JComboBox)windowsContent_[3][index]).getSelectedItem().equals("")){
                  String typeName = ((JComboBox)windowsContent_[3][index]).getSelectedItem().toString();
                  aux.setProperty(".VALUE."+(String)windowsContent_[0][index],typeName);
            }

          } else {
                  // If the textField contains anything, set the default value
              if (!(((JTextField)windowsContent_[3][index]).getText() == null) &&
                      !(((JTextField)windowsContent_[3][index]).getText().equals(""))) {
                     aux.setProperty(".VALUE."+(String)windowsContent_[0][index],
                                    ((JTextField)windowsContent_[3][index]).getText());
              }
          }
        }

      }


      //problemConfigurations_.addConfiguration(aux, problemName_);
      int problemIndex = ProblemWareHouse.getProblemIndex(problemName_);
      if (problemIndex == -1)
        ProblemWareHouse.addProblem(problemName_, aux);
      else
        ProblemWareHouse.setSettings(problemIndex, aux);
      return aux;
  }
} // ConfigureAlgorithmAction
