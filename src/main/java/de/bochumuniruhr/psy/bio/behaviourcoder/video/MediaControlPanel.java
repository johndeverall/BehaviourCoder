package de.bochumuniruhr.psy.bio.behaviourcoder.video;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;

@SuppressWarnings("serial")
public class MediaControlPanel extends JPanel implements TrialSectionListener, VideoListener {

	private JButton playButton;
	private JButton pauseButton;
	private JTextField seekField;
	private JLabel totalTime;
	private List<MediaControlListener> mediaControlListeners;
	private double videoLength;
	private DecimalFormat decimalFormatter;

	public MediaControlPanel() {

		setLayout(new GridLayout(1, 4));
		decimalFormatter = new DecimalFormat("0.00"); 
		
		
		playButton = new JButton("Play");
		playButton.setFont(new Font("Arial", Font.BOLD, 20));

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					firePlayEvent();
			}
		});
		add(playButton);

		pauseButton = new JButton("Pause");
		pauseButton.setFont(new Font("Arial", Font.BOLD, 20));
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					firePauseEvent();
			}
		});
		add(pauseButton);

		seekField = new JTextField("0");
		seekField.setHorizontalAlignment(JLabel.CENTER);
		seekField.setFont(new Font("Arial", Font.BOLD, 20));
		seekField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						fireSeekEvent();
					}
			}
		});
		add(seekField);

		totalTime = new JLabel("0");
		totalTime.setFont(new Font("Arial", Font.BOLD, 20));
		totalTime.setHorizontalAlignment(JLabel.CENTER);
		add(totalTime);

		mediaControlListeners = new ArrayList<MediaControlListener>();
		
		disableControls();
	}

	public void addMediaControlListener(MediaControlListener mediaControlListener) {
		mediaControlListeners.add(mediaControlListener);
	}

	private void firePlayEvent() {
		for (MediaControlListener mediaControlListener : mediaControlListeners) {
			mediaControlListener.onPlay();
		}
	}

	private void firePauseEvent() {
		for (MediaControlListener mediaControlListener : mediaControlListeners) {
			mediaControlListener.onPause();
		}
	}

	private void fireSeekEvent() {
		for (MediaControlListener mediaControlListener : mediaControlListeners) {
			if (isDouble(seekField.getText())) {
				double seekTo = Double.parseDouble(seekField.getText());
				mediaControlListener.onSeek(seekTo);
			}
		}
	}

	private static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
	
	private void disableControls() { 
		playButton.setEnabled(false);
		pauseButton.setEnabled(false);
		seekField.setEnabled(false);
	}
	
	private void enableControls() { 
		playButton.setEnabled(true);
		pauseButton.setEnabled(true);
		seekField.setEnabled(true);
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		this.videoLength = videoLength;
		totalTime.setText("" + videoLength);
		enableControls();
	}

	@Override
	public void onVideoPositionChange(double videoPosition) {
		// TODO Auto-generated method stub
	}

	public void resetAll() {
		seekField.setText("" + 0);
		enableControls();
	}

	@Override
	public void onAreaChange(Area name) {
	}
	
	@Override
	public void onTrialSectionStart() { 
		disableControls();
	}

	@Override
	public void onTrialSectionSuspend() {
	}

	@Override
	public void onTrialSectionResume() {
	}

	@Override
	public void timeIsUp() {
		enableControls();
	}

	@Override
	public void trialStopWatchUpdate(String trialTime) {

	}

	@Override
	public void onTimeLimitChange(Integer seconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoTimeChange(double videoTime) {
		videoTime = videoTime / 1000;
		String videoTimeString = decimalFormatter.format(videoTime);
		totalTime.setText(videoTimeString + " of " + videoLength);
	}

}