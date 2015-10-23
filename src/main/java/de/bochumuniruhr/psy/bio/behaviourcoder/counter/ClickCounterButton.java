package de.bochumuniruhr.psy.bio.behaviourcoder.counter;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import javax.swing.JButton;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.VideoListener;

@SuppressWarnings("serial")
public class ClickCounterButton extends JButton implements GlobalKeyListener, TrialSectionListener, VideoListener {

	private Stack<Click> clicks;
	private char incrementKey;
	private char decrementKey;
	private Area currentArea;
	private boolean suspended = true;
	private boolean videoLoaded = false;
	
	public ClickCounterButton(String startLabel, final char incrementKey, final char decrementKey) { 
		this.incrementKey = incrementKey;
		this.decrementKey = decrementKey;
		
		addMouseListener(new MouseAdapter() { 
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!suspended && videoLoaded) { 
					if (e.getButton() ==  MouseEvent.BUTTON1) { 
						increment();
					} else if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) { 
						decrement();
					}
				}
			}
		});
		
		setText(startLabel);
		setFont(new Font("Arial", Font.BOLD, 60));
		
		clicks = new Stack<Click>();
		
	}
	
	private void increment() {
		clicks.add(new Click(currentArea));
		setText("" + clicks.size());
		SoundMaker.playMouseClick();
	}
	
	private void decrement() {
		if (clicks.isEmpty() == false) { 
			clicks.pop();
		}
		setText("" + clicks.size());
		SoundMaker.playMouseClick();
	}
	
	public void reset() { 
		clicks.clear();
		setText("" + clicks.size());
		onTrialSectionSuspend();
	}
	
	@Override
	public void keyPressed(char key) { 
		if (!suspended && videoLoaded) { 
			if (key == incrementKey) { 
				increment();
			} else if (key == decrementKey) { 
				decrement();
			}
		}
	}

	public void onAreaChange(Area area) {
		this.currentArea = area;
	}

	@Override
	public void onTrialSectionSuspend() {
		this.suspended = true;
	}

	@Override
	public void onTrialSectionResume() {
		this.suspended = false;
	}

	@Override
	public void timeIsUp() {
		onTrialSectionSuspend();
	}

	public Integer getCloseClicks() {
		int closeClicks = 0;
		for (Click click : clicks) { 
			if (click.getArea() == Area.CLOSE) { 
				closeClicks++;
			} 
		}
		return closeClicks;
	}
	
	public Integer getFarClicks() { 
		int farClicks = 0;
		for (Click click : clicks) { 
			if (click.getArea() == Area.FAR) { 
				farClicks++;
			}
		}
		return farClicks;
	}

	@Override
	public void trialStopWatchUpdate(String trialTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeLimitChange(Integer seconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		this.videoLoaded = true;
		reset();
	}

	@Override
	public void onVideoPositionChange(long videoPosition) {
		
	}

	@Override
	public void onVideoStart() {
		
	}

	@Override
	public void onVideoStop() {
		
	}

	@Override
	public void onTrialSectionStart() {
		
	}

	@Override
	public void onVideoError(String message) {
		
	}

	@Override
	public void onVideoPercentThroughChange(int videoTime) {
		
	}
}
