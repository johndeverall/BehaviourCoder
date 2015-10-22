package de.bochumuniruhr.psy.bio.behaviourcoder.timer.location;

import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSection;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.details.ValidationError;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.VideoListener;

@SuppressWarnings("serial")
public class LocationTimerPanel extends JPanel implements TrialSectionListener, VideoListener {

	private LocationTimerMediator timerMediator;
	private StatusPanel statusBar; 
	private DecimalFormat decimalFormatter;
	private boolean trialOver;
	private double timeLimit;
	private List<TrialSectionListener> trialSectionListeners;
	private LocationTimerButton closeTimer;
	private LocationTimerButton farTimer;
	
	public LocationTimerPanel(StatusPanel statusBar, int defaultTimeLimit) { 
		
		trialSectionListeners = new ArrayList<TrialSectionListener>();
		
		this.timeLimit = (double)defaultTimeLimit;
		
		this.statusBar = statusBar;
		
		this.trialOver = false;
		
		decimalFormatter = new DecimalFormat("0.00");
		
		timerMediator = new LocationTimerMediator(statusBar);
		
		setLayout(new GridLayout(0, 1));

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
	}

	public void reDraw() {
		timerMediator.reDraw();
		double totalTime = timerMediator.getTotalTime();
		fireTrialStopWatchUpdateEvent(totalTime);
		
		stopIfTimeLimitReached(totalTime);
		
		//transitions.setText("" + timerMediator.getTransitionCount());
	}

	private void fireTrialStopWatchUpdateEvent(double totalTime) {
		for (TrialSectionListener trialSectionListener : trialSectionListeners) { 
			trialSectionListener.trialStopWatchUpdate(decimalFormatter.format(totalTime));
		}
	}

	private void stopIfTimeLimitReached(double totalTime) {
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

	@Override
	public void onAreaChange(Area name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrialSectionSuspend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrialSectionResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeIsUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trialStopWatchUpdate(String trialTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeLimitChange(Integer seconds) {
		this.timeLimit = seconds;
		statusBar.setMessage("Time limit set to " + seconds + " seconds.");
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		timerMediator.onVideoLoaded(videoLength);
	}

	@Override
	public void onVideoPositionChange(double videoPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrialSectionStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoTimeChange(double videoTime) {
		// TODO Auto-generated method stub
		
	}
	
}
