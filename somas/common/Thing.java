/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package common;

import java.io.Serializable;

import java.util.*;

public class Thing implements Serializable {
    public Thing() {}
    private HashMap<String, Object> properties = new HashMap<String, Object>();

    public Thing(Thing t) {
        this.properties = (HashMap<String, Object>) t.properties.clone();
    }
        
    public Thing setProperty(String p, Object v) {
        properties.put(p, v);

        return this;
    }

    public Boolean hasProperty(String p) {
        return properties.get(p) != null;
    }

    public Boolean hasBoxedProperty(String p) {
        return hasProperty(p+"Container");
    }

    public Thing setNewBoxedProperty(String p, Object v) {
        String boxedP = p + "Container";

        Thing t = new Thing();
        t.setProperty("object", v);
        this.setProperty(boxedP, t);

        return this;
    }

    public Thing setBoxedProperty(String p, Object v) {
        String boxedP = p + "Container";

        assert(getProperty(boxedP) != null);
        ((Thing) getProperty(boxedP)).setProperty("object", v);

        return this;
    }

    public Thing setNewBox(String p) {
        String boxedP = p + "Container";

        this.setProperty(boxedP, new Thing());

        return this;
    }

    public Object getProperty(String p) {
        assert(properties.get(p) != null);
        return properties.get(p);
    }

    public Object getBoxedProperty(String p) {
        String boxedP = p + "Container";
        
        assert(getProperty(boxedP) != null);
        Thing container = (Thing) getProperty(boxedP);

        return container.getProperty("object");
    }

    public Thing getBox(String p) {
        String boxedP = p + "Container";

        assert(getProperty(boxedP) != null);
        return (Thing) getProperty(boxedP);
    }
    
    public void removeProperty(String p) {
        assert(getProperty(p) != null);
        properties.remove(p);
    }
    
    public void removeBoxedProperty(String p) {
        String boxedP = p + "Container";

        assert(getProperty(boxedP) != null);
        removeProperty(boxedP);
    }
}


