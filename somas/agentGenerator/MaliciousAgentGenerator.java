package agentGenerator;

import sensors.*;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import rules.*;
import actuators.*;

import common.*;
import agent.*;
import environment.*;
import util.*;

import java.util.*;


abstract public class MaliciousAgentGenerator extends Machine {
	public MaliciousAgentGenerator() {super();
Sampler sampler = (Sampler) new UniformSampler()
.setNewBoxedProperty("start", -100.0) // Double.MIN_VALUE)
.setNewBoxedProperty("end", 100.0); // Double.MAX_VALUE);
this.setNewBoxedProperty("sampler", sampler);
	}
	public MaliciousAgentGenerator(Thing t) {super(t);}
}
