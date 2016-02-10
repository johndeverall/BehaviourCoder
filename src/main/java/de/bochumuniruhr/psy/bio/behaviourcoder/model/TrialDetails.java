package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the details of a trial.
 */
public class TrialDetails {

	/**
	 * The date of the trial.
	 */
	private Date date;
	
	/**
	 * Duration of the trial session in seconds.
	 */
	private long duration;
	
	/**
	 * The time in the video before the trial started in seconds.
	 */
	private double videoTimeOffset;
	
	/**
	 * The map of detail names to detail values.
	 */
	private Map<String, String> details;
	
	/**
	 * The list of all detail names.
	 */
	private List<String> names;
	
	/**
	 * The map from detail names to constraints on the details.
	 */
	private Map<String, Constraint> constraints;
	
	/**
	 * Creates the details for a trial.
	 * 
	 * @param duration - initial duration for the trial in seconds
	 * @param names - the list of names for the details
	 * @param constraints - the list of constraints for each detail. 
	 * 		Constraints are applied to the details in the same order they are listed in the "names" parameter.
	 */
	public TrialDetails(long duration, List<String> names, List<Constraint> constraints){
		details = new HashMap<String, String>();
		this.constraints = new HashMap<String, Constraint>();
		this.names = names;
		this.duration = duration;
		videoTimeOffset = 0;
		
		//Build the maps
		for (int i = 0; i < names.size(); ++i){
			details.put(names.get(i), "");
			this.constraints.put(names.get(i), constraints.get(i));
		}
	}
	
	/**
	 * Gets the date of the trial.
	 * 
	 * @return The date.
	 */
	public Date getDate(){
		return date;
	}
	
	/**
	 * Sets the date of the trial.
	 * 
	 * @param date - the date of the trial
	 */
	public void setDate(Date date){
		this.date = date;
	}
	
	/**
	 * Gets the length of time that the trial runs for.
	 * 
	 * @return The duration of the trial.
	 */
	public long getDuration(){
		return duration;
	}
	
	/**
	 * Sets the length of time that the trial runs for.
	 * @param duration - the duration of the trial
	 */
	public void setDuration(long duration){
		this.duration = duration;
	}
	
	/**
	 * Gets the list of detail names.
	 * @return The list of detail names.
	 */
	public List<String> getDetailNames() {
		return names;
	}
	
	/**
	 * Gets the value of a detail.
	 * 
	 * @param detail - the name of the detail
	 * @return The value of the detail.
	 */
	public String getDetail(String detail){
		return details.get(detail);
	}
	
	/**
	 * Sets the value of a detail.
	 * 
	 * @param detail - the name of the detail to set
	 * @param value - the value to set it as
	 * @return Whether the detail was set.
	 * 		Will only return false if the detail does not belong to the trial.
	 */
	public boolean setDetail(String detail, String value){
		if (details.containsKey(detail)){
			details.put(detail, value);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether all the details are valid.
	 * 
	 * @return Whether the details are valid.
	 */
	public boolean isValid(){
		for (String name : details.keySet()){
			if (!constraints.get(name).holdsFor(details.get(name))){
				return false;
			}
		}
		return date != null;
	}
	
	/**
	 * Checks whether a specific detail is valid.
	 * 
	 * @param detail - the name of the detail to check
	 * @return Whether that detail is valid
	 */
	public boolean isValid(String detail){
		if (details.containsKey(detail) 
				&& constraints.get(detail).holdsFor(details.get(detail))){
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the constraint that is applied to a detail.
	 * @param detail - the name of detail to get the constraint of
	 * @return The constraint that is applied to the detail.
	 */
	public Constraint getConstraint(String detail){
		return constraints.get(detail);
	}
	
	/**
	 * Gets the time in the video before the trial started in seconds.
	 * 
	 * @return The offset before the trial started.
	 */
	public double getVideoTimeOffset() {
		return videoTimeOffset;
	}
	
	/**
	 * Sets the time in the video before the trial started in seconds.
	 * 
	 * @param offset - the offset before the trial started.
	 */
	public void setVideoTimeOffset(double offset) {
		videoTimeOffset = offset;
	}
	
	/**
	 * Represents a constraint on a detail.
	 */
	public static class Constraint {
		/**
		 * The whitelist of valid values. An empty array means all nonempty values are valid.
		 */
		private String[] whitelist;
		
		/**
		 * Create a constraint that accepts any nonempty value.
		 */
		public Constraint(){
			whitelist = new String[0];
		}
		
		/**
		 * Creates a constraint that accepts only values in the white list.
		 * 
		 * @param whitelist - the array of values that will be counted as valid
		 */
		public Constraint(String... whitelist){
			this.whitelist = whitelist;
		}
		
		/**
		 * Checks whether a value holds for the constraint.
		 * 
		 * @param value - the value to check
		 * @return Whether the value was valid.
		 */
		public boolean holdsFor(String value){
			//Check if it holds for any nonempty
			if (whitelist.length == 0 && !"".equals(value) && value != null){
				return true;
			}
			//Otherwise check whether it is in the whitelist
			for (String allowed : whitelist){
				if (allowed.equals(value)){
					return true;
				}
			}
			//If not then it is invalid
			return false;
		}
		
		/**
		 * Gets whether the constraint holds for any nonempty value.
		 * 
		 * @return Whether the constraints holds for all nonempty values.
		 */
		public boolean holdsForAny(){
			return whitelist.length == 0;
		}
		
		/**
		 * Gets the white list.
		 * 
		 * @return The list of valid values.
		 * 		Will return an empty array if any nonempty value is valid.
		 */
		public List<String> getWhitelist(){
			return Arrays.asList(whitelist);
		}
	}
}
