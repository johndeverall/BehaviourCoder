package de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.instant;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;

/**
 * Button that enables users to interact with instant behaviours.
 * Displays the count of occurrences for the behaviour.
 */
@SuppressWarnings("serial")
public class InstantBehaviourButton extends JButton implements GlobalKeyListener {

	/**
	 * The behaviour that this button interacts with.
	 */
	private InstantBehaviour behaviour;
	
	/**
	 * The trial the button is for.
	 */
	private Trial trial;
	
	/**
	 * The key used to specify that the behaviour occurred.
	 */
	private char incrementKey;
	
	/**
	 * The key used to specify that the last occurrence should be dropped.
	 */
	private char decrementKey;
	
	/**
	 * Creates a button for interacting with an instant behaviour.
	 * 
	 * @param trial - the trial the button is for
	 * @param behaviour - the behaviour the button interacts with
	 * @param incrementKey - the key that specifies that the behaviour occurred
	 * @param decrementKey - the key that specified that the last occurrence of the behaviour should be dropped
	 */
	public InstantBehaviourButton(Trial trial, InstantBehaviour behaviour, final char incrementKey, final char decrementKey) { 
		this.incrementKey = incrementKey;
		this.decrementKey = decrementKey;
		this.behaviour = behaviour;
		this.trial = trial;
		
		addMouseListener(new MouseCounter());
		
		//Set text
		setText(behaviour.getName() + ": 0");
		setFont(new Font("Arial", Font.BOLD, 60));
		
		//Set the background text if a colour is specified
		if (behaviour.getColor() != null){
			setBackground(behaviour.getColor());
		}
	}
	
	/**
	 * Informs the behaviour that it occurred.
	 */
	private void behaviourOccurred() {
		behaviour.behaviourOccurred();
		setText(behaviour.getName() + ": " + behaviour.getNumberOfOccurrences());
		SoundMaker.playMouseClick();
	}
	
	/**
	 * Drops the last occurrence of the behaviour.
	 */
	private void dropLastOccurrence() {
		behaviour.removeLast();
		setText(behaviour.getName() + ": " + behaviour.getNumberOfOccurrences());
		SoundMaker.playMouseClick();
	}
	
	/**
	 * Resets this button to its initial state.
	 */
	void reset() { 
		setText(behaviour.getName() + ": 0");
	}
	
	@Override
	public void keyPressed(char key) { 
		//The behaviour should only occur if the trial is running
		if (trial.isRunning()) { 
			//Depending on the key do the corresponding action
			if (key == incrementKey) { 
				behaviourOccurred();
			} else if (key == decrementKey) { 
				dropLastOccurrence();
			}
		}
	}

	/**
	 * Mouse Listener to detect and inform the behaviour of clicks.
	 */
	private class MouseCounter extends MouseAdapter { 
		@Override
		public void mousePressed(MouseEvent e) {
			//The behaviour should only occur if the trial is running
			if (trial.isRunning()) { 
				//Depending on the mouse button do the corresponding action
				if (e.getButton() ==  MouseEvent.BUTTON1) { 
					behaviourOccurred();
				} else if (e.getButton() == MouseEvent.BUTTON3 
						|| e.getButton() == MouseEvent.BUTTON2) { 
					dropLastOccurrence();
				}
			}
		}
	}
}
