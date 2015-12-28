package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class TimedBehaviour extends Behaviour implements TrialListener {

	private List<Occurrence> occurrences;
	private Occurrence current;
	
	public TimedBehaviour(String name, Color color, Trial trial) {
		super(name, color, trial);
		occurrences = new ArrayList<Occurrence>();
	}
	
	public void behaviourStarted(){
		current = new Occurrence(trial.getCurrentArea(), trial.getCurrentTime());
		occurrences.add(current);
	}
	
	public void behaviourEnded(){
		current.setEnd(trial.getCurrentTime());
		current = null;
	}
	
	public List<Occurrence> getOccurences(){
		return occurrences;
	}
	
	/**
	 * Gets the total duration in seconds.
	 * 
	 * @return
	 */
	public double getTotalDuration(){
		double duration = 0;
		for (Occurrence o : occurrences){
			duration += o.getDuration() / 1000.0;
		}
		return duration;
	}
	
	public double getDuration(Area area){
		double duration = 0;
		for (Occurrence o : occurrences){
			if (o.area.equals(area)){
				duration += o.getDuration() / 1000.0;
			}
		}
		return duration;
	}
	
	public boolean isOngoing(){
		return current != null;
	}
	
	public void reset() {
		occurrences.clear();
	}
	
	public class Occurrence {
		public final Area area;
		private long start;
		private long end;

		private Occurrence(Area area, long start){
			this.area = area;
			this.start = start;
			end = -1;
		}
		
		public long getStartTime(){
			return start;
		}
		
		public long getEndTime(){
			return end;
		}
		
		private void setEnd(long end){
			this.end = end;
		}
		
		public long getDuration(){
			if (end != -1){
				return end - start;
			} else {
				return trial.getCurrentTime() - start;
			}
		}
	}

	@Override
	public void onAreaChange(Area newArea) {
		if (current != null && newArea != null && !current.area.equals(newArea)){
			long time = trial.getCurrentTime();
			current.setEnd(time);
			current = new Occurrence(newArea, time);
			occurrences.add(current);
		}
	}

	@Override
	public void onStart() {}

	@Override
	public void onStop() {
		if (current != null){
			current.setEnd(trial.getCurrentTime());
			current = null;
		}
	}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}
	
}
