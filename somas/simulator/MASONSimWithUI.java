/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package simulator;

import sim.display.*;
import common.*;

public class MASONSimWithUI extends FiniteStateSimulator {
    public MASONSimWithUI() {super();}
    public MASONSimWithUI(Thing t) {
        super(t);
    }

    @Override
	public void run() {
        final MASONSimClassContext simClassContext = (MASONSimClassContext) getProperty("simClassContext");

        
        String[] args = (String[]) getProperty("argList");

        Console console = new Console(simClassContext.new MASONSimWithUIClass());
        console.setVisible(true);
        console.setPlaySleep(250);

    }

}
