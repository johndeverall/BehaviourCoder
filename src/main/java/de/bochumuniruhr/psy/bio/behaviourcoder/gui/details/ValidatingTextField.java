package de.bochumuniruhr.psy.bio.behaviourcoder.gui.details;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;

@SuppressWarnings("serial")
public class ValidatingTextField extends JTextField {
	
	private String detail;
	private TrialDetails details;
	
	public ValidatingTextField(TrialDetails trialDetails, String detailName) { 
		detail = detailName;
		details = trialDetails;
		addKeyListener(new KeyAdapter() { 
			@Override
			public void keyReleased(KeyEvent e) {
				details.setDetail(detail, getText());
				if (details.isValid(detail)){ 
					setBackground(Color.WHITE);
				} else {
					setBackground(Color.PINK);
				}
			}
		});
		setFont(new Font("Arial", Font.PLAIN, 30));
	}
	
	public List<ValidationError> validateTextField() { 
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if (!details.isValid(detail)) { 
			String message = "Invalid " + detail + ".";
			if (details.getConstraint(detail).holdsForAny()){
				message = detail + " cannot be empty.";
			}
			validationErrors.add(new ValidationError(message));
			setBackground(Color.PINK);
		}
		return validationErrors;
	}

	public void reset() { 
		setText("");
	}
}
