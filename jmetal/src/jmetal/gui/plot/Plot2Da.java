package jmetal.gui.plot;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import jmetal.gui.components.OkJPanel;
/**
 * Displays a JFrame and draws a line on it using the Java 2D Graphics API
 *
 * @author www.javadb.com
 */
public class Plot2Da extends OkJPanel {

    public Plot2D plot_;
    public JCheckBox check_;


    public Plot2Da()
    {      
      check_ = new JCheckBox("Hold on");
      plot_  = new Plot2D();


      setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.insets             = new Insets(2,2,2,2);
      c.weightx = 1.0;
      c.weighty = 1.0;

      c.gridheight = 1;
      c.gridwidth  = 1;
      c.gridx      = 0;
      c.gridy      = 0;
      add(check_,c);
      
      c.gridwidth  = 1;
      c.gridheight = 1;
      c.gridx      = 0;
      c.gridy      = 1;
      c.fill       = GridBagConstraints.REMAINDER;
      c.ipadx      = plot_.getSize().width + 100;
      c.ipady      = plot_.getSize().height+ 100;
      plot_.setBackground(Color.WHITE);
      
      
      add(plot_,c);

      setBorder(BorderFactory.createEtchedBorder());
    }

  @Override
  public void draw() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}