package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.details;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;

/**
 * Field for setting a single detail of a trial.
 */
@SuppressWarnings("serial")
public class DetailField extends JTextField {
	
	/**
	 * The name of the detail that this field modifies.
	 */
	private String detail;
	
	/**
	 * The details of the trial.
	 */
	private TrialDetails details;
	
	/**
	 * Creates a field for modifying a detail of the trial.
	 * @param trialDetails - the details for the trial
	 * @param detailName - the name of the detail that the field will modify
	 */
	public DetailField(TrialDetails trialDetails, String detailName) { 
		detail = detailName;
		details = trialDetails;
		
		//Add the key listener
		addKeyListener(new KeyAdapter() { 
			@Override
			public void keyReleased(KeyEvent e) {
				//After each key press set the value of the detail
				details.setDetail(detail, getText());
				
				//Indicate to the user whether the current value is valid
				if (details.isValid(detail)){ 
					setBackground(Color.WHITE);
				} else {
					setBackground(Color.PINK);
				}
			}
		});
		
		//Set the styling
		setFont(new Font("Arial", Font.PLAIN, 30));
	}

	/**
	 * Resets the field to its initial state.
	 */
	void reset() { 
		setText("");
	}
}
