package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.timed;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

/**
 * Button for toggling whether a timed behaviour is currently active.
 * Displays the total time for the behaviour in seconds.
 */
@SuppressWarnings("serial")
public class TimedBehaviourButton extends JButton implements GlobalKeyListener {

	/**
	 * The timed behaviour this button is for.
	 */
	private TimedBehaviour behaviour;
	
	/**
	 * The trial this button is for.
	 */
	private Trial trial;
	
	/**
	 * Formatter for displaying the time.
	 */
	private DecimalFormat decimalFormatter;
	
	/**
	 * The key that toggles the button.
	 */
	private char activationKey;

	/**
	 * Creates a button for toggling the timed behaviour.
	 * 
	 * @param trial - the trial the button is for
	 * @param behaviour - the behaviour the button toggles
	 * @param activationKey - the key that toggles the button
	 */
	public TimedBehaviourButton(Trial trial, TimedBehaviour behaviour, char activationKey) {
		this.activationKey = activationKey;
		this.behaviour = behaviour;
		this.trial = trial;
		decimalFormatter = new DecimalFormat("0.00");
		
		//Add the listener to toggle the button
		addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleBehaviour();
			}
		});
		
		//Set the styling
		setFont(new Font("Arial", Font.BOLD, 30));
		if (behaviour.getColor() != null){
			setBackground(behaviour.getColor());
		}
	}

	/**
	 * Updates this button to display the total duration of its behaviour in seconds.
	 */
	void updateText() {
		Double timeInSeconds = behaviour.getTotalDuration();
		super.setText(behaviour.getName() + ": " + decimalFormatter.format(timeInSeconds));
	}
	
	/**
	 * Toggles whether the behaviour is active.
	 */
	private void toggleBehaviour() { 
		//Only toggle if the trial is running
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
		//Toggle the button if the key is the activation key
		if (key == activationKey) { 
			toggleBehaviour();
		}
	}

}