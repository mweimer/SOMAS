/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package simulator;

import sim.field.continuous.*;
import sim.field.network.*;
import sim.field.network.Network;
import sim.engine.*;
import sim.portrayal.network.*;
import sim.portrayal.continuous.*;
import sim.display.*;
import javax.swing.*;

import org.jgrapht.Graph;

import java.awt.Color;

import common.*;
import environment.*;
import java.util.*;

public class MASONSimClassContext extends Thing {

    public static final double XMIN = 0;
    public static final double XMAX = 800;
    public static final double YMIN = 0;
    public static final double YMAX = 600;

    public static final double DIAMETER = 25;

    class MASONSimClass extends SimState {
        private static final long serialVersionUID = -6781770712864446585L;

        public Continuous2D environment = new Continuous2D(16.0, (XMAX-XMIN), (YMAX-YMIN) );
        public Network network = new Network();

        public MASONSimClass(long seed) {
            super(seed);

            // This ensures any machine that need to schedule new components, can 
            ArrayList<Machine> schedulingMachineList = (ArrayList<Machine>) getProperty("schedulingMachineList");

            for(Machine schedulingMachine : schedulingMachineList)
                schedulingMachine.setProperty("simState", this);
        }

        @Override
            public void start() {
            super.start();  // clear out the schedule

            // Schedule everything
            ArrayList<MASONComponent> componentList = (ArrayList<MASONComponent>) getProperty("componentList");

            Graph<Location, Thing> graph = (Graph<Location, Thing>) getProperty("graph");

            for(MASONComponent source : componentList) {
                Machine m = source.m;

                if(source.m instanceof Location) { 
                    environment.setObjectLocation(source, source.location); 
                    network.addNode(source);

                    for(MASONComponent target : componentList) 
                        if(target.m instanceof Location)
                            if(graph.containsEdge((Location) source.m, (Location) target.m))
                                network.addEdge(source, target, "");
                }

                m.setProperty("component", schedule.scheduleRepeating(source, source.order, 1.0)); // HACK
            }
        }
    }

    class MASONSimWithUIClass extends GUIState {
        public Display2D display;
        public JFrame displayFrame;

        NetworkPortrayal2D edgePortrayal = new NetworkPortrayal2D();
        ContinuousPortrayal2D nodePortrayal = new ContinuousPortrayal2D();

        public MASONSimWithUIClass() { super(new MASONSimClass( System.currentTimeMillis())); }

        public MASONSimWithUIClass(SimState state) { super(state); }

            
        @Override
            public void start()
        {
            super.start();
            setupPortrayals();
        }

        @Override
            public void load(SimState state)
        {
            super.load(state);
            setupPortrayals();
        }

        public void setupPortrayals()
        {
            // tell the portrayals what to portray and how to portray them
            edgePortrayal.setField( new SpatialNetwork2D( ((MASONSimClass)state).environment, ((MASONSimClass)state).network ) );

            SimpleEdgePortrayal2D p = new SimpleEdgePortrayal2D(Color.lightGray, Color.lightGray, Color.black);
            p.setShape(SimpleEdgePortrayal2D.SHAPE_LINE);
            p.setBaseWidth(10);
            edgePortrayal.setPortrayalForAll(p);
            nodePortrayal.setField( ((MASONSimClass)state).environment );

                        
            // reschedule the displayer
            display.reset();
            display.setBackdrop(Color.white);

            // redraw the display
            display.repaint();
        }

        @Override
            public void init(Controller c)
        {
            super.init(c);

            // make the displayer
            display = new Display2D(800,600,this,1);

            displayFrame = display.createFrame();
            displayFrame.setTitle("Network Test Display");
            c.registerFrame(displayFrame);   // register the frame so it appears in the "Display" list
            displayFrame.setVisible(true);
            display.attach( edgePortrayal, "Edges" );
            display.attach( nodePortrayal, "Nodes" );
        }

        @Override
            public void quit()
        {
            super.quit();

            if (displayFrame!=null) displayFrame.dispose();
            displayFrame = null;
            display = null;
        }
    }

}


