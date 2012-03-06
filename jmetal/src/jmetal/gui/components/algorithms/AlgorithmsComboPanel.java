/**
 * AlgorithmsConfigurationPanel.java
 * @author Juan J. Durillo
 * @version 1.0
 **/
package jmetal.gui.components.algorithms;

// This class is amied at providing a Panel containing all the algorithms

import jmetal.gui.ConfigurationsContainer;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.*;
import jmetal.gui.components.OkJPanel;
import jmetal.gui.utils.*;
import jmetal.gui.warehouses.AlgorithmsWareHouse;

public class AlgorithmsComboPanel extends OkJPanel implements ConfigurationsContainer {

  private static final long serialVersionUID = 5327707118619333469L;
  public JComboBox algorithmsBox_;
  protected Properties jMetalProperties;
  ConfigurationsContainer selfReference_;
  private JPanel [] paneles;
  private String [] names;
  private JPanel jpanel_;

  // AlgorithmsConfigurationPanel
  public AlgorithmsComboPanel() {
    
    jMetalProperties = Configuration.getSettings();
    Properties aux = PropUtils.getPropertiesWithPrefix(jMetalProperties, "Algorithm.");
    Iterator<Object> iterator = aux.keySet().iterator();
    
    algorithmsBox_     = new JComboBox();
    jpanel_            = new JPanel();
    jpanel_.setLayout(new CardLayout());
    selfReference_ = this;

    String name;    
    
    while (iterator.hasNext()) {
       name = (String)iterator.next();
       AlgorithmsWareHouse.addAlgorithm(name, PropUtils.setDefaultParameters2(jMetalProperties,name));
    }

    paneles = new JPanel[(aux.keySet().size())];
    names   = new String[aux.keySet().size()];
    
    for (int i = 0; i < AlgorithmsWareHouse.size();i++) {    
      name = AlgorithmsWareHouse.getAlgorithmName(i);
      algorithmsBox_.addItem(name);
      paneles[i] = new ConfigureAlgorithmAction(name);
      ((ConfigureAlgorithmAction)paneles[i]).draw();
      names[i] = name;
    }
    
    int ancho = -1, alto = -1;
    for (int j = 0; j < paneles.length;j++) {
      if (paneles[j].getSize().height > alto)
        alto = paneles[j].getSize().height;
      
      if (paneles[j].getSize().width > ancho)
        ancho = paneles[j].getSize().width;
    }

    Dimension d = new Dimension();
    d.setSize(ancho, alto);

    for (int j = 0; j < paneles.length; j++) {
       paneles[j].setSize(d);
       paneles[j].setMaximumSize(d);
       paneles[j].setMinimumSize(d);
       jpanel_.add(paneles[j],AlgorithmsWareHouse.getAlgorithmName(j));
    }

    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.insets = new Insets(2,2,2,2);

    c.gridheight = 1;
    c.gridwidth  = 1;
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.fill   = GridBagConstraints.HORIZONTAL;
    add(new JLabel("Algorithm"),c);


    c.gridheight = 1;
    c.gridwidth  = 1;
    c.gridx = 1;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.fill   = GridBagConstraints.HORIZONTAL;
    add(algorithmsBox_,c);

    c.gridheight = 1;
    c.gridwidth  = 2;
    c.gridx = 0;
    c.gridy = 1;
    c.anchor = GridBagConstraints.SOUTH;
    // c.fill = GridBagConstraints.VERTICAL;
    add(jpanel_,c);
    

    algorithmsBox_.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent arg0) {
        CardLayout cl = (CardLayout)(jpanel_.getLayout());
        cl.show(jpanel_, (String)arg0.getItem());

        addConfiguration(PropUtils.setDefaultParameters2(jMetalProperties,(String)arg0.getItem()),(String)arg0.getItem());
      }
    });
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
    boolean found = false;

    while ((i < names.length) && (!found)) {
      if (names[i].equals(name))
        found = true;
      else
        i++;
    }

    if (!found)
       return null;
    else
      return ((ConfigureAlgorithmAction)paneles[i]).getConfiguration(name);
  }

  @Override
  public void draw() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

} // AlgorithmsConfigurationPanel
