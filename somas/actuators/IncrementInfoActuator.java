/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import agent.*;
import environment.*;
import scenario.InfoWarScenario;
import util.*;
import java.util.*;

public class IncrementInfoActuator extends Actuator {
    public IncrementInfoActuator() {super();}
    public IncrementInfoActuator(Thing t) {super(t);}
    public void run() {
        ArrayList<Agent> commAgentList = (ArrayList<Agent>) this.getBoxedProperty("commAgentList");

        for(Agent commAgent : commAgentList) {
            //Object message = this.getBoxedProperty("message");

//            if(message instanceof String && message.equals("info")) {
                Double info = (Double) this.getBoxedProperty("infoCounter");
                info += 1.0;
                this.setBoxedProperty("infoCounter", info);
//            }
        }
    }
}

