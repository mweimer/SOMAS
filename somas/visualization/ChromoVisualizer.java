package visualization;

import java.util.ArrayList;
import representations.IntBitRep;
import common.Machine;
import unknown.Chromosome;
import unknown.IntBitToDelimitedDoubleChromosomeDecoder;

/**
 * ChromoVisualizer is used to visually display the contents and structure
 * of a SOMAS agent's chromosome. The structure of the chromosome is defined
 * in the ChromoVisualizer class by several String and int arrays. The contents
 * of these arrays are determined by looking at the way the chromosomes are 
 * structured in the SOMASAgentGenerator class.
 * <p>
 * Currently, the chromosome's are structured in the following format. First 
 * is the miscellaneous genes. Right now there is only one, Pheromone Value. All 
 * miscellaneous gene names should be listed in the miscNames array.
 * Second is the weights for the sensor decision values. For ChromoVisualizer to 
 * determine these, all names for actuators created for SOMAS agents in the 
 * SOMASAgentGenerator should be listed in the array actuatorNames in the order 
 * they are added in SOMASAgentGenerator. Also, all names for sensors created for 
 * SOMAS agents and their sensor variables added to the global sensor variable list 
 * in the SOMASAgentGenerator should be listed in the array sensorNames in the order 
 * they are added in SOMASAgentGenerator.
 * Third is the weights for the actuator parameters. For ChromoVisualizer to 
 * determine these, the number of parameters each actuator has added to the global
 * parameter list in SOMASAgentGenerator must be listed in the array numParametersPerActuator
 * in the same order they are added in SOMASAgentGenerator.
 * Fourth is a special sensor value, this is determined in ChromoVisualizer by
 * the string setValue. 
 * Fifth, is values for sensor parameters. For ChromoVisualizer 
 * to determine these the, number of parameters each sensor adds to the global
 * list in SOMASAgentGenerator must be listed in the array numParametersPerSensor
 * in the order they are added in SOMASAgentGenerator
 * 
 * @author      Matt Weimer
 */
public class ChromoVisualizer 
{
	private ChromoFrame chromoFrame = null;
	
	public static String[] miscNames = { "Pheromone Value" };
	
	public static String[] actuatorNames = { 
			"Create Agent",
			"Delete Agent",
			"Increase Pheromone",
			"Decrease Pheromone",
			"Set Pheromone On Self",
			"Mutate Self Chromosome",
			"Crossover Local Chromo",
			"Mark Location",
			"Unmark Location",
			"Change Location" };
	
	public static int[] numParametersPerActuator = {
			1,
			1,
			1,
			1,
			0,
			1,
			2,
			1,
			1,
			1 };
		
	public static String[] sensorNames = {
			"Location Number Agents",
			"Location Number Neighbors",
			"Location Pheromone",
			"Self Pheromone",
			"Location Agents' Cumulative Pheromone",
			"Location Marked",
			"Set Value",
			"Random Value" };
	
	public static int[] numParametersPerSensor = {
		2,
		2,
		2,
		2,
		2,
		0,
		0,
		0 };
	
	public static String setValue = "Set Value Sensor Variable";

	/** 
	 * Creates an instance of ChromoVisualizer with a chromosome
	 * created from the given decisionSpace
	 * 
	 * @param decisionSpace		the entire decisionSpace, all chromosomes
	 * 							listed in binary integer form.
	 */
	public ChromoVisualizer(Integer[] decisionSpace)
	{
		initFrame(decisionSpace);
	}
	
	public ChromoVisualizer(String decisionSpace)
	{
		initFrame(stringToIntArray(decisionSpace));
	}
	
	private void initFrame(Integer[] decisionSpace)
	{
		assert(actuatorNames.length == numParametersPerActuator.length && sensorNames.length == numParametersPerSensor.length);
		
		int numChromosomes = 3;
		int chromosomeSize = decisionSpace.length/numChromosomes;

		int[] chromosomeBits = new int[chromosomeSize];
	
		for(int k = 0; k < chromosomeSize; k++)
		    chromosomeBits[k] = decisionSpace[k];
	
		IntBitRep iBR = new IntBitRep(chromosomeBits);
	
		// Add chromosome bits to chromosome .
		Chromosome chromosome = (Chromosome) (new Chromosome());
		chromosome.setNewBoxedProperty("representation", iBR);
	
		// Set pheromone value of chromosome.
		Machine decoder = ((Machine) (new IntBitToDelimitedDoubleChromosomeDecoder()));
		decoder.setNewBoxedProperty("chromosome", chromosome);
		decoder.setNewBoxedProperty("result", new Object());
		decoder.execute();
		Double[] genes = ((Double[]) decoder.getBoxedProperty("result"));
		int geneIndex = 0;
		GeneGroup geneGroup;
		
		ArrayList<GeneGroup> geneGroups = new ArrayList<GeneGroup>();
		
		for(int i=0; i < miscNames.length; i++)
		{		
			geneGroup = new GeneGroup(0);
			geneGroup.addGene(miscNames[i] + " (misc)", genes[geneIndex++]);
			geneGroups.add(geneGroup);
		}
		
		for(int i=0; i < actuatorNames.length; i++)
		{
			geneGroup = new GeneGroup(1);
			geneGroup.setActuatorNumber(i);
			geneGroup.setActuatorName(actuatorNames[i]);
			for(int j=0; j < sensorNames.length; j++)
			{
				geneGroup.addGene(actuatorNames[i] + " Actuator Decision Variable-" + sensorNames[j] + " Sensor Weight",
						genes[geneIndex++]);
			}
			geneGroups.add(geneGroup);
		}
		
		for(int i=0; i < actuatorNames.length; i++)
		{
			for(int j=0; j < numParametersPerActuator[i]; j++)
			{
				geneGroup = new GeneGroup(2);
				geneGroup.setActuatorNumber(i);
				geneGroup.setActuatorName(actuatorNames[i]);
				for(int k=0; k < sensorNames.length; k++)
				{
					geneGroup.addGene(actuatorNames[i] + " Actuator Parameter " + Integer.toString(j+1) + "-" + sensorNames[k] + " Sensor Weight",
							genes[geneIndex++]);
				}
				geneGroups.add(geneGroup);
			}
		}
		
		geneGroup = new GeneGroup(3);
		geneGroup.addGene(setValue, genes[geneIndex++]);
		geneGroups.add(geneGroup);
		
		for(int i=0; i < sensorNames.length; i++)
		{
			geneGroup = new GeneGroup(3);
			for(int j=0; j < numParametersPerSensor[i]; j++)
			{
					geneGroup.addGene(sensorNames[i] + " Sensor Set Parameter " + Integer.toString(j+1) + " Variable",
							genes[geneIndex++]);
			}
			geneGroups.add(geneGroup);
		}
		
        chromoFrame = new ChromoFrame(geneGroups, geneIndex);
	}
	
	/** 
	 * The main method allows this class to be utilized through the command line.
	 * It takes just decision space as a command line arg
	 * 
	 * @param args		command line arguments. It needs just one, the decision space.
	 */
    public static void main(String[] args) 
    {
    	String chromo = args[0];
    	int[] decisionSpace = new int[chromo.length()];
		for(int i = 0; i < chromo.length(); i++) {
			decisionSpace[i] = chromo.charAt(i) == '1' ? 1:0;
		}
		
		Integer[] boxedDecisionSpace = new Integer[decisionSpace.length];
    	for(int i = 0; i < decisionSpace.length; i++)
    	    boxedDecisionSpace[i] = new Integer(decisionSpace[i]);
		new ChromoVisualizer(boxedDecisionSpace);

    }
	
    private Integer[] stringToIntArray(String chromo)
    {
    	int[] decisionSpace = new int[chromo.length()];
		int k = 0; 

		for(int i = 0; i < chromo.length(); i++, k++) {
			decisionSpace[i] = chromo.charAt(i) == '1' ? 1:0;
		}

		Integer[] boxedDecisionSpace = new Integer[decisionSpace.length];
		for(int i = 0; i < decisionSpace.length; i++)
			boxedDecisionSpace[i] = new Integer(decisionSpace[i]);
		return boxedDecisionSpace;
    }
    
    public void setFrameVisiblity(boolean visible)
    {
    	chromoFrame.setVisible(visible);
    }
    
    public boolean getFrameVisiblity()
    {
    	return chromoFrame.isVisible();
    }

}
