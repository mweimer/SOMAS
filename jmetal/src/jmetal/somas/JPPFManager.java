package jmetal.somas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFClientConnection;
import org.jppf.client.JPPFClientConnectionImpl;
import org.jppf.management.JMXDriverConnectionWrapper;
import org.jppf.management.JMXNodeConnectionWrapper;
import org.jppf.management.JPPFManagementInfo;

public class JPPFManager {
	
	public static JPPFClient jppfClient = null;

	public static void closeClient()
	{
		if (jppfClient != null)
			jppfClient.close();
	}
	
	public static void initClient()
	{
		jppfClient = new JPPFClient();
	}
	
	public static void shutdown()
	{
		List<JMXDriverConnectionWrapper> serverConnections = new ArrayList<JMXDriverConnectionWrapper>();
		List<JMXNodeConnectionWrapper> nodeConnections = new ArrayList<JMXNodeConnectionWrapper>();

		// get a handle to the driver connections
		List<JPPFClientConnection> clientConnections = jppfClient.getAllConnections();
		// open actual connections to servers and add to list
		for(JPPFClientConnection c : clientConnections)
			serverConnections.add(((JPPFClientConnectionImpl) c).getJmxConnection());
		
		for(JMXDriverConnectionWrapper serverConnection: serverConnections)
		{
			 Collection<JPPFManagementInfo> nodeInfoList = null;
			 try 
			 {
				  // query the driver's JMX server for the attached nodes
				 nodeInfoList = serverConnection.nodesInformation();
			 } 
			 catch (Exception e) { System.err.println("Error getting JPPF node info."); }
			 
			 if(nodeInfoList != null)
			 {
				 for(JPPFManagementInfo nodeInfo : nodeInfoList)
				 {
					   // obtain a wrapper around the JMX connection
					   JMXNodeConnectionWrapper jmxClient = new JMXNodeConnectionWrapper(nodeInfo.getHost(), nodeInfo.getPort());
					   nodeConnections.add(jmxClient);
				 }
			 }
		 }
		
		for(JMXNodeConnectionWrapper nodeConnection: nodeConnections)
		{
			try 
			{
				nodeConnection.connectAndWait(10000);
				if(nodeConnection.isConnected())
					nodeConnection.shutdown(); 
				else
				{
					nodeConnection.close();
					nodeConnection = null; 
				}
			} 
			catch (Exception e) { System.err.println("Error shutting down JPPF node."); }
		}
		
		for(JMXDriverConnectionWrapper serverConnection: serverConnections)
		{
			 try 
			 { 
				 serverConnection.restartShutdown(0L, -1L); 
			 } 
			 catch (Exception e) { System.err.println("Error shutting down JPPF server."); }
		}
		
		closeClient();
	}
	
}
