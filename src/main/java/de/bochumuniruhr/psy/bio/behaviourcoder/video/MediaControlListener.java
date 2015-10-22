package de.bochumuniruhr.psy.bio.behaviourcoder.video;

public interface MediaControlListener {

	public void onPlay();
	
	public void onSeek(double positionInSeconds);
	
	public void onPause();
	
}
