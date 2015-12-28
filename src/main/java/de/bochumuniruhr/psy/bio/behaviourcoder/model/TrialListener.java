package de.bochumuniruhr.psy.bio.behaviourcoder.model;

public interface TrialListener {
	
	public void onAreaChange(Area newArea);

	public void onStart();
	
	public void onStop();
	
	public void onPause();
	
	public void onResume();
	
}