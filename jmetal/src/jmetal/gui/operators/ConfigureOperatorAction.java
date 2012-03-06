/*
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class provides a single GUI for configuring algorithms
 */

package jmetal.gui.operators;

import jmetal.gui.ConfigurationsContainer;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;
import java.util.Iterator;
import javax.swing.*;
import jmetal.gui.utils.PropUtils;
import jmetal.gui.utils.Configuration;
import jmetal.gui.components.OkJPanel;
import jmetal.gui.warehouses.AlgorithmsWareHouse;

public class ConfigureOperatorAction extends OkJPanel implements ConfigurationsContainer {

	private Properties properties_;
	private Properties defaultProperties_;
	private String operatorName_;
	private ConfigurationsContainer algorithmConfigurations_;

	private Object [][] windowsContent_;
	private String algorithmName_;
	private JComboBox combo_;


	public ConfigureOperatorAction(String algorithmName, JComboBox combo) {    
		combo_ = combo;
		algorithmName_ = algorithmName;
	}

	public void draw() {
		operatorName_ = (String)combo_.getSelectedItem();

		// load algorithm properties		
		properties_         = PropUtils.getPropertiesWithPrefix(Configuration.getSettings(), operatorName_+".PARAMETER.");
          	defaultProperties_  = PropUtils.getPropertiesWithPrefix(Configuration.getSettings(), operatorName_+".DEFAULT.");


		Iterator iterator = properties_.keySet().iterator();

		windowsContent_ = new Object[5][properties_.keySet().size()+1];
		windowsContent_[0][0] = null;
		windowsContent_[1][0] = new JLabel("Parameter");
		windowsContent_[2][0] = new JLabel("Type");
		windowsContent_[3][0] = new JLabel("Default");
		windowsContent_[4][0] = new JLabel("Value");
		int i = 0;
		// Fill the windowsContent


		while (iterator.hasNext()) {
			windowsContent_[0][++i] = iterator.next();
			String type = properties_.getProperty((String)windowsContent_[0][i]);
			windowsContent_[1][i]   = new JLabel((String)windowsContent_[0][i]);
			windowsContent_[2][i]   = new JLabel(type);
			windowsContent_[3][i]   = new JLabel(defaultProperties_.getProperty((String)windowsContent_[0][i]));
			windowsContent_[4][i]   = new JTextField();

			((JTextField)windowsContent_[4][i]).setColumns(4);
                        ((JTextField)windowsContent_[4][i]).setText(defaultProperties_.getProperty((String)windowsContent_[0][i]));

		}

		// Place the windowsContent in the panel
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		c.insets = new Insets(4,4,4,4);
		int k = 1;
		for (int j = 0; j < windowsContent_[0].length; j++) {
			k = 1;
			while (k < windowsContent_.length) {
				c.gridx      = k - 1;
				c.gridy      = j;
				c.gridheight = 1;
				c.gridwidth  = 1;
				c.anchor = c.WEST;
				if (k == windowsContent_.length-1)
					c.anchor = c.EAST;
				add((Component)windowsContent_[k][j],c);
				k++;
			}
		}

		//      c.gridx = 2;
		//      c.gridy = windowsContent_[0].length;
		//      JButton okButton = new JButton("OK");
		//      okButton.addActionListener(new ActionListener() {
		//
		//        public void actionPerformed(ActionEvent e) {
		//         frame.setVisible(false);
		//
		//          Properties algorithmSettings_ = algorithmConfigurations_.getConfiguration(algorithmName_);
		//          if (windowsContent_.length > 0) {
		//            for (int index = 1; index < windowsContent_[0].length;index++) {
		//
		//                  // If the textField contains anything, set the default value
		//                  if (!(((JTextField)windowsContent_[4][index]).getText() == null) &&
		//                      !(((JTextField)windowsContent_[4][index]).getText().equals(""))) {
		//
		//                     algorithmSettings_.setProperty(
		//                                    operatorName_+"."+(String)windowsContent_[0][index],
		//                                    ((JTextField)windowsContent_[4][index]).getText());
		//                  }
		//            }
		//            //previousFrame_.setVisible(true);
		//          }
		//        }
		//      } );
		//      mainPanel.add(okButton,c);
		//      frame.add(mainPanel);
		//          pack();
		//      frame.setResizable(false);
		//
		//      Toolkit toolkit = Toolkit.getDefaultToolkit();
		//      Dimension screenSize = toolkit.getScreenSize();
		//      frame.setLocation(screenSize.width /2 - frame.getSize().width/2,
		//                        screenSize.height/2 - frame.getSize().width/2);
		//      frame.setVisible(true);


		//frame.
	}

	public void addConfiguration(Properties configuration, String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Properties getConfiguration(String name) {
		Properties algorithmSettings =  AlgorithmsWareHouse.getSettings(algorithmName_);
		if (windowsContent_.length > 0) {
			for (int index = 1; index < windowsContent_[0].length;index++) {
                          // If the textField contains anything, set the default value
			  if (!(((JTextField)windowsContent_[4][index]).getText() == null) &&
                              !(((JTextField)windowsContent_[4][index]).getText().equals(""))) {
                                algorithmSettings.setProperty(
                                operatorName_+".VALUE."+(String)windowsContent_[0][index],
                                ((JTextField)windowsContent_[4][index]).getText());
                          }
			}
			//previousFrame_.setVisible(true);
		}
                int index = AlgorithmsWareHouse.getAlgorithmIndex(algorithmName_);
                AlgorithmsWareHouse.setSettings(index, algorithmSettings);
		return algorithmSettings;
	}



} // ConfigureAlgorithmAction
