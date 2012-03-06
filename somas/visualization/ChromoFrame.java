package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;

/**
 * ChromoFrame is JFrame used by the ChromoVisualizer to visually 
 * display a chromosome's genes. The ChromoFrame contains a list 
 * with the gene's number, name, and value.
 * 
 * @author      Matt Weimer
 * @see			ChromoVisualizer
 */
public class ChromoFrame extends JFrame
{
	private ChromoCanvas chromoCanvas;
	
	public ChromoFrame (ArrayList<GeneGroup> geneGroups, int numGenes)
	{
		super("Chromosome Visualizer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
		chromoCanvas = new ChromoCanvas(geneGroups, numGenes);
		add(chromoCanvas);
		

		setSize(chromoCanvas.Xright+33, chromoCanvas.Ybottom+106);
		setLocation(0, 682);
		setVisible(true);
	}
}
