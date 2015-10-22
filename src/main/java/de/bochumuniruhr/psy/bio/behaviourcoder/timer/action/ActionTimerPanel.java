package de.bochumuniruhr.psy.bio.behaviourcoder.timer.action;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSection;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.details.ValidatingTextField;
import de.bochumuniruhr.psy.bio.behaviourcoder.details.ValidationError;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.VideoListener;

@SuppressWarnings("serial")
public class ActionTimerPanel extends JPanel implements TrialSectionListener, VideoListener {

	private ActionTimerMediator timerMediator;
	private StatusPanel statusBar; 
	private ValidatingTextField timeLimit;
	private final int DEFAULT_TIME_LIMIT;
	private boolean suspended = false;
	private ActionTimerButton followingTimer;
	private ActionTimerButton facingAwayTimer;
	private ActionTimerButton groomingMarkTimer;
	private ActionTimerButton groomingOtherTimer;
	
	public ActionTimerPanel(StatusPanel statusBar, int defaultTimeLimit) { 
		
		
		this.DEFAULT_TIME_LIMIT = defaultTimeLimit;
		
		this.statusBar = statusBar;
		
		timerMediator = new ActionTimerMediator(statusBar);
		
		setLayout(new GridLayout(4, 2));

		followingTimer = new ActionTimerButton(timerMediator);
		timerMediator.register(followingTimer);
		add(followingTimer);
		
		JLabel label1 = new JLabel(" :Following");
		label1.setFont(new Font("Arial", Font.BOLD, 20));
		label1.setHorizontalAlignment(JLabel.LEFT);
		add(label1);
		
		facingAwayTimer = new ActionTimerButton(timerMediator);
		timerMediator.register(facingAwayTimer);
		add(facingAwayTimer);

		JLabel label2 = new JLabel(" :Facing Away");
		label2.setFont(new Font("Arial", Font.BOLD, 20));
		label2.setHorizontalAlignment(JLabel.LEFT);
		add(label2);
		
		groomingMarkTimer = new ActionTimerButton(timerMediator);
		timerMediator.register(groomingMarkTimer);
		add(groomingMarkTimer);

		JLabel label3 = new JLabel(" :Grooming Mark");
		label3.setFont(new Font("Arial", Font.BOLD, 20));
		label3.setHorizontalAlignment(JLabel.LEFT);
		add(label3);

		groomingOtherTimer = new ActionTimerButton(timerMediator);
		timerMediator.register(groomingOtherTimer);
		add(groomingOtherTimer);
		
		JLabel label4 = new JLabel(" :Grooming Other");
		label4.setFont(new Font("Arial", Font.BOLD, 20));
		label3.setHorizontalAlignment(JLabel.LEFT);
		add(label4);
		
		setupClockRedrawRate();
		
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
		timerMediator.resetAll();
	}

	public void reDraw() {
		timerMediator.reDraw();
	}
	
	public void populateTrial(TrialSection trial) {
		trial.setFollowingClose(followingTimer.getCloseTime());
		trial.setFollowingFar(followingTimer.getFarTime());
		trial.setFacingAwayClose(facingAwayTimer.getCloseTime());
		trial.setFacingAwayFar(facingAwayTimer.getFarTime());
		trial.setGroomMarkClose(groomingMarkTimer.getCloseTime());
		trial.setGroomMarkFar(groomingMarkTimer.getFarTime());
		trial.setOtherClose(groomingOtherTimer.getCloseTime());
		trial.setOtherFar(groomingOtherTimer.getFarTime());
	}

	public Collection<? extends ValidationError> validateTrialData() {
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		if (timerMediator.hasCountingTimer()) { 
			validationErrors.add(new ValidationError("All timers must be stopped to save. "));
		}
		
		return validationErrors;
	}

	@Override
	public void timeIsUp() {
		timerMediator.disableAllButtons();
		timerMediator.suspendAll();
	}

	@Override
	public void onAreaChange(Area name) {
		timerMediator.onAreaChange(name);
	}

	@Override
	public void onTrialSectionSuspend() {
		timerMediator.suspendAll();
	}
	
	@Override
	public void onTrialSectionResume() { 
		timerMediator.resume();
	}

	@Override
	public void trialStopWatchUpdate(String trialTime) {
	}

	@Override
	public void onTimeLimitChange(Integer seconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		followingTimer.onVideoLoaded(videoLength);
		facingAwayTimer.onVideoLoaded(videoLength);
		groomingMarkTimer.onVideoLoaded(videoLength);
		groomingOtherTimer.onVideoLoaded(videoLength);
	}

	@Override
	public void onVideoPositionChange(double videoPosition) {
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
	
}
