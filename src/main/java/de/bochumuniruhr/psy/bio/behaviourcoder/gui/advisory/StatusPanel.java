package de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.validation.ValidationError;

/**
 * Panel used to display messages and errors about the trial.
 */
@SuppressWarnings("serial")
public class StatusPanel extends JPanel implements TrialListener {

	/**
	 * The text of the panel.
	 */
	private JLabel statusLabel;
	
	/**
	 * Creates a status panel the width of the frame.
	 * 
	 * @param frame - the frame that will contain the panel
	 */
	public StatusPanel(JFrame frame) {
		//Setup styling
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(frame.getWidth(), 20));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//Setup label
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(statusLabel);
		
		//Default text
		statusLabel.setText("Stopped");
	}

	/**
	 * Displays the given validation errors in red text.
	 * 
	 * @param errors - the errors to display
	 */
	public void showErrors(List<ValidationError> errors) {
		statusLabel.setForeground(Color.RED);
		StringBuilder builder = new StringBuilder();
		for (ValidationError error : errors) { 
			builder.append(error.getMessage());
		}
		statusLabel.setText(builder.toString());
	}

	/**
	 * Displays the message.
	 * 
	 * @param message - the message to display
	 */
	public void setMessage(String message) {
		statusLabel.setForeground(Color.BLACK);
		statusLabel.setText(message);
	}
	
	@Override
	public void onReset(){
		setMessage("Stopped");
	}

	@Override
	public void onLocationChange(Location newLocation) {}

	@Override
	public void onStop() {
		setMessage("Trial finished");
	}
	
	@Override
	public void onPause() {
		setMessage("Trial suspended");
	}

	@Override
	public void onResume() {
		setMessage("Trial in progress");
	}

	@Override
	public void onStart() {
		setMessage("Trial in progress");
	}
}
