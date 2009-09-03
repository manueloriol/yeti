package yeti.monitoring;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
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
public class YetiRoutineGraph extends JPanel implements YetiUpdatable {

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
		this.name = yt.getSignature();
		this.setBackground(Color.white);
		
		// we set a tooltip at the beginning to activate the functionality.
		// the actual tooltip will be decided by the getToolTip method.
		this.setToolTipText(this.name+" // Passed: "+(int)this.passed+", Undecided: "+(int)this.inconclusive+", Failed: "+(int)this.failed);
	}

	/* (non-Javadoc)
	 * Method used to paint the component.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		// this is not useful:
		//		super.paintComponent(g);
		// we get the graph component
		Graphics2D g2 = (Graphics2D)g;
		// we get the size of the area to paint 
		double w = getWidth();
		double h = getHeight();
		// we paint the background
		g2.setPaint(Color.white);
		g2.fill(new Rectangle2D.Double(0,0,w, h));
		// we draw the name of the routine
		g2.setPaint(Color.black);
		g2.drawString(name, leftBorder , this.topBorder-5);

		// we draw the number of calls
		int nDigits=(int)Math.floor(Math.log10(this.total))+1;
		// first we mask a part of the name that was shown before
		int xString = (int) (w-rightBorder-(nDigits*7.5)-1);
		int yString = this.topBorder-5;
		g2.setPaint(Color.white);
		g2.fill(new Rectangle2D.Double( xString-5, yString-10,w-rightBorder, 15));
		// then we write the number
		g2.setPaint(Color.black);
		g2.drawString(((int)this.total)+"", xString , yString);

		
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
			// if there was no call we draw only one box
			g2.fill(new Rectangle2D.Double( xMin, yMin, xMax, yMax));
		}
	}

	/* (non-Javadoc)
	 * Returns a custom tooltip
	 * 
	 * @see javax.swing.JComponent#getToolTipText()
	 */
	@Override
	public String getToolTipText(MouseEvent event) {
		return this.name+" // Passed: "+(int)this.passed+", Undecided: "+(int)this.inconclusive+", Failed: "+(int)this.failed;
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
