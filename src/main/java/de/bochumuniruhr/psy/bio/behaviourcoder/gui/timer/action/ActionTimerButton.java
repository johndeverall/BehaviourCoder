package de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.action;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

@SuppressWarnings("serial")
public class ActionTimerButton extends JButton implements ActionListener, GlobalKeyListener {

	private TimedBehaviour behaviour;
	private Trial trial;
	private DecimalFormat decimalFormatter;
	private char activationKey;

	public ActionTimerButton(Trial trial, TimedBehaviour behaviour, char activationKey) {
		this.activationKey = activationKey;
		this.behaviour = behaviour;
		this.trial = trial;
		addActionListener(this);
		setFont(new Font("Arial", Font.BOLD, 30));
		decimalFormatter = new DecimalFormat("0.00");
		if (behaviour.getColor() != null){
			setBackground(behaviour.getColor());
		}
	}

	/**
	 * Shows the combined total of both the close and far timer.
	 */
	public void updateText() {
		Double timeInSeconds = behaviour.getTotalDuration();
		super.setText(behaviour.getName() + ": " + decimalFormatter.format(timeInSeconds));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		toggleButton();
	}
	
	private void toggleButton() { 
		if (trial.isRunning()) {
			if (behaviour.isOngoing()){
				behaviour.behaviourEnded();
			} else {
				behaviour.behaviourStarted();
			}
			SoundMaker.playMouseClick();
		}		
	}
	
	@Override
	public void keyPressed(char key) { 
		if (key == activationKey) { 
			toggleButton();
		}
	}

}