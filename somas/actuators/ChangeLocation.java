/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
 package actuators;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

/**
 * SOMAS agent change location actuator.  Uses actuator parameter to
 * select new location from list.
 */

public class ChangeLocation extends Actuator {
    public ChangeLocation(Thing t) {
        super(t);
    }
    public ChangeLocation() {
        super();
    }
    @Override
        public void run() {
        // HACK makes sure the agent doesn't stick itself on another list if it's already been deleted, otherwise the deletion is inneffective
        if(((Agent)this.getBoxedProperty("agent")).getProperty("dead") == null) {
                
            ArrayList<Location> locationList = (ArrayList<Location>) this.getBoxedProperty("locationList");
            Integer chosenLocation = SOMAUtil.doubleToInteger

                ((Double)
                 ((ArrayList<Thing>)
                  this
                  .getBoxedProperty("parameterList"))
                 .get(0)
                 .getProperty("object")

                 *

                 (locationList.size() - 1)) ;
                
            Agent agent = (Agent) this.getBoxedProperty("agent");
                 
                 
            Location oldLocation = (Location) this.getBoxedProperty("location");
            Location newLocation = locationList.get(chosenLocation);


            ArrayList<Agent> oldAgentList = (ArrayList<Agent>) oldLocation.getBoxedProperty("agentList");
            ArrayList<Agent> newAgentList = (ArrayList<Agent>) newLocation.getBoxedProperty("agentList");

            oldAgentList.remove(agent);
            newAgentList.add(agent);
            this.setBoxedProperty("location", newLocation);
        }
    }

}
