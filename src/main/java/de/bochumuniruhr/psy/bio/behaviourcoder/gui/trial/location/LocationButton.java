package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.location;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

/**
 * Button for toggling whether the subject is in a location. Displays the total
 * time that the subject has been in the location in seconds.
 */
@SuppressWarnings("serial")
public class LocationButton extends JButton implements MouseMotionListener, MouseInputListener {

	/**
	 * The location the button is for.
	 */
	private Location location;

	/**
	 * The trial the button is for.
	 */
	private Trial trial;

	/**
	 * Whether this is a drag to resize the button or not.
	 */
	private boolean resizeDrag;

	/**
	 * Formatter for displaying the time.
	 */
	private DecimalFormat decimalFormatter;

	/**
	 * Whether the mouse button is pressed or not.
	 */
	private boolean isMousePressed;

	/**
	 * Creates a button for toggling whether the trial subject is in the location.
	 * 
	 * @param trialSession
	 *            - the trial the button is for
	 * @param locationToToggle
	 *            - the location to toggle whether it's the current location
	 */
	public LocationButton(Trial trialSession, Location locationToToggle) {
		initialize(trialSession, locationToToggle);
	}

	private void initialize(Trial trialSession, Location locationToToggle) {
		trial = trialSession;
		location = locationToToggle;
		decimalFormatter = new DecimalFormat("0.00");

		// Set style
		setFont(new Font("Arial", Font.BOLD, 30));

		addMouseListener(this);

		addMouseMotionListener(this);
	}

	/**
	 * Updates the text of this button to show the time in the location in seconds.
	 */
	void updateText() {
		Double timeInSeconds = (double) trial.getLocationTime(location) / 1000.00;
		String buttonLabel = location.getName() + ": " + decimalFormatter.format(timeInSeconds);
		super.setText(buttonLabel);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Only allow the location to be selected if the trial is ready
		if (trial.isReady()) {
			SoundMaker.playMouseClick();
			// Set no location for the trial if it is the current location
			if (location.equals(trial.getCurrentLocation())) {
				trial.setCurrentLocation(null);
			} else {
				trial.setCurrentLocation(location);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		isMousePressed = true;

		MouseEvent me = SwingUtilities.convertMouseEvent(this, e, this);
		int clickX = me.getX();
		int clickY = me.getY();

		// did we click the bottom right hand corner of the component
		resizeDrag = clickX > (getWidth() - 20) && clickY > (getHeight() - 20);

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		isMousePressed = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isMousePressed) {
			if (resizeDrag) {
				MouseEvent me = SwingUtilities.convertMouseEvent(this, e, this);
				int deltaX = getWidth() - me.getX();
				int deltaY = getHeight() - me.getY();
				setSize(getWidth() - deltaX, getHeight() - deltaY);
			} else {
				MouseEvent panelReference = SwingUtilities.convertMouseEvent(this, e, this.getParent());
				setBounds(panelReference.getX() - getWidth() / 2, panelReference.getY() - getHeight() / 2, getWidth(), getHeight());
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

}
