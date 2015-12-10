package de.bochumuniruhr.psy.bio.behaviourcoder.gui.timer;

import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.VideoListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Area;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialSectionListener;

public interface TimerButton extends TrialSectionListener, VideoListener, GlobalKeyListener {

	public boolean isLastClicked();

	public void setLastClicked(boolean wasLastClicked);

	public void startResumeOrSuspend();

	public void suspendIfNotStoppedOrSuspended();

	public void reset();

	public boolean isSuspended();

	public boolean isStopped();

	public void updateText();

	public void stop();

	public boolean isStarted();
	
	public void setEnabled(boolean b);
	
	public String getText();
	
	public void onAreaChange(Area name);
	
	public String getButtonName();
	
	public boolean isCounting();
	
}
