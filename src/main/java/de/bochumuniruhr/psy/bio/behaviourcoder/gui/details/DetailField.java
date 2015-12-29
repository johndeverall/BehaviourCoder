package de.bochumuniruhr.psy.bio.behaviourcoder.gui.details;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;

@SuppressWarnings("serial")
public class DetailField extends JTextField {
	
	private String detail;
	private TrialDetails details;
	
	public DetailField(TrialDetails trialDetails, String detailName) { 
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

	public void reset() { 
		setText("");
	}
}
