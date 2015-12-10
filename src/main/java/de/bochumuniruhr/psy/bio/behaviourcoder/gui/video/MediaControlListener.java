package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

public interface MediaControlListener {

	public void onPlay(boolean play);
	
	public void onSkip(long positionInSeconds);
	
}
