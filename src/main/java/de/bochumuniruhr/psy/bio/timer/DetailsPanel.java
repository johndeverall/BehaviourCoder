package de.bochumuniruhr.psy.bio.timer;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class DetailsPanel extends JPanel {
	
	private ValidatingTextField subject;
	private ValidatingTextField cohort;
	private JDatePickerImpl datePicker;
	private ValidatingTextField task;
	private StatusPanel statusBar;
	
	public DetailsPanel(StatusPanel statusBar) { 
		
		this.statusBar = statusBar;
		setLayout(new GridLayout(1, 7));

		datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
		add(datePicker);
		
		task = new ValidatingTextField("Task", "Task cannot be empty. ");
		add(task);
		
		subject = new ValidatingTextField("Subject", "Subject cannot be empty. ");
		add(subject);
		
		cohort = new ValidatingTextField("Cohort", "Cohort cannot be empty. ");
		add(cohort);
	}

	public void populateTrial(Trial trial) {
		trial.setTaskName(task.getText());
		trial.setSubjectNumber(subject.getText());
		trial.setCohort(cohort.getText());
		trial.setDate((Date)datePicker.getModel().getValue());
	}
	
	public List<ValidationError> validateTrialData() { 
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		errors.addAll(task.validateTextField());
		
		errors.addAll(cohort.validateTextField());
		
		errors.addAll(subject.validateTextField());
		
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
		task.reset();
		cohort.reset();
		subject.reset();
		datePicker.getModel().setValue(null);
	}

}
