/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package sensors;

import common.Thing;

public class ArcTanFilterSensor extends Sensor {
    public ArcTanFilterSensor() {super();}
    public ArcTanFilterSensor(Thing t) {
        super(t);
    }

    @Override
	public void run() {
        Double value = (Double) this.getBoxedProperty("value");
        Double scalarNumerator = (Double) this.getBoxedProperty("scalarNumerator");
        Double scalarDenominator = (Double) this.getBoxedProperty("scalarDenominator");
                
        Double result = new Double(0);
                
        if(scalarDenominator > 0 )
            result = Math.pow(Math.atan(value), scalarNumerator/scalarDenominator);
                
        this.setBoxedProperty("sensorVariable", result);
    }

}
