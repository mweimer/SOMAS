/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package simulator;

import sim.engine.*;
import sim.util.*;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.SimplePortrayal2D;
import java.awt.Graphics2D;

import common.*;

public class MASONComponent extends SimplePortrayal2D implements Steppable {
    public Machine m = null;

    public int order = 0;
    
    public Double2D location = null;
    
    public MASONComponent () {
        location = new Double2D((Math.random() - 0.5)*((MASONSimClassContext.XMAX-MASONSimClassContext.XMIN)/5-MASONSimClassContext.DIAMETER) +
                                //NetworkTest.XMIN
                                location.x 
                                //+NetworkTest.DIAMETER/2
                                ,
                                (Math.random()-0.5)*((MASONSimClassContext.YMAX-MASONSimClassContext.YMIN)/5-MASONSimClassContext.DIAMETER) +
                                location.y
                                //NetworkTest.YMIN
                                //+NetworkTest.DIAMETER/2
                                );
    }
    
    public MASONComponent (Machine m) {
        this.m = m;
    }

    @Override
        public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        super.draw(object, graphics, info);

        if(m.getBoxedProperty("draw") != null)
            ((Machine)
             ((MASONComponentDraw)
              m
              .getBoxedProperty("draw"))
             .setNewBoxedProperty("object", object) // HACK
             .setNewBoxedProperty("graphics", graphics) // HACK
             .setNewBoxedProperty("info", info) // HACK
             .setNewBoxedProperty("context", m)) // HACK
                .execute()
                ;
    }
    
    public void step(SimState state) {
        if(m != null) // HACK
            m.execute();
    }
}


