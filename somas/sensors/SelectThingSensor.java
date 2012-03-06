/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import common.Machine;
import common.Thing;

public class SelectThingSensor extends Sensor {
    public SelectThingSensor() {super();}
        
    public SelectThingSensor(Thing t) {
        super(t);
    }

    @Override
	public void run() {
        ArrayList<Thing> thingList = (ArrayList<Thing>) this.getBoxedProperty("thingList");

        ArrayList<Thing> parameterList = (ArrayList<Thing>) this.getBoxedProperty("parameterList"); 
        
        Double normalizedSelection = (Double) parameterList.get(0).getProperty("object");
        
        Integer selection = new Double((thingList.size() - 1) * normalizedSelection).intValue();
        
        Thing thing =  thingList.get(selection);
    
        this.setBoxedProperty("result", thing);
    }
}

