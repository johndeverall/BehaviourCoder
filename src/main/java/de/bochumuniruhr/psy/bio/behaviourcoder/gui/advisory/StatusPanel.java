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

import de.bochumuniruhr.psy.bio.behaviourcoder.gui.details.ValidationError;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {

	private JLabel statusLabel;
	
	public StatusPanel(JFrame frame) {
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(frame.getWidth(), 20));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(statusLabel);
	}

	public void showErrors(final List<ValidationError> errors) {
		statusLabel.setForeground(Color.RED);
		StringBuilder builder = new StringBuilder();
		for (ValidationError error : errors) { 
			builder.append(error.getMessage());
		}
		statusLabel.setText(builder.toString());
	}

	public void setMessage(String message) {
		statusLabel.setForeground(Color.BLACK);
		statusLabel.setText(message);
	}

}
