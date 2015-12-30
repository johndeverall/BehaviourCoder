package de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * A class that controls sound output. 
 */
public class SoundMaker {

	/**
	 * Play the sound for when the mouse is clicked.
	 */
	public static void playMouseClick() { 
		playSound("ButtonClick.wav");
	}
	
	/**
	 * Play the sound for when there is a validation error.
	 */
	public static void playValidationError() { 
		playSound("ValidationError.wav");
	}
	
	/**
	 * Play the sound for when the trial's time is up.
	 */
	public static void playTimesUp() { 
		playSound("TimesUp.wav");
	}
	
	/**
	 * Play the sound for when the trial is saved.
	 */
	public static void playSave() { 
		playSound("Save.wav");
	}
	
	/**
	 * Plays the specified sound.
	 * 
	 * @param soundFileName - the filename of the sound file
	 */
	private static synchronized void playSound(final String soundFileName) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
		        		ClassLoader.getSystemResource(soundFileName));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}
	
	
}
