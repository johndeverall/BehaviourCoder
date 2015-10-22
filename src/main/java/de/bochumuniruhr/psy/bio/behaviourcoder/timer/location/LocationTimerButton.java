package de.bochumuniruhr.psy.bio.behaviourcoder.timer.location;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;

import org.apache.commons.lang3.time.StopWatch;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.timer.TimerButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.timer.TimerMediator;

@SuppressWarnings("serial")
public class LocationTimerButton extends JButton implements ActionListener, TimerButton {

	private StopWatch stopWatch;
	private TimerMediator mediator;
	private DecimalFormat decimalFormatter;
	private boolean lastClicked;
	private String buttonName;
	private boolean videoLoaded = false;
	
	public LocationTimerButton(String buttonName, TimerMediator mediator) { 
		this.mediator = mediator;
		this.buttonName = buttonName;
		stopWatch = new StopWatch();
		addActionListener(this);
		setFont(new Font("Arial", Font.BOLD, 30));
		decimalFormatter = new DecimalFormat("0.00");
	}
	
	public void stop() { 
		stopWatch.stop();
	}
	
	public void updateText() { 
		Double timeInSeconds = (double) stopWatch.getTime() / 1000.00;
		super.setText(decimalFormatter.format(timeInSeconds));
	}
	
	public void suspendIfNotStoppedOrSuspended() { 
		if (stopWatch.isStopped() || stopWatch.isSuspended()) { 
			// do nothing
		} else { 
			stopWatch.suspend();
		}
	}
	
	public void startResumeOrSuspend() { 
		if (stopWatch.isStopped()) { 
			stopWatch.start();
		} else if (stopWatch.isSuspended()) { 
			stopWatch.resume();
		} else { 
			stopWatch.suspend();
		} 
	}

	public void actionPerformed(ActionEvent e) {
		if (videoLoaded) { 
			mediator.enable(this);
			SoundMaker.playMouseClick();
			lastClicked = true;
		}
	}
	
	public void reset() { 
		stopWatch.reset();
	}
	
	public boolean isStopped() { 
		return stopWatch.isStopped();
	}
	
	public boolean isSuspended() { 
		return stopWatch.isSuspended();
	}
	
	public boolean isStarted() { 
		return stopWatch.isStarted();
	}

	public boolean isLastClicked() {
		return lastClicked;
	}

	public void setLastClicked(boolean lastClicked) {
		this.lastClicked = lastClicked;
	}

	@Override
	public void onAreaChange(Area area) {
	}
	
	@Override
	public String getButtonName() { 
		return this.buttonName;
	}

	@Override
	public void onTrialSectionSuspend() {
	}

	@Override
	public void onTrialSectionResume() {
	}

	@Override
	public boolean isCounting() {
		return !(this.isStopped() || this.isSuspended());
	}

	@Override
	public void timeIsUp() {
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
	
}
