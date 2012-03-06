    /**
 * AlgorithmsConfigurationPanel.java
 * @author Juan J. Durillo
 * @version 1.0
 **/
package jmetal.gui.components.algorithms;

// This class is amied at providing a Panel containing all the algorithms

import jmetal.gui.components.OkJFrame;
import jmetal.gui.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.*;
import javax.swing.JCheckBox;
import jmetal.gui.components.OkJPanel;
import jmetal.gui.events.OkEvent;
import jmetal.gui.listeners.OkEventListener;
import jmetal.gui.utils.Configuration;
import jmetal.gui.utils.PropUtils;
import jmetal.gui.warehouses.AlgorithmsWareHouse;

public class AlgorithmsConfigurationPanel extends OkJPanel implements ConfigurationsContainer,
                                                                      OkEventListener {

  /**
  * 
  */
  private static final long serialVersionUID = 1L;
  private static final int WIDTH_  = 150;
  private static final int HEIGHT_ = 400;
  
  private List<JCheckBox> algorithmsBox_;
  private List<ConfigureAlgorithmAction> algoritmsCaa_;
  private Properties jMetalProperties_;
  private int selectedCount_;


  private JScrollPane basePanel_ = null;
  
  // AlgorithmsConfigurationPanel
  public AlgorithmsConfigurationPanel()
  {
    selectedCount_ = 0;
    jMetalProperties_ = Configuration.getSettings();
    Properties aux = PropUtils.getPropertiesWithPrefix(jMetalProperties_, "Algorithm.");
    Iterator<Object> iterator = aux.keySet().iterator();
    algorithmsBox_     = new ArrayList<JCheckBox>(aux.keySet().size());
    algoritmsCaa_      = new ArrayList<ConfigureAlgorithmAction>(aux.keySet().size());
    while (iterator.hasNext()) {
       String algorithmName = (String)iterator.next();
       AlgorithmsWareHouse.addAlgorithm(algorithmName,PropUtils.setDefaultParameters2(jMetalProperties_,algorithmName));
    }
     basePanel_ = new JScrollPane();
     drawPanel();
  } // AlgorithmsConfigurationPanel


  public void addAlgorithm() {
    Configuration.reload();
    jMetalProperties_ = Configuration.getSettings();
    Properties aux = PropUtils.getPropertiesWithPrefix(jMetalProperties_, "Algorithm.");
    Iterator<Object> iterator = aux.keySet().iterator();

    while (iterator.hasNext()) {
       String algorithmName = (String) iterator.next();
       int algorithmIndex = AlgorithmsWareHouse.getAlgorithmIndex(algorithmName);
       if (algorithmIndex == -1) {
         AlgorithmsWareHouse.addAlgorithm(algorithmName, PropUtils.setDefaultParameters2(jMetalProperties_,algorithmName));
         algorithmsBox_.add(new JCheckBox(algorithmName));
       }
     }
     selectedCount_ = 0;
     drawPanel();
  }


  public Container getPanel() {
      return basePanel_;
  }

  // Return a Panel containing all the algorithms
  public void drawPanel() {
     // The base container is a ScrollPane() container
     Dimension d = new Dimension(WIDTH_,HEIGHT_);
     basePanel_.setMinimumSize(d);
     basePanel_.setMaximumSize(d);
     basePanel_.setSize(d);

     // Panel containing the information
     JPanel panelContainer = new JPanel();
     panelContainer.setMaximumSize(d);
     panelContainer.setMinimumSize(d);
     panelContainer.setSize(d);


     panelContainer.setLayout(new GridBagLayout());
     GridBagConstraints c = new GridBagConstraints();


     c.gridx = 0;
     c.gridy = 0;
     c.weightx = 1.0;
     c.weighty = 1.0;
     c.anchor = GridBagConstraints.CENTER;
     c.gridwidth = 2;
     JLabel label = new JLabel("Algorithms");
     panelContainer.add(label,c);
   //  algorithmsBox_ = new JCheckBox[algorithms_.length];

     for (int i = 0; i < AlgorithmsWareHouse.size(); i++) {  
         JCheckBox tmpCheck = new JCheckBox(AlgorithmsWareHouse.getAlgorithmName(i));
         tmpCheck.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
               JCheckBox jcb = (JCheckBox)ev.getSource();
               if (jcb.isSelected())
                  selectedCount_++;
               else if (selectedCount_  > 0)
                   selectedCount_--;
            }
         });
         algorithmsBox_.add(tmpCheck);
         c.gridx = 0;
         c.gridy = i+1;
         c.gridwidth = 1;
         //c.anchor = GridBagConstraints.WEST;
         c.anchor = GridBagConstraints.WEST;
         //c.fill  = GridBagConstraints.FIRST_LINE_START;
         panelContainer.add(algorithmsBox_.get(i),c);

         JButton configureButton = new JButton("...");
         OkJFrame sa = new OkJFrame(AlgorithmsWareHouse.getAlgorithmName(i));
         ConfigureAlgorithmAction caa = new ConfigureAlgorithmAction(AlgorithmsWareHouse.getAlgorithmName(i));
         algoritmsCaa_.add(caa);
         sa.setOkJPanel(caa);
         
         caa.addOkEvenListener(sa);
         sa.addOkEvenListener(this);
         configureButton.addActionListener(sa);

         c.gridx = 1;
         c.gridy = i+1;
         c.gridwidth = 1;
         //c.anchor = GridBagConstraints.EAST;
         c.anchor = GridBagConstraints.EAST;
         //c.fill  = GridBagConstraints.FIRST_LINE_START;
         panelContainer.add(configureButton,c);

     }

     basePanel_.setViewportView(panelContainer);

     basePanel_.repaint();
  } // getPanel


  // getSelectedAlgorithms
  public String [] getSelectedAlgorithms() {
     String [] result = new String[selectedCount_];
     
     int index = 0;
     for (int i = 0; i < AlgorithmsWareHouse.size();i++) {
         if (algorithmsBox_.get(i).isSelected()) {
             
             result[index] = AlgorithmsWareHouse.getAlgorithmName(i);
             index++;
         }
     }
     return result;
  }

    // getSelectedAlgorithms
  public Properties [] getParameters() {
     Properties [] result = new Properties[selectedCount_];
     
     int index = 0;
     for (int i = 0; i < AlgorithmsWareHouse.size();i++) {
         if (algorithmsBox_.get(i).isSelected()) {
             algoritmsCaa_.get(i).draw();
             result[index] = algoritmsCaa_.get(i).getConfiguration(AlgorithmsWareHouse.getAlgorithmName(i));;
             index++;
         }
     }
     return result;
  }


  public void addConfiguration(Properties configuration, String name) {
    int i = 0;
    while ((i < AlgorithmsWareHouse.size()) &&
           (!name.equals(AlgorithmsWareHouse.getAlgorithmName(i)))) {
      i++;
    }
    if (i < AlgorithmsWareHouse.size()) {
      if (AlgorithmsWareHouse.getSettings(i) == null)
      AlgorithmsWareHouse.setSettings(i, new Properties());
      Iterator<Object> iterator = configuration.keySet().iterator();
      while (iterator.hasNext()) {
        String next = (String) iterator.next();
        AlgorithmsWareHouse.getSettings(i).setProperty(next, configuration.getProperty(next));
      }
    }
  }

  public Properties getConfiguration(String name) {
    int i = 0;
    while ((i < AlgorithmsWareHouse.size()) &&
           (!name.equals(AlgorithmsWareHouse.getAlgorithmName(i)))) {
      i++;
    }
    if (i < AlgorithmsWareHouse.size())
      return AlgorithmsWareHouse.getSettings(i);
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

} // AlgorithmsConfigurationPanel
