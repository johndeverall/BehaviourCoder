package de.bochumuniruhr.psy.bio.behaviourcoder.video;

public interface MediaControlListener {

	public void onPlay(boolean play);
	
	public void onSkip(long positionInSeconds);
	
}
