package de.bochumuniruhr.psy.bio.behaviourcoder.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel implements TrialListener {

	private JLabel totalTime;
	private JFormattedTextField timeLimit;
	private JLabel transitions;
	private final int LABEL_FONT_SIZE = 30;
	private Trial trial;
	private DecimalFormat decimalFormatter;
	
	public InfoPanel(Trial trialSession, final StatusPanel statusBar) { 
		trial = trialSession;
		decimalFormatter = new DecimalFormat("0.00");
		this.setLayout(new GridLayout(1, 3));
		
		totalTime = new JLabel("Time: 0.00");
		totalTime.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		totalTime.setHorizontalAlignment(JLabel.CENTER);
		add(totalTime);
		
		JLabel duration = new JLabel("Trial Duration: ");
		duration.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		duration.setHorizontalAlignment(JLabel.RIGHT);
		add(duration);
		
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);
		timeLimit = new JFormattedTextField(format);
		timeLimit.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		timeLimit.setHorizontalAlignment(JLabel.LEFT);
		
		
		timeLimit.addKeyListener(new KeyAdapter(){

			@Override
			public void keyReleased(KeyEvent e) {
				//Coerce value to be an integer
				try {
					timeLimit.commitEdit();
				} catch (ParseException e1) {
				}
				trial.getDetails().setDuration((long) timeLimit.getValue());
				statusBar.setMessage("Time limit set to " + (long) timeLimit.getValue() + " seconds.");
			}
		});
		timeLimit.setValue(trial.getDetails().getDuration());
		
		add(timeLimit);
		
		transitions = new JLabel("Location Changes: 0");
		transitions.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		transitions.setHorizontalAlignment(JLabel.CENTER);
		add(transitions);
		
		setupClockRedrawRate();
	}
	
	private void setupClockRedrawRate() { 
		TimerTask clockRedrawer = new TimerTask() {

			@Override
			public void run() {
				totalTime.setText("Time: " + decimalFormatter.format(trial.getCurrentTime() / 1000.0));
			} 
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockRedrawer, 0, 10); // redraw every 1 ms after a delay of 0 ms
	}
	
	public void resetAll() {
		totalTime.setText("Time: 0.00");
		timeLimit.setValue(trial.getDetails().getDuration());
		transitions.setText("Location Changes: 0");
	}

	@Override
	public void onAreaChange(Area area) {
		transitions.setText("Location Changes: " + trial.getNumberOfAreaChanges());
	}

	@Override
	public void onStart() {}

	@Override
	public void onStop() {}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}

}