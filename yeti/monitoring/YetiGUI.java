package yeti.monitoring;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

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
	 * The sampler to update the samplable objects.
	 */
	public YetiSampler sampler = null;

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
		this.sampler.setToUpdate(false);
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

		// we create the sampler
		this.nMSBetweenUpdates = nMSBetweenUpdates;
		sampler = new YetiSampler(nMSBetweenUpdates);

		// we create the panel with the methods
		JPanel p = generateMethodPane();
		
		// we create a frame for it and show the frame
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new JScrollPane(p),BorderLayout.CENTER);
		f.setSize(400,400);
		f.setLocation(1000,200);
		f.setVisible(true);

		
		// we add the number of faults over time	
		YetiGraph graph0 = new YetiGraphFaultsOverTime(YetiLog.proc,nMSBetweenUpdates);
		sampler.addSamplable(graph0);
		this.allComponents.add(graph0);

		JFrame f0 = new JFrame();
		f0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f0.add(graph0);
		f0.setSize(400,200);
		f0.setLocation(200,200);
		f0.setVisible(true);

		// we add the number of calls over time
		YetiGraph graph1 = new YetiGraphNumberOfCallsOverTime(YetiLog.proc,nMSBetweenUpdates);
		sampler.addSamplable(graph1);
		this.allComponents.add(graph1);

		JFrame f1 = new JFrame();
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.add(graph1);
		f1.setSize(400,200);
		f1.setLocation(600,200);
		f1.setVisible(true);

		// we add the number of failures over time
		JFrame f2 = new JFrame();
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		YetiGraph graph2 = new YetiGraphNumberOfFailuresOverTime(YetiLog.proc,nMSBetweenUpdates);
		sampler.addSamplable(graph2);
		this.allComponents.add(graph2);

		f2.add(graph2);
		f2.setSize(400,200);
		f2.setLocation(200,400);
		f2.setVisible(true);


		// we add the number of failures over time
		YetiGraph graph3 = new YetiGraphNumberOfVariablesOverTime(YetiLog.proc,nMSBetweenUpdates);
		sampler.addSamplable(graph3);
		this.allComponents.add(graph3);

		JFrame f3 = new JFrame();
		f3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f3.add(graph3);
		f3.setSize(400,200);
		f3.setLocation(600,400);
		f3.setVisible(true);

		new Thread(this).start();
		new Thread(sampler).start();

	}


	/**
	 * @return
	 */
	public JPanel generateMethodPane() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(0,3));


		int numberOfMethods = Yeti.testModule.routinesInModule.values().size();
		p.setPreferredSize(new Dimension(300,10*numberOfMethods));

		p.setBackground(Color.white);

		// we first sort the routines
		ArrayList<YetiRoutine> l = new ArrayList<YetiRoutine>();

		// we build the list by sorting out instances
		for (YetiRoutine r: Yeti.testModule.routinesInModule.values()) {
			// if the size of the list is null, we add the first element
			if (l.size()==0) {
				l.add(r);
			} else {
				// otherwise, we iterate through and add it where needed 
				String signature = r.getSignature();
				for (int i=0; i<l.size(); i++) {
					// if we have found a bigger one, we insert the routine in there
					if (l.get(i).getSignature().compareTo(signature)>=0) {
						l.add(i, r);
						break;
					} else {
						// if we reached the maximum size, we insert it aftewards
						if (i==l.size()-1) {
							l.add(i+1,r);
							break;
						}
					}
				}
			}
		}

		// we add all routines to the panel of routines.		
		for (YetiRoutine r: l) {
			YetiRoutineGraph graph = new YetiRoutineGraph(r);
			graph.setSize(50, 30);
			p.add(graph);
			this.allComponents.add(graph);
		}
		return p;
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
