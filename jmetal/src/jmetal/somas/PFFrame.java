package jmetal.somas;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.BitSet;

import javax.swing.*;

import jmetal.base.SolutionSet;
import jmetal.base.variable.Binary;

import org.math.plot.Plot3DPanel;
import org.math.somas.CDFrame;

import scenario.Scenario;

public class PFFrame extends JFrame implements ActionListener
{
	private int colorIndex = 0;
	private int xObjective = 0;
	private int yObjective = 1;
	private int zObjective = 2;
	private static final Color[] colors = {Color.RED, Color.CYAN, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.BLUE, Color.ORANGE};
	private JComboBox xAxisBox;
	private JComboBox yAxisBox;
	private JComboBox zAxisBox;
	private JButton qiButton;
	private Plot3DPanel plot;
	private String scenarioName;
	private ArrayList<SolutionSet> paretoFronts;
	private ArrayList<String> paretoFrontNames;
	private QIFrame qiFrame = null;
	private JMenuItem newGraphItem;
	private JMenuItem closeWindowsItem;
	private JMenuItem exitItem;
	private JMenuItem runMAItem;
	private JMenuItem clearMAItem;
	
	public PFFrame(String scenarioName, ArrayList<SolutionSet> paretoFronts, ArrayList<String> paretoFrontNames)
	{
		super("Pareto Fronts for " + scenarioName);
		
		this.scenarioName = scenarioName;
		this.paretoFronts = paretoFronts;
		this.paretoFrontNames = paretoFrontNames;
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		newGraphItem = new JMenuItem("New Graph",KeyEvent.VK_N);
		newGraphItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		newGraphItem.addActionListener(this);
		menu.add(newGraphItem);
		closeWindowsItem = new JMenuItem("Close All Other Windows",KeyEvent.VK_C);
		closeWindowsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		closeWindowsItem.addActionListener(this);
		menu.add(closeWindowsItem);
		
		exitItem = new JMenuItem("Exit",KeyEvent.VK_E);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		exitItem.addActionListener(this);
		menu.add(exitItem);
		menuBar.add(menu);
		
		menu = new JMenu("Multi-agent");
		menu.setMnemonic(KeyEvent.VK_M);
		runMAItem = new JMenuItem("Run sim with multi-agents",KeyEvent.VK_R);
		runMAItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		runMAItem.addActionListener(this);
		menu.add(runMAItem);
		clearMAItem = new JMenuItem("Clear multi-agent list",KeyEvent.VK_L);
		clearMAItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		clearMAItem.addActionListener(this);
		menu.add(clearMAItem);
		menuBar.add(menu);
		
		
		JPanel mainPanel = new JPanel();
		BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(mainLayout);
		
		JPanel optionPanel = new JPanel();
		BoxLayout optionLayout = new BoxLayout(optionPanel, BoxLayout.X_AXIS);
		optionPanel.setLayout(optionLayout);
		JLabel xAxisLabel = new JLabel("X-Axis: ");
	    xAxisBox = new JComboBox(Scenario.getMetricNames(scenarioName));
	    xAxisBox.addActionListener(this);
	    xAxisBox.setSelectedIndex(xObjective);
		JLabel yAxisLabel = new JLabel(" Y-Axis: ");
		yAxisBox = new JComboBox(Scenario.getMetricNames(scenarioName));
		yAxisBox.addActionListener(this);
		yAxisBox.setSelectedIndex(yObjective);
		JLabel zAxisLabel = new JLabel(" Z-Axis: ");
		zAxisBox = new JComboBox(Scenario.getMetricNames(scenarioName));
		zAxisBox.addActionListener(this);
		zAxisBox.setSelectedIndex(zObjective);
		optionPanel.add(xAxisLabel);
		optionPanel.add(xAxisBox);
		optionPanel.add(yAxisLabel);
		optionPanel.add(yAxisBox);
		optionPanel.add(zAxisLabel);
		optionPanel.add(zAxisBox);
		qiButton = new JButton("Quality Indicators");
		qiButton.addActionListener(this);
		optionPanel.add(qiButton);
		mainPanel.add(optionPanel);
		
		plot = new Plot3DPanel();
		createPlot();
		mainPanel.add(plot);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(mainPanel);
		setSize(1200, 900);
		setVisible(true);
	}
	
	private Color getNextColor()
	{
		if(colorIndex >= colors.length)
			colorIndex = 0;
		return colors[colorIndex++];
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(xAxisBox))
		{
			if(xObjective != xAxisBox.getSelectedIndex())
			{
				xObjective = xAxisBox.getSelectedIndex();
				plot.removeAllPlots();
				colorIndex = 0;
				createPlot();
			}
		}
		else if(e.getSource().equals(yAxisBox))
		{
			if(yObjective != yAxisBox.getSelectedIndex())
			{
				yObjective = yAxisBox.getSelectedIndex();
				plot.removeAllPlots();
				colorIndex = 0;
				createPlot();
			}
		}
		else if(e.getSource().equals(zAxisBox))
		{
			if(zObjective != zAxisBox.getSelectedIndex())
			{
				zObjective = zAxisBox.getSelectedIndex();
				plot.removeAllPlots();
				colorIndex = 0;
				createPlot();
			}
		}
		else if(e.getSource().equals(qiButton))
		{
			if(qiFrame == null)
			{
				qiFrame = new QIFrame(scenarioName, paretoFronts, paretoFrontNames);
				qiFrame.setLocation(getX()+getWidth(), qiFrame.getY());
			}
			else if(qiFrame != null && !qiFrame.isVisible())
				qiFrame.setVisible(true);
			else if(qiFrame != null && qiFrame.isVisible())
			
				qiFrame.setVisible(false);	
		}
		else if(e.getSource().equals(closeWindowsItem))
			closeAllOtherWindows();
		else if(e.getSource().equals(exitItem))
			System.exit(0);
		else if(e.getSource().equals(newGraphItem))
		{
			closeAllOtherWindows();
			this.dispose();
			ResultsVisualizer.main(null);
		}
		else if(e.getSource().equals(runMAItem))
		{
			Scenario.getNewScenario(scenarioName).visualize(CDFrame.multiAgentChromoList);
		}
		else if(e.getSource().equals(clearMAItem))
		{
			CDFrame.multiAgentChromoList.clear();
		}
		
	}
	
	private void closeAllOtherWindows()
	{
		Frame[] frames = JFrame.getFrames();
		for(int i = 0; i < frames.length; i++)
		{
			if(!frames[i].equals(this))
			{
				frames[i].dispose();
			}
		}
	}
	private void createPlot()
	{
		
	    for(int i = 0; i < paretoFronts.size(); i++)
	    {
	    	double [][] objectiveValues = paretoFronts.get(i).writeObjectivesToMatrix();
	    	double [] xvalues = new double[objectiveValues.length];
	    	double [] yvalues = new double[objectiveValues.length];
	    	double [] zvalues = new double[objectiveValues.length];
			
			for(int j =0; j < objectiveValues.length; j++)
			{
				xvalues[j] = objectiveValues[j][xObjective];
				yvalues[j] = objectiveValues[j][yObjective];
				zvalues[j] = objectiveValues[j][zObjective];
				
			}
			
			BitSet[] chromosomes = new BitSet[paretoFronts.get(i).size()];
			int numBits = ((Binary) paretoFronts.get(i).get(0).getDecisionVariables()[0]).getNumberOfBits();
			for(int j = 0; j < paretoFronts.get(i).size(); j++)
				chromosomes[j] = ((Binary) paretoFronts.get(i).get(j).getDecisionVariables()[0]).bits_;
			
			
			plot.addSomasScatterPlot(paretoFrontNames.get(i) + " Results Objective Values", 
					getNextColor(), xvalues, yvalues, zvalues, chromosomes, numBits, scenarioName);
	    }
		
		String[] objNames = Scenario.getMetricNames(scenarioName);
		plot.setAxisLabels(objNames[xObjective], objNames[yObjective], objNames[zObjective]);
		
		for(int i = 0; i < plot.plotCanvas.plots.size() -1 ; i++)
			plot.plotCanvas.plots.get(i).setVisible(false);

	}
}
