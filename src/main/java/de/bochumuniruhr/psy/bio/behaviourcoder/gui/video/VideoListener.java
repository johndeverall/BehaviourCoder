package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

/**
 * Specifies a listener for a video panel.
 */
public interface VideoListener { 
	
	/**
	 * Called when the panel has loaded a video.
	 * 
	 * @param videoLength - the length of the video
	 */
	public void onVideoLoaded(double videoLength);
	
	/**
	 * Called when the position in the video has changed.
	 * 
	 * @param videoPosition - the current position in the video in milliseconds
	 */
	public void onVideoPositionChange(long videoPosition);
	
	/**
	 * Called when the position as a percentage in the video has changed.
	 * 
	 * @param videoTime - the position in the video as a percentage
	 */
	public void onVideoPercentThroughChange(int videoTime);
	
	/**
	 * Called when the video has finished playing.
	 */
	public void onVideoFinished();
}