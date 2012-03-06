/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package rules;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class ReceiverAgentRule extends Rule {
    public ReceiverAgentRule() {super();}
    public ReceiverAgentRule(Thing t) {super(t);}
    public void run() {
        ((Machine) this.getBoxedProperty("incrementInfoActuator"))
            .execute();
    }
}

