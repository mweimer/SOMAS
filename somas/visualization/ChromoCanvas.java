package visualization;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ChromoCanvas extends Canvas implements MouseListener
{				
	public int Xleft = 25;
	public int Xright = 1400;
	public int Ytop = 20;
	public int Ybottom = 200;
	private int numGenes = 0;
	private int totalX, totalY;

	private int ovalSize = 8;
	private int adjust = ovalSize / 2;
	private ArrayList<Point> points;
	private Boolean ovalClicked = false;
	private int selectedOval = 0;
	private static final Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA};
	private int colorIndex = 0;
	
	private Font normalFont;
	private Font smallFont;

	private ArrayList<GeneGroup> geneGroups;

	public ChromoCanvas(ArrayList<GeneGroup> geneGroups, int numGenes)
	{
		this.geneGroups = geneGroups;
		this.numGenes = numGenes;

		totalX = Xright - Xleft + 1;
	    totalY = Ybottom - Ytop + 1;
	    
	    addMouseListener(this);
	}

	@Override
	public void paint(Graphics g)
	{
		normalFont = g.getFont();
		smallFont = new Font(normalFont.getName(), normalFont.getStyle(), normalFont.getSize()-1);
		g.setColor(Color.DARK_GRAY);
		g.clearRect(0, Ybottom, totalX, 200);
		g.fillRect(Xleft-5, Ytop, totalX+10, totalY+32);
		drawGraph(g);
	}
		
	private Color getNextColor()
	{
		if(colorIndex >= colors.length)
			colorIndex = 0;
		return colors[colorIndex++];
	}
			
	private void drawGraph(Graphics g)
	{
        int x, y, xIncrement, yIncrement, x2, geneIndex;
        boolean actuatorNameOnTop = true;
        xIncrement = totalX / numGenes;
        yIncrement = totalY;

        //draw X axis
		g.setColor(Color.WHITE);
        g.drawLine(Xleft-3, Ybottom, Xright+3, Ybottom);
        
        colorIndex = 0;
        geneIndex = 0;
        points = new ArrayList<Point>();
        g.setColor(getNextColor());
        
        int previousSectionNumber = 0;
        for(int i = 0; i < geneGroups.size(); i++)
        {
        	GeneGroup group = geneGroups.get(i);
        	int groupStartIndex = geneIndex;
        	int groupEndIndex = groupStartIndex + group.getSize();
        	
        	for(int j = 0; j < group.getSize(); j++)
        	{
        		x = getXCoordinate(geneIndex, xIncrement);
   	          	y = getYCoordinate(group.getGeneValue(j), yIncrement);
   	          	points.add(new Point(x, y-adjust));
   	          	g.fillOval(x, y-adjust, ovalSize, ovalSize);
   	          	g.drawLine(x+adjust, Ybottom-1, x+adjust, y);
   	          	geneIndex++;
        	}

	        //draw mean
        	x = getXCoordinate(groupStartIndex, xIncrement);
          	y = getYCoordinate(group.getMeanValue(), yIncrement);
          	x2 = getXCoordinate(groupEndIndex, xIncrement);
          	g.drawLine(x+adjust, y, x2-adjust, y);
          	
          	//draw actuator name
          	if(group.getSectionNumber() == 1 || group.getSectionNumber() == 2)
          	{
          		x = getXCoordinate(groupStartIndex, xIncrement);
          		g.setFont(smallFont);
          		if(actuatorNameOnTop)
          			g.drawString(group.getActuatorName(), x, Ybottom+15);
          		else
          			g.drawString(group.getActuatorName(), x, Ybottom+30);
          		actuatorNameOnTop = !actuatorNameOnTop;
          		g.setFont(normalFont);
          			
          	}
          
          	
          	//draw section divider and info
          	if(group.getSectionNumber() > previousSectionNumber)
          	{
          		previousSectionNumber++;
          		g.setColor(Color.WHITE);
          		g.drawLine(x, Ybottom, x, Ytop);
          		g.setColor(Color.BLACK);
          		if(group.sectionNumber == 1)
          			g.drawString("Actuator Decision Variable Weights", x + 150, Ytop-5);
          		if(group.sectionNumber == 2)
          			g.drawString("Actuator Parameter Weights", x + 150, Ytop-5);
          		if(group.sectionNumber == 3)
          			g.drawString("Sensor Parameter Values", x - 47, Ytop-5);
          	}
          	
          	if(group.sectionNumber == 0)
          	{
          		g.setColor(Color.BLACK);
      			g.drawString("Misc", Xleft-4, Ytop-5);
          	}
          	
        	g.setColor(getNextColor());
	         
        }

        // draw y axis labels
        g.setColor(Color.black);
        g.drawString("0.0", 0, Ybottom+5);
        g.drawString("0.5", 0, (totalY/2)+Ytop +5);
        g.drawString("1.0", 0, Ytop+5);
        
        
        //draw sensor order info
        String text = "*Sensor Order: ";
        for(int i = 0; i < ChromoVisualizer.sensorNames.length; i++)
        {
        	if(i != ChromoVisualizer.sensorNames.length - 1)
        		text = text.concat(ChromoVisualizer.sensorNames[i] + ", ");
        	else
        		text = text.concat(ChromoVisualizer.sensorNames[i]);
        }
        g.setFont(smallFont);
        g.drawString(text, Xleft, Ybottom+ 63);
        g.setFont(normalFont);
        
        if(ovalClicked);
        	drawDetails(selectedOval, g);
        
	}
		
	private int getXCoordinate(int i, int xIncrement)
	{
		return Xleft + xIncrement *i;
	}
	
	private int getYCoordinate(Double n, int yIncrement)
	{
		return (int) (Ybottom - yIncrement * n);
	}

	public void mouseClicked(MouseEvent e) 
	{
		int xpos = e.getX();
		int ypos = e.getY();
		Point p;
		ovalClicked = false;
		for(int i = 0; i < points.size(); i++)
		{
			p = points.get(i);
			if(xpos >= p.x && xpos <= p.x+ovalSize && ypos >= p.y && ypos <= p.y+ovalSize)
			{
				ovalClicked = true;
				selectedOval = i;
				break;
			}		
		}
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void drawDetails(int index, Graphics g)
	{
		int increment = -1;
		String geneName = "";
		double geneValue = 0;
		int indexInGroup = 0;
		for(int i = 0; i < geneGroups.size(); i++)
		{
			GeneGroup group = geneGroups.get(i);
			if(index > group.getSize() + increment)
			{
				increment += group.getSize();
				continue;
			}
			else
			{
				indexInGroup = index - increment - 1;
				geneName = group.getGeneName(indexInGroup);
				geneValue = group.getGeneValue(indexInGroup);
				break;
			}
		}

		g.drawString(geneName + " (" + Double.toString(geneValue) + ")", Xleft, Ybottom + 47);
	}	
}
