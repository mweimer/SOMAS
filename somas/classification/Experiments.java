package classification;

import classification.NodeCounter;
import scenario.Scenario;
import java.io.*;

public class Experiments {
	
	
	public static void main(String[] args)
	{

		int i =0;
		
		// Change numRuns in accordance with how many samples are desired: numsamples / (observer% * numnodes)
		int numRuns = 80;
				
		int numBits = 5000;
		Integer[] boxedDecisionSpace = new Integer[numBits];
		
		for(i = 0; i < numBits; i++) {
			if(Math.random() < 0.5) {
				boxedDecisionSpace[i] = new Integer(1);
			}
			else
				boxedDecisionSpace[i] = new Integer(0);
		}
		
		i = 0;		
		while(i < numRuns)
		{
			i++;
			Scenario scenario = new InfoWarClassification();
			scenario.addDecisionSpace(boxedDecisionSpace);
			scenario.run();
			FileWriter fstream;
			BufferedWriter out;
			
			try {
				fstream = new FileWriter("InfoWar_exps_05percent.txt", true);
				out = new BufferedWriter(fstream);
				int nodeid = 0;
				//out.write("data = [");
				for(NodeCounter nc : scenario.classmets.NodeCounters.values()) {
					nodeid++;
					nc.finalStats();
					for(int j = 0; j < NodeCounter.NUM_COUNTS; j++) {
						out.write(nc.means[j] + " " + nc.vars[j] + " " + nc.maxs[j] + " " + nc.tmaxs[j] + " ");
					}
					out.write(";\n");
/*					for(int j = 0; j < NodeCounter.TIMESTEPS; j++) {
						out.write(" " + nc.counts[NodeCounter.C_AGENT][j]);
					}
					out.newLine();
					out.write("DDoSAgents density:");
					for(int j = 0; j < NodeCounter.TIMESTEPS; j++) {
						out.write(" " + nc.counts[NodeCounter.D_AGENT][j]);
					}
					out.newLine();
					out.write("DDoSAttack density:");
					for(int j = 0; j < NodeCounter.TIMESTEPS; j++) {
						out.write(" " + nc.counts[NodeCounter.D_ATTACK][j]);
					}
					out.newLine();
					out.write("DDosComm density:");
					for(int j = 0; j < NodeCounter.TIMESTEPS; j++) {
						out.write(" " + nc.counts[NodeCounter.D_COMM][j]);
					}
					out.newLine();
*/					
/*					out.write("CompromiseAgents density at timehack 0 = " + nc.t[0] + ": " + nc.timehack_cts[NodeCounter.C_AGENT][NodeCounter.INITIAL] + "\n");
					out.write("CompromiseAgents density at timehack 1 = " + nc.t[1] + ": " + nc.timehack_cts[NodeCounter.C_AGENT][NodeCounter.ONE] + "\n");
					out.write("CompromiseAgents density at timehack 2 = " + nc.t[2] + ": " + nc.timehack_cts[NodeCounter.C_AGENT][NodeCounter.TWO] + "\n");
					out.write("CompromiseAgents density at timehack 3 = " + nc.t[3] + ": " + nc.timehack_cts[NodeCounter.C_AGENT][NodeCounter.FINAL] + "\n");					
					out.write("CompromiseAgents Vi: " + nc.vi_counts[NodeCounter.C_AGENT] + "\n");
					out.write("CompromiseAgents Vf: " + nc.vf_counts[NodeCounter.C_AGENT] + "\n");
					out.write("CompromiseAgents Acceleration: " + nc.a_counts[NodeCounter.C_AGENT] + "\n");
					out.write("DDoSAgents density at timehack 0: " + nc.timehack_cts[NodeCounter.D_AGENT][NodeCounter.INITIAL] + "\n");
					out.write("DDoSAgents density at timehack 1: " + nc.timehack_cts[NodeCounter.D_AGENT][NodeCounter.ONE] + "\n");
					out.write("DDoSAgents density at timehack 2: " + nc.timehack_cts[NodeCounter.D_AGENT][NodeCounter.TWO] + "\n");
					out.write("DDoSAgents density at timehack 3: " + nc.timehack_cts[NodeCounter.D_AGENT][NodeCounter.FINAL] + "\n");
					out.write("DDoSAgents Vi: " + nc.vi_counts[NodeCounter.D_AGENT] + "\n");
					out.write("DDoSAgents Vf: " + nc.vf_counts[NodeCounter.D_AGENT] + "\n");
					out.write("DDoSAgents Acceleration: " + nc.a_counts[NodeCounter.D_AGENT] + "\n");
					out.write("DDoSAttack density at timehack 0: " + nc.timehack_cts[NodeCounter.D_ATTACK][NodeCounter.INITIAL] + "\n");
					out.write("DDoSAttack density at timehack 1: " + nc.timehack_cts[NodeCounter.D_ATTACK][NodeCounter.ONE] + "\n");
					out.write("DDoSAttack density at timehack 2: " + nc.timehack_cts[NodeCounter.D_ATTACK][NodeCounter.TWO] + "\n");
					out.write("DDoSAttack density at timehack 3: " + nc.timehack_cts[NodeCounter.D_ATTACK][NodeCounter.FINAL] + "\n");
					out.write("DDoSAttack Vi: " + nc.vi_counts[NodeCounter.D_ATTACK] + "\n");
					out.write("DDoSAttack Vf: " + nc.vf_counts[NodeCounter.D_ATTACK] + "\n");
					out.write("DDoSAttack Acceleration: " + nc.a_counts[NodeCounter.D_ATTACK] + "\n");
					out.write("DDosComm density at timehack 0: " + nc.timehack_cts[NodeCounter.D_COMM][NodeCounter.INITIAL] + "\n");
					out.write("DDosComm density at timehack 1: " + nc.timehack_cts[NodeCounter.D_COMM][NodeCounter.ONE] + "\n");
					out.write("DDosComm density at timehack 2: " + nc.timehack_cts[NodeCounter.D_COMM][NodeCounter.TWO] + "\n");
					out.write("DDosComm density at timehack 3: " + nc.timehack_cts[NodeCounter.D_COMM][NodeCounter.FINAL] + "\n");
					out.write("DDosComm Vi: " + nc.vi_counts[NodeCounter.D_COMM] + "\n");
					out.write("DDosComm Vf: " + nc.vf_counts[NodeCounter.D_COMM] + "\n");
					out.write("DDoSComm Acceleration: " + nc.a_counts[NodeCounter.D_COMM] + "\n");
					*/
//					out.write("-----------------\n");
				}
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
}