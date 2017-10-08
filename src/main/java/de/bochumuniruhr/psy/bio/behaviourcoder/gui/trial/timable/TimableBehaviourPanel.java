package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.timable;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimableBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

/**
 * Panel for holding the buttons that interact with timed behaviours.
 */
@SuppressWarnings("serial")
public class TimableBehaviourPanel extends JPanel implements GlobalKeyListener, TrialListener {
	
	/**
	 * List of buttons.
	 */
	private List<TimableBehaviourButton> buttons;
	
	/**
	 * Creates a panel with the buttons for interacting with the trial's timed behaviours.
	 * 
	 * @param trial - the trial the panel is for
	 * @param activationKeys - the keys for toggling the buttons. Each key is matched to a behaviour that has the same index.
	 */
	public TimableBehaviourPanel(Trial trial, List<Character> activationKeys) { 
		List<TimableBehaviour> behaviours = trial.getTimableBehaviours();
		
		//Set the layout
		setLayout(new GridLayout(behaviours.size(), 1));

		//Create the buttons
		buttons = new ArrayList<TimableBehaviourButton>();
		for (int i = 0; i < behaviours.size(); ++i){
			TimableBehaviourButton button = new TimableBehaviourButton(trial, behaviours.get(i),
					activationKeys.get(i));
			button.setEnabled(false);
			buttons.add(button);
			add(button);
		}
		//Setup a clock to keep the buttons' text up to date
		setupClockRedrawRate();
	}
	
	/**
	 * Setups a clock to keep the text of every button up to date.
	 */
	private void setupClockRedrawRate() { 
		//Create the task to update the text
		TimerTask clockRedrawer = new TimerTask() {
			@Override
			public void run() {
				for (TimableBehaviourButton button : buttons){
					button.updateText();
				}
			} 
		};
		//Create the timer with the buttons updated every 10 milliseconds
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockRedrawer, 0, 10);
	}

	@Override
	public void keyPressed(char key) {
		//Forward key presses to the buttons
		for (TimableBehaviourButton button : buttons){
			button.keyPressed(key);
		}
	}
	
	@Override
	public void onTrialReset() {
		for (TimableBehaviourButton button : buttons){
			button.setEnabled(false);
		}
	}

	@Override
	public void onTrialStart() {
		for (TimableBehaviourButton button : buttons){
			button.setEnabled(true);
		}
	}

	@Override
	public void onTrialStop() {
		for (TimableBehaviourButton button : buttons){
			button.setEnabled(false);
		}
	}

	@Override
	public void onTrialPause() {}

	@Override
	public void onTrialResume() {}

	@Override
	public void onTrialLocationChange(Location newLocation) {}

}
