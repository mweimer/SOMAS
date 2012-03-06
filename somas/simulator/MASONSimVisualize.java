/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package simulator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


import representationUtil.Long_Coder;
import representations.GrayBitRep;
import representations.IntBitRep;
import sim.field.continuous.*;
import sim.field.network.*;
import sim.engine.*;
import sim.util.*;

import sim.portrayal.network.*;
import sim.portrayal.continuous.*;
import sim.engine.*;
import sim.display.*;
import javax.swing.*;
import java.awt.Color;

import common.*;
import actuators.*;
import agent.*;
import environment.*;
import metric.*;
import rules.*;
import scenario.*;
import sensors.*;
import simulator.*;
import unknown.*;
import util.*;
import java.util.*;

public class MASONSimVisualize extends FiniteStateSimulator {

    public void run() {
        Class simClass = (Class) getProperty("simClass");

        Method mainMethod = null;
        try {
                mainMethod = simClass.getDeclaredMethod("main", String[].class);
        } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }

        String[] args = (String[]) getProperty("args");

        try {
                mainMethod.invoke(args);
        } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }

}


