package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.timed;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

/**
 * Panel for holding the buttons that interact with timed behaviours.
 */
@SuppressWarnings("serial")
public class TimedBehaviourPanel extends JPanel implements GlobalKeyListener, TrialListener {
	
	/**
	 * List of buttons.
	 */
	private List<TimedBehaviourButton> buttons;
	
	/**
	 * Creates a panel with the buttons for interacting with the trial's timed behaviours.
	 * 
	 * @param trial - the trial the panel is for
	 * @param activationKeys - the keys for toggling the buttons. Each key is matched to a behaviour that has the same index.
	 */
	public TimedBehaviourPanel(Trial trial, List<Character> activationKeys) { 
		List<TimedBehaviour> behaviours = trial.getTimedBehaviours();
		
		//Set the layout
		setLayout(new GridLayout(behaviours.size(), 1));

		//Create the buttons
		buttons = new ArrayList<TimedBehaviourButton>();
		for (int i = 0; i < behaviours.size(); ++i){
			TimedBehaviourButton button = new TimedBehaviourButton(trial, behaviours.get(i),
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
				for (TimedBehaviourButton button : buttons){
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
		for (TimedBehaviourButton button : buttons){
			button.keyPressed(key);
		}
	}
	
	@Override
	public void onTrialReset() {
		for (TimedBehaviourButton button : buttons){
			button.setEnabled(false);
		}
	}

	@Override
	public void onTrialStart() {
		for (TimedBehaviourButton button : buttons){
			button.setEnabled(true);
		}
	}

	@Override
	public void onTrialStop() {
		for (TimedBehaviourButton button : buttons){
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
