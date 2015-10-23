package de.bochumuniruhr.psy.bio.behaviourcoder.video;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class PlayPauseButton extends JButton {

	private boolean playing = false;
	
	public PlayPauseButton(final MediaControlPanel parent) { 

		setText("Play");
		
		addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
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
	
	public void reset() { 
		this.playing = false;
		setText("Play");
	}
}
