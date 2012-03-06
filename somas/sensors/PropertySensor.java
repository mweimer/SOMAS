/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.*;
import agent.*;
import environment.*;
import util.*;
import java.util.*;

public class PropertySensor extends Sensor {
    public PropertySensor() {super();}
    public PropertySensor(Thing t) {super(t);}
    public void run() {
        Thing thing = (Thing) this.getBoxedProperty("thing");
        String propertyName = (String) this.getBoxedProperty("propertyName");
        Object property = (Object) this.getBoxedProperty("property");
          
        this.setBoxedProperty("result", property);
    }
}

