/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package simulator;

import sim.engine.*;

abstract public class SimStateComponent extends Encapsulation implements Steppable {
    public SimStateComponent() {super();}
    
    public void step(SimState state) {
        // TODO Auto-generated method stub
        execute();
                
    }

}


