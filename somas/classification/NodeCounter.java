package classification;

import scenario.Scenario;

public class NodeCounter {

	public final static int NUM_COUNTS = 4;
	public final static int C_AGENT = 0;
	public final static int D_AGENT = 1;
	public final static int D_ATTACK = 2;
	public final static int D_COMM = 3;
	public final static int TIMESTEPS = 100;
	
	public int n = 0;
	public double[] counts_avg = new double[NUM_COUNTS];
	public int[][] counts = new int[NUM_COUNTS][TIMESTEPS];
	
	public double[] means = new double[NUM_COUNTS];
	public double[] vars = new double[NUM_COUNTS];
	public int[] maxs = new int[NUM_COUNTS];
	public int[] tmaxs = new int[NUM_COUNTS];

	public int uptime = 0;
	
	public NodeCounter() {		
		for(int i = 0; i < NUM_COUNTS; i++) {
			counts_avg[i] = 0.0;
		}		
	}
	
	public void finalStats() {
		double[] subvars = new double[TIMESTEPS];
		for(int i = 0; i < NUM_COUNTS; i++) {
			int sum = 0;
			maxs[i] = 0;
			for(int j = 0; j < TIMESTEPS; j++) {
				sum += counts[i][j];
				if(counts[i][j] > maxs[i]) {
					maxs[i] = counts[i][j];
					tmaxs[i] = j;
				}
			}
			means[i] = ((double) sum) / (double) TIMESTEPS;
			
			double varsum = 0.0;
			for(int j = 0; j < TIMESTEPS; j++) {
				subvars[j] = ((double) counts[i][j]) - means[i];
				subvars[j] = subvars[j] * subvars[j];
				varsum += subvars[j];
			}
			vars[i] = varsum / (double) TIMESTEPS;
		}
		
	}
		
}
