/**
 * OkJPanel.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class should be implemented for all the objects which generates
 * OkJPanel
 */
package jmetal.gui.components;

import java.awt.Component;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import javax.swing.JPanel;
import jmetal.gui.events.OkEvent;
import jmetal.gui.listeners.OkEventListener;


public abstract class OkJPanel extends JPanel {

  private List<OkEventListener> list_ = new LinkedList<OkEventListener>();
  private int rows_;
  private int columns_;

  public OkJPanel()  {    
    rows_            = 0;
    columns_         = 0;
  }
  
  public Component getFrameContainer() {
    Component c = getParent();
    while (c.getParent()!= null)
      c = c.getParent();
    return c;
  }

  
  // Add a new OkEventListener Listener
  public void addOkEvenListener(OkEventListener listener) {
    list_.add(listener);
  } // OkEventListener

  // Lauch a new OkEvent
  public void newOkEvent(OkEvent event) {
    Iterator<OkEventListener> iterator = list_.iterator();
    while (iterator.hasNext())
      iterator.next().catchOkEvent(event);
  } // newOkEvent
  
  // Return the number of columns of the panel
  public int getColumns() {
	  return columns_;
  } // getColumns;
  
  // Increase the number of columns of the panel
  public void increaseColumns() {
	  columns_++;
  } // increaseColumns
  
  // Return the number of rows of the panel
  public int getRows() {
	  return rows_;
  } // getRows;
  
  // Increase the number rows 
  public void increaseRows() {
	  rows_++;
  } // increaseRows

  public abstract void draw();
} // OkJPanel
