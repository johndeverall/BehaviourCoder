package de.bochumuniruhr.psy.bio.timer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ClickCounterButton extends JButton implements GlobalKeyListener {

	private int numberOfClicks = 0;
	private char incrementKey;
	private char decrementKey;
	
	public ClickCounterButton(String startLabel, final char incrementKey, final char decrementKey) { 
		this.incrementKey = incrementKey;
		this.decrementKey = decrementKey;
		
		// addActionListener(this);
		addMouseListener(new MouseAdapter() { 
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() ==  MouseEvent.BUTTON1) { 
					increment();
				} else if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) { 
					decrement();
				}
			}
		});
		
		setText(startLabel);
		setFont(new Font("Arial", Font.BOLD, 60));
		
	}
	
	private void increment() {
		numberOfClicks++;
		setText("" + numberOfClicks);
		SoundMaker.playMouseClick();
	}
	
	private void decrement() {
		if (numberOfClicks > 0) { 
			numberOfClicks--;
			setText("" + numberOfClicks);
			SoundMaker.playMouseClick();
		}
	}
	
	public void reset() { 
		numberOfClicks = 0;
		setText("" + numberOfClicks);
	}
	
	public void keyPressed(char key) { 
		if (key == incrementKey) { 
			increment();
		} else if (key == decrementKey) { 
			decrement();
		}
	}
}
