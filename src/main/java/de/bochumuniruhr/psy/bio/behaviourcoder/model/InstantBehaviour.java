package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents and keeps track of a behaviour that occurs instantaneously.
 */
public class InstantBehaviour extends Behaviour {

	/**
	 * The list of every occurrence of this behaviour.
	 */
	private List<Occurrence> occurrences;
	
	/**
	 * Creates an instant behaviour.
	 * 
	 * @param name - the name of the behaviour
	 * @param color - the colour of the behaviour
	 * @param trial - the trial of the behaviour
	 */
	public InstantBehaviour(String name, Color color, Trial trial) {
		super(name, color, trial);
		occurrences = new ArrayList<Occurrence>();
	}
	
	/**
	 * Records that this behaviour occurred during the trial.
	 */
	public void behaviourOccurred(){
		occurrences.add(new Occurrence(trial.getCurrentLocation(),	
				trial.getCurrentTime()));
	}
	
	/**
	 * Removes the last occurrence if there exists one.
	 */
	public void removeLast(){
		if (occurrences.size() > 0){
			occurrences.remove(occurrences.size()-1);
		}
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
	 * Gets the number of times the behaviour has occurred.
	 * 
	 * @return The number of occurrences.
	 */
	public int getNumberOfOccurrences(){
		return occurrences.size();
	}
	
	/**
	 * Gets the number of times the behaviour has occurred in a given location.
	 * 
	 * @param location - the location to get the number for
	 * @return The number of occurrences at the location.
	 */
	public int getNumberOfOccurrences(Location location){
		int count = 0;
		for (Occurrence occ : occurrences){
			if (occ.location.equals(location)){
				++count;
			}
		}
		return count;
	}
	
	/**
	 * Resets the behaviour to its initial state.
	 */
	void reset() {
		occurrences.clear();
	}
	
	/**
	 * Represents an occurrence of this behaviour.
	 */
	public class Occurrence {
		/**
		 * The location that the behaviour occurred in.
		 */
		public final Location location;
		
		/**
		 * The time that the behaviour occurred at in milliseconds.
		 */
		public final long time;

		/**
		 * Creates an occurrence of this behaviour.
		 * 
		 * @param location - the location it took place
		 * @param time - the time it took place in milliseconds
		 */
		private Occurrence(Location location, long time){
			this.location = location;
			this.time = time;
		}
	}
}
