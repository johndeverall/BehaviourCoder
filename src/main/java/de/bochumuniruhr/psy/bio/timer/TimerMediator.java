package de.bochumuniruhr.psy.bio.timer;

import java.util.ArrayList;
import java.util.List;

public class TimerMediator {
	
	private ArrayList<TimerButton> buttons;
	
	private int switches = 0;
	
	private StatusPanel statusBar;

	public TimerMediator(StatusPanel statusBar) {
		this.statusBar = statusBar;
		buttons = new ArrayList<TimerButton>();
	}

	public void reDraw() { 
		for (TimerButton button : buttons) { 
			button.updateText();
		}
	}
	
	public void register(TimerButton button) { 
		if (buttons.contains(button)) { 
			return;
		} else { 
			buttons.add(button);
		}
	}
	
	public void enable(TimerButton buttonToEnable) { 
		for (TimerButton timerButton : buttons) { 
			if (timerButton.equals(buttonToEnable)) {
				if (!timerButton.isLastClicked()) { 
					// we don't want to increment switches if a running timer is clicked on
					switches++;
				}
				timerButton.setLastClicked(true);
				buttonToEnable.startResumeOrSuspend();
			} else { 
				timerButton.suspendIfNotStopped();		
				timerButton.setLastClicked(false);
			}
		}
		if (hasCountingTimer()) { 
			statusBar.setMessage("Trial in progress");
		} else { 
			statusBar.setMessage("Trial suspended");
		}
		
	}

	public void resetAll() {
		for (TimerButton button : buttons) { 
			button.reset();
			button.setEnabled(true);
		}
		switches = 0;
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
			button.suspendIfNotStopped();
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
	
}
