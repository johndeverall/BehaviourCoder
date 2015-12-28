package de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer.action;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

@SuppressWarnings("serial")
public class ActionTimerPanel extends JPanel implements GlobalKeyListener {
	
	private List<ActionTimerButton> buttons;
	
	public ActionTimerPanel(Trial trial, List<Character> activationKeys) { 
		
		setLayout(new GridLayout(4, 2));
		
		List<TimedBehaviour> behaviours = trial.getTimedBehaviours();
		setLayout(new GridLayout(behaviours.size(), 1));

		buttons = new ArrayList<ActionTimerButton>();
		
		for (int i = 0; i < behaviours.size(); ++i){
			ActionTimerButton button = new ActionTimerButton(trial, behaviours.get(i),
					activationKeys.get(i));
			buttons.add(button);
			add(button);
		}

		setupClockRedrawRate();
		
	}
	
	private void setupClockRedrawRate() { 
		TimerTask clockRedrawer = new TimerTask() {

			@Override
			public void run() {
				for (ActionTimerButton button : buttons){
					button.updateText();
				}
			} 
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockRedrawer, 0, 10); // redraw every 1 ms after a delay of 0 ms
	}

	@Override
	public void keyPressed(char key) {
		for (ActionTimerButton button : buttons){
			button.keyPressed(key);
		}
	}
}
