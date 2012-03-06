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

public class ThingPheromoneOrderSensor extends Sensor {
    public ThingPheromoneOrderSensor() {super();}
        
    public ThingPheromoneOrderSensor(Thing t) {
        super(t);
    }

    @Override
	public void run() {
        // Assumes everything has a pheromone
        // HACK should really be a machine list, to fall in line with the conversion process
        ArrayList<Thing> thingList = (ArrayList<Thing>) this.getBoxedProperty("thingList");
        if(thingList.size() == 0) {
            this.setNewBoxedProperty("result", thingList);
        	
        	return;
        }
        Class clazz = thingList.get(0).getClass();
        Constructor constructor = null;
        try {
            constructor = clazz.getConstructor((new Thing()).getClass());
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
                
        // Convert everything to a comparable Thing
        ArrayList<PheromoneComparableThing> pheromoneThingList = new ArrayList<PheromoneComparableThing>();
        for(Thing t : thingList)
            pheromoneThingList.add(new PheromoneComparableThing(t));
        Collections.sort(pheromoneThingList);
                
        // Convert everthing back to normal Thing
        thingList.clear();
        for(PheromoneComparableThing t : pheromoneThingList)
            try {
                thingList.add((Machine) constructor.newInstance((Thing) t));
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                        
        this.setNewBoxedProperty("result", thingList);
    }
        
}

class PheromoneComparableThing extends Thing implements Comparable {
    public PheromoneComparableThing() {super();}
    public PheromoneComparableThing(Thing t) {
        super(t);
    }
        
    public int compareTo(Object arg0) {
        PheromoneComparableThing compObj = (PheromoneComparableThing) arg0;
                
        Double thisPheromone = (Double) this.getBoxedProperty("pheromone");
        Double otherPheromone = (Double) compObj.getBoxedProperty("pheromone");
                
        if(thisPheromone > otherPheromone)
            return 1;
        if(thisPheromone < otherPheromone)
            return -1;
        /* if(thisPheromone.equals(otherPheromone)) */
        return 0;
    }
        
}