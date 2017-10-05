package de.bochumuniruhr.psy.bio.behaviourcoder.model;

/**
 * Specifies a listener for a trial.
 */
public interface TrialListener {
	
	/**
	 * Called when the location of the subject has changed.
	 * 
	 * @param newLocation - the new location of the subject
	 */
	public void onTrialLocationChange(Location newLocation);

	/**
	 * Called when the trial has started.
	 */
	public void onTrialStart();
	
	/**
	 * Called when the trial has stopped.
	 */
	public void onTrialStop();
	
	/**
	 * Called when the trial has paused.
	 */
	public void onTrialPause();
	
	/**
	 * Called when the trial has resumed.
	 */
	public void onTrialResume();
	
	/**
	 * Called when the trial has reset.
	 * All listeners must also reset to their initial state when this is called.
	 */
	public void onTrialReset();
	
}
