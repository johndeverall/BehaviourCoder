package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

import jxl.common.Logger;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialSection;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class VLCVideoPanel extends JPanel implements TrialListener, MediaControlListener {

	private List<VideoListener> videoListeners;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private Logger logger = Logger.getLogger(this.getClass());
	private Trial trial;
	
	public VLCVideoPanel(Trial trial) { 
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		setLayout(new BorderLayout());
		add(mediaPlayerComponent, BorderLayout.CENTER);
		videoListeners = new ArrayList<VideoListener>();
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			
			@Override
			public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
				int iPos = (int)(newPosition * 100);
				fireVideoPercentThroughChangeEvent(iPos);
				fireVideoPositionChangeEvent(mediaPlayer.getTime());
			}
			
			
		});
		this.trial = trial;
	}

	public void openVideo(final File file, StatusPanel statusBar) {
		String video = null;
		try {
			video = file.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setVisible(true);
		mediaPlayerComponent.getMediaPlayer().prepareMedia(video);	
		fireVideoLoadedEvent();
	}
	
	public void addVideoListener(VideoListener videoListener) { 
		this.videoListeners.add(videoListener);
	}
		
	private void fireVideoLoadedEvent() {
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoLoaded(mediaPlayerComponent.getMediaPlayer().getLength());
		}
	}
	
	private void fireVideoErrorEvent(String message) {
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoError(message);
		}
	}
	
	private void fireVideoPercentThroughChangeEvent(int iPos) { 
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoPercentThroughChange(iPos);
		}
	}
	
	private void fireVideoPositionChangeEvent(long videoPositionChange) { 
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoPositionChange(videoPositionChange);
		}
	}
	
	public void pause() { 
		mediaPlayerComponent.getMediaPlayer().pause();
	}
	
	public void play() { 
		mediaPlayerComponent.getMediaPlayer().play();
	}
	
	public void skip(final long intervalInMiliseconds) { 
		mediaPlayerComponent.getMediaPlayer().skip(intervalInMiliseconds);
	}
	
	public void reset() { 
		skip(0);
		mediaPlayerComponent.getMediaPlayer().stop();
	}
	
	public void resetAll() {
		skip(0);
		mediaPlayerComponent.getMediaPlayer().stop();
	}

	@Override
	public void onPause() {
		pause();		
	}

	@Override
	public void onResume() {
		play();
	}

	@Override
	public void onStop() {
		pause();		
	}

	@Override
	public void onPlay(boolean play) {
		if (play) { 
			play();
		} else { 
			pause();
		}
	}
	
	@Override
	public void onSkip(final long intervalInMiliseconds) {
		skip(intervalInMiliseconds);
	}


	@Override
	public void onAreaChange(Area name) {}

	@Override
	public void onStart() {
		trial.setVideoTimeOffset(mediaPlayerComponent.getMediaPlayer().getTime() / 1000.0);
	}

}
