package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.details;

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
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * Panel that contains the fields for specifying the details of a trial.
 */
@SuppressWarnings("serial")
public class DetailsPanel extends JPanel {
	
	/**
	 * The picker for the date.
	 */
	private JDatePickerImpl datePicker;
	
	/**
	 * The list of detail fields.
	 */
	private List<DetailField> fields;
	
	/**
	 * Creates a panel for specifying the details of a trial.
	 * 
	 * @param trial - the trial to specify the details for.
	 */
	public DetailsPanel(Trial trial) { 
		final TrialDetails details = trial.getDetails();
		setLayout(new GridLayout(1, details.getDetailNames().size() * 2 + 2));

		//Create the label for the date picker
		JLabel label = new JLabel("Date:");
		label.setToolTipText("Date of the trial");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		add (label);
		
		//Create the date picker
		datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
		datePicker.setBackground(Color.PINK);
		datePicker.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Set the date of the trial
				details.setDate((Date) ((JDatePanelImpl) e.getSource()).getModel().getValue());
				datePicker.setBackground(Color.WHITE);
			}
		});
		add(datePicker);
		
		//Create the fields
		fields = new ArrayList<DetailField>();
		for (String detail : details.getDetailNames()){
			//Create the label
			label = new JLabel(detail + ":");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(new Font("Arial", Font.BOLD, 14));
			add(label);
			
			//Create the field
			DetailField field = new DetailField(details, detail);
			add(field);
			field.setBackground(Color.PINK);
			fields.add(field);
			
			//Create the tooltip
			String tooltip = "Valid values:";
			if (details.getConstraint(detail).holdsForAny()){
				tooltip = "Cannot be blank";
			} else {
				for (String value : details.getConstraint(detail).getWhitelist()){
					tooltip += " " + value;
				}
			}
			label.setToolTipText(tooltip);
			field.setToolTipText(tooltip);
		}
	}

	/**
	 * Resets the fields and date picker to their initial state.
	 */
	public void resetAll() {
		for (DetailField field : fields){
			field.reset();
			field.setBackground(Color.PINK);
		}		
		datePicker.getModel().setValue(null);
		datePicker.setBackground(Color.PINK);
	}

}
