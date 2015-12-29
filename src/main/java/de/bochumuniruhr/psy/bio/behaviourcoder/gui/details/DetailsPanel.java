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
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class DetailsPanel extends JPanel {
	
	private JDatePickerImpl datePicker;
	private List<DetailField> fields;
	
	public DetailsPanel(Trial trial) { 
		final TrialDetails details = trial.getDetails();
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
				datePicker.setBackground(Color.WHITE);
			}
		});
		add(datePicker);
		datePicker.setBackground(Color.PINK);
		fields = new ArrayList<DetailField>();
		
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
			DetailField field = new DetailField(details, detail);
			field.setToolTipText(tooltip);
			add(field);
			field.setBackground(Color.PINK);
			fields.add(field);
		}
	}

	public void resetAll() {
		for (DetailField field : fields){
			field.reset();
		}		
		datePicker.getModel().setValue(null);
	}

}
