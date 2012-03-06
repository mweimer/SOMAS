package visualization;

import javax.swing.*;

import common.Thing;

/**
 * StatisticsFrame is JFrame used by the StatisticsVisualizer to display
 * various statistics occurring in a scenario. 
 * 
 * @author      Matt Weimer
 * @see			StatisticsVisualizer
 */
public class StatisticsFrame extends JFrame
{
	private StatisticsVisualizer statisticsVisualizer;
	
	private JPanel statsPanel = new JPanel();
	private JLabel somasAgentCountLabel = new JLabel("SOMAS Agent Count:");
	private JLabel compromiseAgentCountLabel = new JLabel("Compromise Agent Count:");
	private JLabel ddosAttackCountLabel = new JLabel("DDoS Attack Count:");
	private JLabel ddosCountLabel = new JLabel("DDoS Count:");
	private JLabel senderCountLabel = new JLabel("Sender Agent Count:");
	private JLabel receiverCountLabel = new JLabel("Receiver Agent Count:");
	private JLabel infoCountLabel = new JLabel("Information Packet Count:");
	private JLabel activeNodeCountLabel = new JLabel("Active Node Count:");
	private JLabel inactiveNodeCountLabel = new JLabel("Inactive Node Count:");
	private JLabel insiderNodeCountLabel = new JLabel("Insider Count:");
	private JLabel defenseAgentCountLabel = new JLabel("Defense Agent Count:");
	private JLabel badInfoCountLabel = new JLabel("Bad Information Packet Count:");
	private JLabel goodInfoCountLabel = new JLabel("Good Information Packet Count:");
	private JLabel ddosInfoCountLabel = new JLabel("DDoS Packet Count:");
	
	/** 
	 * Creates an instance of StatisticsFrame with a given
	 * StatisticsVisualizer as the source of statistics
	 * 
	 * @param s		the StatisticsVisualizer that will be generating the information
	 */
	public StatisticsFrame(StatisticsVisualizer s)
	{
		super("Simulation Statistics");
		
		statisticsVisualizer = s;
		
		setSize(381, 294);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		BoxLayout layout = new BoxLayout(statsPanel, BoxLayout.Y_AXIS);
		statsPanel.setLayout(layout);
		
		add(statsPanel);
		addLabels();
		
		setLocation(840, 385);
		setVisible(true);
	}
	
	/**
     * Updates the contents of the frame with current statistics from the StatisticsVisualizer
     */
	public void update()
	{
		somasAgentCountLabel.setText("SOMAS Agent Count: " + statisticsVisualizer.somasAgentCount);
		defenseAgentCountLabel.setText("Defense Agent Count: " + statisticsVisualizer.defenseAgentCount);
		ddosCountLabel.setText("DDoS Count: " + statisticsVisualizer.ddosCount);
		activeNodeCountLabel.setText("Active Node Count: " + statisticsVisualizer.activeNodeCount);		
		inactiveNodeCountLabel.setText("Inactive Node Count: " + statisticsVisualizer.inactiveNodeCount);
		ddosAttackCountLabel.setText("DDoS Attack Count: " + statisticsVisualizer.ddosAttackCount);
		senderCountLabel.setText("Sender Agent Count: " + statisticsVisualizer.senderCount);
		receiverCountLabel.setText("Receiver Agent Count: "+ statisticsVisualizer.receiverCount);
		infoCountLabel.setText("Information Packet Count: " + statisticsVisualizer.infoCount);
		badInfoCountLabel.setText("Bad Information Packet Count: " + statisticsVisualizer.badInfoCount);
		goodInfoCountLabel.setText("Good Information Packet Count: " + statisticsVisualizer.goodInfoCount);
		ddosInfoCountLabel.setText("DDoS Packet Count: " + statisticsVisualizer.ddosInfoCount);
		compromiseAgentCountLabel.setText("Compromise Agent Count: " + statisticsVisualizer.compromiseAgentCount);
		insiderNodeCountLabel.setText("Insider Count: " + statisticsVisualizer.insiderCount);
	}
	
	private void addLabels()
	{

		statsPanel.add(somasAgentCountLabel);
		statsPanel.add(defenseAgentCountLabel);
		statsPanel.add(compromiseAgentCountLabel);
		statsPanel.add(ddosCountLabel);
		statsPanel.add(ddosAttackCountLabel);
		statsPanel.add(senderCountLabel);
		statsPanel.add(receiverCountLabel);
		statsPanel.add(infoCountLabel);
		statsPanel.add(badInfoCountLabel);
		statsPanel.add(goodInfoCountLabel);
		statsPanel.add(ddosInfoCountLabel);
		statsPanel.add(activeNodeCountLabel);
		statsPanel.add(inactiveNodeCountLabel);
		statsPanel.add(insiderNodeCountLabel);
		
//	    if(statisticsVisualizer.ScenarioName.equals("CompetitionScenario"))
//		{
//	    	
//		}
//	    else if(statisticsVisualizer.ScenarioName.equals("DataExtractionScenario"))
//		{
//	    	
//		}
//	    else if(statisticsVisualizer.ScenarioName.equals("DDoSScenario"))
//		{
//	  
//		}
//	    else if(statisticsVisualizer.ScenarioName.equals("InfoWarScenario"))
//		{
//
//		}
//	    else if(statisticsVisualizer.ScenarioName.equals("InsiderAttackScenario"))
//		{
//	   
//		}
//	    else if(statisticsVisualizer.ScenarioName.equals("IntrusionEliminationScenario"))
//		{
//	    	
//		}
//	    else if(statisticsVisualizer.ScenarioName.equals("VitalElementScenario"))
//		{
//
//		}
	    
	}

	  
}
