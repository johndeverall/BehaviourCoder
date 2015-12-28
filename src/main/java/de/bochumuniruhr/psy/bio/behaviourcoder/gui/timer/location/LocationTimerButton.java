package de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.location;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

@SuppressWarnings("serial")
public class LocationTimerButton extends JButton implements ActionListener {

	private DecimalFormat decimalFormatter;
	private Area area;
	private Trial trial;
	
	public LocationTimerButton(Trial trial, Area area) { 
		this.trial = trial;
		this.area = area;
		addActionListener(this);
		setFont(new Font("Arial", Font.BOLD, 30));
		decimalFormatter = new DecimalFormat("0.00");
	}

	
	public void updateText() { 
		Double timeInSeconds = (double) trial.getAreaTime(area) / 1000.00;
		super.setText(area.getName() + ": " + decimalFormatter.format(timeInSeconds));
	}

	public void actionPerformed(ActionEvent e) {
		if (trial.isReady()) { 
			SoundMaker.playMouseClick();
			if (area.equals(trial.getCurrentArea())){
				System.out.println(area);
				trial.setCurrentArea(null);
			} else {
				trial.setCurrentArea(area);
			}
		}
	}
}
