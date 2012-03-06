/**
 * ConfigurationsContainer.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This interface should be implemented by all the GUI components which are aimed
 * at storing configurations. Each configuration is stored in a properties
 * item
 */

package jmetal.gui;

import java.util.Properties;

public interface ConfigurationsContainer {

  public void addConfiguration(Properties configuration, String name);
  public Properties getConfiguration(String name);

} // ConfigurationsContainer
