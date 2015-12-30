package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 * Panel for displaying a video using VLC.
 */
@SuppressWarnings("serial")
public class VLCVideoPanel extends JPanel implements TrialListener, MediaControlListener {
	
	/**
	 * The list of listeners.
	 */
	private List<VideoListener> videoListeners;
	
	/**
	 * The component that displays the video.
	 */
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	
	/**
	 * The details for the trial the video is for.
	 */
	private TrialDetails trialDetails;
	
	/**
	 * Creates a video panel.
	 * 
	 * @param trialDetails - the details of the trial that loaded videos will be for
	 */
	public VLCVideoPanel(TrialDetails trialDetails) { 
		videoListeners = new ArrayList<VideoListener>();
		this.trialDetails = trialDetails;
		
		//Set the layout
		setLayout(new BorderLayout());
		
		//Create the media component
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			
			@Override
			public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
				//Inform listeners of new position as time and a percentage
				int iPos = (int)(newPosition * 100);
				fireVideoPercentThroughChangeEvent(iPos);
				fireVideoPositionChangeEvent(mediaPlayer.getTime());
			}
		});
		add(mediaPlayerComponent, BorderLayout.CENTER);
	}

	/**
	 * Loads a video to be shown.
	 * 
	 * @param file - the file that is the video to be loaded
	 */
	public void openVideo(final File file) {
		//Load video
		String video = null;
		try {
			video = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Prepare the video to be shown
		this.setVisible(true);
		mediaPlayerComponent.getMediaPlayer().prepareMedia(video);	
		
		//Inform listeners
		fireVideoLoadedEvent();
	}
	
	/**
	 * Adds a listener to this panel.
	 * @param videoListener - the listener for video events
	 */
	public void addVideoListener(VideoListener videoListener) { 
		this.videoListeners.add(videoListener);
	}
		
	/**
	 * Informs listeneres that a video has been loaded.
	 */
	private void fireVideoLoadedEvent() {
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoLoaded(mediaPlayerComponent.getMediaPlayer().getLength());
		}
	}
	
	/**
	 * Informs users that the video is now at a certain position.
	 * 
	 * @param iPos - the position in milliseconds
	 */
	private void fireVideoPercentThroughChangeEvent(int iPos) { 
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoPercentThroughChange(iPos);
		}
	}
	
	/**
	 * Informs users that the video is now at a certain position.
	 * 
	 * @param iPos - the position as a percentage
	 */
	private void fireVideoPositionChangeEvent(long videoPositionChange) { 
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoPositionChange(videoPositionChange);
		}
	}

	@Override
	public void onPause() {
		mediaPlayerComponent.getMediaPlayer().pause();		
	}

	@Override
	public void onResume() {
		mediaPlayerComponent.getMediaPlayer().play();
	}

	@Override
	public void onStop() {
		mediaPlayerComponent.getMediaPlayer().pause();	
	}

	@Override
	public void onPlay(boolean play) {
		if (play) { 
			mediaPlayerComponent.getMediaPlayer().play();
		} else { 
			mediaPlayerComponent.getMediaPlayer().pause();
		}
	}
	
	@Override
	public void onSkip(final long intervalInMiliseconds) {
		mediaPlayerComponent.getMediaPlayer().skip(intervalInMiliseconds);
	}

	@Override
	public void onAreaChange(Location name) {}

	@Override
	public void onStart() {
		trialDetails.setVideoTimeOffset(mediaPlayerComponent.getMediaPlayer().getTime() / 1000.0);
	}

	@Override
	public void onReset() {
		mediaPlayerComponent.getMediaPlayer().skip(0);
		mediaPlayerComponent.getMediaPlayer().stop();
	}

}
