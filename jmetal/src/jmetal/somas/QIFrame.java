package jmetal.somas;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;

import scenario.Scenario;

import jmetal.base.SolutionSet;
import jmetal.qualityIndicator.GeneralizedSpread;
import jmetal.qualityIndicator.GenerationalDistance;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.qualityIndicator.InvertedGenerationalDistance;
import jmetal.qualityIndicator.Spread;
import jmetal.somas.modified.Epsilon_SOMAS;

public class QIFrame extends JFrame
{
	DecimalFormat df = new DecimalFormat("#.####");
	private String scenarioName;
	private ArrayList<SolutionSet> paretoFronts;
	private ArrayList<String> paretoFrontNames;
	private ArrayList<double[]> qualityIndicators;
	private ArrayList<String> qualityIndicatorNames;
	private ArrayList<double[]> metrics;
	
	public QIFrame(String scenarioName, ArrayList<SolutionSet> paretoFronts, ArrayList<String> paretoFrontNames)
	{
		super("Quality Indicators");
		
		this.scenarioName = scenarioName;
		this.paretoFronts = paretoFronts;
		this.paretoFrontNames = paretoFrontNames;
		
		calculateQIs();
		
		JScrollPane scrollPane = new JScrollPane();
		JPanel mainPanel = new JPanel();
		BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(layout);
		
		JPanel qiPanel = new JPanel();
		int rows = qualityIndicators.size() + 1;
		int columns = qualityIndicators.get(0).length + 1;
		GridLayout qiLayout = new GridLayout(rows, columns);
		qiPanel.setLayout(qiLayout);
		
		qiPanel.add(new JLabel("(ref-approx)"));
		qiPanel.add(new JLabel("HyperVolume"));
		qiPanel.add(new JLabel("IGD"));
		qiPanel.add(new JLabel("GD"));
		qiPanel.add(new JLabel("Spread"));
		qiPanel.add(new JLabel("GS"));
		qiPanel.add(new JLabel("Epsilon"));
		
		int nameIndex = 0;
		for(int i = 0; i < qualityIndicators.size(); i++)
		{
			qiPanel.add(new JLabel(qualityIndicatorNames.get(nameIndex++)));
			for(int j = 0; j < qualityIndicators.get(i).length; j++)
				qiPanel.add(new JLabel(df.format(qualityIndicators.get(i)[j])));
		}
		
		mainPanel.add(qiPanel);
		
		JPanel metricsPanel = new JPanel();
		rows = metrics.size() + 1;
		columns = metrics.get(0).length+1;
		GridLayout metricsLayout = new GridLayout(rows, columns);
		metricsPanel.setLayout(metricsLayout);
		
		metricsPanel.add(new JLabel());
		for(int i = 0; i < paretoFrontNames.size(); i++)
			metricsPanel.add(new JLabel(paretoFrontNames.get(i)));
	
		metricsPanel.add(new JLabel("Hypervolume"));
		for(int i = 0; i < metrics.get(0).length; i++)
			metricsPanel.add(new JLabel(df.format(metrics.get(0)[i])));
		metricsPanel.add(new JLabel("Spread"));
		for(int i = 0; i < metrics.get(1).length; i++)
			metricsPanel.add(new JLabel(df.format(metrics.get(1)[i])));
		metricsPanel.add(new JLabel("GS"));
		for(int i = 0; i < metrics.get(2).length; i++)
			metricsPanel.add(new JLabel(df.format(metrics.get(2)[i])));
		
		mainPanel.add(metricsPanel);
		
		JPanel fyiPanel = new JPanel();
		GridLayout fyiLayout = new GridLayout(3, 1);
		fyiPanel.setLayout(fyiLayout);
		fyiPanel.add(new JLabel("*IGD: Inverted Generational Distance"));
		fyiPanel.add(new JLabel("*GD: Generational Distance"));
		fyiPanel.add(new JLabel("*GS: Generalized Spread"));
		mainPanel.add(fyiPanel);
		
		scrollPane.setViewportView(mainPanel);
		setContentPane(scrollPane);
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);
		pack();
		setSize(getWidth()+50, getHeight()+30);
	}
	
	private void calculateQIs()
	{
		qualityIndicators = new ArrayList<double[]>();
		qualityIndicatorNames = new ArrayList<String>();
		metrics = new ArrayList<double[]>();
		                       
		Hypervolume hypervolume = new Hypervolume();
		InvertedGenerationalDistance igd = new InvertedGenerationalDistance();
		GenerationalDistance gd = new GenerationalDistance();
		Spread spread = new Spread();
		GeneralizedSpread gs = new GeneralizedSpread();
		Epsilon_SOMAS epsilon = new Epsilon_SOMAS();
		
		int numberOfObjectives = Scenario.getNumObjectives(scenarioName);
		
		for(int i = 0; i < paretoFronts.size() - 1; i++)
		{
			for(int j = i+1; j < paretoFronts.size(); j++)
			{
				double[][] referenceFront = paretoFronts.get(i).writeObjectivesToMatrix();
				double[][] approxFront = paretoFronts.get(j).writeObjectivesToMatrix();
				double[] indicators = new double[6];
				indicators[0] = hypervolume.hypervolume(approxFront, referenceFront, numberOfObjectives);
				indicators[1] = igd.invertedGenerationalDistance(approxFront, referenceFront, numberOfObjectives);
				indicators[2] = gd.generationalDistance(approxFront, referenceFront, numberOfObjectives);
				indicators[3] = spread.spread(approxFront, referenceFront, numberOfObjectives);
				indicators[4] = gs.generalizedSpread(approxFront, referenceFront, numberOfObjectives);
				indicators[5] = epsilon.epsilon(approxFront, referenceFront, numberOfObjectives);
				qualityIndicators.add(indicators);
				qualityIndicatorNames.add(paretoFrontNames.get(i)+"-"+paretoFrontNames.get(j));
				
				referenceFront = paretoFronts.get(j).writeObjectivesToMatrix();
				approxFront = paretoFronts.get(i).writeObjectivesToMatrix();
				indicators = new double[6];
				indicators[0] = hypervolume.hypervolume(approxFront, referenceFront, numberOfObjectives);
				indicators[1] = igd.invertedGenerationalDistance(approxFront, referenceFront, numberOfObjectives);
				indicators[2] = gd.generationalDistance(approxFront, referenceFront, numberOfObjectives);
				indicators[3] = spread.spread(approxFront, referenceFront, numberOfObjectives);
				indicators[4] = gs.generalizedSpread(approxFront, referenceFront, numberOfObjectives);
				indicators[5] = epsilon.epsilon(approxFront, referenceFront, numberOfObjectives);
				qualityIndicators.add(indicators);
				qualityIndicatorNames.add(paretoFrontNames.get(j)+"-"+paretoFrontNames.get(i));
			}
		}
		
		double[] hypervolumes = new double[paretoFronts.size()];
		double[] spreads = new double[paretoFronts.size()];
		double[] gss = new double[paretoFronts.size()];
		
		for(int i = 0; i < paretoFronts.size(); i++)
		{
			double[][] front = paretoFronts.get(i).writeObjectivesToMatrix();
			hypervolumes[i] = hypervolume.hypervolume(front, front, numberOfObjectives);
		    spreads[i] = spread.spread(front, front, numberOfObjectives);
		    gss[i] = gs.generalizedSpread(front, front, numberOfObjectives);
		}
		
		metrics.add(hypervolumes);
		metrics.add(spreads);
		metrics.add(gss);
	}
}

	
