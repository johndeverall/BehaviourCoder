package de.bochumuniruhr.psy.bio.behaviourcoder.video;

public interface VideoListener { 
	
	public void onVideoLoaded(double videoLength);
	
	public void onVideoPositionChange(double videoPosition);

	public void onVideoStart();
	
	public void onVideoStop();
		
}