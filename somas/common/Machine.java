/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package common;

import java.io.Serializable;

public abstract class Machine extends Thing implements Serializable {
    public Machine() {super();}
    
    public Machine(Thing t) {
        super(t);
    }
        
    public Machine execute() {
        run();
        return this;
    }

    public abstract void run();
}

