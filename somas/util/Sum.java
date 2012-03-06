/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package util;

import common.*;
import java.util.*;

public class Sum extends Machine {
    public Sum() {super();}
    public Sum(Thing t) {
        super(t);
    }
    @Override
	public void run() {
        this.setBoxedProperty("object", new Double(0.0));

        for(Object object : (List<Object>) this.getProperty("objectList"))
            this.setBoxedProperty("object",
                                  ((Double) getBoxedProperty("object")) +
                                  ((Double)
                                   ((Machine)
                                    ((Machine)
                                     this
                                     .getBoxedProperty("accessor"))
                                    .setProperty("object", object))
                                   .execute()
                                   .getProperty("object")));
    }
}

