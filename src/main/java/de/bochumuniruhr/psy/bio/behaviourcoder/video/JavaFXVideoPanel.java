package de.bochumuniruhr.psy.bio.behaviourcoder.video;

import javafx.embed.swing.JFXPanel;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSection;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.StatusPanel;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.media.*;
import javafx.stage.Stage;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class JavaFXVideoPanel extends JFXPanel implements TrialSectionListener, MediaControlListener {

	private MediaPlayer mediaPlayer;
	private Stage stage;
	private ReadOnlyObjectProperty<Duration> totalDuration;
	private List<VideoListener> videoListeners;
	private boolean videoLoaded = false;
	private double trialSectionStart;
	
	public JavaFXVideoPanel() { 
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Scene scene = new Scene(new StackPane(), Color.BLACK);
				setScene(scene);
			} });
		
		videoListeners = new ArrayList<VideoListener>();
	}

	public void openVideo(final File file, StatusPanel statusBar) {
		Platform.runLater(new Runnable() {

			private Duration totalDuration2;

			@Override
			public void run() {
				
				stage = new Stage();
				stage.setResizable(true);
				
				StackPane root = new StackPane();
				mediaPlayer = new MediaPlayer(new Media(file.toURI().toString()));
				mediaPlayer.setOnError(new Runnable(){
					@Override
					public void run() {
						fireVideoErrorEvent(mediaPlayer.getError().getMessage());
					}
					});
				MediaView mediaView = new MediaView(mediaPlayer);
				mediaView.setPreserveRatio(true);
				mediaView.fitWidthProperty();
				mediaView.fitHeightProperty();
				root.getChildren().add(mediaView);
				StackPane.setAlignment(mediaView, Pos.CENTER);
				Scene scene = new Scene(root);
				scene.setFill(Color.BLACK);
				setScene(scene);
				
				mediaPlayer.setOnReady(new Runnable() {
					@Override
					public void run() {
						fireVideoLoadedEvent();
					} });
			}});
	}
	
	public void addVideoListener(VideoListener videoListener) { 
		this.videoListeners.add(videoListener);
	}
		
	private void fireVideoLoadedEvent() {
		this.videoLoaded = true;
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoLoaded(mediaPlayer.getTotalDuration().toSeconds());
		}
	}
	
	private void fireVideoErrorEvent(String message) {
		for (VideoListener videoListener : videoListeners) { 
			videoListener.onVideoError(message);
		}
	}
	
	private void fireVideoTimeChangeEvent() { 
		if (videoLoaded) { 
			for (VideoListener videoListener : videoListeners) { 
				videoListener.onVideoTimeChange(mediaPlayer.getCurrentTime().toMillis());
			}
		}
	}
	
	public void pause() { 
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.pause();
			} 
		});
	}
	
	public void play() { 
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.play();
			} 
		});
	}
	
	public void seek(final double positionInSeconds) { 
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Duration duration = new Duration(positionInSeconds * 1000);
				mediaPlayer.seek(duration);
			} 
		});
	}
	
	public void reset() { 
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.stop();
				seek(0);
			} 
		});
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
	public void onPlay() {
		play();
	}
	
	@Override
	public void onPause() {
		pause();
	}

	@Override
	public void onSeek(final double positionInSeconds) {
		seek(positionInSeconds);
	}

	@Override
	public void trialStopWatchUpdate(String trialTime) {
		fireVideoTimeChangeEvent();
	}

	@Override
	public void onTimeLimitChange(Integer seconds) {
	}

	@Override
	public void onAreaChange(Area name) {
	}

	public void resetAll() {
		pause();
		seek(0);
	}

	@Override
	public void onTrialSectionStart() {
		this.trialSectionStart = mediaPlayer.getCurrentTime().toSeconds();
	}

	public void populateTrial(TrialSection trial) {
		trial.setTrialSectionStart(trialSectionStart);
	}

}
