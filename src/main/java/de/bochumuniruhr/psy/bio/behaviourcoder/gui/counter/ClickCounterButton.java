package de.bochumuniruhr.psy.bio.behaviourcoder.gui.counter;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.VideoListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

@SuppressWarnings("serial")
public class ClickCounterButton extends JButton implements GlobalKeyListener, VideoListener {

	private InstantBehaviour behaviour;
	private final Trial trial;
	private char incrementKey;
	private char decrementKey;
	private boolean videoLoaded = false;
	
	public ClickCounterButton(Trial trial, InstantBehaviour behaviour, final char incrementKey, final char decrementKey) { 
		this.incrementKey = incrementKey;
		this.decrementKey = decrementKey;
		this.behaviour = behaviour;
		this.trial = trial;
		
		addMouseListener(new MouseCounter());
		
		setText(behaviour.getName() + ": 0");
		setFont(new Font("Arial", Font.BOLD, 60));
		if (behaviour.getColor() != null){
			setBackground(behaviour.getColor());
		}
	}
	
	private void increment() {
		behaviour.behaviourOccurred();
		setText(behaviour.getName() + ": " + behaviour.getNumberOfOccurrences());
		SoundMaker.playMouseClick();
	}
	
	private void decrement() {
		behaviour.removeLast();
		setText(behaviour.getName() + ": " + behaviour.getNumberOfOccurrences());
		SoundMaker.playMouseClick();
	}
	
	public void reset() { 
		behaviour.reset();
		setText(behaviour.getName() + ": 0");
	}
	
	@Override
	public void keyPressed(char key) { 
		if (trial.isRunning() && videoLoaded) { 
			if (key == incrementKey) { 
				increment();
			} else if (key == decrementKey) { 
				decrement();
			}
		}
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		this.videoLoaded = true;
		reset();
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

	private class MouseCounter extends MouseAdapter { 
		@Override
		public void mousePressed(MouseEvent e) {
			if (trial.isRunning() && videoLoaded) { 
				if (e.getButton() ==  MouseEvent.BUTTON1) { 
					increment();
				} else if (e.getButton() == MouseEvent.BUTTON3 
						|| e.getButton() == MouseEvent.BUTTON2) { 
					decrement();
				}
			}
		}
	}
}
