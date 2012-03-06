package org.math.somas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import scenario.Scenario;
import visualization.ChromoVisualizer;

public class CDFrame extends JFrame implements ActionListener
{
	private double[] objectiveValues;
	private String scenarioName;
	private String chromo;
	private JButton visualizeButton;
	private JButton launchButton;
	private JButton addButton;
	private ChromoVisualizer chromoVisualizer = null;
	public static ArrayList<String> multiAgentChromoList = new ArrayList<String>();
	
	public CDFrame(double[] objectiveValues, String scenarioName, String chromo)
	{
		super("Chromosome Details");
		this.objectiveValues = objectiveValues;
		this.scenarioName = scenarioName;
		this.chromo = chromo;
		
		this.setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(new JLabel("Objective Values:"), BorderLayout.NORTH);
		JTextArea objectiveValuesField = new JTextArea();
		objectiveValuesField.setEditable(false);
		objectiveValuesField.setText(objectiveValues[0] + " " + objectiveValues[1] + " " + objectiveValues[2]);
		topPanel.add(objectiveValuesField, BorderLayout.CENTER);
		topPanel.add(new JLabel("Chromosome:"), BorderLayout.SOUTH);
		this.add(topPanel, BorderLayout.NORTH);
		
		JTextArea chromoArea = new JTextArea();
		chromoArea.setEditable(false);
		chromoArea.setLineWrap(true);
		chromoArea.setText(chromo);
		JScrollPane chromoScrollPane = new JScrollPane(chromoArea);
		this.add(chromoScrollPane, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(3, 2));
		launchButton = new JButton("Launch");
		launchButton.addActionListener(this);
		bottomPanel.add(new JLabel("Run " + scenarioName + " with this chromosome:"));
		bottomPanel.add(launchButton);
		bottomPanel.add(new JLabel("Visualize chromosome:"));
		visualizeButton = new JButton("Visualize");
		visualizeButton.addActionListener(this);
		bottomPanel.add(visualizeButton);
		bottomPanel.add(new JLabel("Add chromosome to multi-agent list:"));
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		bottomPanel.add(addButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(launchButton))
			Scenario.getNewScenario(scenarioName).visualize(chromo, false);
		else if(e.getSource().equals(visualizeButton))
		{
			if(chromoVisualizer == null)
				chromoVisualizer = new ChromoVisualizer(chromo);
			else
				chromoVisualizer.setFrameVisiblity(!chromoVisualizer.getFrameVisiblity());
			
		}
		else if(e.getSource().equals(addButton))
		{
			for(String s : multiAgentChromoList)
			{
				if(s.equals(chromo))
				{
					JOptionPane.showMessageDialog(null, "Chromosome is already in the list", "Chromo not added",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			multiAgentChromoList.add(chromo);
			JOptionPane.showMessageDialog(null, "Chromosome added to the list", "Chromo added",JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
