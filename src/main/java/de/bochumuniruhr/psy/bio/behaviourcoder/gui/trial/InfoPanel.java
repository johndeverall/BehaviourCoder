package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial;

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
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

/**
 * Panel for displaying and choosing the total duration of a trial.
 * Displays time in seconds.
 */
@SuppressWarnings("serial")
public class InfoPanel extends JPanel implements TrialListener {

	/**
	 * The label for the total time.
	 */
	private JLabel totalTime;
	
	/**
	 * The field for entering the time limit.
	 */
	private JFormattedTextField timeLimit;
	
	/**
	 * The label for the number of transitions.
	 */
	private JLabel transitions;
	
	/**
	 * The font size for the labels.
	 */
	private final int LABEL_FONT_SIZE = 30;
	
	/**
	 * The trial to display the information of.
	 */
	private Trial trial;
	
	/**
	 * The formatter for the times.
	 */
	private DecimalFormat decimalFormatter;
	
	/**
	 * Create a panel for displaying information about the trial.
	 * 
	 * @param trialSession - the trial the panel is for
	 * @param statusBar - the status bar to use to display messages
	 */
	public InfoPanel(Trial trialSession, final StatusPanel statusBar) { 
		trial = trialSession;
		decimalFormatter = new DecimalFormat("0.00");
		this.setLayout(new GridLayout(1, 3));
		
		//Create the label for the total time
		totalTime = new JLabel("Time: 0.00");
		totalTime.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		totalTime.setHorizontalAlignment(JLabel.CENTER);
		add(totalTime);
		
		//Create the label for choosing the trial duration
		JLabel duration = new JLabel("Trial Duration: ");
		duration.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		duration.setHorizontalAlignment(JLabel.RIGHT);
		add(duration);
		
		//Create the field for choosing the trial duration
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);
		timeLimit = new JFormattedTextField(format);
		timeLimit.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		timeLimit.setHorizontalAlignment(JLabel.LEFT);
		timeLimit.setValue(trial.getDetails().getDuration());
		timeLimit.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				//Coerce the fields value to be an integer
				try {
					timeLimit.commitEdit();
				} catch (ParseException e1) {
				}
				//Set the duration of the trial
				trial.getDetails().setDuration((long) timeLimit.getValue());
				statusBar.setMessage("Time limit set to " + (long) timeLimit.getValue() + " seconds.");
			}
		});
		add(timeLimit);
		
		//Create the label for the number of transitions
		transitions = new JLabel("Location Changes: 0");
		transitions.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		transitions.setHorizontalAlignment(JLabel.CENTER);
		add(transitions);
		
		setupClockRedrawRate();
	}
	
	/**
	 * Setups a clock to keep the total time up to date.
	 */
	private void setupClockRedrawRate() { 
		//Create the task to update the total time
		TimerTask clockRedrawer = new TimerTask() {
			@Override
			public void run() {
				totalTime.setText("Time: " + decimalFormatter.format(trial.getCurrentTime() / 1000.0));
			} 
		};
		//Create the timer to update the total time every 10 milliseconds 
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockRedrawer, 0, 10);
	}
	
	@Override
	public void onReset() {
		totalTime.setText("Time: 0.00");
		timeLimit.setValue(trial.getDetails().getDuration());
		transitions.setText("Location Changes: 0");
	}

	@Override
	public void onAreaChange(Location area) {
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