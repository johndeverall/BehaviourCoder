package de.bochumuniruhr.psy.bio.timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CounterPanel extends JPanel implements GlobalKeyListener {
	
	private ClickCounterButton movements;
	private ClickCounterButton socialCalls;
	private ClickCounterButton alarmCalls;
	private ClickCounterButton headBobs;
	private StatusPanel statusBar;
	
	public CounterPanel(StatusPanel statusBar) { 
		
		this.statusBar = statusBar;
		
		setLayout(new GridLayout(4, 2));
		
		// movements
		JLabel label1 = new JLabel("MOVEMENTS:");
		label1.setFont(new Font("Arial", Font.BOLD, 20));
		movements = new ClickCounterButton("0", 's', 'c');
		movements.setBackground(Color.YELLOW);		
		
		// social calls
		JLabel label2 = new JLabel("<html>SOCIAL<br>CALLS:</html>");
		label2.setFont(new Font("Arial", Font.BOLD, 20));
		socialCalls = new ClickCounterButton("0", 's', 'c');
		socialCalls.setBackground(Color.GREEN);
		
		// alarm calls
		JLabel label3 = new JLabel("<html>ALARM<br>CALLS:</html>");
		label3.setFont(new Font("Arial", Font.BOLD, 20));
		alarmCalls = new ClickCounterButton("0", 's', 'c');
		alarmCalls.setBackground(Color.RED);
		
		// head bobs
		JLabel label4 = new JLabel("<html>HEAD<br>BOBS:</html>");
		label4.setFont(new Font("Arial", Font.BOLD, 20));
		headBobs = new ClickCounterButton("0", 's', 'c');
		headBobs.setBackground(Color.CYAN);
		
		// add everything
		add(label1);
		add(movements);
		add(label2);
		add(socialCalls);
		add(label3);
		add(alarmCalls);
		add(label4);
		add(headBobs);
	}

	public void resetAll() {
		movements.reset();
		socialCalls.reset();
		alarmCalls.reset();
		headBobs.reset();
	}

	public void populateTrial(Trial trial) {
		trial.setAlarmCalls(Integer.parseInt(alarmCalls.getText()));
		trial.setHeadBobs(Integer.parseInt(headBobs.getText()));
		trial.setSocialCalls(Integer.parseInt(socialCalls.getText()));
		trial.setMovements(Integer.parseInt(movements.getText()));
	}

	public void keyPressed(char key) {
		movements.keyPressed(key);
		socialCalls.keyPressed(key);
		alarmCalls.keyPressed(key);
		headBobs.keyPressed(key);
	}

}
