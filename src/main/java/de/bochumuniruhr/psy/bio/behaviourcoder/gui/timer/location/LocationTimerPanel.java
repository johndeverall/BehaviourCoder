package de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.location;

import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.details.ValidationError;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialSection;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialSectionListener;

@SuppressWarnings("serial")
public class LocationTimerPanel extends JPanel implements TrialSectionListener, TrialListener {

	private StatusPanel statusBar; 
	private DecimalFormat decimalFormatter;
	private List<TrialSectionListener> trialSectionListeners;
	private List<LocationTimerButton> buttons;
	private Trial trial;
	
	public LocationTimerPanel(Trial trial, StatusPanel statusBar) { 
		
		trialSectionListeners = new ArrayList<TrialSectionListener>();
		buttons = new ArrayList<LocationTimerButton>();
		
		this.statusBar = statusBar;
		this.trial = trial;
		
		decimalFormatter = new DecimalFormat("0.00");
		
		setLayout(new GridLayout(0, 1));
		
		for (Area a : trial.getAreas()){
			LocationTimerButton button = new LocationTimerButton(trial, a);
			buttons.add(button);
			trial.addListener(this);
			add(button);
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
		for (LocationTimerButton button : buttons) { 
			button.setEnabled(true);
		}
		statusBar.setMessage("Stopped");
	}

	public void reDraw() {
		for (LocationTimerButton button : buttons){
			button.updateText();
		}
		double totalTime = trial.getCurrentTime() / 1000.00;
		fireTrialStopWatchUpdateEvent(totalTime);
		
	}

	private void fireTrialStopWatchUpdateEvent(double totalTime) {
		for (TrialSectionListener trialSectionListener : trialSectionListeners) { 
			trialSectionListener.trialStopWatchUpdate(decimalFormatter.format(totalTime));
		}
	}

	public void populateTrial(TrialSection trial) {
		trial.setClose(this.trial.getAreaTime(this.trial.getAreas().get(0))/1000.0);
		trial.setFar(this.trial.getAreaTime(this.trial.getAreas().get(1))/1000.0);
	}

	public Collection<? extends ValidationError> validateTrialData() {
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		if (trial.isRunning()) { 
			validationErrors.add(new ValidationError("All timers must be stopped to save. "));
		}
		
		return validationErrors;
	}

	public void addTrialSectionListener(TrialSectionListener trialSectionListener) {
		this.trialSectionListeners.add(trialSectionListener);
	}

	@Override
	public void onAreaChange(Area name) {}

	@Override
	public void onTrialSectionSuspend() {}

	@Override
	public void onTrialSectionResume() {}

	@Override
	public void timeIsUp() {}

	@Override
	public void trialStopWatchUpdate(String trialTime) {}

	@Override
	public void onTimeLimitChange(Integer seconds) {
		//this.timeLimit = seconds;
		statusBar.setMessage("Time limit set to " + seconds + " seconds.");
	}

	@Override
	public void onTrialSectionStart() {}

	@Override
	public void onStop() {
		for (LocationTimerButton button : buttons){
			button.setEnabled(false);
		}
	}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}

	@Override
	public void onStart() {}

}
