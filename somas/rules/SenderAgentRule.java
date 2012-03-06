/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package rules;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class SenderAgentRule extends Rule {
    public SenderAgentRule() {super();}
    public SenderAgentRule(Thing t) {super(t);}
    public void run() {
    	Double sendProbability = (Double) this.getBoxedProperty("sendProbability");
    	
    	if(Math.random() < sendProbability)
        ((Machine) this.getBoxedProperty("SendCommAgentListActuator"))
            .execute();
    }
}

