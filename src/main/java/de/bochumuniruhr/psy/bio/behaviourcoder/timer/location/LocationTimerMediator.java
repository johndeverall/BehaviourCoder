package de.bochumuniruhr.psy.bio.behaviourcoder.timer.location;

import java.util.ArrayList;
import java.util.List;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.timer.TimerButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.timer.TimerMediator;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.VideoListener;

public class LocationTimerMediator implements TimerMediator, VideoListener {
	
	private ArrayList<TimerButton> buttons;
	
	private List<TrialSectionListener> trialSectionListeners = new ArrayList<TrialSectionListener>();
	
	private int switches = 0;
	
	private StatusPanel statusBar;
	
	private boolean trialSectionStarted = false;

	public LocationTimerMediator(StatusPanel statusBar) {
		this.statusBar = statusBar;
		buttons = new ArrayList<TimerButton>();
	}

	public void reDraw() { 
		for (TimerButton button : buttons) { 
			button.updateText();
		}
	}
	
	@Override
	public void register(TimerButton button) { 
		if (buttons.contains(button)) { 
			return;
		} else { 
			buttons.add(button);
		}
	}
	
	@Override
	public void enable(TimerButton buttonToEnable) { 
		if (trialSectionStarted == false) { 
			trialSectionStarted = true;
			fireTrialStartEvent();
		}
		for (TimerButton timerButton : buttons) { 
			if (timerButton.equals(buttonToEnable)) {
				if (!timerButton.isLastClicked()) { 
					// we don't want to increment switches if a running timer is clicked on
					switches++;
					fireAreaChangeEvent(Area.valueOf(buttonToEnable.getButtonName()));
				}
				timerButton.setLastClicked(true);
				buttonToEnable.startResumeOrSuspend();
			} else { 
				timerButton.suspendIfNotStoppedOrSuspended();		
				timerButton.setLastClicked(false);
			}
		}
		if (hasCountingTimer()) { 
			statusBar.setMessage("Trial in progress");
			fireTrialSectionResumeEvent();
		} else { 
			statusBar.setMessage("Trial suspended");
			fireTrialSectionSuspendedEvent();
		}
		
	}

	private void fireTrialStartEvent() {
		for (TrialSectionListener trialSectionListener : trialSectionListeners) { 
			trialSectionListener.onTrialSectionStart();
		}
	}

	private void fireTrialSectionResumeEvent() {
		for (TrialSectionListener trialSectionListener : trialSectionListeners) { 
			trialSectionListener.onTrialSectionResume();
		}
	}

	private void fireTrialSectionSuspendedEvent() {
		for (TrialSectionListener trialSectionListener : trialSectionListeners) { 
			trialSectionListener.onTrialSectionSuspend();
		}
	}

	private void fireAreaChangeEvent(Area area) {
		for (TrialSectionListener areaChangeListener : trialSectionListeners) { 
			areaChangeListener.onAreaChange(area);
		}
	}
	
	public void addTrialSectionListener(TrialSectionListener areaChangeListener) { 
		this.trialSectionListeners.add(areaChangeListener);
	}

	public void resetAll() {
		for (TimerButton button : buttons) { 
			button.reset();
			button.setEnabled(true);
		}
		switches = 0;
		trialSectionStarted = false;
	}

	public List<String> getTimes() {
		List<String> times = new ArrayList<String>();
		
		for (TimerButton button : buttons) { 
			times.add(button.getText());
		}
		
		return times;
	}

	public double getTotalTime() {
		double totalTime = 0;
		
		for (TimerButton button : buttons) { 
			totalTime += Double.parseDouble(button.getText());
		}
		
		return totalTime;
	}

	public void suspendAll() {
		
		for (TimerButton button : buttons) { 
			button.suspendIfNotStoppedOrSuspended();
		}
	}

	public void disableAllButtons() {
		for (TimerButton button : buttons) { 
			button.setEnabled(false);
		}
	}

	public int getTransitionCount() {
		return switches;
	}
	
	public boolean hasCountingTimer() { 
		
		boolean hasCountingTimer = false;
		
		for (TimerButton button : buttons) { 
			if (!button.isSuspended() && !button.isStopped()) { 
				hasCountingTimer = true;
			}
		}
		
		return hasCountingTimer;
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		for (TimerButton button : buttons) { 
			button.onVideoLoaded(videoLength);
		}
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
	
}
