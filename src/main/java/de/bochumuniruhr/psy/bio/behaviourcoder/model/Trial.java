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

public class Trial implements VideoListener {
	
	private List<Area> areas;
	private List<TimedBehaviour> timed;
	private List<InstantBehaviour> instant;
	private TrialDetails details;
	
	private Area beforePause;
	private Area currentArea;
	private Map<Area, StopWatch> areaTimes;
	private int areaChanges;

	private StopWatch time;
	
	private boolean ready;
	
	private List<TrialListener> listeners;
	
	
	public Trial(long duration, List<Area> areas, List<String> detailNames, List<Constraint> detailConstraints){
		details = new TrialDetails(duration, detailNames, detailConstraints);
		this.areas = areas;
		timed = new ArrayList<TimedBehaviour>();
		instant = new ArrayList<InstantBehaviour>();
		areaChanges = 0;
		time = new StopWatch();
		areaTimes = new HashMap<Area, StopWatch>();
		listeners = new ArrayList<TrialListener>();
		ready = false;
		
		setupTimeLimitCheck();
	}
	
	public void start() {
		if (ready){
			time.start();
			for (TrialListener ls : listeners){
				ls.onStart();
			}
		}
	}
	
	public void pause(){
		if (time.isStarted() && !time.isSuspended()){
			time.suspend();
			if (currentArea != null){
				areaTimes.get(currentArea).suspend();
			}
			fireOnPause();
		}
	}
	
	public void resume(){
		if (time.isSuspended()){
			time.resume();
			if (currentArea != null){
				areaTimes.get(currentArea).resume();
			}
			fireOnResume();
		}
	}
	
	public void reset() {
		for (StopWatch watch : areaTimes.values()){
			watch.stop();
		}
		areaTimes.clear();
		time.reset();
		ready = false;
	}
	
	public boolean isRunning(){
		return time.isStarted() && !time.isSuspended() && !time.isStopped();
	}
	
	public boolean isActive(){
		return time.isStarted();
	}
	
	public long getCurrentTime(){
		return time.getTime();
	}
	
	public long getAreaTime(Area area){
		if (areaTimes.containsKey(area)){
			return areaTimes.get(area).getTime();
		} 
		return 0;
	}
	
	public void addTimedBehaviour(TimedBehaviour t){
		timed.add(t);
		listeners.add(t);
	}
	
	public void addInstantBehaviour(InstantBehaviour i){
		instant.add(i);
	}
	
	public void addListener(TrialListener listener){
		listeners.add(listener);
	}
	
	public void setCurrentArea(Area area){
		if (area != null && !time.isStarted()) {
			start();
		} else if (area != null && time.isSuspended()){
			time.resume();
		}
		
		if (currentArea != area){
			if (currentArea != null){
				areaTimes.get(currentArea).suspend();
			} else {
				fireOnResume();
			}
			if (area != null && (currentArea != null || beforePause != area)){
				++areaChanges;
			} else {
				beforePause = currentArea;
			}
			
			currentArea = area;
			
			
			if (area == null){
				time.suspend();
				fireOnPause();
			} else if (!areaTimes.containsKey(area)){
				StopWatch watch = new StopWatch();
				areaTimes.put(area, watch);
				watch.start();
			} else {
				areaTimes.get(area).resume();
			}
			for (TrialListener t : listeners){
				t.onAreaChange(area);
			}
		}
	}
	
	private void setupTimeLimitCheck() { 
		TimerTask clockCheck = new TimerTask() {

			@Override
			public void run() {
				if (time.getTime() > details.getDuration() * 1000 && ready){
					ready = false;
					time.suspend();
					for (TrialListener lis : listeners){
						lis.onStop();
					}
					if (currentArea != null){
						areaTimes.get(currentArea).suspend();
					}
					SoundMaker.playTimesUp();
				}
			} 
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(clockCheck, 0, 10);
	}
	
	public TrialDetails getDetails(){
		return details;
	}
	
	public Area getCurrentArea(){
		return currentArea;
	}
	
	public int getNumberOfAreaChanges(){
		return areaChanges;
	}
	
	public List<Area> getAreas(){
		return areas;
	}
	
	public List<TimedBehaviour> getTimedBehaviours(){
		return timed;
	}
	
	public List<InstantBehaviour> getInstantBehaviours(){
		return instant;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	private void fireOnPause(){
		for (TrialListener ls : listeners){
			ls.onPause();
		}
	}
	
	private void fireOnResume(){
		for (TrialListener ls : listeners){
			ls.onResume();
		}
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		ready = true;
	}

	@Override
	public void onVideoPositionChange(long videoPosition) {}

	@Override
	public void onVideoStart() {}

	@Override
	public void onVideoStop() {}

	@Override
	public void onVideoError(String message) {}

	@Override
	public void onVideoPercentThroughChange(int videoTime) {}

	public void setVideoTimeOffset(double d) {
		// TODO Auto-generated method stub
		
	}
}
