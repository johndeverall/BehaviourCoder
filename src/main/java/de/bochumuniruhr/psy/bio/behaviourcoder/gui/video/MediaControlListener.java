package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

/**
 * Specifies a listener for actions of the media control panel.
 */
public interface MediaControlListener {

	/**
	 * Called when the control has paused or started the video. 
	 * 
	 * @param play - whether the video was told to play. 
	 * 		Is true when the video was told to start and false when the video was told to pause.
	 */
	public void onPlay(boolean play);
	
	/**
	 * Called when the control has skipped to part of the video.
	 * 
	 * @param positionInSeconds - the current time in the video in seconds.
	 */
	public void onSkip(long positionInSeconds);
	
}
