package de.bochumuniruhr.psy.bio.behaviourcoder;

public interface TrialSectionListener {

	public void onAreaChange(Area name);

	public void onTrialSectionSuspend();
	
	public void onTrialSectionResume();
	
	public void timeIsUp(); 
}
