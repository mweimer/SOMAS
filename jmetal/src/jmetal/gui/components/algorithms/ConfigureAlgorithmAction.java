/*
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class provides a single GUI for configuring algorithms
 */

package jmetal.gui.components.algorithms;

import jmetal.gui.ConfigurationsContainer;

import jmetal.gui.events.OkEvent;
import jmetal.gui.operators.ConfigureOperatorAction;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;
import java.util.Iterator;
import javax.swing.*;
import jmetal.gui.components.OkJFrame;
import jmetal.gui.components.OkJPanel;
import jmetal.gui.utils.Configuration;
import jmetal.gui.utils.PropUtils;
import jmetal.gui.warehouses.AlgorithmsWareHouse;
import jmetal.gui.listeners.OkEventListener;

public class ConfigureAlgorithmAction extends OkJPanel implements ConfigurationsContainer, OkEventListener {

    private static final long serialVersionUID = 8559151680852062304L;
    private Properties algorithmParameters_;
    private Properties crossoverProperties_;
    private Properties mutationProperties_;
    private Properties defaultProperties_;
    private Properties jMetalProperties_;
    private Properties algorithmOperators_;
    private String algorithmName_;
    



    private Object [][] windowsContent_;
    private JPanel [] windowsPanel_;

    public ConfigureAlgorithmAction(String name) {      
      // save the algorithmName      
      algorithmName_        =  name;
      jMetalProperties_     =  Configuration.getSettings();
      crossoverProperties_  =  PropUtils.getPropertiesWithPrefix(jMetalProperties_,"Crossover.");
      mutationProperties_   =  PropUtils.getPropertiesWithPrefix(jMetalProperties_,"Mutation.");
      algorithmParameters_  =  PropUtils.getPropertiesWithPrefix(jMetalProperties_,algorithmName_+".PARAMETER.");
      algorithmOperators_   =  PropUtils.getPropertiesWithPrefix(jMetalProperties_, name+".OPERATOR.");
      defaultProperties_    =  AlgorithmsWareHouse.getSettings(name);

    }

    public void draw() {
      Iterator<Object> iterator;
      int algorithmComponents = algorithmParameters_.keySet().size() +
                                algorithmOperators_.keySet().size();
      windowsContent_ = new Object[5][algorithmComponents+1];
      windowsContent_[0][0] = null;      
      windowsContent_[1][0] = new JLabel("Parameter");      
      windowsContent_[2][0] = new JLabel("Type");      
      windowsContent_[3][0] = new JLabel("Value");
      windowsContent_[4][0] = null;
      windowsPanel_  = new JPanel[algorithmParameters_.keySet().size()+
                                  algorithmOperators_.keySet().size()];
      int i = 0;

      iterator = algorithmParameters_.keySet().iterator();
      // Fill the windowsContent with basic parameters
      while (iterator.hasNext()) {
          windowsContent_[0][++i] = iterator.next(); 
          String type = algorithmParameters_.getProperty((String)windowsContent_[0][i]);


            windowsContent_[1][i]   = new JLabel((String)windowsContent_[0][i]);
            windowsContent_[2][i]   = new JLabel(type);            
            windowsContent_[3][i]   = new JTextField();
            ((JTextField)windowsContent_[3][i]).setColumns(4);

       
            String auxValue = defaultProperties_.getProperty(".DEFAULT."+(String)windowsContent_[0][i]);
            if (new Double(auxValue).equals(Double.NaN)) {
              auxValue = "1/(Number of Variables)";
              ((JTextField)windowsContent_[3][i]).setColumns(auxValue.length());
            }
            ((JTextField)windowsContent_[3][i]).setText(auxValue);
            windowsContent_[4][i] = windowsContent_[3][i];

            windowsPanel_[i-1] = new JPanel();
            ((JPanel) windowsPanel_[i-1]).add((JTextField)windowsContent_[3][i]);
            windowsPanel_[i-1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),(String)windowsContent_[0][i]));
      }

      iterator = algorithmOperators_.keySet().iterator();
      // Fill the windowsContent with operators
      while (iterator.hasNext()) {
          windowsContent_[0][++i] = iterator.next();
          String type = algorithmOperators_.getProperty((String)windowsContent_[0][i]);

          if (type.equals("Crossover")){

            windowsContent_[1][i]   = new JLabel((String)windowsContent_[0][i]);
            windowsContent_[2][i]   = new JLabel(type);
            JComboBox auxCombo = new JComboBox();
            Iterator<Object> iterator2 = crossoverProperties_.keySet().iterator();
            while (iterator2.hasNext()) {
               String crossoverOperator = (String) iterator2.next();
//               if (defaultProperties_.getProperty((String)windowsContent_[0][i]).contains(crossoverOperator)) {
//                 auxCombo.addItem(crossoverOperator);
//                 auxCombo.setSelectedItem(crossoverOperator);
//               } else {
                 auxCombo.addItem(crossoverOperator);
//               }
            }
            JButton configureButton = new JButton("...");
            OkJFrame ok = new OkJFrame(algorithmName_);
            ConfigureOperatorAction coa = new ConfigureOperatorAction(algorithmName_,auxCombo);
            ok.setOkJPanel(coa);


            coa.addOkEvenListener(ok);
            ok.addOkEvenListener(this);
            configureButton.addActionListener(ok);

            //configureButton.addActionListener(new ConfigureOperatorAction(algorithmName_,auxCombo,getFrameContainer()));
            JPanel auxPanel = new JPanel();
            auxPanel.setLayout(new BoxLayout(auxPanel,BoxLayout.X_AXIS));
            auxPanel.add(auxCombo);
            auxPanel.add(configureButton);
            windowsContent_[3][i] = auxPanel;
            windowsContent_[4][i] = auxCombo;

            windowsPanel_[i-1] = (JPanel) windowsContent_[3][i];
            //->windowsPanel_[i-1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),(String)windowsContent_[0][i]));

          }  else if (type.equals("Mutation")) {

            windowsContent_[1][i]   = new JLabel((String)windowsContent_[0][i]);
            windowsContent_[2][i]   = new JLabel(type);
            JComboBox auxCombo = new JComboBox();
      
            Iterator<Object> iterator2 = mutationProperties_.keySet().iterator();
            while (iterator2.hasNext()) {
               String mutationOperator = (String) iterator2.next();
//               if (defaultProperties_.getProperty((String)windowsContent_[0][i]).contains(mutationOperator)) {
//                 auxCombo.addItem(mutationOperator);
//                 auxCombo.setSelectedItem(mutationOperator);
//               } else {
                 auxCombo.addItem(mutationOperator);
//               }
            }


            JButton configureButton = new JButton("...");
            OkJFrame ok = new OkJFrame(algorithmName_);
            ConfigureOperatorAction coa = new ConfigureOperatorAction(algorithmName_,auxCombo);
            ok.setOkJPanel(coa);


            coa.addOkEvenListener(ok);
            ok.addOkEvenListener(this);
            configureButton.addActionListener(ok);

            JPanel auxPanel = new JPanel();
            auxPanel.setLayout(new BoxLayout(auxPanel,BoxLayout.X_AXIS));
            auxPanel.add(auxCombo);
            auxPanel.add(configureButton);
            windowsContent_[3][i] = auxPanel;
            windowsContent_[4][i] = auxCombo;

            windowsPanel_[i-1] = (JPanel) windowsContent_[3][i];
            //->windowsPanel_[i-1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),(String)windowsContent_[0][i]));


          }
      }


//      // Place the windowsContent in the panel
      GridBagConstraints c = new GridBagConstraints();
      setLayout(new GridBagLayout());
      c.insets = new Insets(2,2,2,2);
      c.weightx = 1.0;
      c.weighty = 1.0;
      int k = 1;
      for (int j = 0; j < windowsContent_[0].length; j++) {
        k = 1;
        while (k < windowsContent_.length-1) {
              c.gridx      = k - 1;
              c.gridy      = j;
              c.gridheight = 1;
              c.gridwidth  = 1;
              c.anchor = GridBagConstraints.WEST;
              if (k == windowsContent_.length-1)
                 c.anchor = GridBagConstraints.EAST;
              add((Component)windowsContent_[k][j],c);
              k++;
        }
      }
    }

  public void addConfiguration(Properties configuration, String name) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Properties getConfiguration(String name) {
    Properties algorithmSettings_ = AlgorithmsWareHouse.getSettings(algorithmName_);

  

    if (windowsContent_.length > 0) {
      for (int index = 1; index < windowsContent_[0].length; index++) {
        if (windowsContent_[4][index].getClass().equals(javax.swing.JComboBox.class)) {
          String operatorName = ((JComboBox) windowsContent_[4][index]).getSelectedItem().toString();


          // Add all the properties of the Operator
          Properties operatorSetting = PropUtils.getPropertiesWithPrefix(jMetalProperties_, ((JComboBox) windowsContent_[4][index]).getSelectedItem().toString());
          Iterator it = operatorSetting.keySet().iterator();
          while (it.hasNext()) {
              String next = (String) it.next();
              algorithmSettings_.setProperty(operatorName+next, operatorSetting.getProperty(next));
          }

          algorithmSettings_.setProperty(".VALUE."+(String) windowsContent_[0][index],operatorName);
          
          // Add the parameters of the new opeator
          Properties aux;
          aux = PropUtils.getPropertiesWithPrefix(jMetalProperties_, ((JComboBox) windowsContent_[4][index]).getSelectedItem().toString() + ".PARAMETER.");

          Iterator iterator = aux.keySet().iterator();
          while (iterator.hasNext()) {
            String parameter = (String) iterator.next();            
            if (!algorithmSettings_.containsKey(operatorName + ".VALUE." + parameter)) {
              String value;
              value = jMetalProperties_.getProperty(operatorName + ".DEFAULT." + parameter);
              algorithmSettings_.setProperty(operatorName + ".VALUE." + parameter, value);
            }
          }
        } else {
           String auxString = ((JTextField) windowsContent_[3][index]).getText();
           if (!auxString.equals("1/(Number of Variables)"))
              algorithmSettings_.setProperty(".VALUE."+(String) windowsContent_[0][index],
                    ((JTextField) windowsContent_[3][index]).getText());
           else
             algorithmSettings_.setProperty(".VALUE."+(String) windowsContent_[0][index],Double.NaN+"");
         
        }
      }
    }


  
    return algorithmSettings_;
  }

  public void catchOkEvent(OkEvent evt) {
      getFrameContainer().setVisible(true);
  }
} // ConfigureAlgorithmAction
