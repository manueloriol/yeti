package yeti.monitoring;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import yeti.Yeti;
import yeti.YetiLog;
import yeti.YetiRoutine;

/**
 * Class that represents the GUI for Yeti.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Sep 3, 2009
 *
 */
public class YetiGUI implements Runnable {

	/**
	 * Checks whether the update thread should be stopped or not.
	 */
	public boolean isToUpdate = true;

	/**
	 * The timeout between updates. 
	 */
	public long nMSBetweenUpdates;

	/**
	 * Method to stop the update of the GUI.
	 */
	public void stopRoutine() {
		this.isToUpdate = false;
	}

	/**
	 * All the components in the current GUI.
	 */
	public ArrayList<YetiUpdatable> allComponents= new ArrayList<YetiUpdatable>();

	/**
	 * Simple creation procedure for YetiGUI.
	 * 
	 * @param nMSBetweenUpdates the time in ms between 2 updates.
	 */
	public YetiGUI(long nMSBetweenUpdates) {

		this.nMSBetweenUpdates = nMSBetweenUpdates;

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(0,3));

		f.add(new JScrollPane(p),BorderLayout.CENTER);

		int numberOfMethods = Yeti.testModule.routinesInModule.values().size();
		p.setPreferredSize(new Dimension(300,10*numberOfMethods));

		// we add all routines to the panel of routines.
		for (YetiRoutine r: Yeti.testModule.routinesInModule.values()) {
			YetiRoutineGraph graph = new YetiRoutineGraph(r);
			graph.setSize(50, 30);
			p.add(graph);
			this.allComponents.add(graph);
		}
		f.setSize(400,400);
		f.setLocation(1000,200);
		f.setVisible(true);
		new Thread(this).start();

	}


	/* (non-Javadoc)
	 * We use this method to actually update the values in real time.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		// We use these two points in time to set an interval up.
		// It happens that the update takes more time than a cycle to proceed.
		// Do not use this loop to sample values.
		while (isToUpdate) {
			for (YetiUpdatable u: allComponents) {
				u.updateValues();
			}
			try {
				Thread.sleep(nMSBetweenUpdates);
			} catch (InterruptedException e) {
				// Should never happen
				// e.printStackTrace();
			}

		}
	}



}
