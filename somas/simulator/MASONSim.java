/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package simulator;

import sim.engine.*;
import simulator.MASONSimClassContext.MASONSimClass;
import common.*;

public class MASONSim extends FiniteStateSimulator {
    public MASONSim() {super();}
    
    public MASONSim(Thing t) {
        super(t);
    }
    @Override
        public void run() {
        final MASONSimClassContext simClassContext = (MASONSimClassContext) this.getProperty("simClassContext");
        final MASONSimClass masonSimClass = simClassContext.new MASONSimClass(System.currentTimeMillis());
        
        String[] args = (String[]) getProperty("argList");

        try{
            SimState.doLoop(new MakesSimState()
                {
                    public SimState newInstance(long seed, String[] args)
                    {
                        try
                            {
                                return masonSimClass; 
                            }
                        catch (Exception e)
                            {
                                throw new RuntimeException("Exception occurred while trying to construct the simulation: " + e);
                            }
                    }
                    public Class simulationClass() { return MASONSimClassContext.MASONSimClass.class; }
                }, args);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}


