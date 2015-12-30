package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * Button for playing or pausing a video.
 */
@SuppressWarnings("serial")
public class PlayPauseButton extends JButton {

	/**
	 * Whether the video is playing.
	 */
	private boolean playing;
	
	/**
	 * Creates a button for playing and pausing video.
	 * 
	 * @param parent - the control panel the button will belong to
	 */
	public PlayPauseButton(final MediaControlPanel parent) { 
		//Setup the button with no video playing
		final PlayPauseButton that = this;
		playing = false;
		setText("Play");
		
		//Create a listener to toggle the state
		addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//If not enabled do not continue
				if (!that.isEnabled()) return;
				
				//If currently playing, then the press should pause
				if (playing) { 
					parent.firePlayEvent(false);
					setText("Play");
					playing = false;
				} else { 
					//Otherwise it should play
					parent.firePlayEvent(true);
					setText("Pause");
					playing = true;
				}
				
			}});
	}
	
	/**
	 * Sets whether the button should play or pause.
	 * 
	 * @param playing - whether the video is currently playing. When true the button should pause,
	 * 		otherwise it should play
	 */
	void setPlaying(boolean playing){
		this.playing = playing;
		setText((playing) ? "Pause" : "Play");
	}
	
	/**
	 * Resets this button to its initial state.
	 */
	void reset() { 
		this.playing = false;
		setText("Play");
	}
}
