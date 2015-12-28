package de.bochumuniruhr.psy.bio.behaviourcoder.gui.video;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class PlayPauseButton extends JButton {

	private boolean playing = false;
	
	public PlayPauseButton(final MediaControlPanel parent) { 
		final PlayPauseButton that = this;
		setText("Play");
		
		addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!that.isEnabled()) return;
				if (playing) { 
					parent.firePlayEvent(false);
					setText("Play");
					playing = false;
				} else { 
					parent.firePlayEvent(true);
					setText("Pause");
					playing = true;
				}
				
			}});
	}
	
	public void setPlaying(boolean playing){
		this.playing = playing;
		setText((playing) ? "Pause" : "Play");
	}
	
	public void reset() { 
		this.playing = false;
		setText("Play");
	}
}
