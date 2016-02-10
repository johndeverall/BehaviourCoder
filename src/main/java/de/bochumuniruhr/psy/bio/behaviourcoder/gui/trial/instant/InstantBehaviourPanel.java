package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.instant;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

/**
 * Panel that contains the buttons for interacting with instant behaviours.
 */
@SuppressWarnings("serial")
public class InstantBehaviourPanel extends JPanel implements GlobalKeyListener, TrialListener {
	
	/**
	 * The list of buttons
	 */
	private List<InstantBehaviourButton> buttons;
	
	/**
	 * Creates a panel of buttons for the instant behaviours of the trial.
	 * 
	 * @param trial - the trial the panel is for.
	 * @param increment - the list of keys that increment the behaviours. Each key is matched to a behaviour that has the same index.
	 * @param decrememt - the list of keys that decrement the behaviours. Each key is matched to a behaviour that has the same index.
	 */
	public InstantBehaviourPanel(Trial trial, List<Character> increment, List<Character> decrememt) { 
		List<InstantBehaviour> behaviours = trial.getInstantBehaviours();
		
		//Setup the layout
		setLayout(new GridLayout(behaviours.size(), 1));
		
		//Create a button for each instant behaviour
		buttons = new ArrayList<InstantBehaviourButton>();
		for (int i = 0; i < behaviours.size(); ++i){
			InstantBehaviourButton button = new InstantBehaviourButton(trial, behaviours.get(i),
					increment.get(i), decrememt.get(i));
			button.setEnabled(false);
			buttons.add(button);
			add(button);
		}
	}

	@Override
	public void keyPressed(char key) {
		//Inform each button of the key press
		for (InstantBehaviourButton button : buttons){
			button.keyPressed(key);
		}
	}
	
	@Override
	public void onReset() {
		for (InstantBehaviourButton button : buttons){
			button.reset();
			button.setEnabled(false);
		}
	}

	@Override
	public void onStart() {
		for (InstantBehaviourButton button : buttons){
			button.setEnabled(true);
		}
	}

	@Override
	public void onStop() {
		for (InstantBehaviourButton button : buttons){
			button.setEnabled(false);
		}
	}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}

	@Override
	public void onLocationChange(Location newLocation) {}

}
