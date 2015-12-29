package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

public interface VideoListener { 
	
	public void onVideoLoaded(double videoLength);
	
	public void onVideoPositionChange(long videoPosition);

	public void onVideoStart();
	
	public void onVideoStop();
	
	public void onVideoPercentThroughChange(int videoTime);
}