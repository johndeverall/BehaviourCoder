package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrialDetails {

	private Date date;
	
	/**
	 * Duration of the trial session in seconds.
	 */
	private long duration;
	private double videoTimeOffset;
	
	private Map<String, String> details;
	private List<String> names;
	private Map<String, Constraint> constraints;
	
	public TrialDetails(long duration, List<String> names, List<Constraint> constraints){
		details = new HashMap<String, String>();
		this.constraints = new HashMap<String, Constraint>();
		this.names = names;
		this.duration = duration;
		videoTimeOffset = 0;
		for (int i = 0; i < names.size(); ++i){
			details.put(names.get(i), "");
			this.constraints.put(names.get(i), constraints.get(i));
		}
	}
	
	public Date getDate(){
		return date;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
	public long getDuration(){
		return duration;
	}
	
	public void setDuration(long duration){
		this.duration = duration;
	}
	
	public List<String> getDetailNames() {
		return names;
	}
	
	public String getDetail(String detail){
		return details.get(detail);
	}
	
	public boolean setDetail(String detail, String value){
		if (details.containsKey(detail)){
			details.put(detail, value);
			return true;
		}
		return false;
	}
	
	public boolean isValid(){
		for (String name : details.keySet()){
			if (!constraints.get(name).holdsFor(details.get(name))){
				return false;
			}
		}
		return date != null;
	}
	
	public boolean isValid(String detail){
		if (details.containsKey(detail) 
				&& constraints.get(detail).holdsFor(details.get(detail))){
			return true;
		}
		return false;
	}
	
	public Constraint getConstraint(String detail){
		return constraints.get(detail);
	}
	
	public double getVideoTimeOffset() {
		return videoTimeOffset;
	}
	
	public void setVideoTimeOffset(double offset) {
		videoTimeOffset = offset;
	}
	
	
	public static class Constraint {
		private String[] whitelist;
		
		public Constraint(){
			whitelist = new String[0];
		}
		
		public Constraint(String... whitelist){
			this.whitelist = whitelist;
		}
		
		public boolean holdsFor(String value){
			if (whitelist.length == 0 && !"".equals(value) && value != null){
				return true;
			}
			for (String allowed : whitelist){
				if (allowed.equals(value)){
					return true;
				}
			}
			return false;
		}
		
		public boolean holdsForAny(){
			return whitelist.length == 0;
		}
		
		public List<String> getWhitelist(){
			return Arrays.asList(whitelist);
		}
	}
}
