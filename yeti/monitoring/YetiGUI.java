package yeti.monitoring;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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
	 * The dimensions of the screen. 
	 */
	Dimension screenDimensions = null;

	/**
	 * The usable dimensions on the screen. 
	 */
	Dimension usableDimensions = null;
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

		// we set the size of the window to fill in the screen
		screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
		
		// we set up the dimensions of the tool
		usableDimensions=new Dimension(screenDimensions.width,screenDimensions.height-100);

		// we create the sampler
		this.nMSBetweenUpdates = nMSBetweenUpdates;
		sampler = new YetiSampler(nMSBetweenUpdates);

		// we create the panel with the methods
		JComponent p = this.generateMainPanel();
		
		// we create a frame for it and show the frame
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(p,BorderLayout.CENTER);

		// we set the size
		f.setSize(screenDimensions);
		f.setLocation(0,0);
		f.setVisible(true);

		
		

		new Thread(this).start();
		new Thread(sampler).start();

	}


	/**
	 * A method that generates a panel for monitoring methods.
	 * 
	 * @return a panel that has a panel with all methods being tested 
	 */
	public JComponent generateMethodPane() {
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
		
		
		JToolBar toolbar = new JToolBar("Method Monitor");
		toolbar.add(new JScrollPane(p));
     
		return toolbar;
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

	public JPanel generatePropertyPane() {
		return new JPanel();
	}
	
	public JComponent generateRightSubpanel() {
		
		 //Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				generateRightCentralSubpanel(), generateLowerRightSubpanel());
		splitPane.setOneTouchExpandable(true);
		// reestablish when finalized
//		splitPane.setDividerLocation(screenDimensions.height-300);
		splitPane.setDividerLocation(screenDimensions.height);
		
		return splitPane;
	}
	
	
	public JComponent generateRightCentralSubpanel() {
		
		// create a panel with methods monitor
		JPanel pMeth = new JPanel(new BorderLayout());
		JComponent pMInt = generateMethodPane();
		// to set up the dimensions to the screen size
		pMInt.setPreferredSize(this.usableDimensions);
		pMeth.add(pMInt, BorderLayout.CENTER);
		
		 //Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				generateGraphsPane(), pMeth );
		
		splitPane.setOneTouchExpandable(true);
		// reestablish when finalized
//		splitPane.setDividerLocation(screenDimensions.width-650);
		splitPane.setDividerLocation(screenDimensions.width-400);
		
		return splitPane;
	}
	

	public JComponent generateGraphsPane() {
	

		// we add the number of faults over time	
		YetiGraph graph0 = new YetiGraphFaultsOverTime(YetiLog.proc,nMSBetweenUpdates);
		sampler.addSamplable(graph0);
		this.allComponents.add(graph0);
		
		// we add the number of calls over time
		YetiGraph graph1 = new YetiGraphNumberOfCallsOverTime(YetiLog.proc,nMSBetweenUpdates);
		sampler.addSamplable(graph1);
		this.allComponents.add(graph1);

		// we add the number of failures over time
		YetiGraph graph2 = new YetiGraphNumberOfFailuresOverTime(YetiLog.proc,nMSBetweenUpdates);
		sampler.addSamplable(graph2);
		this.allComponents.add(graph2);


		// we add the number of failures over time
		YetiGraph graph3 = new YetiGraphNumberOfVariablesOverTime(YetiLog.proc,nMSBetweenUpdates);
		sampler.addSamplable(graph3);
		this.allComponents.add(graph3);

		// the panel containing the graphs
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(0,2));

		// we add all the graphs
		p.add(graph0);
		p.add(graph1);
		p.add(graph2);
		p.add(graph3);

		return p;
	}


	public JComponent generateLowerRightSubpanel() {
		return new JPanel();
	}

	public JSplitPane generateMainPanel() {
		
		 //Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                generatePropertyPane(), generateRightSubpanel());
		splitPane.setOneTouchExpandable(true);
		// reestablish when finalized:
		//	splitPane.setDividerLocation(250);
		splitPane.setDividerLocation(0);
		
		return splitPane;

	}

}
