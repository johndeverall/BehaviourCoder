package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.location;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

/**
 * Button for toggling whether the subject is in a location.
 * Displays the total time that the subject has been in the location in seconds.
 */
@SuppressWarnings("serial")
public class LocationButton extends JButton {

	/**
	 * The location the button is for.
	 */
	private Location location;
	
	/**
	 * The tiral the button is for.
	 */
	private Trial trial;
	
	/**
	 * Formatter for displaying the time.
	 */
	private DecimalFormat decimalFormatter;
	
	/**
	 * Creates a button for toggling whether the trial subject is in the location.
	 * 
	 * @param trialSession - the trial the button is for
	 * @param locationToToggle - the location to toggle whether it's the current location
	 */
	public LocationButton(Trial trialSession, Location locationToToggle) { 
		trial = trialSession;
		location = locationToToggle;
		decimalFormatter = new DecimalFormat("0.00");
		
		//Set style
		setFont(new Font("Arial", Font.BOLD, 30));
		
		//Add the listener for button presses
		addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Only allow the location to be selected if the trial is ready
				if (trial.isReady()) { 
					SoundMaker.playMouseClick();
					//Set no location for the trial if it is the current location
					if (location.equals(trial.getCurrentLocation())){
						trial.setCurrentLocation(null);
					} else {
						trial.setCurrentLocation(location);
					}
				}
			}
		});
	}

	/**
	 * Updates the text of this button to show the time in the location in seconds.
	 */
	void updateText() { 
		Double timeInSeconds = (double) trial.getLocationTime(location) / 1000.00;
		super.setText(location.getName() + ": " + decimalFormatter.format(timeInSeconds));
	}
}
