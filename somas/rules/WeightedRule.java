/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package rules;

import common.*;
import actuators.*;
import java.util.*;

public class WeightedRule extends Rule {
    public WeightedRule() {super();}
    public WeightedRule(Thing t) {
        super(t);
    }

    Double[][] weights = null;
    ArrayList<Thing> sensorVariableContainers = null;
        
    // HACK : This rule also scales the values using the weights
    @Override
	public void run() {
	Thing tempThing = new Thing();
	Machine tempMachine = null;

	/*
	  ========================
	  Initialize variables
	  ========================
	*/

	// Initiate weights for calculating decision and parameter
	// values.
        Thing weightsContainer = (Thing) this.getProperty("weightsContainer");
        weights = (Double[][]) weightsContainer.getProperty("weights");
        
	// Contains information from the environment.  This
	// information is weighted and summed to calculate decision
	// and parameter values.
        sensorVariableContainers = (ArrayList<Thing>) this.getProperty("sensorVariableContainers");

	// Holds decision values.
        ArrayList<Thing> decisionVariableContainers = (ArrayList<Thing>) this.getProperty("decisionVariableContainers");
        int numDecisionVariables = decisionVariableContainers.size();
        
	// Holds parameter values.
        ArrayList<Thing> parameterVariableContainers = (ArrayList<Thing>) this.getProperty("parameterVariableContainers");
        int numParameterVariables = parameterVariableContainers.size();

        int numElements = weights[0].length; // Assumes all weight sets are the same size
        int decisionVariablesStart = 0;
        int parameterVariablesStart = decisionVariablesStart + numDecisionVariables;
        
	/*
	  ========================
	  Calculate values
	  ========================

	  Calculate weighted sums for decision and parameter values.
	*/
        for(int i = 0; i < numDecisionVariables; i++) {
            int location = decisionVariablesStart + i;
	    tempThing = decisionVariableContainers.get(i);
	    tempThing.setProperty("object", calculateDecisionValue(location, numElements));
        }

        for(int i = 0; i < numParameterVariables; i++) {
            int location = parameterVariablesStart + i;
            tempThing = parameterVariableContainers.get(i);
	    tempThing.setProperty("object", calculateDecisionValue(location, numElements));
        }

	/*
	  ========================
	  Execute actuators
	  ========================
	  
	  Execute actuators based on decision values.
	*/
        ArrayList<Actuator> actuators = (ArrayList<Actuator>) this.getProperty("actuators");

        for(int i = 0; i < numDecisionVariables; i++) {
	    tempThing = decisionVariableContainers.get(i);
            Double decisionValue = (Double) tempThing.getProperty("object");

	    // Execute corresponding actuator if decision value passes
	    // threshold.
            if (decisionValue > 0.5) {
                tempMachine = ((Machine) actuators.get(i));
		tempMachine.execute();
	    }
        }
    }

    // The weighting function here is not a simple multiplication.  If
    // it were, then when the weight is < 0.5 or the sensor value is <
    // 0.5 a decision threshold of 0.5 will not cause the actuator to
    // be executed.  Thus, there is a greater than 0.5 chance, given
    // an a priori uniform distribution over weights and sensor
    // values, that the actuator will not execute.
    //
    // There are two solutions to this problem.  One is to find a
    // threshold value for which the chance of executing the actuator
    // is about 0.5.  However, the problem is the variance will still
    // be uneven, leading to local minima in the search space.  The
    // other solution is to generate a weighting function, providing a
    // flatter distribution over decision values and a smoother search
    // landscape.
    //
    // The latter solution is the one chosen for this implementation.  
    private Double calculateDecisionValue(int i, int numElements) {
        Double elementsSum = new Double(0);
        Double weightedScaledVariableSum = new Double(0);
            
        for(int j = 0; j < numElements; j++) {
            Double elementValue = weights[i][j]; // Assumes this is (0, 1) 
            elementsSum += elementValue;

            Double sensorVariableValue = (Double) sensorVariableContainers.get(j).getProperty("object");  // Assumes this is (0, 1)
            
            Double scaleValue = elementValue - 0.5;
            Double scaledAddition = new Double(0);
            Double scaledSubtraction = new Double(0);
           
            if(scaleValue > 0) // Scaling section
                scaledAddition = new Double(Math.sin(scaleValue * Math.PI * 0.5) * (1 - sensorVariableValue)); 
            else
                scaledSubtraction = new Double(Math.sin(scaleValue * Math.PI * 0.5) * (sensorVariableValue)); 
            
            Double scaledSensorVariableValue = sensorVariableValue + scaledSubtraction + scaledAddition;
                
            weightedScaledVariableSum += scaledSensorVariableValue * elementValue;
        }

        Double decisionValue = weightedScaledVariableSum / elementsSum; // TODO [#C] check for divide by zero error
        return decisionValue;
    }
}


