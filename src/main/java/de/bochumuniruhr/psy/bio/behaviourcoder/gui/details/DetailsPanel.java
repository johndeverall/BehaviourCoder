package de.bochumuniruhr.psy.bio.behaviourcoder.gui.details;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialSection;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class DetailsPanel extends JPanel {
	
	private ValidatingTextField roosterId;
	private JDatePickerImpl datePicker;
	private ValidatingTextField trialType;
	private StatusPanel statusBar;
	private ValidatingTextField sessionNumber;
	private ValidatingTextField trialNumber;
	private ValidatingTextField sectionNumber;
	private ValidatingTextField mirror;
	
	public DetailsPanel(StatusPanel statusBar) { 
		
		this.statusBar = statusBar;
		setLayout(new GridLayout(1, 7));

		datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
		add(datePicker);
		
		trialType = new ValidatingTextField("Trial Type", "Invalid trial type. ", "BM", "NM", "BS", "NS", "WM", "WS");
		add(trialType);
		
		roosterId = new ValidatingTextField("Rooster ID", "Invalid rooster id. ", "554", "570", "547", "538", "546", "551", "559", "548", "544", "578", "579", "542", "567");
		add(roosterId);
		
		sessionNumber = new ValidatingTextField("Session #", "Session cannot be empty. ");
		add(sessionNumber);
		
		trialNumber = new ValidatingTextField("Trial #", "Trial Number cannot be empty. ");
		add(trialNumber);
		
		sectionNumber = new ValidatingTextField("Section #", "Section cannot be empty. ");
		add(sectionNumber);
		
		mirror = new ValidatingTextField("Mirror?", "Mirror must be a 1 or a 0. ", "0", "1");
		add(mirror);
	}

	public void populateTrial(TrialSection trial) {
		trial.setDate((Date)datePicker.getModel().getValue());
		trial.setTrialType(trialType.getText());
		trial.setRoosterId(roosterId.getText());
		trial.setSessionNumber(sessionNumber.getText());
		trial.setTrialNumber(trialNumber.getText());
		trial.setSectionNumber(sectionNumber.getText());
		trial.setMirror(mirror.getText());
	}
	
	public List<ValidationError> validateTrialData() { 
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		errors.addAll(trialType.validateTextField());
		errors.addAll(roosterId.validateTextField());
		errors.addAll(sessionNumber.validateTextField());
		errors.addAll(trialNumber.validateTextField());
		errors.addAll(sectionNumber.validateTextField());
		errors.addAll(mirror.validateTextField());
		
		if (datePicker.getModel().getValue() == null) { 
			datePicker.setBackground(Color.PINK);
			errors.add(new ValidationError("Date cannot be empty. "));
		} else { 
			datePicker.setBackground(Color.WHITE);
		}
		
		statusBar.showErrors(errors);
		
		return errors;
	}

	public void resetAll() {
		
		trialType.reset();
		
		roosterId.reset();
		
		sessionNumber.reset();
		
		trialNumber.reset();
		
		sectionNumber.reset();
		
		mirror.reset();
		
		datePicker.getModel().setValue(null);
	}

}
