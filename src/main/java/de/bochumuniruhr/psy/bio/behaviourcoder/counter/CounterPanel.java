package de.bochumuniruhr.psy.bio.behaviourcoder.counter;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.bochumuniruhr.psy.bio.behaviourcoder.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSection;
import de.bochumuniruhr.psy.bio.behaviourcoder.TrialSectionListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.VideoListener;

@SuppressWarnings("serial")
public class CounterPanel extends JPanel implements GlobalKeyListener, TrialSectionListener, VideoListener {
	
	private ClickCounterButton pecks;
	private ClickCounterButton hackles;
	private ClickCounterButton attacks;
	private ClickCounterButton crouches;
	private StatusPanel statusBar;
	private Area currentArea;
	
	public CounterPanel(StatusPanel statusBar) { 
		
		this.statusBar = statusBar;
		
		setLayout(new GridLayout(4, 2));
		
		// movements
		JLabel label1 = new JLabel("Peck: ");
		label1.setFont(new Font("Arial", Font.BOLD, 20));
		label1.setHorizontalAlignment(JLabel.RIGHT);
		pecks = new ClickCounterButton("0", 's', 'c');
		pecks.setBackground(Color.YELLOW);		
		
		// social calls
		JLabel label2 = new JLabel("<html>Hackles: </html>");
		label2.setFont(new Font("Arial", Font.BOLD, 20));
		label2.setHorizontalAlignment(JLabel.RIGHT);
		hackles = new ClickCounterButton("0", 's', 'c');
		hackles.setBackground(Color.GREEN);
		
		// alarm calls
		JLabel label3 = new JLabel("<html>Attack: </html>");
		label3.setFont(new Font("Arial", Font.BOLD, 20));
		label3.setHorizontalAlignment(JLabel.RIGHT);
		attacks = new ClickCounterButton("0", 's', 'c');
		attacks.setBackground(Color.RED);
		
		// head bobs
		JLabel label4 = new JLabel("<html>Crouch: </html>");
		label4.setFont(new Font("Arial", Font.BOLD, 20));
		label4.setHorizontalAlignment(JLabel.RIGHT);
		crouches = new ClickCounterButton("0", 's', 'c');
		crouches.setBackground(Color.CYAN);
		
		// add everything
		add(label1);
		add(pecks);
		add(label2);
		add(hackles);
		add(label3);
		add(attacks);
		add(label4);
		add(crouches);
	}

	public void resetAll() {
		pecks.reset();
		hackles.reset();
		attacks.reset();
		crouches.reset();
	}

	public void populateTrial(TrialSection trial) {
		
		trial.setPecksClose(pecks.getCloseClicks());
		trial.setPecksFar(pecks.getFarClicks());
		trial.setHacklesClose(hackles.getCloseClicks());
		trial.setHacklesFar(hackles.getFarClicks());
		trial.setAttacksClose(attacks.getCloseClicks());
		trial.setAttacksFar(attacks.getFarClicks());
		trial.setCrouchesClose(crouches.getCloseClicks());
		trial.setCrouchesFar(crouches.getFarClicks());
		
		
	}

	public void keyPressed(char key) {
		pecks.keyPressed(key);
		hackles.keyPressed(key);
		attacks.keyPressed(key);
		crouches.keyPressed(key);
	}

	@Override
	public void onAreaChange(Area area) {
		pecks.onAreaChange(area);
		hackles.onAreaChange(area);
		attacks.onAreaChange(area);
		crouches.onAreaChange(area);
	}

	@Override
	public void onTrialSectionSuspend() {
		pecks.onTrialSectionSuspend();
		hackles.onTrialSectionSuspend();
		attacks.onTrialSectionSuspend();
		crouches.onTrialSectionSuspend();
	}

	@Override
	public void onTrialSectionResume() {
		pecks.onTrialSectionResume();
		hackles.onTrialSectionResume();
		attacks.onTrialSectionResume();
		crouches.onTrialSectionResume();		
	}

	@Override
	public void timeIsUp() {
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
		pecks.onVideoLoaded(videoLength);
		hackles.onVideoLoaded(videoLength);
		attacks.onVideoLoaded(videoLength);
		crouches.onVideoLoaded(videoLength);		
	}

	@Override
	public void onVideoPositionChange(long videoPosition) {
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
	public void onTrialSectionStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoPercentThroughChange(int videoTime) {
		// TODO Auto-generated method stub
		
	}

}
