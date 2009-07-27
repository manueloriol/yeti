package yeti.monitoring;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import yeti.YetiLog;

/**
 * Class that represents a JPanel that shows graphs. 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 27, 2009
 *
 */
@SuppressWarnings({ "serial", "unchecked" })
public class YetiGraph extends JPanel {

	/**
	 * The list of points.
	 */
	public ArrayList<Double> []series = new ArrayList[2];

	/**
	 * The size of the border on the left part.
	 */
	int leftBorder = 25;

	/**
	 * The size of the border on the right part.
	 */
	int rightBorder = 25;

	/**
	 * The size of the border on the lower part.
	 */
	int horizontalBorder = 25;

	/**
	 * THe maximum represented value of x.
	 */
	double maxX = 5;

	/**
	 * The maximum represented value in x
	 */
	double maxY = 9;

	/**
	 * The number of digits of the scale of X.
	 */
	int nDigitsX = 1;

	/**
	 * The number of digits of the scale of Y.
	 */
	int nDigitsY = 1;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// we get the graph component
		Graphics2D g2 = (Graphics2D)g;
		// we get the size of the area to paint 
		double w = getWidth();
		double h = getHeight();
		// Draw y-axis.
		g2.draw(new Line2D.Double(leftBorder, horizontalBorder, leftBorder, h-horizontalBorder));
		// Draw x-axis.
		g2.draw(new Line2D.Double(leftBorder, h-horizontalBorder, w-rightBorder, h-horizontalBorder));
		g2.drawString(name, leftBorder , horizontalBorder-5);
		// we draw the y-labels
		double ystep = Math.pow(10, nDigitsY);
		if ((Math.pow(10, nDigitsY)*2)>maxY) 
			ystep = Math.pow(10, nDigitsY-1)*2;

		for (int i = 0; i<20; i++) {
			int grade = (int)(i*ystep);
			if (grade>maxY) {
				break;
			}
			double y1=h-horizontalBorder-(h-2*horizontalBorder)*grade/maxY;
			g2.draw(new Line2D.Double(leftBorder-1,y1,leftBorder+1,y1));
			g2.drawString(""+grade, 5, (int)y1+5);
		}

		// we draw the x-labels
		double xstep = Math.pow(10, nDigitsX);
		if ((Math.pow(10, nDigitsX)*2)>maxX) 
			xstep = Math.pow(10, nDigitsX-1)*2;
		for (int i = 0; i<10; i++) {
			int grade = (int)(i*xstep);
			if (grade>maxX) {
				break;
			}
			double x1=leftBorder+(w-leftBorder-rightBorder)*grade/maxX;
			g2.draw(new Line2D.Double(x1,h-horizontalBorder+1,x1,h-horizontalBorder-1));
			g2.drawString(""+grade, (int)(x1-(nDigitsX*4)-2), (int) (h-horizontalBorder+17));
		}

		// we draw data points.
		try {
			g2.setPaint(Color.red);
			int size = series[0].size();
			for(int i = 0; i < size; i++) {
				double x = leftBorder + (w-leftBorder-rightBorder)*series[0].get(i)/maxX;
				double y =  h - horizontalBorder - (h-2*horizontalBorder)*series[1].get(i)/maxY;
				g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
			}
		} catch (NullPointerException e) {

		}
	}
	/**
	 * Adds a value to draw.
	 * 
	 * @param x the x value.
	 * @param y the y value
	 */
	public void addValue(double x, double y) {
		// if the maximum x is too low we extend it.
		if (maxX<x) {
			maxX=1.2*x;
			nDigitsX=(int)Math.floor(Math.log10(maxX));
		}
		// if the maximum y is too low, we extend it.
		if (maxY<y) {
			maxY=2*y;
			nDigitsY=(int)Math.floor(Math.log10(maxY));
			leftBorder = 20+nDigitsY*7;
		}
		YetiLog.printDebugLog("x= "+x+"y= "+y, this);
		// we add the point to the graph
		series[0].add(x);
		series[1].add(y);
		// we repaint the component
		repaint();
	}

	/**
	 * Stores the title of the graph.
	 */
	public String name = "";

	/**
	 * Simple constructor, stores the name of the graph.
	 * 
	 * @param name
	 */
	public YetiGraph(String name) {
		super();
		this.name = name;
		this.setBackground(Color.white);
		series[0] = new ArrayList<Double>();
		series[1] = new ArrayList<Double>();
	}
}
