package de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.TimerButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.TimerMediator;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Area;

public class ActionTimerMediator implements TimerMediator, GlobalKeyListener {
	
	private ArrayList<TimerButton> buttons;
	
	private StatusPanel statusBar;

	public ActionTimerMediator(StatusPanel statusBar) {
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
	public void enable(TimerButton timerButton) { 
		timerButton.startResumeOrSuspend();
	}

	public void resetAll() {
		for (TimerButton button : buttons) { 
			button.reset();
			button.setEnabled(true);
		}
		this.suspendAll();
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
			button.onTrialSectionSuspend();
		}
	}

	public void disableAllButtons() {
		for (TimerButton button : buttons) { 
			button.setEnabled(false);
		}
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

	public void onAreaChange(Area area) {
		for (TimerButton button : buttons) { 
			button.onAreaChange(area);
		}
	}

	public void resume() {
		for (TimerButton button : buttons) { 
			button.onTrialSectionResume();
		}
	}

	@Override
	public void keyPressed(char key) {
		for (TimerButton button : buttons) { 
			button.keyPressed(key);
		}
	}

}
