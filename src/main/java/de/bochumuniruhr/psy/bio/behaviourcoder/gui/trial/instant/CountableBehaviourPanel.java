package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.instant;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.CountableBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

/**
 * Panel that contains the buttons for interacting with countable behaviours.
 */
@SuppressWarnings("serial")
public class CountableBehaviourPanel extends JPanel implements GlobalKeyListener, TrialListener {
	
	/**
	 * The list of buttons
	 */
	private List<CountableBehaviourButton> buttons;
	
	/**
	 * Creates a panel of buttons for the instant behaviours of the trial.
	 * 
	 * @param trial - the trial the panel is for.
	 * @param increment - the list of keys that increment the behaviours. Each key is matched to a behaviour that has the same index.
	 * @param decrememt - the list of keys that decrement the behaviours. Each key is matched to a behaviour that has the same index.
	 */
	public CountableBehaviourPanel(Trial trial, List<Character> increment, List<Character> decrememt) { 
		List<CountableBehaviour> behaviours = trial.getCountableBehaviours();
		
		//Setup the layout
		setLayout(new GridLayout(behaviours.size(), 1));
		
		//Create a button for each instant behaviour
		buttons = new ArrayList<CountableBehaviourButton>();
		for (int i = 0; i < behaviours.size(); ++i){
			CountableBehaviourButton button = new CountableBehaviourButton(trial, behaviours.get(i),
					increment.get(i), decrememt.get(i));
			button.setEnabled(false);
			buttons.add(button);
			add(button);
		}
	}

	@Override
	public void keyPressed(char key) {
		//Inform each button of the key press
		for (CountableBehaviourButton button : buttons){
			button.keyPressed(key);
		}
	}
	
	@Override
	public void onTrialReset() {
		for (CountableBehaviourButton button : buttons){
			button.reset();
			button.setEnabled(false);
		}
	}

	@Override
	public void onTrialStart() {
		for (CountableBehaviourButton button : buttons){
			button.setEnabled(true);
		}
	}

	@Override
	public void onTrialStop() {
		for (CountableBehaviourButton button : buttons){
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
