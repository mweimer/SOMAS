/**
 * @author Juan J. Durillo
 *
 */

package jmetal.gui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jmetal.gui.ConfigurationsContainer;
import jmetal.gui.events.OkEvent;
import jmetal.gui.generators.OkEventGenerator;
import jmetal.gui.listeners.OkEventListener;


public class OkJFrame extends OkEventGenerator implements ActionListener, OkEventListener {

  private String name_;
  private OkJPanel panel_;
  private JFrame frame_;
  private JFrame previousFrame_;
  private JButton okButton_ = null;


  public OkJFrame(String name) {
    name_             = name;                
  }

  public void setOkJPanel(OkJPanel panel) {
    panel_ = panel;
  }


  public void actionPerformed(ActionEvent event) {

   
   Component c = (Component) event.getSource();
   while (c.getParent() != null)
     c = c.getParent();


   previousFrame_ = (JFrame)c;
   //previousFrame_.setVisible(false);
   

   frame_ = new JFrame(name_+" configuration");
   panel_.removeAll();
   panel_.draw();
   
   frame_.addWindowListener(new WindowAdapter()  {
   public void windowClosing(WindowEvent e) {
       previousFrame_.setVisible(true);
   }

  });
   okButton_ = new JButton("OK");
   okButton_.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (panel_!=null) {
           ((ConfigurationsContainer)panel_).getConfiguration("");
           frame_.setVisible(false);
           previousFrame_.setVisible(true);
        }
      }
    });



   JPanel buttonPanel = new JPanel();
   buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.LINE_AXIS));
   buttonPanel.add(Box.createHorizontalGlue());
   buttonPanel.add(okButton_);
   

   frame_.setLayout(new BoxLayout(frame_.getContentPane(),BoxLayout.PAGE_AXIS));
   frame_.add(panel_);
   frame_.add(buttonPanel);
   frame_.pack();
 
   Toolkit toolkit = Toolkit.getDefaultToolkit();
   Dimension screenSize = toolkit.getScreenSize();
   frame_.setLocation(screenSize.width /2 - frame_.getSize().width/2,
                         screenSize.height/2 - frame_.getSize().width/2);

    frame_.setVisible(true);
  }

  public void catchOkEvent(OkEvent evt) {
    frame_.setVisible(false);
    newOkEvent(new OkEvent(this));
  }

  public JFrame getPrevious() {
    return previousFrame_;
  }

  public void setPrevious(JFrame previousFrame) {
     previousFrame_ = previousFrame;
  }

  public void setPreviouisVisible() {
    previousFrame_.setVisible(true);
  }

  public void setPreviousInvisible() {
    previousFrame_.setVisible(false);
  }

  public JFrame getFrame() {
    return frame_;
  }
}
