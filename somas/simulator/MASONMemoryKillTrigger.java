/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package simulator;

import java.lang.management.ManagementFactory;

import sim.engine.SimState;
import common.Machine;
import common.Thing;

public class MASONMemoryKillTrigger extends Machine {
    public MASONMemoryKillTrigger() {super();}
    public MASONMemoryKillTrigger(Thing t) {
        super(t);
    }

    @Override
	public void run() {
        Double memoryUsePercentageLimit = (Double) this.getBoxedProperty("memoryUsePercentageLimit");
        Long memoryUsed = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        Long memoryMax = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
        Double memoryUsePercentage = new Double(new Double(memoryUsed)/new Double(memoryMax));  
                
        Boolean trigger = memoryUsePercentage > memoryUsePercentageLimit;
                
        if(trigger) {
            this.setBoxedProperty("errorType", "out of Memory");
            ((SimState) this.getProperty("simState")).kill();
        }
    }

}
