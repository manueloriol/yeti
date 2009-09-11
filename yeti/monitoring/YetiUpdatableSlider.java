package yeti.monitoring;

import javax.swing.JSlider;

import yeti.YetiVariable;

public class YetiUpdatableSlider extends JSlider implements YetiUpdatable {

	public YetiUpdatableSlider(int orientation, int min, int max, int value) {
		super(orientation, min, max, value);
	}
	public void updateValues() {
		this.repaint();
	}

}
