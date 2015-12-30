package de.bochumuniruhr.psy.bio.behaviourcoder.model;

/**
 * Represents a location that a subject could be at in a trial.
 */
public final class Location {
	
	/**
	 * The name of the location
	 */
	private String name;

	/**
	 * Creates a location.
	 * 
	 * @param name - the name for the location
	 */
	public Location(String name){
		this.name = name;
	}
	
	/**
	 * Gets the name of the location.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Location)){
			return false;
		}
		return name.equals(((Location) obj).name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
