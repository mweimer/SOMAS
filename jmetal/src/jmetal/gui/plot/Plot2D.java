package jmetal.gui.plot;

import java.awt.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.util.LinkedList;
import jmetal.qualityIndicator.util.MetricsUtil;
/**
 * Displays a JFrame and draws a line on it using the Java 2D Graphics API
 *
 * @author www.javadb.com
 */
public class Plot2D extends javax.swing.JPanel {

    private int width_   = 400;
    private int height_  = 400;
    
    public java.util.List<double [][]> points_ ;
    public java.util.List<Color> colors_       ;
    private MetricsUtil utils_ = new MetricsUtil();
    double [][] objsValues = new double [2][2];
    Point2D start, end;

    public Plot2D()
    {
      Dimension d = new Dimension(width_,height_);
      setSize(d);
      setMaximumSize(d);
      setMinimumSize(d);


      points_ = new LinkedList<double [][]>();
      colors_ = new LinkedList<Color>();
      setBackground(Color.WHITE);

      objsValues[0][0] = 0.0;
      objsValues[0][1] = 1.0;
      objsValues[1][0] = 0.0;
      objsValues[1][1] = 1.0;
  }

    public void refresh(String path, Color color, boolean holdOn) {
      if (!holdOn) {
        points_ = new LinkedList<double [][]>();
        colors_ = new LinkedList<Color>();
      }

      double [][] aux = utils_.readFront(path);
      points_.add(aux);
      colors_.add(color);
    }


    public void drawAxis(Graphics2D g2) {
        g2.setBackground(Color.WHITE);
        g2.clearRect(1, 1, width_ + 100, height_+100);

        start = new Point2D.Double(30.0,30.0);
        end   = new Point2D.Double(start.getX()+width_,start.getY()+height_);

        Point2D [] verticalPoints = new Point2D[5];
        for (int i = 0; i < verticalPoints.length; i++) {
          verticalPoints[i] = new Point2D.Double(start.getX(), start.getY() + i * (height_/5.0));
          Line2D line = new Line2D.Double(verticalPoints[i].getX()-2.5, verticalPoints[i].getY(), verticalPoints[i].getX() + 2.5, verticalPoints[i].getY());
          g2.draw(line);
        }

        Point2D [] horizontalPoints = new Point2D[5];
        for (int i = 0; i < horizontalPoints.length; i++) {
          horizontalPoints[i] = new Point2D.Double( start.getX() + (i+1) * (width_/5.0),end.getY());
          Line2D line = new Line2D.Double(horizontalPoints[i].getX(), horizontalPoints[i].getY()-2.5, horizontalPoints[i].getX(), horizontalPoints[i].getY() + 2.5);
          g2.draw(line);
        }




        double [] verticalAxis = new double [5];
        for (int i = 0; i < verticalAxis.length; i++) {
          verticalAxis[i] =  (float)objsValues[1][1] - (float)i * (float)((float)objsValues[1][1] - (float)objsValues[1][0])/5.0;

          BigDecimal round = new BigDecimal(verticalAxis[i]).setScale(2,BigDecimal.ROUND_HALF_UP);
          Double doubleValue= new Double(round.doubleValue());
          verticalAxis[i] = doubleValue;

          g2.drawString((new Double(verticalAxis[i]))+"", (float)0.0, (float)(verticalPoints[i].getY()+5.0));
        }

        double [] horizontalAxis = new double [5];
        for (int i = 0; i < horizontalAxis.length; i++) {
          horizontalAxis[i] =  (float)objsValues[0][0] + (float)(i+1) * (float)((float)objsValues[0][1] - (float)objsValues[0][0])/5.0;

          BigDecimal round = new BigDecimal(horizontalAxis[i]).setScale(2,BigDecimal.ROUND_HALF_UP);
          Double doubleValue= new Double(round.doubleValue());
          horizontalAxis[i] = doubleValue;

          g2.drawString((new Double(horizontalAxis[i]))+"", (float)(horizontalPoints[i].getX()-10.0), (float)(end.getY()+20.0));
        }


        Line2D verticalLine = new Line2D.Double(start.getX(),start.getX(),start.getY(),end.getY());
        Line2D horizontalLine = new Line2D.Double(start.getX(),end.getX(),end.getY(),end.getY());

        g2.draw(verticalLine);
        g2.draw(horizontalLine);

    }

    public void drawChart(Graphics2D g2) {        
        double [] maxValues = null;
        double [] minValues = null;
        if (points_.size() > 0) {
          double [][] aux = points_.get(0);
          maxValues = utils_.getMaximumValues(aux, 2);
          minValues = utils_.getMinimumValues(aux, 2);
          for (int i = 1; i < points_.size(); i++) {
            double [][] otherPoints = points_.get(i);
            double [] candidateMaxValues = utils_.getMaximumValues(otherPoints, 2);
            double [] candidateMinValues = utils_.getMinimumValues(otherPoints, 2);
            for (int j = 0; j < maxValues.length; j++) {
              if (candidateMaxValues[j] > maxValues[j])
                maxValues[j] = candidateMaxValues[j];
              if (candidateMinValues[j] < minValues[j])
                minValues[j] = candidateMinValues[j];
            }
          }

          objsValues[0][0] = minValues[0];
          objsValues[0][1] = maxValues[0];
          objsValues[1][0] = minValues[1];
          objsValues[1][1] = maxValues[1];
        }
        drawAxis(g2);

        if (points_.size() > 0) {

          Rectangle2D auxRectangle;

          for (int p = 0; p < points_.size(); p++) {
            double [][] currentPoints = points_.get(p);
            Color currentColor        = colors_.get(p);
            g2.setColor(currentColor);

            float [] tmpPoint;
            for (int i = 0; i < currentPoints.length; i++) {
              tmpPoint = new float[2];

              tmpPoint[0] = (float) ((float) start.getX() + ((width_ * (currentPoints[i][0]-objsValues[0][0])) / (objsValues[0][1] - objsValues[0][0])));

              tmpPoint[1] = (float) ((float) start.getY() + height_ - ((height_ * (currentPoints[i][1]-objsValues[1][0])) / (objsValues[1][1] - objsValues[1][0])));


              auxRectangle = new Rectangle2D.Double(tmpPoint[0],tmpPoint[1],1.0,1.0);



              g2.draw(auxRectangle);

            }
          }
        }
    }


//    public void drawChart(Graphics2D g2) {
//        g2.clearRect(0, 0, width_, height_);
//
//        g2.setBackground(Color.WHITE);
//        corner_ = getLocation();
//        Line2D verticalLine = new Line2D.Double(5.0, 5.0, 5.0, height_+5.0);
//        Line2D [] semiVerticalLines = new Line2D[5];
//        for (int i = 0; i < semiVerticalLines.length; i++) {
//          semiVerticalLines[i] = new Line2D.Double(2.5, 5.0 + i * (height_/5.0) , 7.5, 5.0 + i * (height_/5.0));
//        }
//
//
//        Line2D horizontalLine = new Line2D.Double(5.0,height_ + 5.0,width_+ 5.0,height_ + 5.0);
//        Line2D [] semiHorizontalLines = new Line2D[5];
//        for (int i = 0; i < semiHorizontalLines.length; i++) {
//          semiHorizontalLines[i] = new Line2D.Double(5.0 + (i+1) * (width_/5.0),height_ + 2.5 ,5.0 + (i+1) * (width_/5.0),height_ + 7.5);
//        }
//
//
//        g2.setColor(Color.BLACK);
//        g2.draw(verticalLine);
//
//
//        for (int i = 0; i < semiVerticalLines.length; i++) {
//          g2.draw(semiVerticalLines[i]);
//          g2.draw(semiHorizontalLines[i]);
//        }
//
//        g2.draw(horizontalLine);
//
//        if (points_.size() > 0) {
//          double [][] aux = points_.get(0);
//          double [] maxValues = utils_.getMaximumValues(aux, 2);
//          double [] minValues = utils_.getMinimumValues(aux, 2);
//          for (int i = 1; i < points_.size(); i++) {
//            double [][] otherPoints = points_.get(i);
//            double [] candidateMaxValues = utils_.getMaximumValues(otherPoints, 2);
//            double [] candidateMinValues = utils_.getMinimumValues(otherPoints, 2);
//            for (int j = 0; j < maxValues.length; j++) {
//              if (candidateMaxValues[j] > maxValues[j])
//                maxValues[j] = candidateMaxValues[j];
//              if (candidateMinValues[j] < minValues[j])
//                minValues[j] = candidateMinValues[j];
//            }
//          }
//
//          float [] ratio     = new float[2];
//          ratio[0] = (float) ((width_ - 10) / (maxValues[0] - minValues[0]));
//          ratio[1] = (float) ((height_- 10) / (maxValues[1] - minValues[1]));
//
//
//          Rectangle2D auxRectangle;
//
//          for (int p = 0; p < points_.size(); p++) {
//            double [][] currentPoints = points_.get(p);
//            Color currentColor        = colors_.get(p);
//            g2.setColor(currentColor);
//
//            float [] tmpPoint;
//            for (int i = 0; i < currentPoints.length; i++) {
//              tmpPoint = new float[2];
//
//              tmpPoint[0] = (float)Math.floor(((currentPoints[i][0] - minValues[0])/(maxValues[0]-minValues[0]))*ratio[0]) ;
//              tmpPoint[1] = (float)Math.ceil(((currentPoints[i][1] - minValues[1])/(maxValues[1]-minValues[1]))*ratio[1]) ;
//              auxRectangle = new Rectangle2D.Double(Math.floor(tmpPoint[0]+5.0),
//                                            Math.ceil((height_ -5.0) - tmpPoint[1]),
//                                            3.0,
//                                            3.0);
//
//
//
//              g2.draw(auxRectangle);
//
//            }
//          }
//        }
//    }


    /**
     * This is the method where the line is drawn.
     *
     * @param g The graphics object
     */
    public void paint(Graphics g) {

        // Cast the Graphic object to Graphic2D, in order to work with Java2D
        Graphics2D g2 = (Graphics2D)g;

        drawChart(g2);

    }
}