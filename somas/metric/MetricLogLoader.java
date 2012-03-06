/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package metric;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import weka.core.Instances;
import common.*;

public class MetricLogLoader extends Metric {
    public MetricLogLoader() {}
    public MetricLogLoader(Thing t) {super(t);}
    
    public void run() {
    	StringWriter writer = ((ARFFWriter) this.getProperty("arffWriter")).getSW();
        this.setNewBoxedProperty("result", readDataString(writer));
    }
    
    Instances readDataString(Writer writer) {
        Instances result = null;
        
        try {
            Reader reader = new StringReader(writer.toString());

            result = new Instances(reader);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
