/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package rules;

import actuators.*;
import common.*;
import agent.*;
import agentGenerator.CommAgentGenerator;
import agentGenerator.DDoSAgentGenerator;
import environment.*;
import simulator.MASONSimDeScheduler;
import simulator.MASONSimScheduler;
import util.*;
import java.util.*;

public class CompromiseAgentRule extends Rule {
    public CompromiseAgentRule() {super();}
    public CompromiseAgentRule(Thing t) {super(t);}
    public void run() {
        Thing tempThing = null;
        Machine tempMachine = null;
        Machine tempMachine2 = null;
        Machine tempMachine3 = null;

        /*
          ==============================
          Attempt to compromise the node
          ==============================

          If the node's compromise parameter is false, then attempt to
          set it.  Currently, compromises are always successful, but
          it is possible to make compromises conditional, such as
          conditioned on a probability.
        */

        Boolean compromiseCheck = (Boolean) this.getBoxedProperty("compromiseCheck");

        if(!compromiseCheck) {
            Actuator compromiseActuator  = (Actuator) this.getBoxedProperty("compromiseActuator");
            compromiseActuator.execute();
        }

        /*
          ===============
          Carry out tasks
          ===============

          If the compromise is successful, then carry out tasks on node:

          - Install DDoS agent.

          - Steal information if the node has a sender agent or an
          information agent.  This action consists of creating a
          comm agent and setting its target to one of the external
          nodes, where the stolen information receiving agents are.
          
          - Add bad information if the node has a receiver agent,
          simulating information corruption.

        */
        Location location = (Location) this.getBoxedProperty("location");
        ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");

        // Value is retrieved twice incase the compromise actuator is
        // not always successful.
        compromiseCheck = (Boolean) this.getBoxedProperty("compromiseCheck");

        if(compromiseCheck){
            tempMachine = ((Actuator) this.getBoxedProperty("deleteAgentListActuator"));
            tempMachine.execute();

            /*
              ------------------
              Install DDoS agent
              ------------------
            */
            ArrayList<Agent> DDoSAgentList = (ArrayList<Agent>) this.getBoxedProperty("DDoSAgentList");
            if(DDoSAgentList.size() == 0) {
                Thing graphContainer = (Thing) this.getProperty("graphContainer");
                MASONSimScheduler scheduler = (MASONSimScheduler) this.getBoxedProperty("scheduler");
                MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");

                ArrayList<String> ddosAgentTypeList = new ArrayList<String>();
                ddosAgentTypeList.add("ddos");

                ArrayList<Location> targetList = this.hasBoxedProperty("targetList") ? (ArrayList<Location>) this.getBoxedProperty("targetList") : new ArrayList<Location>();

                tempMachine = ((Machine) (new DDoSAgentGenerator()));
                tempMachine.setNewBoxedProperty("targetList", targetList);
                tempMachine.setNewBoxedProperty("typeList", ddosAgentTypeList);
                tempMachine.setNewBoxedProperty("scheduler", scheduler);
                tempMachine.setNewBoxedProperty("descheduler", descheduler);
                tempMachine.setNewBoxedProperty("location", location);
                tempMachine.setProperty("graphContainer", graphContainer);

                tempMachine2 = ((Machine) ((Actuator) this.getBoxedProperty("createAgentActuator")));
                tempMachine2.setNewBoxedProperty("scheduler", scheduler);
                tempMachine2.setNewBoxedProperty("descheduler", descheduler);
                tempMachine2.setProperty("resultContainer", new Thing());
                tempMachine2.setNewBoxedProperty("agentList", agentList);// HACK
                tempMachine2.setNewBoxedProperty("agentGenerator", tempMachine);
                tempMachine2.execute(); 
            }

            ArrayList<Agent> senderAgentList = (ArrayList<Agent>) this.getBoxedProperty("senderAgentList");
            ArrayList<Location> infoDepotList = (ArrayList<Location>) this.getBoxedProperty("infoDepotList");

            ArrayList<String> ddoscommAgentTypeList = new ArrayList<String>();
            ddoscommAgentTypeList.add("ddoscomm");
            ArrayList<String> commAgentTypeList = new ArrayList<String>();
            commAgentTypeList.add("badinfocomm");

            MASONSimScheduler scheduler = (MASONSimScheduler) this.getBoxedProperty("scheduler");
            MASONSimDeScheduler descheduler = (MASONSimDeScheduler) this.getBoxedProperty("descheduler");

            Thing graphContainer = (Thing) this.getProperty("graphContainer");
            if(senderAgentList.size() > 0) {
                /*
                  -----------------------------
                  Steal information from sender
                  -----------------------------

                  Creates an agent representing information stolen from the
                  sender at location, and send to one of the external nodes.
                */
                tempMachine = ((Machine) (new CommAgentGenerator()));
                tempMachine.setNewBoxedProperty("scheduler", scheduler);
                tempMachine.setNewBoxedProperty("descheduler", descheduler);
                tempMachine.setNewBoxedProperty("typeList", commAgentTypeList);
                tempMachine.setNewBoxedProperty("location", location);
                tempMachine.setNewBoxedProperty("message", "info");
                tempMachine.setProperty("graphContainer", graphContainer);
                tempMachine.setNewBoxedProperty("lifeCounter", new Double(100));

                tempMachine2 = ((Machine) (new CreateAgentActuator()));
                tempMachine2.setNewBoxedProperty("agentList", agentList);
                tempMachine2.setNewBoxedProperty("agentGenerator", tempMachine);

                tempMachine3 = ((Machine) (new SendCommAgentListActuator()));
                tempMachine3.setNewBoxedProperty("scheduler", scheduler);
                tempMachine3.setNewBoxedProperty("descheduler", descheduler);
                tempMachine3.setNewBoxedProperty("location", location);
                tempMachine3.setNewBoxedProperty("targetList", infoDepotList);
                tempMachine3.setProperty("graphContainer", graphContainer);
                tempMachine3.setNewBoxedProperty("agentList", agentList);
                tempMachine3.setNewBoxedProperty("CreateAgentActuator", tempMachine2);
                tempMachine3.execute();
            }

            /*
              -----------------------------
              Steal information in transit 
              -----------------------------

              If there is an information agent at the compromise agent's
              node, then create and send an stolen information agent back
              to one of the external nodes.
            */
            ArrayList<Agent> infoAgentList = (ArrayList<Agent>) this.getBoxedProperty("infoAgentList");
            if(infoAgentList.size() > 0) {
                ArrayList<String> infoStealingAgentTypeList = new ArrayList<String>();
                infoStealingAgentTypeList.add("infoSteal");

                tempMachine = ((Machine) (new CommAgentGenerator()));
                tempMachine.setNewBoxedProperty("scheduler", scheduler);
                tempMachine.setNewBoxedProperty("descheduler", descheduler);
                tempMachine.setNewBoxedProperty("typeList", commAgentTypeList);
                tempMachine.setNewBoxedProperty("location", location);
                tempMachine.setNewBoxedProperty("message", "info");
                tempMachine.setProperty("graphContainer", graphContainer);
                tempMachine.setNewBoxedProperty("lifeCounter", new Double(100));

                tempMachine2 = ((Machine) (new CreateAgentActuator()));
                tempMachine2.setNewBoxedProperty("agentList", agentList);
                tempMachine2.setNewBoxedProperty("agentGenerator", tempMachine);

                tempMachine3 = ((Machine) (new SendCommAgentListActuator()));
                tempMachine3.setNewBoxedProperty("scheduler", scheduler);
                tempMachine3.setNewBoxedProperty("descheduler", descheduler);
                tempMachine3.setNewBoxedProperty("location", location);
                tempMachine3.setNewBoxedProperty("targetList", infoDepotList);
                tempMachine3.setProperty("graphContainer", graphContainer);
                tempMachine3.setNewBoxedProperty("agentList", agentList);
                tempMachine3.setNewBoxedProperty("CreateAgentActuator", tempMachine2);
                tempMachine3.execute();
            }

            /*
              -------------------
              Corrupt information
              -------------------

              Increments the "corrupt information" counter at location.
            */
            ArrayList<Agent> receiverAgentList  = (ArrayList<Agent>) this.getBoxedProperty("receiverAgentList");
            if(receiverAgentList.size() > 0) {
                tempMachine = ((Machine) this.getBoxedProperty("depositBadInfoActuator"));
                tempMachine.setNewBoxedProperty("location", location);
                tempMachine.setNewBoxedProperty("badInfoIncrementer", new Double(1));
                tempMachine.execute();
            }
        }

        /*
          ===============
          Change location
          ===============
        */
        tempMachine = ((Machine) this.getBoxedProperty("changeLocationActuator"));
        tempMachine.execute();
    }

}



