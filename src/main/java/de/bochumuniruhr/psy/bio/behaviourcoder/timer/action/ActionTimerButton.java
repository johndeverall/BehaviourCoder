package de.bochumuniruhr.psy.bio.behaviourcoder.timer.action;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;

import org.apache.commons.lang3.time.StopWatch;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.timer.TimerButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.timer.TimerMediator;

@SuppressWarnings("serial")
public class ActionTimerButton extends JButton implements ActionListener, TimerButton, TrialSectionListener, GlobalKeyListener {

	private StopWatch closeStopWatch;
	private StopWatch farStopWatch;
	private TimerMediator mediator;
	private DecimalFormat decimalFormatter;
	private boolean lastClicked = false;
	private boolean videoLoaded = false;

	// the timer that starts after a stop is the timer for the area we are
	// currently in.
	private Area currentArea;
	private boolean suspended = false;
	private boolean allSuspended = false;
	private char activationKey;

	public ActionTimerButton(TimerMediator mediator, char activationKey) {
		this.mediator = mediator;
		this.activationKey = activationKey;
		closeStopWatch = new StopWatch();
		farStopWatch = new StopWatch();
		addActionListener(this);
		setFont(new Font("Arial", Font.BOLD, 30));
		decimalFormatter = new DecimalFormat("0.00");
	}

	/**
	 * Stops both timers.
	 */
	@Override
	public void stop() {
		closeStopWatch.stop();
		farStopWatch.stop();
	}

	/**
	 * Shows the combined total of both the close and far timer.
	 */
	@Override
	public void updateText() {
		Double timeInSeconds = ((double) closeStopWatch.getTime() + (double) farStopWatch.getTime()) / 1000.00;
		super.setText(decimalFormatter.format(timeInSeconds));
	}

	@Override
	public void suspendIfNotStoppedOrSuspended() {
		if (closeStopWatch.isStopped() || closeStopWatch.isSuspended()) {
			// do nothing
		} else {
			closeStopWatch.suspend();
		}

		if (farStopWatch.isStopped() || farStopWatch.isSuspended()) {
			// do nothing
		} else {
			farStopWatch.suspend();
		}
	}

	@Override
	public void startResumeOrSuspend() {

		if (currentArea == Area.CLOSE) {

			if (closeStopWatch.isStopped()) {
				closeStopWatch.start();
			} else if (closeStopWatch.isSuspended()) {
				closeStopWatch.resume();
			} else {
				closeStopWatch.suspend();
			}
		} else if (currentArea == Area.FAR) {
			if (farStopWatch.isStopped()) {
				farStopWatch.start();
			} else if (farStopWatch.isSuspended()) {
				farStopWatch.resume();
			} else {
				farStopWatch.suspend();
			}
		} else {
			// do nothing. We don't have an area so the trial has not started
			// yet.
		}
	}

	private void switchStopWatch() {

		if (currentArea == Area.CLOSE) {
			swapIfNecessary(farStopWatch, closeStopWatch);
		} else {
			swapIfNecessary(closeStopWatch, farStopWatch);
		}

		// if we are in the close area
	}

	private void swapIfNecessary(StopWatch toStop, StopWatch toStart) {

		// stop watch1
		if (toStop.isStarted() && !toStop.isSuspended()) {

			// we want to stop / suspend the watch
			toStop.suspend();

			// then we want to start the toStart watch
			if (toStart.isStopped()) {
				toStart.start();
			} else {
				toStart.resume();
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		toggleButton();
	}
	
	public void toggleButton() { 
		if (!allSuspended && videoLoaded) {
			if (currentArea != null) {
				mediator.enable(this);
				SoundMaker.playMouseClick();
				lastClicked = true;
			}
		}		
	}

	@Override
	public void reset() {
		closeStopWatch.reset();
		farStopWatch.reset();
		lastClicked = false;
		videoLoaded = true;
		suspended = false;
		allSuspended = false;
	}

	@Override
	public boolean isStopped() {
		return closeStopWatch.isStopped() && farStopWatch.isStopped();
	}

	@Override
	public boolean isSuspended() {
		return closeStopWatch.isSuspended() && farStopWatch.isSuspended();
	}

	@Override
	public boolean isStarted() {
		return closeStopWatch.isStarted() && farStopWatch.isStarted();
	}

	@Override
	public boolean isLastClicked() {
		return lastClicked;
	}

	@Override
	public void setLastClicked(boolean lastClicked) {
		this.lastClicked = lastClicked;
	}

	@Override
	public void onAreaChange(Area area) {
		currentArea = area;
		switchStopWatch();
	}

	@Override
	public boolean isCounting() {
		return !((closeStopWatch.isStopped() || closeStopWatch.isSuspended())
				&& (farStopWatch.isStopped() || farStopWatch.isSuspended()));
	}

	@Override
	public void onTrialSectionSuspend() {
		allSuspended = true;
		if (isCounting()) {
			this.suspended = true;
		}
		suspendIfNotStoppedOrSuspended();
	}

	@Override
	public void onTrialSectionResume() {
		allSuspended = false;
		if (suspended) {
			this.suspended = false;
			startResumeOrSuspend();
		}
	}
	
	@Override
	public void keyPressed(char key) { 
		if (key == activationKey) { 
			toggleButton();
		}
	}

	@Override
	public String getButtonName() {
		return "ActionTimerButton";
	}

	@Override
	public void timeIsUp() {
	}

	public Double getCloseTime() {
		return (double) closeStopWatch.getTime() / 1000;
	}
	
	public Double getFarTime() { 
		return (double) farStopWatch.getTime() / 1000;
	}

	@Override
	public void trialStopWatchUpdate(String trialTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeLimitChange(Integer seconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		this.videoLoaded = true;
	}

	@Override
	public void onVideoPositionChange(long videoPosition) {
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
	public void onVideoPercentThroughChange(int videoTime) {
		// TODO Auto-generated method stub
		
	}

}