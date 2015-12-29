package de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.location;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

@SuppressWarnings("serial")
public class LocationTimerPanel extends JPanel implements TrialListener {

	private StatusPanel statusBar; 
	private List<LocationTimerButton> buttons;
	
	public LocationTimerPanel(Trial trial, StatusPanel statusBar) { 
		buttons = new ArrayList<LocationTimerButton>();
		
		this.statusBar = statusBar;
		
		setLayout(new GridLayout(0, 1));
		
		for (Area a : trial.getAreas()){
			LocationTimerButton button = new LocationTimerButton(trial, a);
			buttons.add(button);
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
	}

	@Override
	public void onAreaChange(Area name) {}

	@Override
	public void onStop() {
		for (LocationTimerButton button : buttons){
			button.setEnabled(false);
		}
	}

	@Override
	public void onPause() {
		statusBar.setMessage("Trial suspended");
	}

	@Override
	public void onResume() {
		statusBar.setMessage("Trial in progress");
	}

	@Override
	public void onStart() {
		statusBar.setMessage("Trial in progress");
	}

}
