/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
package simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cern.colt.list.DoubleArrayList;

import sim.portrayal.DrawInfo2D;
import sim.util.Double2D;
import agent.Agent;

import common.Thing;
import environment.Location;

public class MASONLocationDraw extends MASONComponentDraw {
	public MASONLocationDraw() {
		super();
	}

	public MASONLocationDraw(Thing t) {
		super(t);
	}

	Map<Agent, Double2D> agentLocations = new HashMap<Agent, Double2D>();

	static Map<String, Color> agentColors = new HashMap<String, Color>();

	static {
		// Color of each agent type
		agentColors.put("comm", Color.BLUE);
		agentColors.put("ddoscomm", Color.BLUE);
		agentColors.put("goodinfocomm", Color.BLUE);
		agentColors.put("badinfocomm", Color.BLUE);
		agentColors.put("somas", Color.GREEN);
		agentColors.put("compromise", Color.RED);
		agentColors.put("defense", Color.RED);
		agentColors.put("ddos", Color.BLACK);
		agentColors.put("ddosattack", Color.LIGHT_GRAY);
		agentColors.put("sender", Color.ORANGE);
		agentColors.put("receiver", Color.YELLOW);
		agentColors.put("default", Color.LIGHT_GRAY);
	}

	/*
	 * IMPORTANT:
	 * 
	 * The visualization needs to show the SOMAS agents' fitness values, since
	 * they use these to communicate.
	 * 
	 * However, the values can range from 0 to +DOUBLE_MAX, so need to be scaled
	 * appropriately.
	 * 
	 * The following variables are used to generate the global SOMAS swarm
	 * fitness average without keeping track of global swarm. The fitness
	 * variables track the average per location.
	 * 
	 * The fitnessWindow accumulates averages, which are themselves averaged to
	 * get an approximate global average.
	 */
	static int countAgents = 1;

	static Double initfitness = 0.001;

	static Double maxfitness = initfitness;

	static Double totalfitness = initfitness;

	static int windowSize = 50;

	static ArrayDeque<Double> fitnessWindow = new ArrayDeque<Double>(windowSize);

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		Graphics2D graphics = (Graphics2D) this.getBoxedProperty("graphics");
		DrawInfo2D info = (DrawInfo2D) this.getBoxedProperty("info");

		/*
		 * ===== 
		 * Nodes 
		 * =====
		 */
		// Clear background behind node.
		graphics.setColor(Color.WHITE);
		graphics.fillOval(
				(int) (info.draw.x - (MASONSimClassContext.DIAMETER ) / 2),

				(int) (info.draw.y - (MASONSimClassContext.DIAMETER ) / 2),

				(int) Math.round(MASONSimClassContext.DIAMETER),

				(int) Math.round(MASONSimClassContext.DIAMETER));

		// Color node outline based on whether it has been deactivated or not.
Location location = (Location) this.getBoxedProperty("context");
		
		Boolean isVitalNode = (Boolean) location.getBoxedProperty("isVitalNode");
		if (isVitalNode)
			graphics.setColor(Color.RED); // Deactivated
		else if(location.hasBoxedProperty("insiderPresent")) 
			graphics.setColor(Color.ORANGE);
		else if(location.hasBoxedProperty("externalNode"))
	    	graphics.setColor(Color.BLUE);
		else if(location.hasBoxedProperty("backboneNode"))
			graphics.setColor(new Color(255, 0, 255));
		else if(location.hasBoxedProperty("insiderPresent")) 
			graphics.setColor(Color.BLACK);
		else
			graphics.setColor(Color.GREEN); // Active

	    
	    
	    
	    
		
		graphics.drawOval(
				(int) (info.draw.x - MASONSimClassContext.DIAMETER / 2),

				(int) (info.draw.y - MASONSimClassContext.DIAMETER / 2),

				(int) Math.round(MASONSimClassContext.DIAMETER),

				(int) Math.round(MASONSimClassContext.DIAMETER));

		/*
		 * ====== 
		 * Agents 
		 * ======
		 */
		/*
		 * ------------------------------- 
		 * Calculate average agent fitness
		 * -------------------------------
		 */
		ArrayList<Agent> agentList = (ArrayList<Agent>) this
				.getBoxedProperty("agentList");

		totalfitness = 0.0;

		countAgents = 0;
		for (Agent a : agentList) {
			String type = a.hasBoxedProperty("typeList") ? ((ArrayList<String>) a
					.getBoxedProperty("typeList")).get(0)
					: "default";
			if (type == "somas") {
				Double tempfitness = (Double) a.getBoxedProperty("pheromone");
				totalfitness += tempfitness;
				countAgents++;
			}
		}

		// TODO [#B] This is a speed hit. Need a more efficient way to calculate
		// the mean in the window.
		if (countAgents > 0)
			fitnessWindow.push(totalfitness / countAgents);
		while (fitnessWindow.size() > windowSize)
			fitnessWindow.removeLast();
		if (fitnessWindow.size() > 0) {
			DoubleArrayList tempFitnessArray = new cern.colt.list.DoubleArrayList();
			for (int curr = 0; curr < fitnessWindow.size(); curr++) {
				double tempFitnessValue = fitnessWindow.toArray(new Double[0])[curr];
				tempFitnessArray.add(tempFitnessValue);
			}
			maxfitness = cern.jet.stat.Descriptive.mean(tempFitnessArray);
		} else {
			maxfitness = 0.0;
		}

		/*
		 * ---------- 
		 * Draw agent 
		 * ----------
		 * 
		 * Each agent is given a random position on the node, which it retains
		 * throughout its life. This is done so agents can more easily be
		 * differentiated.
		 * 
		 * Then, the agents are colored according to type.
		 * 
		 * Finally, if the agent is a SOMAS agent, it receives a
		 * semi-transparent halo. This halo is sized according to its fitness
		 * value. The fitness value is scaled according to the average agent
		 * fitness.
		 */
		for (Agent a : agentList) {
			// Place agent.
			if (agentLocations.get(a) == null) {
				Double radius = Math.random();
				Double rho = Math.random() * 2 * Math.PI;

				Double x = radius * MASONSimClassContext.DIAMETER
						* Math.sin(rho) / 2;

				Double y = radius * MASONSimClassContext.DIAMETER
						* Math.cos(rho) / 2;

				agentLocations.put(a, new Double2D(x, y));
			}

			String type = a.hasBoxedProperty("typeList") ? ((ArrayList<String>) a
					.getBoxedProperty("typeList")).get(0)
					: "default";

			if (type == "somas") {
				// Draw fitness as yellow halo with reddish border.
				Double halosize = 6 + (((Double) a
						.getBoxedProperty("pheromone")) * 5 / maxfitness);

				graphics.setColor(new Color(1.0f, 0.75f, 0.0f, 0.02f));
				graphics
						.fillOval(
								(int) (info.draw.x + 1
										+ agentLocations.get(a).x - (halosize / 2)),
								(int) (info.draw.y + 1
										+ agentLocations.get(a).y - (halosize / 2)),
								(int) Math.round(halosize), (int) Math
										.round(halosize));
				graphics.setColor(new Color(1.0f, 0.0f, 0.0f, 0.1f));
				graphics
						.drawOval(
								(int) (info.draw.x + 1
										+ agentLocations.get(a).x - (halosize / 2)),
								(int) (info.draw.y + 1
										+ agentLocations.get(a).y - (halosize / 2)),
								(int) Math.round(halosize), (int) Math
										.round(halosize));
			}

			// Color agent.
			graphics.setColor(agentColors.get(type));
			graphics.fillOval(
					(int) (info.draw.x + agentLocations.get(a).x - Math
							.random()), (int) (info.draw.y
							+ agentLocations.get(a).y - Math.random()),
					(int) Math.round(5 + Math.random()), (int) Math
							.round(5 + Math.random()));
			graphics.setColor(Color.BLACK);
			graphics.drawOval(
					(int) (info.draw.x + agentLocations.get(a).x - Math
							.random()), (int) (info.draw.y
							+ agentLocations.get(a).y - Math.random()),
					(int) Math.round(5 + Math.random()), (int) Math
							.round(5 + Math.random()));
		}

		// This is important b/c it keeps deleted agent objects from being
		// retained, thus freeing up memory.
		Set<Agent> agents = new HashSet<Agent>(agentLocations.keySet());
		for (Agent agent : agents) {
			if (agent.getProperty("dead") != null)
				agentLocations.remove(agent);
		}
	}
}
