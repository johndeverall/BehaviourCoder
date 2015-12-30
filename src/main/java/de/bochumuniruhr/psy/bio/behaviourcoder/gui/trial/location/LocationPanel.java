package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.location;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

/**
 * Panel for choosing the current location for a trial.
 */
@SuppressWarnings("serial")
public class LocationPanel extends JPanel implements TrialListener {

	/**
	 * The list of buttons.
	 */
	private List<LocationButton> buttons;
	
	/**
	 * Creates a panel for choosing the current location of a trial.
	 * 
	 * @param trial - the trial to choose locations for
	 */
	public LocationPanel(Trial trial) { 
		buttons = new ArrayList<LocationButton>();
		
		//Set the layout
		setLayout(new GridLayout(0, 1));
		
		//Create the buttons
		for (Location a : trial.getLocations()){
			LocationButton button = new LocationButton(trial, a);
			buttons.add(button);
			add(button);
		}
		
		//Setup the clock for keeping the text up to date
		setupClockRedrawRate();
	}
	
	/**
	 * Setup the clock for keeping the text up to date.
	 */
	private void setupClockRedrawRate() { 
		//Create the task to update the text
		TimerTask clockRedrawer = new TimerTask() {
			@Override
			public void run() {
				//Update all the buttons
				for (LocationButton button : buttons){
					button.updateText();
				}
			} 
		};
		//Create the timer with the buttons updated every 10 milliseconds.
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockRedrawer, 0, 10);
	}
	
	@Override
	public void onStop() {
		//Disable the buttons when the trial has finished
		for (LocationButton button : buttons){
			button.setEnabled(false);
		}
	}
	
	@Override
	public void onReset() {
		//Re-enable the buttons on reset to allow the trial to begin
		for (LocationButton button : buttons) { 
			button.setEnabled(true);
		}
	}

	@Override
	public void onLocationChange(Location newLocation) {}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}

	@Override
	public void onStart() {}
}
