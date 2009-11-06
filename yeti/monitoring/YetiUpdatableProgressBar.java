package yeti.monitoring;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import yeti.Yeti;

public class YetiUpdatableProgressBar extends JPanel implements YetiUpdatable{

	/**
	 * The progress bar itself.
	 */
	JProgressBar prog = new JProgressBar(0,100);
	
	/**
	 * A simple constructor for a nice progress bar.
	 */
	public YetiUpdatableProgressBar() {
		super();
		// we add an unecesary label
		JLabel label = new JLabel("Testing session:");
		this.add(label);
		label.setAlignmentX(.5f);
		
		// we set preferences up for the progress bar
		prog.setMaximumSize(new Dimension (300,20));
		prog.setPreferredSize(new Dimension (300,20));
		prog.setStringPainted(true);
		
		// we add the progress bar to the panel
		this.add(prog);
		prog.setAlignmentX(.5f);
		
		// we add it to compontents to update
		YetiGUI.allComponents.add(this);
		// sets the layount correctly
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

	}

	/* (non-Javadoc)
	 * @see yeti.monitoring.YetiUpdatable#updateValues()
	 */
	public void updateValues() {
		int progress = Yeti.engine.getProgress();
		prog.setValue(progress);
		if (progress==100) {
			
		}
	}
	
	

}
