package de.bochumuniruhr.psy.bio.behaviourcoder.gui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import de.bochumuniruhr.psy.bio.behaviourcoder.Main;

public class GlobalKeyPressHandler {

	private Main main;

	private List<GlobalKeyListener> globalKeyListeners;
	
	public GlobalKeyPressHandler(KeyboardFocusManager keyboardFocusManager, final Main main) { 

		this.main = main;
		globalKeyListeners = new ArrayList<GlobalKeyListener>();
		
		keyboardFocusManager.addKeyEventDispatcher(new KeyEventDispatcher() {

			public boolean dispatchKeyEvent(KeyEvent e) {

				if (KeyEvent.KEY_PRESSED == e.getID()
						&& e.getKeyCode() == 83 && e.isControlDown()) {
					main.save();
				} else if (KeyEvent.KEY_PRESSED == e.getID()) { 
					fireKeyEvent(e.getKeyChar());
				} 
				
				return false;
			}
		});
	}
	
	public void register(GlobalKeyListener listener) { 
		globalKeyListeners.add(listener);
	}
	
	private void fireKeyEvent(char charPressed) { 
		
		for (GlobalKeyListener listener : globalKeyListeners) { 
			listener.keyPressed(charPressed);
		}
	}
	
}
