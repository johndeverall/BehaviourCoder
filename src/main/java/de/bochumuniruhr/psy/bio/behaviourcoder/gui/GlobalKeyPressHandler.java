package de.bochumuniruhr.psy.bio.behaviourcoder.gui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import de.bochumuniruhr.psy.bio.behaviourcoder.Main;

/**
 * Handler for all key presses for the entire application.
 */
public class GlobalKeyPressHandler {

	/**
	 * The list of listeners.
	 */
	private List<GlobalKeyListener> globalKeyListeners;
	
	public GlobalKeyPressHandler(KeyboardFocusManager keyboardFocusManager, final Main main) { 
		globalKeyListeners = new ArrayList<GlobalKeyListener>();
		
		//Create the listener for key presses
		keyboardFocusManager.addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				//If ctrl-s is pressed save
				if (KeyEvent.KEY_PRESSED == e.getID()
						&& e.getKeyCode() == 83 && e.isControlDown()) {
					main.save();
				//Otherwise alert listeners that a key has been pressed
				} else if (KeyEvent.KEY_PRESSED == e.getID()) { 
					for (GlobalKeyListener listener : globalKeyListeners) { 
						listener.keyPressed(e.getKeyChar());
					}
				} 
				return false;
			}
		});
	}
	
	/**
	 * Registers a listener to be notified of key presses.
	 * 
	 * @param listener - the listener to add
	 */
	public void register(GlobalKeyListener listener) { 
		globalKeyListeners.add(listener);
	}
}
