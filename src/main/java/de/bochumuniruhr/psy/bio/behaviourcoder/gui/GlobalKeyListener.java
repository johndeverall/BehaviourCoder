package de.bochumuniruhr.psy.bio.behaviourcoder.gui;

/**
 * Specifies listeners for key presses within the application.
 */
public interface GlobalKeyListener {

	/**
	 * Called when a key has been pressed. Will not be called when Ctrl-S is pressed.
	 * 
	 * @param key - the character of the key that was pressed
	 */
	public void keyPressed(char key);
	
}
