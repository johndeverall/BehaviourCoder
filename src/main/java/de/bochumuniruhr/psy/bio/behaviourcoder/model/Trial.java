package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.time.StopWatch;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.VideoListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails.Constraint;

/**
 * Represents a trial consisting of locations, instant and timed behaviours.
 */
public class Trial implements VideoListener {
	
	/**
	 * The list of locations.
	 */
	private List<Location> locations;
	
	/**
	 * The list of timed behaviours.
	 */
	private List<TimedBehaviour> timed;
	
	/**
	 * The list of instant behaviours.
	 */
	private List<InstantBehaviour> instant;
	
	/**
	 * The details of this trial.
	 */
	private TrialDetails details;
	
	/**
	 * The last location before the trial paused.
	 */
	private Location beforePause;
	
	/**
	 * The location the subject is currently in.
	 */
	private Location currentLocation;
	
	/**
	 * The map of locations to the stop watches tracking how long they were current.
	 */
	private Map<Location, StopWatch> locationTimes;
	
	/**
	 * The number of transitions between locations.
	 */
	private int locationChanges;

	/**
	 * The stop watch tracking the total time of this trial.
	 */
	private StopWatch time;
	
	/**
	 * Whether this trial is ready to start.
	 */
	private boolean ready;
	
	/**
	 * The list of listeners for this trial.
	 */
	private List<TrialListener> listeners;
	
	/**
	 * Creates a trial.
	 * 
	 * @param duration - the initial duration the trial will run for
	 * @param areas - the list of areas in the trial
	 * @param detailNames - the names of the details of the trial
	 * @param detailConstraints - the constraints of those details. Constraints are matched to the names by index.
	 */
	public Trial(long duration, List<Location> areas, List<String> detailNames, List<Constraint> detailConstraints){
		details = new TrialDetails(duration, detailNames, detailConstraints);
		this.locations = areas;
		timed = new ArrayList<TimedBehaviour>();
		instant = new ArrayList<InstantBehaviour>();
		locationChanges = 0;
		time = new StopWatch();
		locationTimes = new HashMap<Location, StopWatch>();
		listeners = new ArrayList<TrialListener>();
		ready = false;
		
		setupTimeLimitCheck();
	}
	
	/**
	 * Starts this trial if ready.
	 */
	public void start() {
		if (ready){
			time.start();
			for (TrialListener ls : listeners){
				ls.onStart();
			}
		}
	}
	
	/**
	 * Pauses this trial if it is running.
	 */
	public void pause(){
		if (time.isStarted() && !time.isSuspended()){
			time.suspend();
			if (currentLocation != null){
				locationTimes.get(currentLocation).suspend();
			}
			fireOnPause();
		}
	}
	
	/**
	 * Resumes this trial if it is paused.
	 */
	public void resume(){
		if (time.isSuspended()){
			time.resume();
			if (currentLocation != null){
				locationTimes.get(currentLocation).resume();
			}
			fireOnResume();
		}
	}
	
	/**
	 * Resets this trial to its initial state.
	 */
	public void reset() {
		for (StopWatch watch : locationTimes.values()){
			watch.stop();
		}
		locationTimes.clear();
		time.reset();
		ready = false;
		for (InstantBehaviour behaviour : instant){
			behaviour.reset();
		}
		
		for (TrialListener listener : listeners){
			listener.onReset();
		}
	}
	
	/**
	 * Checks whether the trial is currently running.
	 * 
	 * @return Whether the trial is running.
	 */
	public boolean isRunning(){
		return time.isStarted() && !time.isSuspended() && !time.isStopped();
	}
	
	/**
	 * Checks whether the trial is active.
	 * 
	 * @return Whether the trial has been started.
	 */
	public boolean isActive(){
		return time.isStarted();
	}
	
	/**
	 * Gets the current time of the trial.
	 * 
	 * @return The current time.
	 */
	public long getCurrentTime(){
		return time.getTime();
	}
	
	/**
	 * Gets the time that a location has been current.
	 * @param location - the location to get the time for
	 * @return The time for which the location was current.
	 */
	public long getLocationTime(Location location){
		if (locationTimes.containsKey(location)){
			return locationTimes.get(location).getTime();
		} 
		return 0;
	}
	
	/**
	 * Adds a timed behaviour to this trial, including it as a listener.
	 * 
	 * @param t - the behaviour to add
	 */
	public void addTimedBehaviour(TimedBehaviour t){
		timed.add(t);
		listeners.add(t);
	}
	
	/**
	 * Adds an instant behaviour to this trial.
	 * 
	 * @param i - the behaviour to add
	 */
	public void addInstantBehaviour(InstantBehaviour i){
		instant.add(i);
	}
	
	/**
	 * Adds a listener to the trial.
	 * 
	 * @param listener - the listener to add
	 */
	public void addListener(TrialListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Sets the current location. If this trial has not already started, then it will be. A null location will pause the trial.
	 * 
	 * @param location - the new location
	 */
	public void setCurrentLocation(Location location){
		//If the location is the first location
		if (location != null && !time.isStarted()) {
			//Start the trial
			start();
		//Otherwise if it is a location when paused
		} else if (location != null && time.isSuspended()){
			//Then resume
			time.resume();
		}
		
		//Ignore the change if it is not different than the current location
		if (currentLocation != location){
			//Pause the current location's timer if it exists
			if (currentLocation != null){
				locationTimes.get(currentLocation).suspend();
			} else {
				//And if it doesn't then the trial has resumed
				fireOnResume();
			}
			
			//If the location is a not the same as before a pause
			if (location != null && (currentLocation != null || beforePause != location)){
				//Then count it as a transition
				++locationChanges;
			} else {
				//Otherwise record the current location as the previous before the pause
				beforePause = currentLocation;
			}
			//Update the current location
			currentLocation = location;
			
			//If the location was null
			if (location == null){
				//Then pause the trial
				time.suspend();
				fireOnPause();
			//Otherwise if it is the first time that location is current
			} else if (!locationTimes.containsKey(location)){
				//Create and start a watch for it
				StopWatch watch = new StopWatch();
				locationTimes.put(location, watch);
				watch.start();
			} else {
				//Otherwise resume the existing one
				locationTimes.get(location).resume();
			}
			
			//Inform listeners of the change of location
			for (TrialListener t : listeners){
				t.onLocationChange(location);
			}
		}
	}
	
	/**
	 * Gets the details of this trial.
	 * 
	 * @return The details.
	 */
	public TrialDetails getDetails(){
		return details;
	}
	
	/**
	 * Gets the current location of the subject within this trial.
	 * 
	 * @return The current location.
	 */
	public Location getCurrentLocation(){
		return currentLocation;
	}
	
	/**
	 * Gets the number of changes to the location.
	 * 
	 * @return The number of transitions between locations.
	 */
	public int getNumberOfLocationChanges(){
		return locationChanges;
	}
	
	/**
	 * Gets the list of locations.
	 * 
	 * @return The list of locations.
	 */
	public List<Location> getLocations(){
		return locations;
	}
	
	/**
	 * Gets the list of timed behaviours.
	 * 
	 * @return The list of timed behaviours.
	 */
	public List<TimedBehaviour> getTimedBehaviours(){
		return timed;
	}
	
	/**
	 * The list of instant behaviours.
	 * 
	 * @return The list of instant behaviours.
	 */
	public List<InstantBehaviour> getInstantBehaviours(){
		return instant;
	}
	
	/**
	 * Checks whether this trial is ready to start.
	 * 
	 * @return Whether this trial is ready.
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Inform listeners that this trial has paused.
	 */
	private void fireOnPause(){
		for (TrialListener ls : listeners){
			ls.onPause();
		}
	}
	
	/**
	 * Inform listeners that this trial has resumed.
	 */
	private void fireOnResume(){
		for (TrialListener ls : listeners){
			ls.onResume();
		}
	}
	
	/**
	 * Setup the clock to check for when the trial ends.
	 */
	private void setupTimeLimitCheck() { 
		//Create the task to check whether the trial has ended
		TimerTask clockCheck = new TimerTask() {
			@Override
			public void run() {
				//If time has ran out when the trial was ready
				if (time.getTime() > details.getDuration() * 1000 && ready){
					//Stop the trial
					ready = false;
					
					//Suspend the current timers (note not stop so getTime etc still work)
					time.suspend();
					if (currentLocation != null){
						locationTimes.get(currentLocation).suspend();
					}
					
					//Inform listeners of the end
					for (TrialListener lis : listeners){
						lis.onStop();
					}
					SoundMaker.playTimesUp();
				}
			} 
		};
		//Create the timer show it checks every 10 milliseconds
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockCheck, 0, 10);
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		//When the video has loaded then the trial is ready
		ready = true;
	}

	@Override
	public void onVideoPositionChange(long videoPosition) {}

	@Override
	public void onVideoPercentThroughChange(int videoTime) {}
}
