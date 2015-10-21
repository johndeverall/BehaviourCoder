package de.bochumuniruhr.psy.bio.behaviourcoder.timer.location;

import java.awt.Color;
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

import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSection;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.details.ValidatingTextField;
import de.bochumuniruhr.psy.bio.behaviourcoder.details.ValidationError;

@SuppressWarnings("serial")
public class LocationTimerPanel extends JPanel {

	private LocationTimerMediator timerMediator;
	private StatusPanel statusBar; 
	private JLabel totalTime;
	private ValidatingTextField timeLimit;
	private JLabel mirrorOrDividerText;
	private JLabel transitions;
	private DecimalFormat decimalFormatter;
	private boolean trialOver;
	private final int DEFAULT_TIME_LIMIT;
	private final int LABEL_FONT_SIZE = 30;
	private List<TrialSectionListener> trialSectionListeners;
	private LocationTimerButton closeTimer;
	private LocationTimerButton farTimer;
	
	public LocationTimerPanel(StatusPanel statusBar, int defaultTimeLimit) { 
		
		trialSectionListeners = new ArrayList<TrialSectionListener>();
		
		this.DEFAULT_TIME_LIMIT = defaultTimeLimit;
		
		this.statusBar = statusBar;
		
		this.trialOver = false;
		
		decimalFormatter = new DecimalFormat("0.00");
		
		timerMediator = new LocationTimerMediator(statusBar);
		
		setLayout(new GridLayout(0, 1));

		totalTime = new JLabel("0");
		totalTime.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		totalTime.setHorizontalAlignment(JLabel.CENTER);
		add(totalTime);
		
		timeLimit = new ValidatingTextField("" + DEFAULT_TIME_LIMIT, "You must enter a time limit");
		timeLimit.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		timeLimit.setHorizontalAlignment(JLabel.CENTER);
		add(timeLimit);
		
		transitions = new JLabel("0");
		transitions.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		transitions.setHorizontalAlignment(JLabel.CENTER);
		add(transitions);
		
		mirrorOrDividerText = new JLabel("Mirror / Divider");
		Font font = new Font("Arial", Font.BOLD, 15);
		mirrorOrDividerText.setFont(font);
		mirrorOrDividerText.setHorizontalAlignment(JLabel.CENTER);
		mirrorOrDividerText.setBackground(Color.BLACK);
		mirrorOrDividerText.setOpaque(true);
		mirrorOrDividerText.setForeground(Color.WHITE);
		add(mirrorOrDividerText);
		
		closeTimer = new LocationTimerButton("CLOSE", timerMediator);
		timerMediator.register(closeTimer);
		add(closeTimer);
		
		farTimer = new LocationTimerButton("FAR", timerMediator);
		timerMediator.register(farTimer);
		add(farTimer);
		
		
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
		
		stopIfTimeLimitReached(totalTime, timeLimitString);
		
		transitions.setText("" + timerMediator.getTransitionCount());
	}

	private void stopIfTimeLimitReached(double totalTime, String timeLimitString) {
		double timeLimit = Double.parseDouble(timeLimitString);
		if ((totalTime > timeLimit) && (trialOver == false)) { 
			this.trialOver = true;
			timerMediator.suspendAll();
			timerMediator.disableAllButtons();
			fireTimeUpEvent();
			SoundMaker.playTimesUp();
		}
	}
	
	private void fireTimeUpEvent() {
		for (TrialSectionListener trialSectionListener : trialSectionListeners) { 
			trialSectionListener.timeIsUp();
		}
	}

	public void populateTrial(TrialSection trial) {
		trial.setClose(Double.parseDouble(closeTimer.getText()));
		trial.setFar(Double.parseDouble(farTimer.getText()));
		trial.setLocationChanges(timerMediator.getTransitionCount());
	}

	public Collection<? extends ValidationError> validateTrialData() {
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		if (timerMediator.hasCountingTimer()) { 
			validationErrors.add(new ValidationError("All timers must be stopped to save. "));
		}
		
		return validationErrors;
	}

	public void addTrialSectionListener(TrialSectionListener trialSectionListener) {
		this.trialSectionListeners.add(trialSectionListener);
		this.timerMediator.addTrialSectionListener(trialSectionListener);
	}
	
}
