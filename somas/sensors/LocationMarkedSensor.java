/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.util.ArrayList;

import environment.Location;

import agent.Agent;
import common.*;

public class LocationMarkedSensor extends Sensor {
    public LocationMarkedSensor() {super();}
    
    @Override
	public void run() {
        Double marked = (Boolean) 
            ((Thing)
             this
             .getBoxedProperty("context")).getBoxedProperty("isVitalNode") ? 0.0:1.0;

        this.setBoxedProperty("sensorVariable", new Double(marked));
    }

}

