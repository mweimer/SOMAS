    package environment;

import common.*;
import actuators.*;
import java.util.*;

import rules.Rule;
import sensors.Sensor;

public class Edge extends Machine {
    public Edge() {super();}
    public Edge(Thing t) {
        super(t);
    }
        
    @Override
	public void run() {
        

        ArrayList<Sensor> sensors = (ArrayList<Sensor>) this.getBoxedProperty("sensors");
        for(Sensor s : sensors) {
            s.execute();
        }
        ArrayList<Rule> rules = (ArrayList<Rule>) this.getBoxedProperty("rules");
        for(Rule r : rules)
            r.execute();
    }

}

