package de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundMaker {

	public static void playMouseClick() { 
		playSound("ButtonClick.wav");
	}
	
	public static void playValidationError() { 
		playSound("ValidationError.wav");
	}
	
	public static void playTimesUp() { 
		playSound("TimesUp.wav");
	}
	
	public static void playSave() { 
		playSound("Save.wav");
	}
	
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
