package de.bochumuniruhr.psy.bio.behaviourcoder.video;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;

@SuppressWarnings("serial")
public class MediaControlPanel extends JPanel implements TrialSectionListener, VideoListener {

	private PlayPauseButton playPauseButton;
	private JButton skipForward;
	private JButton skipBack;
	private JTextField skipField;
	private JLabel currentPosition;
	private List<MediaControlListener> mediaControlListeners;
	private JSlider seeker = new JSlider();
	private Logger logger = Logger.getLogger(this.getClass());
	
	public MediaControlPanel() {

		setLayout(new GridBagLayout());

		playPauseButton = new PlayPauseButton(this);
		playPauseButton.setFont(new Font("Arial", Font.BOLD, 20));

		GridBagConstraints playButtonConstraints = new GridBagConstraints();
		playButtonConstraints.gridx = 0;
		playButtonConstraints.gridy = 0;
		playButtonConstraints.gridwidth = 3;
		playButtonConstraints.gridheight = 3;
		playButtonConstraints.weightx = 0.1;
		playButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(playPauseButton, playButtonConstraints);
		
		GridBagConstraints currentPositionConstraints = new GridBagConstraints();
		currentPositionConstraints.gridx = 3;
		currentPositionConstraints.gridy = 0;
		currentPositionConstraints.gridwidth = 4;
		currentPositionConstraints.gridheight = 3;
		currentPositionConstraints.weightx = 0.1;
		currentPositionConstraints.fill = GridBagConstraints.HORIZONTAL;
		currentPosition = new JLabel("0");
		currentPosition.setFont(new Font("Arial", Font.BOLD, 20));
		currentPosition.setHorizontalAlignment(JLabel.CENTER);
		add(currentPosition, currentPositionConstraints);

		GridBagConstraints seekerConstraints = new GridBagConstraints();
		seekerConstraints.gridx = 7;
		seekerConstraints.gridy = 0;
		seekerConstraints.gridwidth = 16;
		seekerConstraints.gridheight = 3;
		seekerConstraints.weightx = 1;
		seekerConstraints.fill = GridBagConstraints.HORIZONTAL;
		seeker = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		add(seeker, seekerConstraints);

		GridBagConstraints skipBackConstraints = new GridBagConstraints();
		skipBackConstraints.gridx = 23;
		skipBackConstraints.gridy = 0;
		skipBackConstraints.gridwidth = 2;
		skipBackConstraints.gridheight = 3;
		skipBack = new JButton("<<");
		skipBack.setFont(new Font("Arial", Font.BOLD, 20));
		skipBack.setEnabled(false);
		skipBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = skipField.getText();
				if (isLong(text)) { 
					fireSkipEvent(Long.parseLong(text) * 1000 * -1);
				}
			}
		});
		add(skipBack, skipBackConstraints);
		
		GridBagConstraints skipIntervalConstraints = new GridBagConstraints();
		skipIntervalConstraints.gridx = 25;
		skipIntervalConstraints.gridy = 0;
		skipIntervalConstraints.gridwidth = 3;
		skipIntervalConstraints.gridheight = 3;
		skipIntervalConstraints.weightx = 0.1;
		skipIntervalConstraints.fill = GridBagConstraints.HORIZONTAL;
		skipField = new JTextField("0");
		skipField.setHorizontalAlignment(JLabel.CENTER);
		skipField.setFont(new Font("Arial", Font.BOLD, 20));
		add(skipField, skipIntervalConstraints);

		GridBagConstraints skipForwardConstraints = new GridBagConstraints();
		skipForwardConstraints.gridx = 28;
		skipForwardConstraints.gridy = 0;
		skipForwardConstraints.gridwidth = 2;
		skipForwardConstraints.gridheight = 3;
		skipForward = new JButton(">>");
		skipForward.setFont(new Font("Arial", Font.BOLD, 20));
		skipForward.setEnabled(false);
		skipForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = skipField.getText();
				if (isLong(text)) { 
					fireSkipEvent(Long.parseLong(text) * 1000);
				}
			}
		});
		add(skipForward, skipForwardConstraints);
		
		mediaControlListeners = new ArrayList<MediaControlListener>();

		disableControls();
	}

	public void addMediaControlListener(MediaControlListener mediaControlListener) {
		mediaControlListeners.add(mediaControlListener);
	}
	
	private void fireSkipEvent(long timeInMiliseconds) {
		for (MediaControlListener mediaControlListener : mediaControlListeners) { 
			mediaControlListener.onSkip(timeInMiliseconds);
		}
	}

	protected void firePlayEvent(boolean play) {
		if (play) { 
			skipForward.setEnabled(true);
			skipBack.setEnabled(true);
		} else { 
			skipForward.setEnabled(false);
			skipBack.setEnabled(false);
		}
		for (MediaControlListener mediaControlListener : mediaControlListeners) {
			mediaControlListener.onPlay(play);
		}
	}

	private static boolean isLong(String s) {
		try {
			Long.parseLong(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	private void disableControls() {
		playPauseButton.setEnabled(false);
		skipForward.setEnabled(false);
		skipBack.setEnabled(false);
		skipField.setEnabled(false);
	}

	private void enableControls() {
		playPauseButton.setEnabled(true);
		//skipForward.setEnabled(true);
		//skipBack.setEnabled(true);
		skipField.setEnabled(true);
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		enableControls();
	}

	@Override
	public void onVideoPositionChange(long videoPosition) {
		currentPosition.setText("" + videoPosition / 1000);
	}

	public void resetAll() {
		skipField.setText("" + 0);
		enableControls();
		playPauseButton.reset();
		seeker.setValue(0);
		currentPosition.setText("0");
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
	public void onVideoPercentThroughChange(int videoTime) {
		seeker.setValue(videoTime);
	}

}