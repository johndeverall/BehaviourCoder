package de.bochumuniruhr.psy.bio.behaviourcoder;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.bochumuniruhr.psy.bio.behaviourcoder.details.ValidatingTextField;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel implements GlobalKeyListener, TrialSectionListener {

	private JLabel totalTime;
	private ValidatingTextField timeLimit;
	private JLabel transitions;
	private int transitionCount = 0;
	private final int LABEL_FONT_SIZE = 30;
	private final int DEFAULT_TIME_LIMIT;
	private List<TrialSectionListener> trialSectionListeners;
	private Area currentArea;
	
	public InfoPanel(int defaultTimeLimit) { 
		
		this.DEFAULT_TIME_LIMIT = defaultTimeLimit;
	
		trialSectionListeners = new ArrayList<TrialSectionListener>();
		
		this.setLayout(new GridLayout(1, 3));
		
		totalTime = new JLabel("0");
		totalTime.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		totalTime.setHorizontalAlignment(JLabel.CENTER);
		add(totalTime);
		
		timeLimit = new ValidatingTextField("" + DEFAULT_TIME_LIMIT, "You must enter a time limit");
		timeLimit.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		timeLimit.setHorizontalAlignment(JLabel.CENTER);
		timeLimit.addKeyListener(new KeyAdapter(){

			@Override
			public void keyReleased(KeyEvent e) {
				fireTimeLimitChangeEvent(timeLimit.getText());
			}
		});
		
		add(timeLimit);
		
		transitions = new JLabel("0");
		transitions.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
		transitions.setHorizontalAlignment(JLabel.CENTER);
		add(transitions);
	
	}
	
	public void resetAll() {
		totalTime.setText("0.00");
		timeLimit.setText("" + DEFAULT_TIME_LIMIT);
		transitions.setText("0");
		transitionCount = 0;
	}
	
	private void fireTimeLimitChangeEvent(String newTime) {
		for (TrialSectionListener trialSectionListener : trialSectionListeners) { 
			
			if (newTime.length() < 7 && newTime.length() > 0 && isInteger(newTime)) { 
				trialSectionListener.onTimeLimitChange(Integer.parseInt(newTime));
			}
		}
	}
	
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public void addTrialSectionListener(TrialSectionListener trialSectionListener) { 
		this.trialSectionListeners.add(trialSectionListener);
	}

	@Override
	public void onAreaChange(Area area) {
		if (this.currentArea != null) { 
			transitionCount++;
		}
		this.currentArea = area;
		transitions.setText("" + transitionCount);
	}

	@Override
	public void onTrialSectionSuspend() {
	}

	@Override
	public void onTrialSectionResume() {
	}

	@Override
	public void timeIsUp() {
	}

	@Override
	public void keyPressed(char key) {
	}

	@Override
	public void trialStopWatchUpdate(String trialTime) {
		totalTime.setText("" + trialTime);
	}

	@Override
	public void onTimeLimitChange(Integer seconds) {
		// TODO Auto-generated method stub
		
	}

	public void populateTrial(TrialSection trial) {
		trial.setLocationChanges(transitionCount);
	}

	@Override
	public void onTrialSectionStart() {
		// TODO Auto-generated method stub
		
	}
	
}