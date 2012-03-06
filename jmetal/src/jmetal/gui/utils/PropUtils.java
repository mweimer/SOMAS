/**
 * PropUtils.java
 *
 * @author Francisco Chicano
 * @version 1.0
 *
 * This class provides some utilities for working with properties.
 * Thanks to Francisco Chicano.
 */
package jmetal.gui.utils;

import java.util.*;
import java.io.*;

public abstract class PropUtils extends Object
{
	
	static public final char LABEL_RIGHT_DELIMITER = '>';
	static public final char LABEL_LEFT_DELIMITER = '<';
	
	static public Properties getPropertiesWithPrefix (Properties pro, String prefix)
	{
		Enumeration en;
		Properties aux = new Properties();
		
		en = pro.propertyNames();
		
		for (;en.hasMoreElements();)
		{
			String nom = (String) en.nextElement();
			
			if (nom.startsWith(prefix))
			{
				
				aux.setProperty (nom.substring(prefix.length()), pro.getProperty(nom));
			}
		}
		
		return aux;
	}
	
	static public Properties putPrefixToProperties (String prefix, Properties pro)
	{
		Enumeration en;
		Properties res = new Properties();
		
		en = pro.propertyNames();
		
		for (; en.hasMoreElements();)
		{
			String nom = (String) en.nextElement();
			
			res.setProperty (prefix+nom, pro.getProperty (nom));
		}
		
		return res;
	}
	
	static public Properties substituteLabels (Properties base, Properties labels)
	{
		Properties res = new Properties ();
		Properties aux;
		Enumeration en;
		String key;
		String value;
		
		for (en = base.propertyNames();en.hasMoreElements();)
		{
			key = (String)en.nextElement();
			
			value = base.getProperty (key);
			
			value.trim();
			
			if (isLabel(value))
			{
				/*
				if (labels.getProperty(value) != null)
				{
					res.setProperty (key, labels.getProperty (value));
				}
				*/
				aux = getPropertiesWithPrefix (labels, value);
				aux = putPrefixToProperties (key, aux);
				
				res.putAll (aux);
			}
			else
			{
				res.setProperty (key, value);
			}
			
		}
		
		return res;
		
	}
	
	static public Properties dereferenceProperties (Properties pro)
	{
		Properties res = new Properties();
		Properties aux;
		Enumeration en;
		String key;
		String value;
		
		for (en = pro.propertyNames();en.hasMoreElements();)
		{
			key = (String)en.nextElement();
			
			value = pro.getProperty (key);
			
			value.trim();
			
			if (isLabel(value))
			{
				/*
				if (labels.getProperty(value) != null)
				{
					res.setProperty (key, labels.getProperty (value));
				}
				*/
				
				String lab = value.substring(1,value.length()-1);
				
				aux = getPropertiesWithPrefix (pro, lab);
				
				if (aux.isEmpty())
				{
					res.setProperty(key, value);
				}
				else
				{
					aux = putPrefixToProperties (key, aux);
				}
				
				res.putAll (aux);
			}
			else
			{
				res.setProperty (key, value);
			}
			
		}
		
		return res;
		
		
	}
	
	static public boolean isLabel (String str)
	{
		return  (str.indexOf (LABEL_LEFT_DELIMITER)==0 && 
				str.indexOf (LABEL_RIGHT_DELIMITER) == str.length()-1);
	}
	
	static public void main (String [] argv) throws Exception
	{
		Properties base = new Properties();
		Properties delta = new Properties();
		
		InputStream isbase = new FileInputStream (argv[0]);
		//InputStream isdelta = new FileInputStream (argv[1]);
		
		base.load(isbase);
		//delta.load(isdelta);
		
		Properties res = dereferenceProperties (base);
		

				
	}


    /**
     * @param file The file containing the properties
     * @return A <code>Properties</code> object
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    static public Properties load(String file) throws FileNotFoundException, IOException {
       Properties properties = new Properties();    
       FileInputStream in = new FileInputStream(file);
       properties.load(in);
       in.close();
       return properties;
    } // load


    static public Properties setDefaultParameters(Properties properties, String algorithmName) {

      // Parameters and Results are duplicated because of a Concurrent Modification Exception
      Properties parameters = PropUtils.getPropertiesWithPrefix(properties, algorithmName+".DEFAULT");
      Properties results    = PropUtils.getPropertiesWithPrefix(properties, algorithmName+".DEFAULT");
      Iterator<Object> iterator = parameters.keySet().iterator();

      while (iterator.hasNext()) {
         String parameter = parameters.getProperty((String)iterator.next());

         Properties subParameters = PropUtils.getPropertiesWithPrefix(properties,parameter+".DEFAULT");

         if (subParameters!=null) {
            PropUtils.putPrefixToProperties(parameter,subParameters);
            results.putAll(PropUtils.putPrefixToProperties(parameter+".",subParameters));
         }
      }
      return results;
  } //


  static public Properties setDefaultParameters2(Properties properties, String algorithmName) {

      // Parameters and Results are duplicated because of a Concurrent Modification Exception
      Properties parameters = PropUtils.getPropertiesWithPrefix(properties, algorithmName);
     
      return parameters;
  } //


}


