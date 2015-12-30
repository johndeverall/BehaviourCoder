package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class InstantBehaviour extends Behaviour {

	private List<Occurrence> occurrences;
	
	public InstantBehaviour(String name, Color color, Trial trial) {
		super(name, color, trial);
		occurrences = new ArrayList<Occurrence>();
	}
	
	public void behaviourOccurred(){
		occurrences.add(new Occurrence(trial.getCurrentArea(),	
				trial.getCurrentTime()));
	}
	
	public List<Occurrence> getOccurences(){
		return occurrences;
	}
	
	public int getNumberOfOccurrences(){
		return occurrences.size();
	}
	
	public void removeLast(){
		if (occurrences.size() > 0){
			occurrences.remove(occurrences.size()-1);
		}
	}
	
	void reset() {
		occurrences.clear();
	}
	
	public int getNumberOfOccurrences(Location area){
		int count = 0;
		for (Occurrence occ : occurrences){
			if (occ.area.equals(area)){
				++count;
			}
		}
		return count;
	}
	
	public class Occurrence {
		public final Location area;
		public final long time;

		private Occurrence(Location area, long time){
			this.area = area;
			this.time = time;
		}
	}
}
