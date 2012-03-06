package classification;

import scenario.Scenario;

public class OldNodeCounter {

	public final static int NUM_T = 4;
	public final static int INITIAL = 0;
	public final static int ONE = 1;
	public final static int TWO = 2;
	public final static int FINAL = 3;
	public final static int NUM_COUNTS = 4;
	public final static int C_AGENT = 0;
	public final static int D_AGENT = 1;
	public final static int D_ATTACK = 2;
	public final static int D_COMM = 3;
	public final static int TIMESTEPS = 100;
	
	public int n = 0;
	public double[] counts_avg = new double[NUM_COUNTS];
	public int[][] counts = new int[NUM_COUNTS][TIMESTEPS];
	public double[][] timehack_cts = new double[NUM_COUNTS][NUM_T];

	public int uptime = 0;
	
	/**
	 * track increase in agent density per unit time
	 *  over the first 10% of the simulation
	 */
	public double[] vi_counts = new double[NUM_COUNTS];
	
	/**
	 * track increase in agent density per unit time
	 *  over the last 10% of the simulation
	 */
	public double[] vf_counts = new double[NUM_COUNTS];
	
	/**
	 * track acceleration of agent density over the simulation
	 */
	public double[] a_counts = new double[NUM_COUNTS];
	
	public int next_t = 0;
	
	public int t_hack = 0;
	
	/**
	 * Times when measurements are to be taken
	 */
	public double[] t = new double[NUM_T];
	
	public OldNodeCounter() {
		// Set up array of times - used to measure velocities vf and vi, which are needed to measure acceleration of the various counts
		t[INITIAL] = 0.0;
		t[FINAL] = (double) Scenario.numclasssteps;
		t[ONE] = t[INITIAL] + 0.1 * t[FINAL];
		t[TWO] = t[FINAL] - 0.1 * t[FINAL];
		
		for(int i = 0; i < NUM_COUNTS; i++) {
			counts_avg[i] = 0.0;
		}
		
	}
	
	/**
	 * Calculate initial velocities (taken over first 10% of the simulation)
	 */
	public void calculateVi() {
		double v_period = t[ONE] - t[INITIAL];
		for(int i = 0; i < NUM_COUNTS; i++) {
			vi_counts[i] = (timehack_cts[i][ONE] - timehack_cts[i][INITIAL]) / v_period;
		}
	}
	
	/**
	 * Calculate final velocities (taken over last 10% of the simulation)
	 */
	public void calculateVf() {
		double v_period = t[FINAL] - t[TWO];
		for(int i = 0; i < NUM_COUNTS; i++) {
			vf_counts[i] = (timehack_cts[i][FINAL] - timehack_cts[i][TWO]) / v_period;
		}
	}
	
	/**
	 * Calculate acceleration of all measurements (average change between vf and vi)
	 */
	public void calculateAcceleration() {
		double a_period = t[FINAL] - t[ONE];
		for(int i = 0; i < NUM_COUNTS; i++) {
			a_counts[i] = (vf_counts[i] - vi_counts[i]) / a_period;
		}
	}
	
	public void updateStats() {
		if(next_t == 1) { // time to measure vi
			calculateVi();
		}
		else if(next_t == 3) { // time to measure vf and acceleration
			calculateVf();
			calculateAcceleration();
		}
	}
	
	public void updateTimeHacks() {
		for(int i = 0; i < NUM_COUNTS; i++) {
			timehack_cts[i][t_hack] = counts_avg[i];
		}
		t_hack++;
	}
		
}
