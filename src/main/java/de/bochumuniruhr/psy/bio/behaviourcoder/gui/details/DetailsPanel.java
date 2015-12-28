package de.bochumuniruhr.psy.bio.behaviourcoder.gui.details;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class DetailsPanel extends JPanel {
	
	private JDatePickerImpl datePicker;
	private List<ValidatingTextField> fields;
	private StatusPanel statusBar;
	
	public DetailsPanel(StatusPanel statusBar, Trial trial) { 
		final TrialDetails details = trial.getDetails();
		this.statusBar = statusBar;
		setLayout(new GridLayout(1, details.getDetailNames().size() * 2 + 2));

		JLabel label = new JLabel("Date:");
		label.setToolTipText("Date of the trial");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 20));
		add (label);
		datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
		datePicker.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				details.setDate((Date) ((JDatePanelImpl) e.getSource()).getModel().getValue());		
			}
		});
		add(datePicker);
		fields = new ArrayList<ValidatingTextField>();
		
		for (String detail : details.getDetailNames()){
			label = new JLabel(detail + ":");
			String tooltip = "Valid values:";
			if (details.getConstraint(detail).holdsForAny()){
				tooltip = "Cannot be blank";
			} else {
				for (String value : details.getConstraint(detail).getWhitelist()){
					tooltip += " " + value;
				}
			}
			label.setToolTipText(tooltip);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(new Font("Arial", Font.BOLD, 20));
			add(label);
			ValidatingTextField field = new ValidatingTextField(details, detail);
			field.setToolTipText(tooltip);
			add(field);
			fields.add(field);
		}
	}
	
	public List<ValidationError> validateTrialData() { 
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		for (ValidatingTextField field : fields){
			errors.addAll(field.validateTextField());
		}
		
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
		for (ValidatingTextField field : fields){
			field.reset();
		}		
		datePicker.getModel().setValue(null);
	}

}
