/**
 * OkEvent.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class implements a new java event
 *
 */

package jmetal.gui.events;

import java.util.EventObject;


public class OkEvent extends EventObject {

    public OkEvent(Object obj) {
        // Se le pasa el objeto como parametro a la superclase
        super( obj );
        // Se guarda el identificador del objeto
    }
}
