package de.bochumuniruhr.psy.bio.behaviourcoder.video;

import javafx.embed.swing.JFXPanel;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Scene;
import jxl.common.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSection;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.StatusPanel;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.media.Media;

public class VLCVideoPanel extends JPanel implements TrialSectionListener, MediaControlListener {

	private List<VideoListener> videoListeners;
	private boolean videoLoaded = false;
	private long trialSectionStart;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public VLCVideoPanel() { 
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
		this.videoLoaded = true;
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
	public void onTrialSectionSuspend() {
		pause();		
	}

	@Override
	public void onTrialSectionResume() {
		play();
	}

	@Override
	public void timeIsUp() {
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
	public void trialStopWatchUpdate(String trialTime) {
		//fireVideoTimeChangeEvent();
	}

	@Override
	public void onTimeLimitChange(Integer seconds) {
	}

	@Override
	public void onAreaChange(Area name) {
	}

	@Override
	public void onTrialSectionStart() {
		this.trialSectionStart = mediaPlayerComponent.getMediaPlayer().getTime() / 1000;
	}

	public void populateTrial(TrialSection trial) {
		trial.setTrialSectionStart(trialSectionStart);
	}

}
