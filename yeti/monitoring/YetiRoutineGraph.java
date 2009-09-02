package yeti.monitoring;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import yeti.YetiRoutine;

/**
 * Class that represents a graph following the evolution of a routine.
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Sep 2, 2009
 */
public class YetiRoutineGraph extends JPanel {

	/**
	 * The routine to read data from.
	 */
	YetiRoutine yt = null;

	/**
	 * The size of the border on the left part.
	 */
	int leftBorder = 5;

	/**
	 * The size of the border on the right part.
	 */
	int rightBorder = 5;

	/**
	 * The size of the border on the top part.
	 */
	int topBorder = 20;

	/**
	 * The size of the border on the lower part.
	 */
	int bottomBorder = 5;

	/**
	 * Stores the title of the graph.
	 */
	public String name = "";


	/**
	 * The number of passed test cases.
	 */
	double passed = 0;

	/**
	 * The number of failed test cases.
	 */
	double inconclusive = 0;

	/**
	 * The number of failed test cases.
	 */
	double failed = 0;

	/**
	 * The number of total test cases.
	 */
	double total = 0;

	/**
	 * Simple constructor, stores the name of the graph.
	 * 
	 * @param name
	 */
	public YetiRoutineGraph(String name) {
		super();
		this.name = name;
		this.setBackground(Color.white);
	}
	/**
	 * Simple constructor, takes a YetiRoutine to read data from.
	 * 
	 * @param name
	 */
	public YetiRoutineGraph(YetiRoutine yt) {
		super();
		this.yt = yt;
		this.name = yt.toString();
		this.setBackground(Color.white);
	}

	/* (non-Javadoc)
	 * Method used to paint the component.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// we get the graph component
		Graphics2D g2 = (Graphics2D)g;
		// we get the size of the area to paint 
		double w = getWidth();
		double h = getHeight();
		// we draw the name of the routine
		g2.drawString(name, leftBorder , this.topBorder-5);
		int nDigits=(int)Math.floor(Math.log10(this.total))+1;
		g2.drawString(((int)this.total)+"", (int) (w-rightBorder-(nDigits*7)-1), this.topBorder-5);

		// we draw the boxes
		double xMin = this.leftBorder;
		double yMin = this.topBorder;
		double xMax =  w-this.leftBorder-this.rightBorder;
		double yMax = h-this.topBorder-this.bottomBorder;

		if (total!=0) {
			double x1 = (xMax*this.passed/this.total);
			double x2 = (xMax*this.inconclusive/this.total);
			double x3 = (xMax*this.failed/this.total);

			g2.setPaint(Color.green);
			g2.fill(new Rectangle2D.Double( xMin, yMin, x1, yMax));
			g2.setPaint(Color.yellow);
			g2.fill(new Rectangle2D.Double( x1+xMin, yMin, x2, yMax));
			g2.setPaint(Color.red);
			g2.fill(new Rectangle2D.Double( x1+x2+xMin, yMin, x3, yMax));

		} else {
			g2.fill(new Rectangle2D.Double( xMin, yMin, xMax, yMax));
		}
	}

	/**
	 * Call this method to update the component.
	 * 
	 * @param passed the number of tests that passed.
	 * @param inconclusive the number of tests that did not pass but we don't know why.
	 * @param failed the number of tests that did not pass.
	 */
	public void updateValues(int passed, int inconclusive, int failed, int total) {
		this.passed = passed;
		this.inconclusive = inconclusive;
		this.failed = failed;
		this.total = total;
		this.repaint();
	}

	/**
	 * Call this method to update the component when a routine is attached to it.
	 * 
	 */
	public void updateValues() {
		if (yt!=null)
			this.updateValues(yt.getnTimesCalledSuccessfully(),yt.getnTimesCalledUndecidable(),yt.nTimesCalledUnsuccessfully,yt.getnTimesCalled());
	}

}
