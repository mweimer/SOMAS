/**
 * OkEventListener.java
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class implements a new Listener for OkEvent
 */
package jmetal.gui.listeners;

import java.util.EventListener;
import jmetal.gui.events.OkEvent;


public   interface OkEventListener extends EventListener {
   public void catchOkEvent( OkEvent evt );
}

