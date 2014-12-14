package de.bochumuniruhr.psy.bio.timer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;

import org.apache.commons.lang3.time.StopWatch;

@SuppressWarnings("serial")
public class TimerButton extends JButton implements ActionListener {

	private StopWatch stopWatch;
	private TimerMediator mediator;
	private DecimalFormat decimalFormatter;
	private boolean lastClicked;
	
	public TimerButton(TimerMediator mediator) { 
		this.mediator = mediator;
		stopWatch = new StopWatch();
		addActionListener(this);
		setFont(new Font("Arial", Font.BOLD, 30));
		decimalFormatter = new DecimalFormat("0.00");
	}
	
	public void stop() { 
		stopWatch.stop();
	}
	
	public void updateText() { 
		Double timeInSeconds = (double) stopWatch.getTime() / 1000.00;
		super.setText(decimalFormatter.format(timeInSeconds));
	}
	
	public void suspendIfNotStopped() { 
		if (stopWatch.isStopped() || stopWatch.isSuspended()) { 
			// do nothing
		} else { 
			stopWatch.suspend();
		}
	}
	
	public void startResumeOrSuspend() { 
		if (stopWatch.isStopped()) { 
			stopWatch.start();
		} else if (stopWatch.isSuspended()) { 
			stopWatch.resume();
		} else { 
			stopWatch.suspend();
		} 
	}

	public void actionPerformed(ActionEvent e) {
		mediator.enable(this);
		SoundMaker.playMouseClick();
		lastClicked = true;
	}
	
	public void reset() { 
		stopWatch.reset();
	}
	
	public boolean isStopped() { 
		return stopWatch.isStopped();
	}
	
	public boolean isSuspended() { 
		return stopWatch.isSuspended();
	}
	
	public boolean isStarted() { 
		return stopWatch.isStarted();
	}

	public boolean isLastClicked() {
		return lastClicked;
	}

	public void setLastClicked(boolean lastClicked) {
		this.lastClicked = lastClicked;
	}
}
