package visualization;

import java.util.ArrayList;

public class GeneGroup 
{
	int sectionNumber = 0;
	ArrayList<String> geneNames = new ArrayList<String>();
	ArrayList<Double> geneValues = new ArrayList<Double>();
	int actuatorNumber = -1;
	String actuatorName = null;
	
	public GeneGroup(int sectionNumber)
	{
		this.sectionNumber = sectionNumber;
	}
	
	public void addGene(String geneName, double geneValue)
	{
		geneNames.add(geneName);
		geneValues.add(geneValue);
	}
	
	public double getGeneValue(int index)
	{
		return geneValues.get(index);
	}
	
	public String getGeneName(int index)
	{
		return geneNames.get(index);
	}
	
	public int getSize()
	{
		return geneValues.size();
	}
	
	public int getSectionNumber()
	{
		return sectionNumber;
	}
	
	public double getMeanValue()
	{
		double sum = 0;
		for(int i = 0; i < geneValues.size(); i++)
			sum += geneValues.get(i);
		return (sum / geneValues.size());
	}
	
	public int getActuatorNumber()
	{
		return actuatorNumber;
	}
	
	public void setActuatorNumber(int actuatorNumber)
	{
		this.actuatorNumber = actuatorNumber;
	}
	
	public String getActuatorName()
	{
		return actuatorName;
	}
	
	public void setActuatorName(String actuatorName)
	{
		this.actuatorName = actuatorName;
	}
	
}
