package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents and keeps track of a behaviour that occurs over time.
 */
public class TimedBehaviour extends Behaviour implements TrialListener {

	/**
	 * The list of every occurrence of this behaviour.
	 */
	private List<Occurrence> occurrences;
	
	/**
	 * The ongoing occurrence. Null there is none.
	 */
	private Occurrence current;
	
	/**
	 * The number of times started.
	 */
	private int timesStarted;
	
	/**
	 * Creates a timed behaviour.
	 * 
	 * @param name - the name of the behaviour
	 * @param color - the colour of the behaviour
	 * @param trial - the trial of the behaviour
	 * @param associateWithLocation - whether the behaviour should associate occurrences
	 */
	public TimedBehaviour(String name, Color color, Trial trial, boolean associateWithLocation) {
		super(name, color, trial, associateWithLocation);
		occurrences = new ArrayList<Occurrence>();
		timesStarted = 0;
	}
	
	/**
	 * Records that this behaviour started to occur during the trial.
	 */
	public void behaviourStarted(){
		current = new Occurrence(trial.getCurrentLocation(), trial.getCurrentTime());
		occurrences.add(current);
		++timesStarted;
	}
	
	/**
	 * Records that the current occurrence of this behaviour has ended.
	 */
	public void behaviourEnded(){
		current.setEnd(trial.getCurrentTime());
		current = null;
	}
	
	/**
	 * Gets the list of occurrences.
	 * 
	 * @return The list list occurrences.
	 */
	public List<Occurrence> getOccurences(){
		return occurrences;
	}
	
	/**
	 * Gets the number of occurrences.
	 * 
	 * @return The number of occurrences. Differs from the size of the list of occurrences
	 * 		as this number does not include when the location changes during an occurrence.
	 */
	public int getNumberOfOccurrences(){
		return timesStarted;
	}
	
	/**
	 * Gets the total duration of the trial that this behaviour has occurred for.
	 * 
	 * @return The duration that this behaviour has occurred for in seconds.
	 */
	public double getTotalDuration(){
		double duration = 0;
		for (Occurrence o : occurrences){
			duration += o.getDuration() / 1000.0;
		}
		return duration;
	}
	
	/**
	 * Gets the duration of the trial that this behaviour has occurred in a location.
	 * 
	 * @param location - the location to get the duration for
	 * @return The duration that this behaviour has occurred for in seconds.
	 */
	public double getDuration(Location location){
		double duration = 0;
		for (Occurrence o : occurrences){
			if (o.location.equals(location)){
				duration += o.getDuration() / 1000.0;
			}
		}
		return duration;
	}
	
	/**
	 * Gets whether the behaviour is currently occurring.
	 * 
	 * @return Whether the behaviour is ongoing.
	 */
	public boolean isOngoing(){
		return current != null;
	}
	
	/**
	 * Represents an occurrence of this behaviour.
	 */
	public class Occurrence {
		/**
		 * The location the behaviour occurred in.
		 */
		public final Location location;
		
		/**
		 * The time it started in milliseconds.
		 */
		private long start;
		
		/**
		 * The time it ended in milliseconds.
		 */
		private long end;

		/**
		 * Creates an occurrence of this behaviour.
		 * 
		 * @param location - the location it took place
		 * @param start - the time it started in milliseconds
		 */
		private Occurrence(Location location, long start){
			this.location = location;
			this.start = start;
			end = -1;
		}
		
		/**
		 * Gets the start time.
		 * 
		 * @return The start time in milliseconds.
		 */
		public long getStartTime(){
			return start;
		}
		
		/**
		 * Gets the end time.
		 * 
		 * @return The end time in milliseconds. -1 when it is ongoing.
		 */
		public long getEndTime(){
			return end;
		}
		
		/**
		 * Sets the end time.
		 * 
		 * @param end - the end time in milliseconds
		 */
		private void setEnd(long end){
			this.end = end;
		}
		
		/**
		 * Gets the duration of the occurrence.
		 * 
		 * @return The duration of the occurrence in milliseconds.
		 */
		public long getDuration(){
			if (end != -1){
				//Return the final duration if the occurrence has ended
				return end - start;
			} else {
				//Other return the amount of time that has passed since it started
				return trial.getCurrentTime() - start;
			}
		}
	}

	@Override
	public void onLocationChange(Location newLocation) {
		if (associateWithLocation){
			//Create a new occurrence if the location has changed to a different location.
			//This only applies when occurrences are associated with locations.
			if (current != null && newLocation != null 
					&& !current.location.equals(newLocation)){
				long time = trial.getCurrentTime();
				current.setEnd(time);
				current = new Occurrence(newLocation, time);
				occurrences.add(current);
			}
		}
	}
	
	@Override
	public void onStop() {
		if (current != null){
			current.setEnd(trial.getCurrentTime());
			current = null;
		}
	}

	@Override
	public void onReset() {
		occurrences = new ArrayList<Occurrence>();
	}

	@Override
	public void onStart() {}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}
}
