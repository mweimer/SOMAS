/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class DeleteAgent extends Actuator {
    public DeleteAgent(Thing t) {
        super(t);
    }
    public DeleteAgent() {
        ArrayList<String> providedThings = new ArrayList<String>();
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("agentList");
        requiredThings.add("descheduler");
        requiredThings.add("parameterList");
        this.setProperty("requiredThings", requiredThings);
    }
    @Override
	public void run() {
        ArrayList<Agent> localAgentList = (ArrayList<Agent>) this.getBoxedProperty("agentList");
        if(localAgentList.size() == 0)
        	return;
        Integer selectedAgent = SOMAUtil.doubleToInteger
            ((Double)
             ((ArrayList<Thing>)
              this
              .getProperty("parameterList"))
             .get(0)
             .getProperty("object")

             * 

             (localAgentList.size() - 1));
        Agent agent = localAgentList
            .get( selectedAgent
                  );

        ArrayList<Agent> locationAgentList = (ArrayList<Agent>) ((Location) this.getBoxedProperty("location")).getBoxedProperty("agentList");
        locationAgentList
            .remove(agent);

                
        ((Machine)
         ((Thing)
          this
          .getBoxedProperty("descheduler"))
         .setProperty("machine",
                      agent))
            .execute();

        ;
    }
}

