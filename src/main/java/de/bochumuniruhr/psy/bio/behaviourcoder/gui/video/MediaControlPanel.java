package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialListener;

/**
 * Panel for controlling videos displayed by a video panel. Only allows interaction when the trial is not active.
 */
@SuppressWarnings("serial")
public class MediaControlPanel extends JPanel implements TrialListener, VideoListener {

	/**
	 * The button to start/pause the video
	 */
	private PlayPauseButton playPauseButton;
	
	/**
	 * The button for skipping forward.
	 */
	private JButton skipForward;
	
	/**
	 * The button for skipping backward.
	 */
	private JButton skipBack;
	
	/**
	 * The field for choosing how many seconds to skip by.
	 */
	private JFormattedTextField skipField;
	
	/**
	 * The display for the current position in the video in seconds.
	 */
	private JLabel currentPosition;
	
	/**
	 * The slider to show progress in the video.
	 */
	private JSlider seeker;
	
	/**
	 * The list of listeners.
	 */
	private List<MediaControlListener> mediaControlListeners;
	
	/**
	 * Creates a media control panel.
	 */
	public MediaControlPanel() {
		mediaControlListeners = new ArrayList<MediaControlListener>();
		
		//The font all the labels use
		Font labelFont = new Font("Arial", Font.BOLD, 20);

		//Set the layout
		setLayout(new GridBagLayout());

		//Create the play button
		playPauseButton = new PlayPauseButton(this);
		playPauseButton.setFont(labelFont);
		add(playPauseButton, createConstraints(0, 0, 3, 3, GridBagConstraints.HORIZONTAL, 0.1));
		
		//Create the position label
		currentPosition = new JLabel("0");
		currentPosition.setFont(labelFont);
		currentPosition.setHorizontalAlignment(JLabel.CENTER);
		add(currentPosition, createConstraints(3, 0, 4, 3, GridBagConstraints.HORIZONTAL, 0.1));

		//Create the seeker
		seeker = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		add(seeker, createConstraints(7, 0, 16, 3, GridBagConstraints.HORIZONTAL, 1));

		//Create the button to skip back
		skipBack = new JButton("<<");
		skipBack.setFont(labelFont);
		skipBack.setEnabled(false);
		skipBack.addActionListener(new SkipListener(false));
		add(skipBack, createConstraints(23, 0, 2, 3, GridBagConstraints.HORIZONTAL, 0));
		
		//Create the field to specify the amount to skip by in seconds
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);
		skipField = new JFormattedTextField(format);
		skipField.setFont(labelFont);
		skipField.setHorizontalAlignment(JLabel.CENTER);
		skipField.setValue(0);
		skipField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				//Coerce the fields value to be an integer
				try {
					skipField.commitEdit();
				} catch (ParseException e1) {
				}
				System.out.println(skipField.getValue());
			}
		});
		add(skipField, createConstraints(25, 0, 3, 3, GridBagConstraints.HORIZONTAL, 0.1));

		//Create the button to skip forward
		skipForward = new JButton(">>");
		skipForward.setFont(labelFont);
		skipForward.setEnabled(false);
		skipForward.addActionListener(new SkipListener(true));
		add(skipForward, createConstraints(28, 0, 2, 3, GridBagConstraints.HORIZONTAL, 0));
		
		//Initially have the controls disabled
		disableControls();
	}

	/**
	 * Adds a listener for media control events.
	 * 
	 * @param mediaControlListener - the listener to add
	 */
	public void addMediaControlListener(MediaControlListener mediaControlListener) {
		mediaControlListeners.add(mediaControlListener);
	}

	/**
	 * Changes which buttons are enabled and informs listeners that the video will be played or stopped.
	 * 
	 * @param play - whether the video is to be played or stopped. When true the skipping buttons are enabled,
	 * 		otherwise they are disabled
	 */
	protected void firePlayEvent(boolean play) {
		if (play) { 
			//If playing allow the video to be skipped
			skipForward.setEnabled(true);
			skipBack.setEnabled(true);
		} else { 
			//Otherwise disable them
			skipForward.setEnabled(false);
			skipBack.setEnabled(false);
		}
		//Infrom the listeners
		for (MediaControlListener mediaControlListener : mediaControlListeners) {
			mediaControlListener.onPlay(play);
		}
	}
	
	/**
	 * Disables the controls.
	 */
	private void disableControls() {
		playPauseButton.setEnabled(false);
		skipForward.setEnabled(false);
		skipBack.setEnabled(false);
		skipField.setEnabled(false);
	}

	/**
	 * Enables the controls.
	 */
	private void enableControls() {
		playPauseButton.setEnabled(true);
		skipForward.setEnabled(true);
		skipBack.setEnabled(true);
		skipField.setEnabled(true);
	}
	
	/**
	 * Creates constraints for placing elements in the UI.
	 * 
	 * @param x - the x position in the grid
	 * @param y - the y position in the grid
	 * @param width - how many cells wide the element will be
	 * @param height - how many cells high the element will be
	 * @param fill - the GridBagConstraints value for how the element will expand
	 * @param weight - the amount that extra space will be given to the element
	 * @return The created constraints with the given values.
	 */
	private GridBagConstraints createConstraints(int x, int y, int width, int height, int fill, double weight){
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = x;
		cons.gridy = y;
		cons.gridheight = height;
		cons.gridwidth = width;
		cons.fill = fill;
		cons.weightx = weight;
		return cons;
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		enableControls();
	}

	@Override
	public void onVideoPositionChange(long videoPosition) {
		currentPosition.setText("" + videoPosition / 1000);
	}

	@Override
	public void onReset() {
		skipField.setText("0");
		disableControls();
		playPauseButton.reset();
		seeker.setValue(0);
		currentPosition.setText("0");
	}

	@Override
	public void onLocationChange(Location newLocation) {}

	@Override
	public void onVideoPercentThroughChange(int videoTime) {
		seeker.setValue(videoTime);
	}

	@Override
	public void onStop() {
		enableControls();
		playPauseButton.setPlaying(false);
	}

	@Override
	public void onPause() {
		playPauseButton.setPlaying(false);
	}

	@Override
	public void onResume() {
		playPauseButton.setPlaying(true);
	}

	@Override
	public void onStart() {
		disableControls();
		playPauseButton.setPlaying(true);
	}
	
	/**
	 * Listener for the skip buttons.
	 */
	private class SkipListener implements ActionListener {
		
		/**
		 * The modifier to make the skip positive or negative.
		 */
		private int modifier;
		
		/**
		 * Creates a listener that informs the media listeners of a skip.
		 * @param forwards - whether the skips are forwards or backwards. Forward skips are true.
		 */
		SkipListener(boolean forwards){
			modifier = (forwards) ? 1 : -1;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//Get value
			Object value = skipField.getValue();
			long skip = (value instanceof Long) ? (long) value : (long) (int) value;
			
			//Inform listeners of the skip in milliseconds
			for (MediaControlListener mediaControlListener : mediaControlListeners) { 
				mediaControlListener.onSkip(skip * 1000 * modifier);
			}
		}
	}

}