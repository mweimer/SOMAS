/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package actuators;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class IncrementGoodInfoActuator extends Actuator {
    public IncrementGoodInfoActuator() {super();}
    public IncrementGoodInfoActuator(Thing t) {super(t);}
    public void run() {
        ArrayList<Agent> commAgentList = (ArrayList<Agent>) this.getBoxedProperty("commAgentList");

        for(Agent commAgent : commAgentList) {
            Object message = this.getBoxedProperty("message");

            if(message instanceof String && message.equals("info")) {
                Double goodInfo = (Double) this.getBoxedProperty("goodInfo");
                goodInfo += commAgentList.size();
                this.setBoxedProperty("goodInfo", goodInfo);
            }
        }
    }
}

