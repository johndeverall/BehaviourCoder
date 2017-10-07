package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.location;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.VideoListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

/**
 * Panel for choosing the current location for a trial.
 */
@SuppressWarnings("serial")
public class LocationPanel extends JPanel implements TrialListener, VideoListener {

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
		setLayout(null);
		
		//Create the buttons
		for (Location location : trial.getLocations()){
			LocationButton button = new LocationButton(trial, location);
			button.setSize(400, 200);
			button.setEnabled(false);
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
		// Create the timer with the buttons updated every 10 milliseconds.
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockRedrawer, 0, 10);
	}
	
	@Override
	public void onTrialStop() {
		// Disable the buttons when the trial has finished
		for (LocationButton button : buttons){
			button.setEnabled(false);
		}
	}
	
	@Override
	public void onTrialReset() {
		// Re-enable the buttons on reset to allow the trial to begin
		for (LocationButton button : buttons) { 
			button.setEnabled(false);
		}
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		for (LocationButton button : buttons) { 
			button.setEnabled(true);
		}
	}

	@Override
	public void onTrialLocationChange(Location newLocation) {}

	@Override
	public void onTrialPause() {}

	@Override
	public void onTrialResume() {}

	@Override
	public void onTrialStart() {}

	@Override
	public void onVideoPositionChange(long videoPosition) {}

	@Override
	public void onVideoPercentThroughChange(int videoTime) {}

	@Override
	public void onVideoFinished() {}

}
