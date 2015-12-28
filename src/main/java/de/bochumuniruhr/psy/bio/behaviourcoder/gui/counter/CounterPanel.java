package de.bochumuniruhr.psy.bio.behaviourcoder.gui.counter;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.VideoListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

@SuppressWarnings("serial")
public class CounterPanel extends JPanel implements GlobalKeyListener, VideoListener {
	
	private List<ClickCounterButton> buttons;
	
	public CounterPanel(Trial trial, List<Character> increment, List<Character> decrememt) { 
		List<InstantBehaviour> behaviours = trial.getInstantBehaviours();
		setLayout(new GridLayout(behaviours.size(), 1));

		buttons = new ArrayList<ClickCounterButton>();
		
		for (int i = 0; i < behaviours.size(); ++i){
			ClickCounterButton button = new ClickCounterButton(trial, behaviours.get(i),
					increment.get(i), decrememt.get(i));
			buttons.add(button);
			add(button);
		}
	}

	public void resetAll() {
		for (ClickCounterButton button : buttons){
			button.reset();
		}
	}

	public void keyPressed(char key) {
		for (ClickCounterButton button : buttons){
			button.keyPressed(key);
		}
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		for (ClickCounterButton button : buttons){
			button.onVideoLoaded(videoLength);
		}
	}

	@Override
	public void onVideoPositionChange(long videoPosition) {}

	@Override
	public void onVideoStart() {}

	@Override
	public void onVideoStop() {}

	@Override
	public void onVideoError(String message) {}

	@Override
	public void onVideoPercentThroughChange(int videoTime) {}

}
