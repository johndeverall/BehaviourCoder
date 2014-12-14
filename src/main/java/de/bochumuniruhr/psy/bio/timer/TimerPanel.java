package de.bochumuniruhr.psy.bio.timer;

import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TimerPanel extends JPanel {

	private TimerMediator timerMediator;
	private StatusPanel statusBar; 
	private JLabel totalTime;
	private ValidatingTextField timeLimit;
	private JLabel transitions;
	private DecimalFormat decimalFormatter;
	private boolean trialOver;
	private final int DEFAULT_TIME_LIMIT = 300;
	private final int LABEL_FONT_SIZE = 30;
	
	public TimerPanel(StatusPanel statusBar) { 
		
		this.statusBar = statusBar;
		
		this.trialOver = false;
		
		decimalFormatter = new DecimalFormat("0.00");
		
		timerMediator = new TimerMediator(statusBar);
		
		setLayout(new GridLayout(4, 3));

		totalTime = new JLabel("0");
		totalTime.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		add(totalTime);
		
		timeLimit = new ValidatingTextField("" + DEFAULT_TIME_LIMIT, "You must enter a time limit");
		// timeLimit.setText("" + DEFAULT_TIME_LIMIT);
		timeLimit.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		add(timeLimit);
		
		transitions = new JLabel("0");
		transitions.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		add(transitions);
		
		for (int i = 0; i < 9; i++) { 
			TimerButton timerButton = new TimerButton(timerMediator);
			timerMediator.register(timerButton);
			add(timerButton);
		}
		
		setupClockRedrawRate();
		
		statusBar.setMessage("Stopped");
		
	}
	
	private void setupClockRedrawRate() { 
		TimerTask clockRedrawer = new TimerTask() {

			@Override
			public void run() {
				reDraw();
			} 
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockRedrawer, 0, 10); // redraw every 1 ms after a delay of 0 ms
	}

	public void resetAll() {
		trialOver = false;
		timerMediator.resetAll();
		statusBar.setMessage("Stopped");
		timeLimit.setText("" + DEFAULT_TIME_LIMIT);
	}

	public void reDraw() {
		timerMediator.reDraw();
		double totalTime = timerMediator.getTotalTime();
		this.totalTime.setText(decimalFormatter.format(totalTime));
		String timeLimitString = null;
		if (timeLimit.getText().equals("")) { 
			timeLimit.setText("0");
			timeLimitString = "0";
		} else { 
			timeLimitString = timeLimit.getText();
		}
		
		double timeLimit = Double.parseDouble(timeLimitString);
		if ((totalTime > timeLimit) && (trialOver == false)) { 
			this.trialOver = true;
			timerMediator.suspendAll();
			timerMediator.disableAllButtons();
			SoundMaker.playTimesUp();
		}
		transitions.setText("" + timerMediator.getTransitionCount());
	}
	
	public void populateTrial(Trial trial) {
		List<String> times = timerMediator.getTimes();
		trial.setZone(Double.parseDouble(times.get(0)), 1);
		trial.setZone(Double.parseDouble(times.get(1)), 2);
		trial.setZone(Double.parseDouble(times.get(2)), 3);
		trial.setZone(Double.parseDouble(times.get(3)), 4);
		trial.setZone(Double.parseDouble(times.get(4)), 5);
		trial.setZone(Double.parseDouble(times.get(5)), 6);
		trial.setZone(Double.parseDouble(times.get(6)), 7);
		trial.setZone(Double.parseDouble(times.get(7)), 8);
		trial.setZone(Double.parseDouble(times.get(8)), 9);
		trial.setZoneMovements(timerMediator.getTransitionCount());
	}

	public Collection<? extends ValidationError> validateTrialData() {
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		if (timerMediator.hasCountingTimer()) { 
			validationErrors.add(new ValidationError("All timers must be stopped to save. "));
		}
		
		return validationErrors;
	}
	
}
