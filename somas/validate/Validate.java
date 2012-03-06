/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package validate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.*;

abstract public class Validate extends Machine {
    public Validate() {super();}
    public Validate(Thing t) {
        super(t);
    }
    Map<String, Thing> thingNameMap = new HashMap<String, Thing>();

    public Validate(List<Thing> thingList) {
        for(Thing thing : thingList)
            thingNameMap.put(thing.getClass().getName(), thing);

    }

    Set<List<String>> exists(String thingName, Set<List<String>> propertySet) {
        Set<List<String>> existing = new HashSet<List<String>>();
                
        for(List<String> propertyChain : propertySet) {
            int i = 0;
            Thing currThing = thingNameMap.get(thingName);
            for(; i < propertyChain.size(); i++)
                currThing = (Thing) currThing.getBoxedProperty(propertyChain.get(i));
            if(i == propertyChain.size())
                existing.add(propertyChain);
        }
                
        return existing;
    }

    Set<Set<String>> shared(String propertyName, Set<String> thingNameSet) {
        Map<Thing, Set<String>> sharesMap = new HashMap<Thing, Set<String>>(); 

        {
            String thingName = thingNameSet.toArray(new String[thingNameSet.size()])[0];
            thingNameSet.remove(thingName);

            Set<String> shareSet = new HashSet<String>();
            shareSet.add(thingName);

            Thing property = (Thing) thingNameMap.get(thingName).getBoxedProperty(propertyName);
                        
            sharesMap.put(property, shareSet);
        }

        for(String thingName : thingNameSet) {
            Thing property = (Thing) thingNameMap.get(thingName).getBoxedProperty(propertyName);
            Set<String> shareSet = sharesMap.get(thingName);
            if(shareSet == null)
                shareSet = new HashSet<String>();
            shareSet.add(thingName);
        }
                
        Set<Set<String>> setOfShares = new HashSet<Set<String>>(sharesMap.values());
                
        return setOfShares;
    }
}

