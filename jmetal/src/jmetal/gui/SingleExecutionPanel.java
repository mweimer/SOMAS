/**
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.gui;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jmetal.gui.components.algorithms.AlgorithmsComboPanel;
import jmetal.gui.components.OkJPanel;
import jmetal.gui.components.problems.ProblemsComboPanel;


public class SingleExecutionPanel extends OkJPanel {

  AlgorithmsComboPanel acp;
  ProblemsComboPanel   pcp;

  public SingleExecutionPanel() {
    
    acp = new AlgorithmsComboPanel();

    pcp = new ProblemsComboPanel();
    setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));


    // Add a label for the algorithms
    JPanel aux = new JPanel();
    aux.setLayout(new BoxLayout(aux,BoxLayout.X_AXIS));
    aux.add(new JLabel("Algorithms Configuration"));
    aux.add(Box.createHorizontalBox());
    add(Box.createHorizontalStrut(20));
    add(aux);
    add(Box.createHorizontalStrut(20));
    acp.setBorder(BorderFactory.createLoweredBevelBorder());
    add(acp);
    add(Box.createHorizontalStrut(20));

    // Add a label for the problems
    aux = new JPanel();
    aux.setLayout(new BoxLayout(aux,BoxLayout.X_AXIS));
    aux.add(new JLabel("Problem Configuration"));
    aux.add(Box.createHorizontalBox());
    add(aux);

    pcp.setBorder(BorderFactory.createLoweredBevelBorder());
    add(Box.createHorizontalStrut(20));
    add(pcp);


    // set the border of this panel
    //setBorder(BorderFactory.createRaisedBevelBorder());
          setBorder(BorderFactory.createEtchedBorder());
  }

  @Override
  public void draw() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
