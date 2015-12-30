package de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.location;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

@SuppressWarnings("serial")
public class LocationTimerPanel extends JPanel implements TrialListener {

	private List<LocationTimerButton> buttons;
	
	public LocationTimerPanel(Trial trial) { 
		buttons = new ArrayList<LocationTimerButton>();
		
		setLayout(new GridLayout(0, 1));
		
		for (Area a : trial.getAreas()){
			LocationTimerButton button = new LocationTimerButton(trial, a);
			buttons.add(button);
			add(button);
		}
		
		setupClockRedrawRate();
	}
	
	private void setupClockRedrawRate() { 
		TimerTask clockRedrawer = new TimerTask() {

			@Override
			public void run() {
				for (LocationTimerButton button : buttons){
					button.updateText();
				}
			} 
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockRedrawer, 0, 10); // redraw every 1 ms after a delay of 0 ms
	}
	
	@Override
	public void onStop() {
		for (LocationTimerButton button : buttons){
			button.setEnabled(false);
		}
	}
	
	@Override
	public void onReset() {
		for (LocationTimerButton button : buttons) { 
			button.setEnabled(true);
		}
	}

	@Override
	public void onAreaChange(Area name) {}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}

	@Override
	public void onStart() {}
}
