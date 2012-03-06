/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import agent.*;
import environment.*;
import java.util.*;

public class LocationOrderSensor extends Sensor {
    public LocationOrderSensor(Thing t) {
        super(t);
    }
    public LocationOrderSensor() {
        ArrayList<String> providedThings = new ArrayList<String>();
        providedThings.add("locationList");
        this.setProperty("providedThings", providedThings);

        ArrayList<String> requiredThings = new ArrayList<String>();
        requiredThings.add("agent");
        requiredThings.add("location");
        requiredThings.add("neighborList");
        this.setProperty("requiredThings", requiredThings);
    }
    
    @Override
	public void run() {
        ArrayList<Location> locationList = new ArrayList<Location>(((ArrayList<Location>)
                                                                    ((Location)
                                                                     ((Agent)
                                                                      this
                                                                      .getBoxedProperty("agent"))
                                                                     .getBoxedProperty("location"))
                                                                    .getBoxedProperty("locationList")))
            ;

        locationList.add((Location)
                         ((Agent)
                          this
                          .getBoxedProperty("agent"))
                         .getBoxedProperty("location"));
        locationList = (ArrayList<Location>) ((Machine) 
                                              (new ThingPheromoneOrderSensor())
                                              .setNewBoxedProperty("thingList", locationList))
            .execute()
            .getBoxedProperty("result");
        this.setBoxedProperty("locationList", locationList);
    }
}


