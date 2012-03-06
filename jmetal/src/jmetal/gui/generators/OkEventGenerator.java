/**
 * OkEventGenerator.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class should be implemented for all the objects which generates
 * OkJPanel
 */
package jmetal.gui.generators;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import jmetal.gui.events.OkEvent;
import jmetal.gui.listeners.OkEventListener;


public class OkEventGenerator {
  private List<OkEventListener> list_ = new LinkedList<OkEventListener>();

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

} // OkEventGeneratorJPanel
