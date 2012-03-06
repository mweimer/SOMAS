/**
 * QualityIndicatorsPanel.java
 * @author Juan J. Durillo
 * @version 1.0
 **/
package jmetal.gui.qualityIndicators;

// This class is amied at providing a Panel containing all the qualityIndicators

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

public class QualityIndicatorsPanel {

  private static final int WIDTH_  = 150;
  private static final int HEIGHT_ = 400;

  private String [] qualityIndicators_ =
  {
     "HV","SPREAD","EPSILON","IGD"
  };

  private JCheckBox [] qualityIndicatorsBox_;
  protected int selectedCount_;

  // QualityIndicatorsPanel
  public QualityIndicatorsPanel()
  {
    selectedCount_ = 0;
  } // QualityIndicatorsPanel


  // Return a Panel containing all the qualityIndicators
  public Container getPanel() {
     // The base container is a ScrollPane() container
     JLabel label = new JLabel("Quality Indicators");

     // Panel containing the information
     JPanel panelContainer = new JPanel();


     panelContainer.setLayout(new GridBagLayout());
     GridBagConstraints c = new GridBagConstraints();
     c.insets = new Insets(2,2,2,2);

     c.weightx = 1.0;
     c.weighty = 1.0;

     c.gridx = 0;
     c.gridy = 0;
     c.gridheight = 1;
     c.gridwidth = 1;
     c.anchor = GridBagConstraints.NORTH;
     panelContainer.add(label,c);

     qualityIndicatorsBox_ = new JCheckBox[qualityIndicators_.length];

     for (int i = 0; i < qualityIndicators_.length; i++) {
         qualityIndicatorsBox_[i] = new JCheckBox(qualityIndicators_[i]);
         qualityIndicatorsBox_[i].addItemListener(new ItemListener() {

         public void itemStateChanged(ItemEvent ev) {
               JCheckBox jcb = (JCheckBox)ev.getSource();
               if (jcb.isSelected())
                  selectedCount_++;
               else if (selectedCount_  > 0)
                   selectedCount_--;
            }

         });

         c.gridx = 0;
         c.gridy = i+1;
         c.gridheight = 1;
         c.gridwidth = 1;
         c.anchor = GridBagConstraints.NORTHWEST;
         panelContainer.add(qualityIndicatorsBox_[i],c);
     }


     JScrollPane basePanel = new JScrollPane(panelContainer);
     Dimension d = new Dimension(WIDTH_,HEIGHT_);
     basePanel.setMinimumSize(d);
     basePanel.setMaximumSize(d);
     basePanel.setSize(d);

     

     return basePanel;
     //return panelContainer;
  } // getPanel


  // getSelectedQualityIndicators
  public String [] getSelectedQualityIndicators() {
     String [] result = new String[selectedCount_];
     int index = 0;
     for (int i = 0; i < qualityIndicators_.length;i++) {
         if (qualityIndicatorsBox_[i].isSelected()) {
             result[index] = qualityIndicators_[i];
             index++;
         }
     }
     return result;
  }

} // QualityIndicatorsPanel