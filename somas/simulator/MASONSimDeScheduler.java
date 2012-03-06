/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package simulator;

import java.util.ArrayList;

import sim.engine.*;
import common.*;

public class MASONSimDeScheduler extends Machine {
    public MASONSimDeScheduler() {super();}
    public MASONSimDeScheduler(Thing t) {
        super(t);
    }
    @Override
	public void run() {
        ((Stoppable)
         ((Machine)
          this
          .getProperty("machine"))
          .getProperty("component"))
          .stop()
          ;
          
        if(this.hasBoxedProperty("actuators")) { // HACK
        	ArrayList<Machine> actuators = (ArrayList<Machine>) this.getBoxedProperty("actuators"); // HACK
        	for(Machine actuator : actuators) // HACK
        		((Machine) actuator  // HACK
        		.setNewBoxedProperty("machine", (Machine) this.getProperty("machine"))) // HACK
        		.execute(); // HACK
        } // HACK
        
        ((Machine)this.getProperty("machine")) // HACK
            .setProperty("dead", new Boolean(true)); // HACK
    }
}


