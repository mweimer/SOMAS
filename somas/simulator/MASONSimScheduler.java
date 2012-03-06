/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package simulator;

import sim.engine.*;
import common.*;

public class MASONSimScheduler extends Machine {
    public MASONSimScheduler() {super();}
    public MASONSimScheduler(Thing t) {
        super(t);
    }
    @Override
	public void run() {
        Machine machine = (Machine) this
            .getProperty("machine");
        MASONComponent component = new MASONComponent(machine);

        Stoppable stoppableComponent = ((SimState) this
                                        .getProperty("simState"))
            .schedule
            .scheduleRepeating(component
                               , 0 /* CHECK : schedule at the beginning, this may need to be changed */
                               , 1.0);

        machine.setProperty("component", stoppableComponent);
    }
}


