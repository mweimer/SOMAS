/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package rules;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class DDoSAgentRule extends Rule {
    public DDoSAgentRule() {super();}
    public DDoSAgentRule(Thing t) {super(t);}
    public void run() {
    	Double attackSize = Math.random() * 2;
    	for(int i = 0; i < attackSize; i++)
        ((Machine) this.getBoxedProperty("SendCommAgentListActuator"))
            .execute();
    }
}

