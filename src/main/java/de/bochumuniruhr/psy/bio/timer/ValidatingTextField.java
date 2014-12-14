package de.bochumuniruhr.psy.bio.timer;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ValidatingTextField extends JTextField {

	private String defaultText;
	private String validationFailureMessage;
	
	public ValidatingTextField(final String defaultText, String validationFailureMessage) { 
		this.defaultText = defaultText;
		this.validationFailureMessage = validationFailureMessage;
		setText(this.defaultText);
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (getText().equals(defaultText)) { 
					setText("");
					setBackground(Color.WHITE);
				}
			}
		});
		
		addFocusListener(new FocusAdapter() { 
			@Override
			public void focusLost(FocusEvent e) {
				if (getText().equals("")) { 
					setText(defaultText);
				}
			}
		});
		
		addKeyListener(new KeyAdapter() { 
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (getText().equals("") || getText() == null) { 
					setText(defaultText);
				} else { 
					setBackground(Color.WHITE);
				}
			}
		});
		
	}
	
	public List<ValidationError> validateTextField() { 
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if (isEmptyOrNullOrEqual(getText())) { 
			setBackground(Color.PINK);
			validationErrors.add(new ValidationError(validationFailureMessage));
		} else { 
			setBackground(Color.WHITE);
		}
		return validationErrors;
	}
	
	public void reset() { 
		setText(defaultText);
	}
	

	private boolean isEmptyOrNullOrEqual(String text) { 
		return text == null || text.equals("") || defaultText.equals(text);
	}
	
}
