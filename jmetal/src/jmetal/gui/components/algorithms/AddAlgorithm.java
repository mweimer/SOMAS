/*
 * AddAlgorithm.java
 * @author Juan J. Durillo
 * @version 1.0
 * 
 * Thiss class provide a GUI for adding a new Algorithm
 */

package jmetal.gui.components.algorithms;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import jmetal.gui.ConfigurationsContainer;
import jmetal.gui.utils.PrintAlgorithmsInfo;

public class AddAlgorithm implements ActionListener{

    private JFrame mainFrame_;
    private JTextField algorithmName_;
    private JTextField packageName_;

    private ConfigurationsContainer panel_;

    public AddAlgorithm(ConfigurationsContainer panel) {
      panel_ = panel;

    }


    // Action Listener Behavior
    public void actionPerformed(ActionEvent e) {
       mainFrame_ = new JFrame();
       Container panel = mainFrame_.getContentPane();
       panel.setLayout(new GridLayout(3,2));

       panel.add(new JLabel("Algorithm Name"));
       algorithmName_ = new JTextField();
       algorithmName_.setColumns(20);
       panel.add(algorithmName_);


       panel.add(new JLabel("Package Name"));
       packageName_ = new JTextField();
       packageName_.setColumns(20);
       panel.add(packageName_);

       JButton cancelButton = new JButton("Cancel");
       cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              mainFrame_.setVisible(false);
            }
        });
       panel.add(cancelButton);


       JButton addButton     = new JButton("Add");
       addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              try {
                (new PrintAlgorithmsInfo()).printAlgorithmInfo(algorithmName_.getText(), packageName_.getText());
                //panel_.addAlgorithm();

              } catch (ClassNotFoundException ex) {
                 Logger.getLogger(AddAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
              } catch (FileNotFoundException ex) {
                 Logger.getLogger(AddAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IOException ex) {
                 Logger.getLogger(AddAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
              } catch (InstantiationException ex) {
                 Logger.getLogger(AddAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IllegalAccessException ex) {
                 Logger.getLogger(AddAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
              }
              mainFrame_.setVisible(false);
            }
        });
       panel.add(addButton);

       mainFrame_.setVisible(true);
       mainFrame_.pack();
    }
} // AddAlgorithm
