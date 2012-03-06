/**
 * ExperimentsFrame
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class provides a graphical interface for making experiments
 **/
package jmetal.gui;

import jmetal.gui.qualityIndicators.QualityIndicatorsPanel;
import jmetal.gui.utils.Configuration;
import jmetal.gui.components.problems.ProblemsPanel;
import jmetal.gui.components.problems.AddProblem;
import jmetal.gui.operators.AddOperator;
import jmetal.gui.components.algorithms.AddAlgorithm;
import jmetal.gui.components.algorithms.AlgorithmsConfigurationPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import jmetal.experiments.Experiment;

import jmetal.experiments.GUIBasedStudy;
import jmetal.experiments.Settings;
import jmetal.experiments.util.RBoxplot;
import jmetal.experiments.util.RWilcoxon;
import jmetal.gui.components.JMetalDialog;

public class ExperimentsSupportGUI extends JFrame {

	private int MAIN_WINDOWS_HEIGTH = 620;
	private int MAIN_WINDOWS_WIDTH = 550;
	private JTextField experimentNameField_ = new JTextField();
	private File workingFolder_ = new File(".");
	private JTextField workingFolderName_ = new JTextField(80);
	private File frontsFolder_ = new File(".");
	private JTextField frontsFolderName_ = new JTextField(80);
	private JTextField independentRuns_ = new JTextField();
	private AlgorithmsConfigurationPanel algorithmsPanel_;
	private ProblemsPanel problemsPanel_;
	private QualityIndicatorsPanel qualityIndicatorsPanel_;
	private JMenuBar menuBar_;
	private JMetalDialog  dialog = new JMetalDialog("jMetalExperiments");
	private JTextField rows_;
	private JTextField columns_;
	private JTextField threads_;
	private JCheckBox boxPlotsCheck;
	private JCheckBox wilcoxonCheck;
	private JCheckBox latexTablesCheck;
	private JFrame progressBar_;


	// ExperimetnsFrame
	public ExperimentsSupportGUI() {

		Container container      = getContentPane();
		algorithmsPanel_         = new AlgorithmsConfigurationPanel();
		problemsPanel_            = new ProblemsPanel();
		qualityIndicatorsPanel_ = new QualityIndicatorsPanel();


		independentRuns_.setText("30"); // minimum number of recommended runs

		// Title
		setTitle("Experiments Support GUI");

		Dimension d;
		d = new Dimension(Math.max(MAIN_WINDOWS_WIDTH, algorithmsPanel_.getPanel().getSize().width +
				problemsPanel_.getPanel().getSize().width +
				qualityIndicatorsPanel_.getPanel().getSize().width), MAIN_WINDOWS_HEIGTH);


		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		container.add(new JLabel("Experiment Name"), c);


		c.gridwidth = 4;
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		container.add(experimentNameField_, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 3;
		container.add(algorithmsPanel_.getPanel(), c);

		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 3;
		container.add(problemsPanel_.getPanel(), c);

		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 3;
		c.anchor = GridBagConstraints.NORTH;
		container.add(qualityIndicatorsPanel_.getPanel(), c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.WEST;
		container.add(new JLabel("Working Directory"), c);

		c.gridx = 2;
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		workingFolderName_.setText(workingFolder_.getAbsolutePath());
		workingFolderName_.setEditable(false);
		workingFolderName_.setAutoscrolls(true);
		container.add(workingFolderName_, c);

		c.gridx = 5;
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		JButton toFindButton = new JButton("Change");
		toFindButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				workingFolder_ = (new jmetal.gui.components.JMetalFileChooser()).chooseFile();
				workingFolderName_.setText(workingFolder_.getAbsolutePath());
			}

		});
		container.add(toFindButton, c);



		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.WEST;
		container.add(new JLabel("Pareto Fronts Folder"), c);


		c.gridx = 2;
		c.gridy = 5;
		c.gridheight = 1;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		frontsFolderName_.setText(frontsFolder_.getAbsolutePath());
		frontsFolderName_.setEditable(false);
		frontsFolderName_.setAutoscrolls(true);
		container.add(frontsFolderName_, c);

		c.gridx = 5;
		c.gridy = 5;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		JButton toFindButton2 = new JButton("Change");
		toFindButton2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				frontsFolder_ = (new jmetal.gui.components.JMetalFileChooser()).chooseFile();
				frontsFolderName_.setText(frontsFolder_.getAbsolutePath());
			}

		});

		container.add(toFindButton2, c);



		// Row 6
		c.gridx = 0;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		latexTablesCheck = new JCheckBox("Latex Tables");
		container.add(latexTablesCheck, c);


		c.gridx = 2;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		boxPlotsCheck = new JCheckBox("BoxPlots");
		container.add(boxPlotsCheck, c);

		c.gridx = 4;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		wilcoxonCheck = new JCheckBox("Wilcoxon Test");
		container.add(wilcoxonCheck, c);


		// Row 7
		c.gridx = 0;
		c.gridy = 7;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(new JLabel("BoxPlots Layout: "), c);


		c.gridx = 2;
		c.gridy = 7;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		rows_ = new JTextField(4);
		rows_.setText("1");
		container.add(new JLabel("Rows"), c);

		c.gridx = 3;
		c.gridy = 7;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(rows_, c);

		c.gridx = 4;
		c.gridy = 7;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(new JLabel("Columns"), c);

		c.gridx = 5;
		c.gridy = 7;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		columns_ = new JTextField(4);
		columns_.setText("1");
		container.add(columns_, c);


		// Row 8
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.WEST;
		container.add(new JLabel("Runs"), c);


		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(independentRuns_, c);


		c.gridx = 2;
		c.gridy = 8;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.WEST;
		container.add(new JLabel("Threads"), c);


		c.gridx = 3;
		c.gridy = 8;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		threads_ = new JTextField(4);
		threads_.setText("1");
		container.add(threads_, c);


		c.gridx = 4;
		c.gridy = 8;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.EAST;

		JButton executeButton = new JButton("Execute");
		executeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				progressBar_ = new JFrame();
				progressBar_.setTitle("Executing...");
				JProgressBar bar = new JProgressBar();

				progressBar_.getContentPane().setLayout(new GridLayout());
				progressBar_.getContentPane().add(bar);
				bar.setIndeterminate(true);
				bar.setVisible(true);
				progressBar_.setSize(200, 75);
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Dimension screenSize = toolkit.getScreenSize();
				progressBar_.setLocation(screenSize.width / 2 - 200 / 2,
						screenSize.height / 2 - 75 / 2);
				progressBar_.setVisible(true);
				progressBar_.setResizable(false);


				setVisible(false);
				experimentExecution();
			}
		});
		container.add(executeButton, c);

		setMinimumSize(d);
		setMaximumSize(d);
		setSize(d);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		setLocation(screenSize.width / 2 - MAIN_WINDOWS_WIDTH / 2,
				screenSize.height / 2 - MAIN_WINDOWS_HEIGTH / 2);
		setResizable(false);

		// Adding a MenuBar
		menuBar_ = new JMenuBar();

		// File menu
		{
			JMenu menu = new JMenu("File");
			menu.setMnemonic('F');

			JMenuItem menuItemExit = new JMenuItem("Exit");
			menuItemExit.addActionListener(new ActionListener() {

				@SuppressWarnings("empty-statement")
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			menu.add(menuItemExit);
			menuBar_.add(menu);
		}

		// Add menu
		{
			JMenu menu = new JMenu("Add ...");
			menu.setMnemonic('A');

			JMenuItem menuItemAlgorithm = new JMenuItem("Algorithm");
			menuItemAlgorithm.addActionListener(new AddAlgorithm(this.algorithmsPanel_));

			JMenuItem menuItemOperator = new JMenuItem("Operator");
			menuItemOperator.addActionListener(new AddOperator());
			JMenuItem menuItemIndicator = new JMenuItem("Quality Indicator");

			JMenuItem menuItemProblem = new JMenuItem("Problem");
			menuItemProblem.addActionListener(new AddProblem());

			menu.add(menuItemAlgorithm);
			menu.add(menuItemOperator);
			menu.add(menuItemProblem);
			menu.add(menuItemIndicator);
			menuBar_.add(menu);
		}

		setJMenuBar(menuBar_);

		setVisible(true);

	} // ExperimentsFrame

	// experimentExecution
	public void experimentExecution() {

		//System.setOut(new PrintStream(this.dialog));
		Experiment experiment = new GUIBasedStudy();
		experiment.experimentName_ = this.experimentNameField_.getText();
		experiment.algorithmNameList_ = algorithmsPanel_.getSelectedAlgorithms();
		experiment.problemList_ = problemsPanel_.getSelectedProblems();

		experiment.paretoFrontFile_ = problemsPanel_.getSelectedProblemsFronts();

		((GUIBasedStudy) experiment).parameters = algorithmsPanel_.getParameters();
		experiment.problemsSettings_ = problemsPanel_.getParameters();


		experiment.indicatorList_ = qualityIndicatorsPanel_.getSelectedQualityIndicators();
		experiment.experimentBaseDirectory_ = workingFolder_.getAbsolutePath() + "/" + experiment.experimentName_;
		experiment.paretoFrontDirectory_ = frontsFolder_.getAbsolutePath();
		experiment.algorithmSettings_ = new Settings[experiment.algorithmNameList_.length];
		//        experiment.algorithm_ = new Algorithm[experiment.algorithmNameList_.length];
		experiment.independentRuns_ = new Integer(independentRuns_.getText());

		try {
			int threads = new Integer(threads_.getText()) ;
			experiment.runExperiment(threads);
			
			if (latexTablesCheck.isSelected())
				experiment.generateLatexTables();
			int rows = new Integer(rows_.getText());
			int columns = new Integer(columns_.getText());

			if (boxPlotsCheck.isSelected())
				RBoxplot.generateScripts(rows, columns, experiment.problemList_, experimentNameField_.getText(),true, experiment) ;

			if (wilcoxonCheck.isSelected())
				RWilcoxon.generateScripts(experiment.problemList_, experimentNameField_.getText(),experiment) ;
		} catch (Exception e) {

			progressBar_.setVisible(false);
			//JDialog j = new JDialog();
			//j.setTitle("Error ...");

			System.err.print(e);
			e.printStackTrace();
		} finally {
			System.setOut(System.out);
		}
		setVisible(true);
		progressBar_.setVisible(false);

	} // experimentExecution

	public static void main(String args[]) {
		new ExperimentsSupportGUI();

	}
}
