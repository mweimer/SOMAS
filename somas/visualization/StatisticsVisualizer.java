package visualization;

import java.awt.Color;
import java.util.*;

import agent.Agent;
import common.*;
import environment.Location;

/**
 * StatisticsVisualizer is used to generate and display relevant information
 * about an ongoing scenario. 
 * <p>
 * StatisticsVisualizer is a called every step of a MASON Simulation by a special 
 * simulation component added for it in the scenario classes. Each step the run 
 * method will be invoked and the StatisticsVisualizer will update the statistics
 * and redisplay them. The run method simply calls two methods, generateStats and 
 * the StatisticsFrame's update.
 * generateStats will calculate all the required statistics then store them in private
 * class variables. StatisticsVisualizer provides methods in which to access these variables.
 * StatisticsVisualizer uses a JFrame class called StatisticsFrame to actually 
 * display the information. All GUI code should be placed in that class. StatisticsVisualizer 
 * calls the StatisticsFrame's update method in run and the frame will get access the
 * stored information through provided methods and then update its contents.
 * 
 * @author      Matt Weimer
 */
public class StatisticsVisualizer extends Machine
{
	private StatisticsFrame frame;
	private Set<Location> locations;
	public int somasAgentCount = 0;
	public int compromiseAgentCount = 0;
	public int ddosAttackCount = 0;
	public int ddosCount = 0;
	public int senderCount = 0;
	public int receiverCount = 0;
	public int infoCount = 0;
	public int ddosInfoCount = 0;
	public int badInfoCount = 0;
	public int goodInfoCount = 0;
	public int activeNodeCount = 0;
	public int inactiveNodeCount = 0;
	public int insiderCount = 0;
	public int defenseAgentCount = 0;
	public String ScenarioName = null;
	
	/** 
	 * Creates an instance of StatisticsVisualizer with a given
	 * set of locations
	 * 
	 * @param lset		a set of all location nodes in the scenario
	 */
	public StatisticsVisualizer(Set<Location> locationSet)
	{
		super();
		locations = locationSet;
		frame = new StatisticsFrame(this);
	}
	
	public StatisticsVisualizer(Set<Location> locationSet, String scenarioName)
	{
		super();
		this.ScenarioName = scenarioName;
		this.locations = locationSet;
		
		frame = new StatisticsFrame(this);
	}

	@Override
	public void run() 
	{
		generateStats();
		if(frame != null)
			frame.update();
	}
	
	private void generateStats()
	{
		somasAgentCount = 0;
		compromiseAgentCount = 0;
		ddosAttackCount = 0;
		ddosCount = 0;
		senderCount = 0;
		receiverCount = 0;
		infoCount = 0;
		ddosInfoCount = 0;
		badInfoCount = 0;
		goodInfoCount = 0;
		activeNodeCount = 0;
		inactiveNodeCount = 0;
		insiderCount = 0;
		defenseAgentCount = 0;
			
		for(Location location : locations)
		{
			ArrayList<Agent> agentList = (ArrayList<Agent>) location.getBoxedProperty("agentList");
			for(Agent agent : agentList)
			{
				String type = agent.hasBoxedProperty("typeList") ? ((ArrayList<String>) agent.getBoxedProperty("typeList")).get(0): "default";
				
				if(type == "somas")
					somasAgentCount++;
				else if(type == "defense")
					defenseAgentCount++;	
				else if(type == "compromise")
					compromiseAgentCount++;	
				else if(type =="ddosattack")
					ddosAttackCount++;
				else if (type =="ddos")
					ddosCount++;
				else if (type =="sender")
					senderCount++;
				else if (type =="receiver")
					receiverCount++;
				else if (type == "comm")
					infoCount++;
				else if (type =="ddoscomm")
					ddosInfoCount++;
				else if (type =="goodinfocomm")
					goodInfoCount++;
				else if (type =="badinfocomm")
					badInfoCount++;
	
			}
			if((Boolean) location.getBoxedProperty("isVitalNode"))
				inactiveNodeCount++;
			else
				activeNodeCount++;
			
			if(location.hasBoxedProperty("insiderPresent"))
				insiderCount++;
				
		}
	}
}
